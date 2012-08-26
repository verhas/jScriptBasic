/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.AbstractCommandLeftValueListed;
import com.scriptbasic.executors.commands.CommandGlobal;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 * 
 */
public class CommandAnalyzerGlobal extends AbstractCommandAnalyzerGlobalLocal {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer#getName()
     */
    @Override
    protected String getName() {
        return "GLOBAL";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzerGlobalLocal
     * #newNode()
     */
    @Override
    protected AbstractCommandLeftValueListed newNode() {
        return new CommandGlobal();
    }

}
