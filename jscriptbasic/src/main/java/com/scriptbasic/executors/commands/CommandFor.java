/**
 * 
 */
package com.scriptbasic.executors.commands;

import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.NumberUtility;

/**
 * @author Peter Verhas
 * @date July 20, 2012
 * 
 */
public class CommandFor extends AbstractCommand {
    private LeftValue loopVariable;
    private Expression loopStartValue, loopEndValue, loopStepValue;
    private CommandNext loopEndNode;
    private RightValue loopStart, loopEnd, loopStep;

    /**
     * @return the loopEnd
     */
    public RightValue getLoopEnd() {
        return loopEnd;
    }

    /**
     * @return the loopStep
     */
    public RightValue getLoopStep() {
        return loopStep;
    }

    /**
     * @return the loopEndNode
     */
    public CommandNext getLoopEndNode() {
        return loopEndNode;
    }

    /**
     * @param loopEndNode
     *            the loopEndNode to set
     */
    public void setLoopEndNode(final CommandNext loopEndNode) {
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
    public void setLoopVariable(final LeftValue loopVariable) {
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
    public void setLoopStartValue(final Expression loopStartValue) {
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
    public void setLoopEndValue(final Expression loopEndValue) {
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
    public void setLoopStepValue(final Expression loopStepValue) {
        this.loopStepValue = loopStepValue;
    }

    private void startLoopWithLong(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        final Long start = BasicLongValue.convert(loopStart);
        loopVariable.setValue(new BasicLongValue(start), interpreter);
        setNextCommand(interpreter, BasicLongValue.convert(loopStep), start,
                BasicLongValue.convert(loopEnd));
    }

    private void startLoopWithDouble(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        final Double start = BasicDoubleValue.convert(loopStart);
        loopVariable.setValue(new BasicDoubleValue(start), interpreter);
        setNextCommand(interpreter, BasicDoubleValue.convert(loopStep), start,
                BasicDoubleValue.convert(loopEnd));
    }

    private RightValue getLoopVariableAsRightValue(
            final ExtendedInterpreter interpreter) throws ExecutionException {
        return interpreter.getVariables().getVariableValue(
                ((BasicLeftValue) loopVariable).getIdentifier());
    }

    private void finishTheLoop(final ExtendedInterpreter interpreter) {
        interpreter.setNextCommand(loopEndNode.getNextCommand());
    }

    private <T extends Number> void setNextCommand(
            final ExtendedInterpreter interpreter, final T step,
            final T newLoopValue, final T loopEndDouble) {
        if (NumberUtility.isPositive(step)) {
            if (NumberUtility.compare(newLoopValue, loopEndDouble) <= 0) {
                interpreter.setNextCommand(getNextCommand());
            } else {
                finishTheLoop(interpreter);
            }
        } else {
            if (NumberUtility.compare(newLoopValue, loopEndDouble) >= 0) {
                interpreter.setNextCommand(getNextCommand());
            } else {
                finishTheLoop(interpreter);
            }
        }
    }

    private void stepLoopVariable(final ExtendedInterpreter interpreter,
            final Long step) throws ExecutionException {
        final Long loopEndValue = BasicLongValue.convert(loopEnd);
        final RightValue rv = getLoopVariableAsRightValue(interpreter);
        final Long newLoopValue = BasicLongValue.convert(rv) + step;
        loopVariable.setValue(new BasicLongValue(newLoopValue), interpreter);
        setNextCommand(interpreter, step, newLoopValue, loopEndValue);
    }

    private void stepLoopVariable(final ExtendedInterpreter interpreter,
            final Double step) throws ExecutionException {
        final Double loopEndValue = BasicDoubleValue.convert(loopEnd);
        final RightValue rv = getLoopVariableAsRightValue(interpreter);
        final Double newLoopValue = BasicDoubleValue.convert(rv) + step;
        loopVariable.setValue(new BasicDoubleValue(newLoopValue), interpreter);
        setNextCommand(interpreter, step, newLoopValue, loopEndValue);
    }

    void stepLoopVariable(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (loopVariable instanceof BasicLeftValue) {
            if (loopStep instanceof BasicLongValue) {
                stepLoopVariable(interpreter, BasicLongValue.convert(loopStep));
            } else if (loopStep instanceof BasicDoubleValue) {
                stepLoopVariable(interpreter,
                        BasicDoubleValue.convert(loopStep));
            } else {
                throw new BasicRuntimeException(
                        "Loop step value can be long or double");
            }
        } else {
            throw new BasicRuntimeException(
                    "Loop variable is not BasicLeftValue, this is probably internal error");
        }
    }

    void noStepLoopVariable(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (loopVariable instanceof BasicLeftValue) {
            if (loopStep instanceof BasicLongValue) {
                final Long step = BasicLongValue.convert(loopStep);
                final Long loopEndValue = BasicLongValue.convert(loopEnd);
                final Long newLoopValue = BasicLongValue.convert(getLoopVariableAsRightValue(interpreter));
                setNextCommand(interpreter, step, newLoopValue, loopEndValue);
            } else if (loopStep instanceof BasicDoubleValue) {
                final Double step = BasicDoubleValue.convert(loopStep);
                final Double loopEndValue = BasicDoubleValue.convert(loopEnd);
                final Double newLoopValue = BasicDoubleValue
                        .convert(getLoopVariableAsRightValue(interpreter));
                setNextCommand(interpreter, step, newLoopValue, loopEndValue);
            } else {
                throw new BasicRuntimeException(
                        "Loop step value can be long or double");
            }
        } else {
            throw new BasicRuntimeException(
                    "Loop variable is not BasicLeftValue, this is probably internal error");
        }
    }

    private void setLoopStart(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (loopStep instanceof BasicDoubleValue) {
            if (loopStart instanceof BasicDoubleValue) {
                loopVariable.setValue(loopStart, interpreter);
            } else {
                startLoopWithDouble(interpreter);
            }
        } else if (loopStep instanceof BasicLongValue) {
            if (loopStart instanceof BasicLongValue) {
                loopVariable.setValue(loopStart, interpreter);
            } else {
                startLoopWithLong(interpreter);
            }
        } else {
            throw new BasicRuntimeException(
                    "Step expression is not long or double");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.executors.commands.AbstractCommand#execute(com.scriptbasic
     * .interfaces.ExtendedInterpreter)
     */
    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        loopStart = loopStartValue.evaluate(interpreter);
        loopEnd = loopEndValue.evaluate(interpreter);
        if (loopStepValue != null) {
            loopStep = loopStepValue.evaluate(interpreter);
        } else {
            loopStep = new BasicLongValue(1L);
        }
        setLoopStart(interpreter);
        noStepLoopVariable(interpreter);
    }

}
