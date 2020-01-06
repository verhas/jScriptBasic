package com.scriptbasic.interfaces;

import com.scriptbasic.spi.Command;

/**
 * A syntax analyzer analyzes a program source using the result of the lexical
 * analyzer and create an executable program.
 *
 * @author Peter Verhas
 */
public interface SyntaxAnalyzer {

    /**
     * Perform the syntax analysis.
     *
     * @return return value
     * @throws AnalysisException in case of exception
     */
    BuildableProgram analyze() throws AnalysisException;

    /**
     * Add command to the currently build program
     * 
     * @param command Command to be added to the current program
     */
    void addCommand(Command command);

}
