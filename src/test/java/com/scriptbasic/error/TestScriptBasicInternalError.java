package com.scriptbasic.error;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import org.junit.jupiter.api.Test;

public class TestScriptBasicInternalError {
    @Test
    public void testConstructors() {
        new BasicInterpreterInternalError("hamm");
        new BasicInterpreterInternalError("humm", new Throwable());
    }
}
