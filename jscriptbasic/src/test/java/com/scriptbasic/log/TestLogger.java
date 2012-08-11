/**
 *
 */
package com.scriptbasic.log;
import org.junit.Test;
import junit.framework.Assert;
/**
 * @author Peter Verhas
 * date Aug 7, 2012
 *
 */
public class TestLogger {
    @SuppressWarnings("static-method")
    @Test
    public void testFormat() throws Exception {
        Assert.assertEquals("a 123 b",
                Logger.format("a {} b", new Object[] { 123 }));
        Assert.assertEquals("a  b",
                Logger.format("a {} b", new Object[] { "" }));
        Assert.assertEquals("a aaa b",
                Logger.format("a {} b", new Object[] { "aaa" }));
        Assert.assertEquals("a aaabbb b",
                Logger.format("a {}{} b", new Object[] { "aaa","bbb" }));
        Assert.assertEquals("123",
                Logger.format("{}", new Object[] { 123 }));
        Assert.assertEquals("oaka",
                Logger.format("oaka", null));
    }
}