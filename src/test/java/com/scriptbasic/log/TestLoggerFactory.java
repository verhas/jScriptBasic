package com.scriptbasic.log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLoggerFactory {
    @Test
    public void loggerFactoryReturnsTheSameLoggerForTheSameClass() {
        final var logger1 = LoggerFactory.getLogger();
        final var logger2 = LoggerFactory.getLogger();
        Assertions.assertSame(logger1, logger2);
    }
}
