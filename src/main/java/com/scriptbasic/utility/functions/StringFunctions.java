package com.scriptbasic.utility.functions;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.utility.RightValueUtility;

/**
 * <p>
 * This class implements string functions for the BASIC interpreter. Most of the
 * methods implement a wrapper for the method of the same name in the class
 * {@code java.lang.String}. These methods in the class {@code java.lang.String}
 * are not static and therefore need a String object to work on. The wrapper
 * methods are static and take the first argument as the String object to work
 * on. In other words if in Java you would write
 * </p>
 * <pre>
 * s.xyz(parameterlist)
 * </pre>
 * <p>
 * to call the method {@code xyz()} then in BASIC you will be able to call
 * </p>
 * <pre>
 * xyz(s, parameterlist)
 * </pre>
 * <p>
 * The documentation of the methods implemented in {@link java.lang.String} are
 * not repeated here. Other methods, that implement BASIC like string functions
 * not implemented in the class {@code java.lang.String} are documented in this
 * class.
 * </p>
 *
 * @author Peter Verhas
 */
public class StringFunctions {

    /**
     * Chop off the new line character(s) from the end of the string. If there
     * are more than one new line character at the end of the string then all of
     * them are cut off.
     * <p>
     * If there are no new line characters at the end of the string then the
     * unaltered string is returned.
     *
     * @param s the string to chomp
     * @return the string without the trailing new lines
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String chomp(final String s) {
        return s.replaceAll("\\n*$", "");
    }

    /**
     * Returns string for the argument.
     * @param o argument to be converted
     * @return string value
     * @throws BasicRuntimeException failed to convert to string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String cstr(final Object o) throws BasicRuntimeException {
        if (o == null) {
            throw new BasicRuntimeException("Null cannot be converted to string");
        }
        if (o instanceof String) {
            return (String) o;
        }
        return BasicStringValue.asString(RightValueUtility.createRightValue(o));
    }

    /**
     * Trim the white spaces from the start of the string.
     *
     * @param o the string to trim.
     * @return the trimmed string
     * @throws BasicRuntimeException if cannot convert to string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String ltrim(final Object o) throws BasicRuntimeException {
        if(o==null) {
            return null;
        }
        final String s = cstr(o);
        return s.replaceAll("^\\s*", "");
    }

    /**
     * Trim the white spaces from the end of the string.
     *
     * @param o the string to trim
     * @return the trimmed string
     * @throws BasicRuntimeException if cannot convert to string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String rtrim(final Object o) throws BasicRuntimeException {
        if (o == null) {
            return null;
        }
        final String s = cstr(o);
        return s.replaceAll("\\s*$", "");
    }

    /**
     * Return {@code len} number of characters from the left (the beginning) of the
     * string.
     *
     * @param o   parameter
     * @param len parameter
     * @return return value
     * @throws BasicRuntimeException if cannot convert to string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String left(final Object o, final int len) throws BasicRuntimeException {
        if (o == null) {
            return null;
        }
        final String s = cstr(o);
        return s.length() > len ? s.substring(0, len) : s;
    }
    
    /**
     * Return a substring from the string that starts at the position
     * {@code start} and has a length of {@code len}.
     *
     * @param o     parameter
     * @param start parameter
     * @param len   parameter
     * @return return value
     * @throws BasicRuntimeException incorrect parameter
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String mid(final Object o, final int start, final int len) throws BasicRuntimeException {
        if (start < 1) {
            throw new BasicRuntimeException("Incorrect value in parameter start: " + start);
        }
        if (o == null) {
            return null;
        }
        final String s = cstr(o);
        return s.substring(start - 1, start - 1 + len);
    }

    /**
     * Return {@code len} number of characters from the right (the end) of the
     * string.
     *
     * @param o   parameter
     * @param len parameter
     * @return return value
     * @throws BasicRuntimeException if cannot convert to string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String right(final Object o, final int len) throws BasicRuntimeException {
        if (o == null) {
            return null;
        }
        final String s = cstr(o);
        return s.length() > len ? s.substring(s.length() - len) : s;
    }

    /**
     * Return a string that is {@code len} number of space characters.
     *
     * @param len parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class,
            com.scriptbasic.classification.Utility.class})
    static public String space(final int len) {
        return " ".repeat(Math.max(0, len));
    }

    /**
     * Return a string that is {@code len} times the character in {@code s}. If
     * the string {@code s} contains more than one characters then only the
     * first character is repeated.
     *
     * @param len parameter
     * @param s   parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String string(final int len, String s) {
        return s.substring(0, 1).repeat(len);

    }

    /**
     * Return a string with the characters reversed.
     *
     * @param s parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String strreverse(final String s) {
        final var sb = new StringBuilder(s.length());
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s, i, i + 1);
        }
        return sb.toString();
    }

    /**
     * Return a string upper cased.
     *
     * @param s parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String ucase(final String s) {
        return s.toUpperCase();
    }

    /**
     * Return a string lower cased.
     *
     * @param s parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String lcase(final String s) {
        return s.toLowerCase();
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String trim(final Object o) throws BasicRuntimeException {
        if (o == null) {
            return null;
        }
        final String s = cstr(o);
        return s.trim();
    }

    /**
     * Implements the functionality of the method {@code s1.indexOf(s2)}
     *
     * @param s1 parameter
     * @param s2 parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public Long index(final String s1, final String s2) {
        return (long) s1.indexOf(s2);
    }

    /**
     * Implements the functionality of the method {@code s1.lastIndexOf(s2)}
     *
     * @param s1 parameter
     * @param s2 parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public Long lastIndex(final String s1, final String s2) {
        return (long) s1.lastIndexOf(s2);
    }

    /**
     * Implements the functionality of the method {@code s1.indexOf(s2,i)}
     *
     * @param s1 parameter
     * @param s2 parameter
     * @param i  parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public Long indexAfter(final String s1, final String s2, final int i) {
        return (long) s1.indexOf(s2, i);
    }

    /**
     * Implements the functionality of the method {@code s1.lastIndexOf(s2,i)}
     *
     * @param s1 parameter
     * @param s2 parameter
     * @param i  parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public Long lastIndexAfter(final String s1, final String s2, final int i) {
        return (long) s1.lastIndexOf(s2, i);
    }

    /**
     * Returns a one character string that contains the character that is at the
     * position {@code i} in the string {@code s1}.
     *
     * @param s1 parameter
     * @param i  parameter
     * @return return value
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String charAt(final String s1, final int i) {
        final char[] characterArray = new char[1];
        characterArray[0] = s1.charAt(i);
        return new String(characterArray);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String replaceAll(final String s1, final String regex, final String s2) {
        return s1.replaceAll(regex, s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public String replaceFirst(final String s1, final String regex, final String s2) {
        return s1.replaceFirst(regex, s2);
    }

    /**
     * @param s1 parameter
     * @return the length of the string
     */
    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int strlen(final String s1) {
        return s1.length();
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int codePointAt(final String s1, final int i) {
        return s1.codePointAt(i);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int codePointBefore(final String s1, final int i) {
        return s1.codePointBefore(i);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int codePointCount(final String s1, final int i, final int j) {
        return s1.codePointCount(j, j);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int compareTo(final String s1, final String s2) {
        return s1.compareTo(s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public int compareToIgnoreCase(final String s1, final String s2) {
        return s1.compareToIgnoreCase(s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public boolean contains(final String s1, final String s2) {
        return s1.contains(s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public boolean endsWith(final String s1, final String s2) {
        return s1.endsWith(s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public boolean startsWith(final String s1, final String s2) {
        return s1.startsWith(s2);
    }

    @BasicFunction(classification = {com.scriptbasic.classification.String.class})
    static public boolean isEmpty(final String s1) {
        return s1.isEmpty();
    }
}
