/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.executors.commands.AbstractCommandLeftValueListed;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LeftValueList;

/**
 * @author Peter Verhas
 * @date Jul 14, 2012
 * 
 */
public abstract class AbstractCommandAnalyzerGlobalLocal extends
        AbstractCommandAnalyzer {

    abstract protected AbstractCommandLeftValueListed newNode();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        AbstractCommandLeftValueListed node = newNode();
        LeftValueList list = analyzeLeftValueList();
        node.setLeftValueList(list);
        consumeEndOfLine();
        return node;
    }

}
