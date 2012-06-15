package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.BasicRuntimeException;

public interface Evaluator {
    public RightValue evaluate() throws BasicRuntimeException;
}
