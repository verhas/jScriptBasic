package com.scriptbasic.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestCharUtils {

    @Test
    public void newLineIsOnlyNewLineAndNothingElse() {
        assertTrue(CharUtils.isNewLine((int) '\n'));
        assertTrue(CharUtils.isNewLine((int) ':'));
        for (int i = 0; i < 10; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        final int semicolon = (int) ':';
        for (int i = 15; i < semicolon; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        for (int i = semicolon+1; i < 500; i++) {
            assertFalse(CharUtils.isNewLine(i));
        }
        //noinspection ConstantConditions
        assertFalse(CharUtils.isNewLine(null));
        assertTrue(CharUtils.isNewLine((int) Character.LINE_SEPARATOR));
        assertTrue(CharUtils.isNewLine((int) Character.PARAGRAPH_SEPARATOR));
    }
}
