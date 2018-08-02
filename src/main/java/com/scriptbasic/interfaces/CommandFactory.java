package com.scriptbasic.interfaces;


import com.scriptbasic.spi.Command;

/**
 * A {@code CommandFactory} analyzes a BASIC program line and creates a
 * {@code Command} that is the result of the analysis.
 *
 * @author Peter Verhas
 * date June 15, 2012
 */
public interface CommandFactory {
    /**
     * Create a Command that starts with the keyword.
     *
     * @param commandKeyword the command keyword lexeme or {@code null} in case the command
     *                       does not start with a keyword (e.g. procedure call or
     *                       assignment or a DSL sentence)
     * @return the created command
     * @throws AnalysisException is there is a lexical or syntax exception
     *                           if there is some error with the command factory itself and it
     *                           can not analyze the line and does not know what the error is.
     *                           (probably the syntax of the line is totally wrong)
     */
    Command create(String commandKeyword) throws AnalysisException;
}
