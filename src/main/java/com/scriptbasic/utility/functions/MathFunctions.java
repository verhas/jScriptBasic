package com.scriptbasic.utility.functions;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.utility.CastUtility;
import com.scriptbasic.utility.NoInstance;

/**
 * This class contains static methods that are place holders to register the
 * methods of the class {@code java.lang.Math} for the BASIC programs. If the
 * documentation of the method does not specify different then the method in
 * this class can be invoked directly from a BASIC program and does exactly the
 * same as the method of the same name in the class {@code java.lang.Math}.
 * <p>
 * When a method accepts {@code Number} arguments it means that the BASIC can
 * call the function with integer or with floating point arguments.
 *
 * @author Peter Verhas
 */
public class MathFunctions {

    private MathFunctions() {
        NoInstance.isPossible();
    }

    @BasicFunction(classification = com.scriptbasic.classification.Math.class)
    static public Number abs(Number x) throws ExecutionException {
        if (x instanceof Double) {
            return Math.abs((Double) x);
        }
        if (x instanceof Long) {
            return ((Long) x) > 0 ? x : -((Long) x);

        }
        throw new BasicRuntimeException(
                "MathFunctions.abs(Number x) was called with an argument that is neither Double, nor Long: "
                        + x.toString() + "of type " + x.getClass().toString());
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double acos(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double asin(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double atan(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double atan2(double x, double y) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double cbrt(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double ceil(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double copySign(double magnitude, double sign) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double cos(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double cosh(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double exp(double a) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double expm1(double x) {
        return 0.0;
    }

    /**
     * Convert a value to floating point. This method can be called directly
     * from the BASIC program and it will return a java Double, that is
     * internally a BASIC float. (Note that ScriptBasic for Java does not use
     * float or int, only Long and Double.)
     * <p>
     * Use this function from the BASIC program if the BASIC interpreter does
     * not convert automatically a number to floating point but you need a
     * floating point number and not an integer.
     *
     * @param s the number to convert
     * @return the converted number or {@code null}, which means undefined in
     * BASIC in case the parameter {@code s} passed can not be converted
     * to floating point
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Utility.class}, alias = "float")
    static public Double floatF(Object s) {
        try {
            return (Double) CastUtility.cast(s, Double.class);
        } catch (ClassCastException cce) {
            return null;
        }
    }

    /**
     * Convert a value to integer. This method can be called directly from the
     * BASIC program and it will return a java Long, that is internally a BASIC
     * integer. (Note that ScriptBasic for Java does not use float or int, only
     * Long and Double.)
     * <p>
     * Use this function from the BASIC program if the BASIC interpreter does
     * not convert automatically a number to integer but you need an integer
     * number and not a floating point number.
     *
     * @param s the number to convert
     * @return the converted number or {@code null}, which means undefined in
     * BASIC in case the parameter {@code s} passed can not be converted
     * to integer
     */
    @BasicFunction(classification = {com.scriptbasic.classification.Utility.class})
    static public Long integer(Object s) {
        try {
            return (Long) CastUtility.cast(s, Long.class);
        } catch (ClassCastException cce) {
            return null;
        }
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double floor(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static Long getExponent(double d) {
        return (Long) (long) Math.getExponent(d);
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double hypot(double x, double y) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double IEEEremainder(double f1, double f2) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double log(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double log10(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double log1p(double x) {
        return 0.0;
    }

    @BasicFunction(classification = com.scriptbasic.classification.Math.class)
    public static Number max(Number a, Number b) {
        if (a instanceof Double && b instanceof Double) {
            return Math.max((Double) a, (Double) b);
        }
        if (a instanceof Long && b instanceof Long) {
            return Math.max((Long) a, (Long) b);

        }
        return null;
    }

    @BasicFunction(classification = com.scriptbasic.classification.Math.class)
    public static Number min(Number a, Number b) {
        if (a instanceof Double && b instanceof Double) {
            return Math.min((Double) a, (Double) b);
        }
        if (a instanceof Long && b instanceof Long) {
            return Math.min((Long) a, (Long) b);

        }
        return null;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double pow(double a, double b) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double random() {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double rint(double a) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double round(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double scalb(double d, int scaleFactor) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double signum(double d) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double sin(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double sinh(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double sqrt(double a) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double tan(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double tanh(double x) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double toDegrees(double angrad) {
        return 0.0;
    }

    @BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    public static double toRadians(double angdeg) {
        return 0.0;
    }
}
