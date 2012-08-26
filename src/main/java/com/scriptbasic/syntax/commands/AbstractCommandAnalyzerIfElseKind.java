/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.AbstractCommandIfElseKind;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 * 
 */
public abstract class AbstractCommandAnalyzerIfElseKind extends
        AbstractCommandAnalyzer {

    protected void registerAndSwapNode(AbstractCommandIfElseKind node)
            throws AnalysisException {
        registerAndPopNode(node);
        pushNode(node);
    }

    protected void registerAndPopNode(AbstractCommandIfElseKind node)
            throws AnalysisException {
        NestedStructureHouseKeeper nshk = FactoryUtility
                .getNestedStructureHouseKeeper(getFactory());
        nshk.pop(AbstractCommandIfElseKind.class).setNext(node);
    }

}
