package com.scriptbasic.log;


import org.junit.Assert;
import org.junit.Test;

public class TestLoggerFactory {
	@Test
	public void testLoggerFactory() {
		Logger logger1 = LoggerFactory.getLogger();
		Logger logger2 = LoggerFactory.getLogger();
		Assert.assertTrue(logger1 == logger2);
	}
}
