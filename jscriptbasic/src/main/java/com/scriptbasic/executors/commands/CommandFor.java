/**
 * 
 */
package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;

/**
 * @author Peter Verhas
 * @date Jul 20, 2012
 * 
 */
public class CommandFor extends AbstractCommand {
    private LeftValue loopVariable;
    private Expression loopStartValue, loopEndValue, loopStepValue;
    private CommandNext loopEndNode;

    /**
     * @return the loopEndNode
     */
    public CommandNext getLoopEndNode() {
        return loopEndNode;
    }

    /**
     * @param loopEndNode the loopEndNode to set
     */
    public void setLoopEndNode(CommandNext loopEndNode) {
        this.loopEndNode = loopEndNode;
    }

    /**
     * @return the loopVariable
     */
    public LeftValue getLoopVariable() {
        return loopVariable;
    }

    /**
     * @param loopVariable
     *            the loopVariable to set
     */
    public void setLoopVariable(LeftValue loopVariable) {
        this.loopVariable = loopVariable;
    }

    /**
     * @return the loopStartValue
     */
    public Expression getLoopStartValue() {
        return loopStartValue;
    }

    /**
     * @param loopStartValue
     *            the loopStartValue to set
     */
    public void setLoopStartValue(Expression loopStartValue) {
        this.loopStartValue = loopStartValue;
    }

    /**
     * @return the loopEndValue
     */
    public Expression getLoopEndValue() {
        return loopEndValue;
    }

    /**
     * @param loopEndValue
     *            the loopEndValue to set
     */
    public void setLoopEndValue(Expression loopEndValue) {
        this.loopEndValue = loopEndValue;
    }

    /**
     * @return the loopStepValue
     */
    public Expression getLoopStepValue() {
        return loopStepValue;
    }

    /**
     * @param loopStepValue
     *            the loopStepValue to set
     */
    public void setLoopStepValue(Expression loopStepValue) {
        this.loopStepValue = loopStepValue;
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
