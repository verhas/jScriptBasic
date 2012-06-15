package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.LexicalException;

public interface CommandFactory extends FactoryManaged {
    /**
     * Create a Command that starts with the keyword.
     * 
     * @param commandKeyword
     *            the command keyword lexeme
     * @return the created command
     * @throws LexicalException
     */
    public AnalysisResult create(String commandKeyword) throws AnalysisException, CommandFactoryException;
}
