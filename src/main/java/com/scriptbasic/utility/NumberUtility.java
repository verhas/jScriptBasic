package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

/**
 * Utility class to compare Number variables, Long or Double.
 *
 * @author Peter Verhas
 * date Jul 21, 2012
 */
public class NumberUtility {
    final private static Logger LOG = LoggerFactory
            .getLogger();

    private NumberUtility() {
        NoInstance.isPossible();
    }

    private static void assertPreconditions(final Number a, final Number b) {
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

    public static boolean isPositive(final Number a) {
        LOG.debug("a {} is instance of Long = {}", a, a instanceof Long);
        final Number zero;
        if (a instanceof Long) {
            zero = 0L;
        } else {
            zero = 0.0;
        }
        LOG.debug(
                "Calling compare with arguments '{}' '{}' of classes '{}' and '{}'",
                a, zero, a.getClass(), zero.getClass());
        return compare(a, zero) > 0;
    }

    /**
     * Compares the numbers a and b.
     *
     * @param a one of the numbers
     * @param b the other number
     * @return 0 if a == b; 1 if a &gt; b; and -1 if b &lt; a
     */
    public static int compare(final Number a, final Number b) {
        if (a == null && b == null) {
            return 0;
        }
        assertPreconditions(a, b);
        final int retval;
        if (a instanceof Double) {
            final Double aA = (Double) a;
            final Double bB = (Double) b;
            if (aA > bB) {
                retval = 1;
            } else if (aA.equals(bB)) {
                retval = 0;
            } else {
                retval = -1;
            }
        } else {
            final Long aA = (Long) a;
            final Long bB = (Long) b;
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
