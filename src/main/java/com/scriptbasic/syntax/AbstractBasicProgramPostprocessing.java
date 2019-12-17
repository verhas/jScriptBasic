package com.scriptbasic.syntax;

import com.scriptbasic.executors.commands.*;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.spi.Command;

import java.util.Map;

/**
 * @author Peter Verhas
 * date Jul 18, 2012
 */
public abstract class AbstractBasicProgramPostprocessing implements
        BuildableProgram {
    final private static Logger LOG = LoggerFactory
            .getLogger();

    private Command startCommand = null;

    private static boolean commandIsSubHeadCommand(final Command command) {
        return command instanceof CommandLocal
                || command instanceof CommandGlobal;
    }

    private static void signalGlobalLocal() throws AnalysisException {
        throw new BasicSyntaxException(
                "Command 'LOCAL' can only be used inside a subroutine.");
    }

    private static void signalLocalCommandUse() throws AnalysisException {
        throw new BasicSyntaxException(
                "Command 'USE' should not be used inside a subroutine.");
    }

    private static void signalLocalCommandMethod() throws AnalysisException {
        throw new BasicSyntaxException(
                "Command 'METHOD' should not be used inside a subroutine.");
    }

    private static void signalMisplacedGlobalOrLocal() throws AnalysisException {
        throw new BasicSyntaxException(
                "Global and Local declarations shoud be the first definitions in the Sub");
    }

    private static void signalNestedSub() throws AnalysisException {
        throw new BasicSyntaxException(
                "Subroutines can not be nested into each other");
    }

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
     * <p>
     * Check double defined subroutines.
     *
     * @throws BasicSyntaxException in case of exception
     */
    private void collectSubroutines() throws BasicSyntaxException {
        for (Command command = getFirstCommand(); command != null; command = command
                .getNextCommand()) {
            if (command instanceof CommandSub) {
                final var commandSub = (CommandSub) command;
                if (getSubroutineMap().containsKey(commandSub.getSubName())) {
                    throw new BasicSyntaxException("The subroutine '"
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
        for (final Command command : getCommands()) {
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
                    LOG.debug("basic program starts on the line #" + skipNr);
                    break;
                }
            }
        }
    }

    private void checkLocalAndGlobalDeclarations() throws AnalysisException {
        boolean inSub = false;
        boolean subHeadClosed = false;
        for (final Command command : getCommands()) {
            if (inSub) {
                if (command instanceof CommandEndSub) {
                    inSub = false;
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
                    inSub = true;
                    subHeadClosed = false;
                }
            }
        }
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
