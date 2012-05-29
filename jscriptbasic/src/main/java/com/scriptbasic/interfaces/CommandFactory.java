package com.scriptbasic.interfaces;

public interface CommandFactory {
    /**
     * Create a Command that starts with the keyword.
     * 
     * @param commandKeyword
     *            the command keyword lexeme
     * @return the created command
     */
    public Command create(String commandKeyword) throws SyntaxException;
}
