package com.scriptbasic.syntax;

import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.commands.AbstractCommand;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.spi.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BasicProgram extends AbstractBasicProgramPostprocessing {

    private final List<Command> commands = new ArrayList<>();
    private AbstractCommand lastCommand = null;
    private final Map<String, CommandSub> subroutineMap = new HashMap<>();

    @Override
    public void reset() {
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

    @Override
    public Iterable<String> getNamedCommandNames() {
        return subroutineMap.keySet();
    }

    /**
     * @return the subroutineMap
     */
    protected Map<String, CommandSub> getSubroutineMap() {
        return subroutineMap;
    }

    @Override
    public Command getNamedCommand(final String name) {
        return subroutineMap.get(name);
    }

    @Override
    public String toJava(CompilerContext cc) {
        int i = 0;
        for (final var command : commands) {
            cc.nr.put(command, i);
            i++;
        }

        final var sb = new StringBuilder();
        sb.append("""
                package com.scriptbasic.compiled.my;
                import com.scriptbasic.api.ScriptBasicException;
                import com.scriptbasic.executors.rightvalues.BasicArrayValue;
                import com.scriptbasic.spi.Interpreter;
                        
                public class BasicCode {
                        
                public void run() throws ScriptBasicException {
                    run(%d); // the program counter
                    }
                    
                public void run(int _pc) throws ScriptBasicException {        
                    
                    while(true){
                    switch( _pc ){
                """.formatted(cc.nr.get(getFirstCommand()))
        );
        i = 0;
        for (final var command : commands) {
            sb.append("""
                    case %d:                                        
                    """.formatted(i));
            sb.append(command.toJava(cc));
            sb.append("""
                        break;
                    """);
            i++;
        }

        sb.append("""
                default: return;
                }}}}
                """);
        return sb.toString();
    }

}
