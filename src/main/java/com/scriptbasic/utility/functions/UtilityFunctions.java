/**
 * 
 */
package com.scriptbasic.utility.functions;

import java.io.UnsupportedEncodingException;

import com.scriptbasic.Function;
import com.scriptbasic.classification.Constant;
import com.scriptbasic.classification.System;
import com.scriptbasic.classification.Utility;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.utility.UtilityUtility;

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
public class UtilityFunctions {

	private UtilityFunctions() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}

	/**
	 * This method can be used to call the default (parameter less) constructor
	 * of a class. This, of course, can only be used for classes that have
	 * parameter-less constructor.
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

	/**
	 * This function returns the undef value.
	 * 
	 * @return
	 */
	@Function(classification = Constant.class)
	static public Object undef() {
		return null;
	}

	/**
	 * 
	 * @param s
	 * @return true if the parameter is undefined
	 */
	@Function(classification = Constant.class)
	static public Boolean isUndef(Object s) {
		return s == null;
	}

	/**
	 * 
	 * @param s
	 * @return true if the parameter is defined (not isUndef).
	 */
	@Function(classification = Constant.class)
	static public Boolean isDefined(Object s) {
		return s != null;
	}

	/**
	 * Create a new byte buffer of length {@code len}.
	 * 
	 * @param len
	 *            the length of the buffer allocated.
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
		return new Long((int) buffer[i.intValue()]);
	}

	@Function(classification = Utility.class)
	public static void setByte(byte[] buffer, Long i, Long v)
			throws BasicRuntimeException {
		if (v < 0) {
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

	@Function(classification = { Utility.class,
			com.scriptbasic.classification.String.class })
	public static String stringifyBuffer(byte[] buffer)
			throws UnsupportedEncodingException {
		return new String(buffer, "utf-8");
	}

	@Function(classification = Utility.class)
	public static Long length(Object arg) {
		if (arg instanceof BasicArrayValue) {
			BasicArrayValue array = (BasicArrayValue) arg;
			return array.getLength();
		}
		if (arg instanceof String) {
			String string = (String) arg;
			return new Long(string.length());
		}
		return null;
	}

}
