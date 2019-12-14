package com.scriptbasic.utility;

public final class CharUtils {
    private static final Integer NONBREAKING_SPACE = 160;

    private CharUtils() {
        NoInstance.isPossible();
    }

    public static boolean isNewLine(final Integer ch) {
        if (ch != null) {
            return ch == Character.LINE_SEPARATOR
                    || ch == Character.PARAGRAPH_SEPARATOR 
                    || ch == '\n'
                    || ch == ':';
        } else {
            return false;
        }
    }

    public static boolean isWhitespace(final Integer ch) {
        return Character.isWhitespace(ch) || NONBREAKING_SPACE.equals(ch);
    }

    public static String convert(final Integer ch) {
        final var sb = new StringBuilder(1);
        sb.appendCodePoint(ch);
        return sb.toString();
    }
}
