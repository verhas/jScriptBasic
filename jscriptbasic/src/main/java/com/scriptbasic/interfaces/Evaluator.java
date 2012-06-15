package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.BasicRuntimeException;
/**
 * Evaluator evaluates something (probably an {@code Expression} and result a value.
 * @author Peter Verhas
 * @date June 15, 2012
 *
 */
public interface Evaluator {
    public RightValue evaluate() throws BasicRuntimeException;
}
