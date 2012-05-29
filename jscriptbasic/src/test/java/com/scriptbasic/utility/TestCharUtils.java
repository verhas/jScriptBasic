package com.scriptbasic.utility;

import java.io.IOException;

import junit.framework.TestCase;

public class TestCharUtils extends TestCase {
    public TestCharUtils(String testName) {
        super(testName);
    }

    public void test() throws IOException {
        assertTrue(CharUtils.isNewLine((Integer) (int) '\n'));
        for (int i = 0; i < 10; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        for (int i = 15; i < 1000; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        assertFalse(CharUtils.isNewLine(null));
        assertTrue(CharUtils
                .isNewLine((Integer) (int) Character.LINE_SEPARATOR));
        assertTrue(CharUtils
                .isNewLine((Integer) (int) Character.PARAGRAPH_SEPARATOR));
    }
}
