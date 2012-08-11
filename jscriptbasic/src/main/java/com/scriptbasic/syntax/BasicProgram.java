package com.scriptbasic.syntax;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptbasic.executors.commands.AbstractCommand;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Factory;
public final class BasicProgram extends AbstractBasicProgramPostprocessing {
//    final private static Logger LOG = LoggerFactory
//            .getLogger(BasicProgram.class);
    private Factory factory;
    public Factory getFactory() {
        return factory;
    }
    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }
    private final List<Command> commands = new ArrayList<Command>();
    private AbstractCommand lastCommand = null;
    @Override
    public void reset(){
        commands.clear();
        lastCommand = null;
    }
    public void addCommand(final Command command) {
        if (lastCommand != null) {
            lastCommand.setNextCommand(command);
        }
        lastCommand = (AbstractCommand) command;
        this.commands.add(command);
    }
    protected Command getFirstCommand() {
        if (commands.isEmpty()) {
            return null;
        } else {
            return commands.get(0);
        }
    }
    @Override
    public Collection<Command> getCommands() {
        return commands;
    }
    private Map<String, CommandSub> subroutineMap = new HashMap<>();
    /**
     * @return the subroutineMap
     */
    protected Map<String, CommandSub> getSubroutineMap() {
        return subroutineMap;
    }
    @Override
    public Command getNamedCommand(String name) {
        return subroutineMap.get(name);
    }
}