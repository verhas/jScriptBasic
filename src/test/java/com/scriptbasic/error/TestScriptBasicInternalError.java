package com.scriptbasic.error;

import org.junit.Test;

import com.scriptbasic.errors.BasicInterpreterInternalError;

public class TestScriptBasicInternalError {
	@Test
	public void testConstructors() {
		new BasicInterpreterInternalError("hamm");
		new BasicInterpreterInternalError("humm", new Throwable());
	}
}
