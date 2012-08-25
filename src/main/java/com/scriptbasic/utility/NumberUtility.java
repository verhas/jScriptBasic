package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

/**
 * Utility class to compare Number variables, Long or Double.
 * 
 * @author Peter Verhas
 * @date Jul 21, 2012
 * 
 */
public class NumberUtility {
    final private static Logger LOG = LoggerFactory
            .getLogger(NumberUtility.class);

    private NumberUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    private static void assertPreconditions(Number a, Number b) {
        if (a == null || b == null) {
            throw new BasicInterpreterInternalError(
                    "Can not compare null to something");
        }
        if (a.getClass() != b.getClass()) {
            throw new BasicInterpreterInternalError(
                    "Can not compare different number types: " + a.getClass()
                            + " != " + b.getClass());
        }
        if (!(a instanceof Long) && !(a instanceof Double)) {
            throw new BasicInterpreterInternalError(
                    "Only double and long can be compared as Numbers");
        }
    }

    public static boolean isPositive(Number a) {
        LOG.debug("a {} is instance of Long = {}", a, a instanceof Long);
        //
        // Number zero = a instanceof Long ? 0L : 0.0;
        //
        // The 1.7.0_04 and the 1.6.0_33 javac compiler generated code that
        // always resulted double:
        //
        // 0: aload_0
        // 1: instanceof #2 // class java/lang/Long
        // 4: ifeq 11
        // 7: dconst_1
        // 8: goto 12
        // 11: dconst_1
        // 12: invokestatic #3 // Method
        // java/lang/Double.valueOf:(D)Ljava/lang/Double;
        // 15: areturn
        //
        //
        Number zero;
        if (a instanceof Long) {
            zero = 0L;
        } else {
            zero = 0.0;
        }
        LOG.debug(
                "Calling compare with arguments '{}' '{}' of classes '{}' and '{}'",
                new Object[] { a, zero, a.getClass(), zero.getClass() });
        return compare(a, zero) > 0;
    }

    /**
     * Compares the numbers a and b.
     * 
     * @param a
     *            one of the numbers
     * @param b
     *            the other number
     * @return 0 if a == b; 1 if a > b; and -1 if b < a
     */
    public static int compare(Number a, Number b) {
        if (a == null && b == null) {
            return 0;
        }
        assertPreconditions(a, b);
        int retval;
        if (a instanceof Double) {
            Double aA = (Double) a;
            Double bB = (Double) b;
            if (aA > bB) {
                retval = 1;
            } else if (aA.equals(bB)) {
                retval = 0;
            } else {
                retval = -1;
            }
        } else {
            Long aA = (Long) a;
            Long bB = (Long) b;
            if (aA > bB) {
                retval = 1;
            } else if (aA.equals(bB)) {
                retval = 0;
            } else {
                retval = -1;
            }
        }
        return retval;
    }
}
