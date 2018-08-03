package com.scriptbasic.utility;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestCharUtils {

    @Test
    public void newLineIsOnlyNewLineAndNothingElse() {
        assertTrue(CharUtils.isNewLine((int) '\n'));
        for (int i = 0; i < 10; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        for (int i = 15; i < 500; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        assertFalse(CharUtils.isNewLine(null));
        assertTrue(CharUtils.isNewLine((int) Character.LINE_SEPARATOR));
        assertTrue(CharUtils.isNewLine((int) Character.PARAGRAPH_SEPARATOR));
    }
}
