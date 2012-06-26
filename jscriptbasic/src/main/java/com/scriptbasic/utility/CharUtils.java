package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;

public class CharUtils {
    private CharUtils() {
        throw new BasicInterpreterInternalError(
                "Should not instantiate CharUtils utility class");
    }

    public static boolean isNewLine(final Integer ch) {
        if (ch != null) {
            return ch == Character.LINE_SEPARATOR
                    || ch == Character.PARAGRAPH_SEPARATOR || ch == '\n';
        } else {
            return false;
        }
    }

    private static final Integer NONBREAKING_SPACE = 160;

    public static boolean isWhitespace(final Integer ch) {
        return Character.isWhitespace(ch) || NONBREAKING_SPACE.equals(ch);
    }

    public static String convert(final Integer ch) {
        final StringBuilder sb = new StringBuilder(1);
        sb.appendCodePoint(ch);
        return sb.toString();
    }
}
