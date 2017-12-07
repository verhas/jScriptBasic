package com.scriptbasic.spi;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.Value;

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
     * @param rightValue  the value that will be referenced by the left value after the assignment
     * @param interpreter the interpreter controlling the action
     * @throws ScriptBasicException if the value can not be assigned for some reason
     */
    void setValue(RightValue rightValue, Interpreter interpreter)
            throws ScriptBasicException;
}
