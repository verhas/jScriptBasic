package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;

/**
 * Evaluator evaluates something (probably an {@code Expression} and result a
 * value.
 *
 * @author Peter Verhas
 * date June 15, 2012
 */
public interface Evaluator {
    RightValue evaluate(Interpreter interpreter) throws ScriptBasicException;
}
