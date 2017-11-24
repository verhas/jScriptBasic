package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExpressionList;

/**
 * @author Peter Verhas
 * date Jul 14, 2012
 *
 */
public abstract class AbstractCommandExpressionListed extends AbstractCommand {

    private ExpressionList expressionList;

    /**
     * @return the expressionList
     */
    public ExpressionList getExpressionList() {
        return expressionList;
    }

    /**
     * @param expressionList
     *            the expressionList to set
     */
    public void setExpressionList(ExpressionList expressionList) {
        this.expressionList = expressionList;
    }
}
