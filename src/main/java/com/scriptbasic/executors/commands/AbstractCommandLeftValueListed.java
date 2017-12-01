package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.LeftValueList;

/**
 * @author Peter Verhas
 * date Jul 14, 2012
 */
public abstract class AbstractCommandLeftValueListed extends AbstractCommand {

    private LeftValueList leftValueList;

    /**
     * @return the leftValueList
     */
    public LeftValueList getLeftValueList() {
        return leftValueList;
    }

    /**
     * @param leftValueList the leftValueList to set
     */
    public void setLeftValueList(final LeftValueList leftValueList) {
        this.leftValueList = leftValueList;
    }

}
