/**
 * 
 */
package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

/**
 * @author Peter Verhas
 * @date Jul 20, 2012
 * 
 */
public class CommandNext extends AbstractCommand {
    private CommandFor loopStartNode;

    /**
     * @return the loopStartNode
     */
    public CommandFor getLoopStartNode() {
        return loopStartNode;
    }

    /**
     * @param loopStartNode the loopStartNode to set
     */
    public void setLoopStartNode(CommandFor loopStartNode) {
        this.loopStartNode = loopStartNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.executors.commands.AbstractCommand#execute(com.scriptbasic
     * .interfaces.ExtendedInterpreter)
     */
    @Override
    public void execute(ExtendedInterpreter interpreter)
            throws ExecutionException {
        // TODO Auto-generated method stub

    }

}
