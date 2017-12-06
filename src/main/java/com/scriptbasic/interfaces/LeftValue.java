package com.scriptbasic.interfaces;

/**
 * A LeftValue is a value that can stand on the left hand side of an assignment
 * command.
 *
 * @author Peter Verhas
 */
public interface LeftValue extends Value, AnalysisResult {
    /**
     * Assign the value to the left value.
     *
     * @param rightValue          the value that will be referenced by the left value after the assignment
     * @param extendedInterpreter the interpreter controlling the action
     * @throws ExecutionException if the value can not be assigned for some reason
     */
    void setValue(RightValue rightValue, ExtendedInterpreter extendedInterpreter)
            throws ExecutionException;
}
