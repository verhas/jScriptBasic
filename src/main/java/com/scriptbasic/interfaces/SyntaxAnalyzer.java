package com.scriptbasic.interfaces;


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

}
