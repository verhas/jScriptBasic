package com.scriptbasic.log;


import org.junit.Assert;
import org.junit.Test;

public class TestLoggerFactory {
    @Test
    public void loggerFactoryReturnsTheSameLoggerForTheSameClass() {
        final var logger1 = LoggerFactory.getLogger();
        final var logger2 = LoggerFactory.getLogger();
        Assert.assertSame(logger1, logger2);
    }
}
