package com.scriptbasic.executors.commands;

import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.spi.Interpreter;

public class CommandWend extends AbstractCommand {

    @Override
    public String toJava(CompilerContext cc){
        final var sb = new StringBuilder();
        sb.append("_pc=%d;".formatted(cc.nr.get(getCommandWhile())));
        return sb.toString();
    }

    private CommandWhile commandWhile;

    public CommandWhile getCommandWhile() {
        return commandWhile;
    }

    public void setCommandWhile(final CommandWhile commandWhile) {
        this.commandWhile = commandWhile;
    }

    @Override
    public void execute(final Interpreter interpreter) {
        interpreter.setNextCommand(commandWhile);
    }

}
