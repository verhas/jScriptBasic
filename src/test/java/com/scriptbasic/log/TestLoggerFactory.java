package com.scriptbasic.log;

import junit.framework.Assert;

import org.junit.Test;

public class TestLoggerFactory {
	@Test
	public void testLoggerFactory() {
		Logger logger1 = LoggerFactory.getLogger(this.getClass());
		Logger logger2 = LoggerFactory.getLogger(this.getClass());
		Assert.assertTrue(logger1 == logger2);
	}
}
