package com.scriptbasic.error;

import org.junit.Test;

import com.scriptbasic.errors.BasicInterpreterInternalError;

public class TestScriptBasicInternalError {
    @Test
    public void testConstructors() {
        BasicInterpreterInternalError biie = new BasicInterpreterInternalError(
                "hamm");
        biie = new BasicInterpreterInternalError("humm", new Throwable());
    }
}
