package com.scriptbasic.interfaces;

/**
 * The implementing classes contain the execution code of the different program
 * commands, like 'for', 'if' and so on.
 * <p>
 * These objects are multitones, do not contain any runtime data, since many
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
 * 
 */
public interface CommandAnalyzer extends Analyzer {

    public void setSyntaxAnalyzer(SyntaxAnalyzer sa);

    public Command getCommand();
}
