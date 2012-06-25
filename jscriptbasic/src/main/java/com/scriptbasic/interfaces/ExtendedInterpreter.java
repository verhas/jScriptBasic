package com.scriptbasic.interfaces;

import java.util.Map;

/**
 * The extended interpreter is the interface that extends the functions of the
 * Interpreter with the methods that are needed by the command implementations
 * but are not needed by the program that embeds the interpreter.
 * 
 * @author Peter Verhas
 * 
 */
public interface ExtendedInterpreter extends Interpreter {
    /**
     * Get the actual value of the program counter.
     * 
     * @return the actual command pointer
     */
    Integer getProgramCounter();

    /**
     * 
     * @return the program that the interpreter is executing.
     */
    BuildableProgram getProgram();

    /**
     * Tell the interpreter that the next command to call is not the one that
     * follows the actual command but rather the one specified by the argument.
     * 
     * @param nextCommand
     *            is the next command to execute after the current command
     * 
     */
    void setNextCommand(Command nextCommand);

    /**
     * Get the variables of the program.
     * 
     * @return the variables.
     */
    VariableMap getVariables();

    /**
     * Since the Command objects should not contain runtime information there is
     * a need sometime to store information that is runtime. This method returns
     * a map that is unique to the currently executing Command and is also
     * unique to the Interpreter. Thus if two interpreters simultaneously
     * execute the same command they will return two different maps.
     * <p>
     * The interpreter initializes the map when the command asks for it the
     * first time. The life time of the map is the same as the life time of the
     * interpreter. Thus when a program finishes the map is still available to
     * the command when the Interface 'call' method is called or the interpreter
     * is restarted.
     * <p>
     * The interpreter does not alter the map in any other way than initializing
     * it to some map implementation containing initially no element.
     * 
     * @return the map
     */
    Map<String, Object> getMap();
}
