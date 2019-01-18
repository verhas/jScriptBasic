package com.scriptbasic.log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Peter Verhas
 * date Aug 7, 2012
 */
public class TestLogger {
    @Test
    public void loggerFormatFormatsVariableNumberOfArgumentsProperly() {

        Assertions.assertEquals("a 123 b",
                Logger.format("a {} b", new Object[]{123}));

        Assertions.assertEquals("a  b",
                Logger.format("a {} b", new Object[]{""}));

        Assertions.assertEquals("a aaa b",
                Logger.format("a {} b", new Object[]{"aaa"}));

        Assertions.assertEquals("a aaabbb b",
                Logger.format("a {}{} b", new Object[]{"aaa", "bbb"}));

        Assertions.assertEquals("123",
                Logger.format("{}", new Object[]{123}));

        Assertions.assertEquals("oaka",
                Logger.format("oaka", null));
    }
}
