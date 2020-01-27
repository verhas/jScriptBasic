package com.scriptbasic.utility.functions;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.classification.Constant;
import com.scriptbasic.classification.System;
import com.scriptbasic.classification.Utility;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.utility.MagicBean;
import com.scriptbasic.utility.NoInstance;
import com.scriptbasic.utility.RightValueUtility;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;

/**
 * Static methods in this class are registered in the interpreter when the
 * interpreter starts. The interpreter calls the static method {@link
 * com.scriptbasic.utility.MethodRegisterUtility#registerFunctions(Class, Interpreter)} and that function
 * registers the methods in this class with their own name so that BASIC
 * programs can call the functions like BASIC built in functions.
 *
 * @author Peter Verhas date July 15, 2012
 */
public class UtilityFunctions {

    private static final Logger BASIC_LOGGER = LoggerFactory.getBasicLogger();

    private UtilityFunctions() {
        NoInstance.isPossible();
    }

    @BasicFunction(alias = "newRecord", classification = System.class)
    public static Object newMagicBean() {
        return new MagicBean();
    }

    @BasicFunction(classification = System.class)
    public static void logError(final String message) {
        BASIC_LOGGER.error(message);
    }

    @BasicFunction(classification = System.class)
    public static void logInfo(final String message) {
        BASIC_LOGGER.info(message);
    }

    @BasicFunction(classification = System.class)
    public static void logDebug(final String message) {
        BASIC_LOGGER.debug(message);
    }

    /**
     * This function returns the undef value.
     *
     * @return the undef value
     */
    @SuppressWarnings("SameReturnValue")
    @BasicFunction(classification = Constant.class)
    static public Object undef() {
        return null;
    }

    /**
     * @param s is some value or object
     * @return true if the parameter is undefined
     */
    @BasicFunction(classification = Constant.class)
    static public Boolean isUndef(final Object s) {
        return s == null;
    }

    /**
     * @param s is some value or object
     * @return true if the parameter is defined (not isUndef).
     */
    @BasicFunction(classification = Constant.class)
    static public Boolean isDefined(final Object s) {
        return s != null;
    }

    /**
     * @param s is some value or object
     * @return true if the parameter is null
     */
    @BasicFunction(classification = Utility.class)
    static public Boolean isNull(final Object s) {
        return s == null;
    }

    /**
     * Create a new byte buffer of length {@code len}.
     *
     * @param len the length of the buffer allocated.
     * @return the new buffer
     */
    @BasicFunction(classification = Utility.class)
    public static byte[] byteBuffer(final int len) {
        return new byte[len];
    }

    @BasicFunction(classification = Utility.class)
    public static Long getByte(final byte[] buffer, final Long i) {
        if (i < 0 || i >= buffer.length)
            return null;
        return (long) (int) buffer[i.intValue()];
    }

    @BasicFunction(classification = Utility.class)
    public static void setByte(final byte[] buffer, final Long i, Long v)
            throws BasicRuntimeException {
        if (v < 0 && v > -128) {
            v += 128;
        }
        if (v < 0 || v > 255) {
            throw new BasicRuntimeException("Byte value is out of range.");
        }
        if ( 0 <= i && i < buffer.length) {
            buffer[i.intValue()] = (byte) v.intValue();
        } else {
            throw new BasicRuntimeException(
                    "Index out of range indexing a byte array from BASIC");
        }
    }

    @BasicFunction(classification = Utility.class)
    public static byte[] getStringBytes(final String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    @BasicFunction(classification = {Utility.class,
            com.scriptbasic.classification.String.class})
    public static String stringifyBuffer(final byte[] buffer) {
        return new String(buffer, StandardCharsets.UTF_8);
    }

    @BasicFunction(classification = Utility.class, requiredVersion = 2L)
    public static Long length(final Object arg) {
        if (arg == null) {
            return null;
        } else if (arg instanceof BasicArray) {
            final var array = (BasicArray) arg;
            return array.getLength();
        } else if (arg instanceof String) {
            final var string = (String) arg;
            return (long) string.length();
        } else if (arg.getClass().isArray()) {
            return (long) Array.getLength(arg);
        }
        return null;
    }

    @BasicFunction(classification = Utility.class)
    public static Double cdbl(final Object arg) throws BasicRuntimeException {
        if (arg == null) {
            throw new BasicRuntimeException("undef cannot be converted to double");
        }
        return BasicDoubleValue.asDouble(RightValueUtility.createRightValue(arg));
    }

    @BasicFunction(classification = Utility.class)
    static public Long clng(final Object arg) throws BasicRuntimeException {
        if (arg == null) {
            throw new BasicRuntimeException("undef cannot be converted to long");
        }
        return BasicLongValue.asLong(RightValueUtility.createRightValue(arg));
    }

    /**
     * Returns a String containing the character associated with the specified
     * character code.
     * 
     * @param charcode
     *            argument is a Long that identifies a character
     * @return character
     * @throws BasicRuntimeException
     *             fail if incorrect character code
     */
    @BasicFunction(classification = Utility.class)
    static public String chr(final Object charcode) throws BasicRuntimeException {
        if (charcode == null) {
            throw new BasicRuntimeException("undef cannot be converted to character");
        }
        final Long code = BasicLongValue.asLong(RightValueUtility.createRightValue(charcode));
        try {
            return Character.toString(code.intValue());
        } catch (IllegalArgumentException e) {
            throw new BasicRuntimeException("Invalid character code: " + code);
        }
    }

    /**
     * Returns an Integer representing the character code corresponding
     * to the first letter in a string.
     * 
     * @param arg
     *            string argument
     * @return character code corresponding to the first letter
     * @throws BasicRuntimeException
     *             fail on empty input
     */
    @BasicFunction(classification = Utility.class)
    static public Integer asc(final Object arg) throws BasicRuntimeException {
        if (arg == null) {
            throw new BasicRuntimeException("undef cannot be converted to code");
        }
        final String str = BasicStringValue.asString(RightValueUtility.createRightValue(arg));
        if (str == null || str.length() == 0) {
            throw new BasicRuntimeException("empty string cannot be converted to code");
        }
        return Integer.valueOf(str.charAt(0));
    }
}
