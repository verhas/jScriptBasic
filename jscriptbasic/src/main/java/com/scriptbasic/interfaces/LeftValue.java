package com.scriptbasic.interfaces;

/**
 * A LeftValue is a value that can stand on the left hand side of an assignment
 * command.
 * 
 * @author Peter Verhas
 * 
 */
public interface LeftValue extends Value, AnalysisResult {
    //TODO make the exception less broad later when the actual code is ready
    void setValue(RightValue rightValue) throws Exception;
}
