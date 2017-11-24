package com.scriptbasic.syntax;

import java.util.Map;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandEndSub;
import com.scriptbasic.executors.commands.CommandGlobal;
import com.scriptbasic.executors.commands.CommandLocal;
import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

/**
 * @author Peter Verhas
 * date Jul 18, 2012
 * 
 */
public abstract class AbstractBasicProgramPostprocessing implements
        BuildableProgram {
    final private static Logger LOG = LoggerFactory
            .getLogger();

    private Command startCommand = null;

    @Override
    public Command getStartCommand() {
        if (startCommand == null) {
            startCommand = getFirstCommand();
        }
        return startCommand;
    }

    protected abstract Map<String, CommandSub> getSubroutineMap();

    /**
     * Collect all the subroutines and build the subroutine map to ease the
     * location of subroutines based on name.
     * 
     * @param startCommand
     * @throws GenericSyntaxException 
     */
    private void collectSubroutines() throws GenericSyntaxException {
        for (Command command = getFirstCommand(); command != null; command = command
                .getNextCommand()) {
            if (command instanceof CommandSub) {
                CommandSub commandSub = (CommandSub) command;
                if (getSubroutineMap().containsKey(commandSub.getSubName())) {
                    throw new GenericSyntaxException("The subroutine '"
                            + commandSub.getSubName()
                            + "' is defined more than once");
                }
                getSubroutineMap().put(commandSub.getSubName(),
                        (CommandSub) command);
            }
        }
    }

    /**
     * Step over the sub and other commands that are declarations to determine
     * which is really the first executable command where the interpreter should
     * start.
     */
    private void skipDeclarations() {
        boolean loopIsInSub = false;
        int skipNr = 0; // only for logging
        for (Command command = getFirstCommand(); command != null; command = command
                .getNextCommand()) {
            skipNr++;
            if (loopIsInSub) {
                if (command instanceof CommandEndSub) {
                    loopIsInSub = false;
                }
            } else {
                if (command instanceof CommandSub) {
                    loopIsInSub = true;
                } else {
                    startCommand = command;
                    LOG.info("basic program starts on the line #" + skipNr);
                    break;
                }
            }
        }
    }

    private void checkLocalAndGlobalDeclarations() throws AnalysisException {
        boolean loopIsInSub = false;
        boolean subHeadClosed = false;
        for (Command command = getFirstCommand(); command != null; command = command
                .getNextCommand()) {
            if (loopIsInSub) {
                if (command instanceof CommandEndSub) {
                    loopIsInSub = false;
                } else {
                    if (command instanceof CommandSub) {
                        signalNestedSub();
                    }
                    if (command instanceof CommandUse) {
                        signalLocalCommandUse();
                    }
                    if (command instanceof CommandMethod) {
                        signalLocalCommandMethod();
                    }
                    if (subHeadClosed) {
                        if (command instanceof CommandLocal
                                || command instanceof CommandGlobal) {
                            signalMisplacedGlobalOrLocal();
                        }
                    } else {
                        subHeadClosed = !commandIsSubHeadCommand(command);
                    }
                }
            } else {
                if (command instanceof CommandLocal) {
                    signalGlobalLocal();
                }
                if (command instanceof CommandSub) {
                    loopIsInSub = true;
                    subHeadClosed = false;
                }
            }
        }
    }

    private static boolean commandIsSubHeadCommand(final Command command) {
        return command instanceof CommandLocal
                || command instanceof CommandGlobal;
    }

    private static void signalGlobalLocal() throws AnalysisException {
        throw new GenericSyntaxException(
                "Command 'LOCAL' can only be used inside a subroutine.");
    }

    private static void signalLocalCommandUse() throws AnalysisException {
        throw new GenericSyntaxException(
                "Command 'USE' should not be used inside a subroutine.");
    }

    private static void signalLocalCommandMethod() throws AnalysisException {
        throw new GenericSyntaxException(
                "Command 'METHOD' should not be used inside a subroutine.");
    }

    private static void signalMisplacedGlobalOrLocal() throws AnalysisException {
        throw new GenericSyntaxException(
                "Global and Local declarations shoud be the first definitions in the Sub");
    }

    private static void signalNestedSub() throws AnalysisException {
        throw new GenericSyntaxException(
                "Subroutines can not be nested into each other");
    }

    protected abstract Command getFirstCommand();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.BuildableProgram#postprocess()
     */
    @Override
    public void postprocess() throws AnalysisException {
        startCommand = getFirstCommand();
        checkLocalAndGlobalDeclarations();

        skipDeclarations();
        collectSubroutines();
        // TODO rearrange the commands so that the subroutines are out of order
        // and can be totally skipped even if they are not at the start of the
        // code
        // TODO rearrange the USE and METHOD commands so that they appear at the
        // start of the program, no matter where they are
        // TODO optimize expression:
        // 1. execute constant integer arithmetic
        // 2. execute constant string concatenation
        // 3. execute constant boolean arithmetic

    }
}
