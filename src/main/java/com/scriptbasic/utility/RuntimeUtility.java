/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.Function;
import com.scriptbasic.classification.Constant;
import com.scriptbasic.classification.System;

/**
 * Static methods in this class are registered in the interpreter when the
 * interpreter starts. The interpreter calls the static method {@see
 * MethodRegisterUtility#registerFunctions(MethodRegistry)} and that function
 * registers the methods in this class with their own name so that BASIC
 * programs can call the functions like BASIC built in functions.
 * 
 * @author Peter Verhas date July 15, 2012
 * 
 */
public class RuntimeUtility {

	private RuntimeUtility() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}

	@Function(classification = com.scriptbasic.classification.Math.class)
	static public Number abs(Number x) {
		if (x instanceof Double) {
			return Math.abs((Double) x);
		}
		if (x instanceof Long) {
			return ((Long) x) > 0 ? x : -((Long) x);

		}
		return null;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double acos(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double asin(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double atan(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double atan2(double x, double y) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double cbrt(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double ceil(double x) {
		return 0.0;
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String chomp(String s) {
		return s.replaceAll("\\n*$", "");
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String ltrim(String s) {
		return s.replaceAll("^\\s*", "");
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String rtrim(String s) {
		return s.replaceAll("\\s*$", "");
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String mid(String s, int start, int len) {
		return s.substring(start, start + len);
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String right(String s, int len) {
		return s.length() > len ? s.substring(s.length() - len) : s;
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String space(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String string(int len, String s) {
		s = s.substring(0, 1);
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String strreverse(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = s.length(); i > 0; i--) {
			sb.append(s.substring(i, i + 1));
		}
		return sb.toString();
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String ucase(String s) {
		return s.toUpperCase();
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String lcase(String s) {
		return s.toLowerCase();
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double copySign(double magnitude, double sign) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double cos(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double cosh(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double exp(double a) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double expm1(double x) {
		return 0.0;
	}

	@Function(classification = { com.scriptbasic.classification.Utility.class }, alias = "float")
	static public Double floatF(Object s) {
		try {
			return (Double) CastUtility.cast(s, Double.class);
		} catch (ClassCastException cce) {
			return null;
		}
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double floor(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static Long getExponent(double d) {
		return (Long) (long) Math.getExponent(d);
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double hypot(double x, double y) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double IEEEremainder(double f1, double f2) {
		return 0.0;
	}

	@Function(classification = { com.scriptbasic.classification.Utility.class })
	static public Long integer(Object s) {
		try {
			return (Long) CastUtility.cast(s, Long.class);
		} catch (ClassCastException cce) {
			return null;
		}
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double log(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double log10(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double log1p(double x) {
		return 0.0;
	}

	@Function(classification = com.scriptbasic.classification.Math.class)
	public static Number max(Number a, Number b) {
		if (a instanceof Double && b instanceof Double) {
			return Math.max((Double) a, (Double) b);
		}
		if (a instanceof Long && b instanceof Long) {
			return Math.max((Long) a, (Long) b);

		}
		return null;
	}

	@Function(classification = com.scriptbasic.classification.Math.class)
	public static Number min(Number a, Number b) {
		if (a instanceof Double && b instanceof Double) {
			return Math.min((Double) a, (Double) b);
		}
		if (a instanceof Long && b instanceof Long) {
			return Math.min((Long) a, (Long) b);

		}
		return null;
	}

	/**
	 * This method can be used to call the default (parameter less) constructor
	 * of a class. This, of course, can only be used for classes that have
	 * default constructor.
	 * <p>
	 * 
	 * 
	 * @param klass
	 *            the class to instantiate
	 * @return the new object instance
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Function(alias = "new", classification = System.class)
	public static Object newObject(String klass) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return Class.forName(klass).newInstance();
	}

	@Function(alias = "undef", classification = Constant.class)
	static public Object nullFunction() {
		return null;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double pow(double a, double b) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double random() {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double rint(double a) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double round(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double scalb(double d, int scaleFactor) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double signum(double d) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double sin(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double sinh(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double sqrt(double a) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double tan(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	static public double tanh(double x) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double toDegrees(double angrad) {
		return 0.0;
	}

	@Function(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
	public static double toRadians(double angdeg) {
		return 0.0;
	}

	@Function(classification = { com.scriptbasic.classification.String.class,
			com.scriptbasic.classification.Utility.class })
	static public String trim(String s) {
		return s.trim();
	}

	// TODO create functions for regular expression handling

}
