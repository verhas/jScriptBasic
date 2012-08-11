package com.scriptbasic.utility;
import java.io.IOException;
import junit.framework.TestCase;
public class TestCharUtils extends TestCase {
    public TestCharUtils(final String testName) {
        super(testName);
    }
    @SuppressWarnings("static-method")
    public void test() throws IOException {
        assertTrue(CharUtils.isNewLine((int) '\n'));
        for (int i = 0; i < 10; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        for (int i = 15; i < 1000; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        assertFalse(CharUtils.isNewLine(null));
        assertTrue(CharUtils.isNewLine((int) Character.LINE_SEPARATOR));
        assertTrue(CharUtils.isNewLine((int) Character.PARAGRAPH_SEPARATOR));
    }
}