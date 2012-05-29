package com.scriptbasic.syntax;

import java.util.ArrayList;
import java.util.Collection;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Program;

public class BasicProgram implements Program {

    private ArrayList<Command> commandAnalyzers = new ArrayList<Command>();
    
    public void addCommand(Command command){
        commandAnalyzers.add(command);
    }
    
    @Override
    public Command getStartCommand() {
        
        return null;
    }

    @Override
    public Collection<Command> getCommands() {
        
        return null;
    }

    @Override
    public Command getCommand(Integer programCounter) {
        
        return null;
    }

}
