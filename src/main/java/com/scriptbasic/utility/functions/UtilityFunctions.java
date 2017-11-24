package com.scriptbasic.utility.functions;

import com.scriptbasic.api.Function;
import com.scriptbasic.classification.Constant;
import com.scriptbasic.classification.System;
import com.scriptbasic.classification.Utility;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.utility.MagicBean;
import com.scriptbasic.utility.NoInstance;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

/**
 * Static methods in this class are registered in the interpreter when the
 * interpreter starts. The interpreter calls the static method {@link
 * com.scriptbasic.utility.MethodRegisterUtility#registerFunctions(Class, ExtendedInterpreter)} and that function
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

    @Function(alias = "newRecord", classification = System.class)
    public static Object newMagicBean() {
        return new MagicBean();
    }

    @Function(classification = System.class)
    public static void logError(String message) {
        BASIC_LOGGER.error(message);
    }

    @Function(classification = System.class)
    public static void logInfo(String message) {
        BASIC_LOGGER.info(message);
    }

    @Function(classification = System.class)
    public static void logDebug(String message) {
        BASIC_LOGGER.debug(message);
    }

    /**
     * This function returns the undef value.
     *
     * @return the undef value
     */
    @Function(classification = Constant.class)
    static public Object undef() {
        return null;
    }

    /**
     * @param s is some value or object
     * @return true if the parameter is undefined
     */
    @Function(classification = Constant.class)
    static public Boolean isUndef(Object s) {
        return s == null;
    }

    /**
     * @param s is some value or object
     * @return true if the parameter is defined (not isUndef).
     */
    @Function(classification = Constant.class)
    static public Boolean isDefined(Object s) {
        return s != null;
    }

    /**
     * Create a new byte buffer of length {@code len}.
     *
     * @param len the length of the buffer allocated.
     * @return the new buffer
     */
    @Function(classification = Utility.class)
    public static byte[] byteBuffer(int len) {
        return new byte[len];
    }

    @Function(classification = Utility.class)
    public static Long getByte(byte[] buffer, Long i) {
        if (i < 0 || i >= buffer.length)
            return null;
        return (long) (int) buffer[i.intValue()];
    }

    @Function(classification = Utility.class)
    public static void setByte(byte[] buffer, Long i, Long v)
            throws BasicRuntimeException {
        if (v < 0 && v > -128) {
            v += 128;
        }
        if (v < 0 || v > 255) {
            throw new BasicRuntimeException("Byte value is out of range.");
        }
        if (i >= 0 || i < buffer.length) {
            buffer[i.intValue()] = (byte) v.intValue();
        } else {
            throw new BasicRuntimeException(
                    "Index out of range indexing a byte array from BASIC");
        }
    }

    @Function(classification = Utility.class)
    public static byte[] getStringBytes(String s)
            throws UnsupportedEncodingException {
        return s.getBytes("utf-8");
    }

    @Function(classification = {Utility.class,
            com.scriptbasic.classification.String.class})
    public static String stringifyBuffer(byte[] buffer)
            throws UnsupportedEncodingException {
        return new String(buffer, "utf-8");
    }

    @Function(classification = Utility.class, requiredVersion = 2L)
    public static Long length(Object arg) {
        if (arg == null) {
            return null;
        } else if (arg instanceof BasicArrayValue) {
            BasicArrayValue array = (BasicArrayValue) arg;
            return array.getLength();
        } else if (arg instanceof String) {
            String string = (String) arg;
            return (long) string.length();
        } else if (arg.getClass().isArray()) {
            return (long) Array.getLength(arg);
        }
        return null;
    }

}
