package com.scriptbasic.interfaces;

/**
 * Classes that provide methods to execute a command implement this interface.
 * <p>
 * Since the objects are created as a result of the BASIC program analysis any
 * command is also an {@code AnalysisResult}.
 * <p>
 * The implementing classes contain the execution code of the different program
 * commands, like 'for', 'if' and so on.
 * <p>
 * These objects are multitons, do not contain any runtime data, since many
 * interpreters may execute the same program at the same time. The code also has
 * to be thread safe.
 * <p>
 * The data that may be stored in a command is referential data to other
 * Commands that are in the same program. For example a command implementing
 * 'while' may have a program counter that points to the corresponding 'wend',
 * but should not contain any data that holds the value of the expression
 * evaluated.
 * 
 * @author Peter Verhas
 * date June 15, 2012
 * 
 */
public interface Command extends AnalysisResult, Executor {
    Command getNextCommand();
    void checkedExecute(ExtendedInterpreter interpreter) throws ExecutionException;
}
