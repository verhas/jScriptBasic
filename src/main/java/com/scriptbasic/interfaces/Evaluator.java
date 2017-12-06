package com.scriptbasic.interfaces;

/**
 * Evaluator evaluates something (probably an {@code Expression} and result a
 * value.
 *
 * @author Peter Verhas
 * date June 15, 2012
 */
public interface Evaluator {
    RightValue evaluate(Interpreter extendedInterpreter)
            throws ExecutionException;
}
