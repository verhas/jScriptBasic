package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.exceptions.SyntaxException;

/**
 * A syntax analyzer analyzes a program source using the result of the lexical
 * analyzer and create an executable program.
 * 
 * @author Peter Verhas
 * 
 */
public interface SyntaxAnalyzer extends FactoryManaged {

    /**
     * Perform the syntax analysis.
     * 
     * @return
     * 
     * @throws SyntaxException
     */
    public Program analyze() throws AnalysisException;

}
