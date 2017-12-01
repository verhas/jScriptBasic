package com.scriptbasic.interfaces;

import java.util.Collection;

/**
 * The program that was created by the syntax analyzer. The object state is
 * independent from the execution. The actual runtime should contain all state
 * information, global variable values, program counter (actual command),
 * whatever it wants. The implementing class has to be thread safe and immutable
 * after it was created by the syntax analysis.
 *
 * @author Peter Verhas date June 15, 2012
 */
public interface Program {
    Command getStartCommand();

    Command getNamedCommand(String name);

    Iterable<String> getNamedCommandNames();

    Collection<Command> getCommands();
}
