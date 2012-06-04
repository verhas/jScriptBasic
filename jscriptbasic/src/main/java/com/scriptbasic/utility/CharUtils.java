package com.scriptbasic.utility;

public class CharUtils {
    public static boolean isNewLine(final Integer ch) {
        if (ch != null) {
            return ch == Character.LINE_SEPARATOR
                    || ch == Character.PARAGRAPH_SEPARATOR || ch == '\n';
        } else {
            return false;
        }
    }

    public static String convert(final Integer ch) {
        final StringBuilder sb = new StringBuilder(1);
        sb.appendCodePoint(ch);
        return sb.toString();
    }
}
