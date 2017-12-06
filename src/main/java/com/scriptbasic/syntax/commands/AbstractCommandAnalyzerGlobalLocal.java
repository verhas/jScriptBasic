package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.AbstractCommandLeftValueListed;
import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LeftValueList;

/**
 * @author Peter Verhas
 * date Jul 14, 2012
 */
public abstract class AbstractCommandAnalyzerGlobalLocal extends AbstractCommandAnalyzer {

    public AbstractCommandAnalyzerGlobalLocal(final Context ctx) {
        super(ctx);
    }

    abstract protected AbstractCommandLeftValueListed newNode();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        final AbstractCommandLeftValueListed node = newNode();
        final LeftValueList list = analyzeSimpleLeftValueList();
        node.setLeftValueList(list);
        consumeEndOfLine();
        return node;
    }

}
