package com.scriptbasic.utility.functions;

import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.utility.UtilityUtility;

/**
 * 
 * Utility class to register the functions implemented in Java and that are
 * available to BASIC programs.
 * 
 * @author Peter Verhas
 * 
 */
public class BasicRuntimeFunctionRegisterer {
	private BasicRuntimeFunctionRegisterer() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}

	private static final Class<?>[] basicRuntimeFunctionClasses = new Class<?>[] {
			ErrorFunctions.class, StringFunctions.class,
			UtilityFunctions.class, MathFunctions.class, };

	/**
	 * Registers the functions that are implemented in Java into the interpreter
	 * passed as argument.
	 * 
	 * @param interpreter
	 *            the interpreter to register the functions into.
	 */
	public static void registerBasicRuntimeFunctions(Interpreter interpreter) {
		for (Class<?> klass : basicRuntimeFunctionClasses) {
			interpreter.registerFunctions(klass);
		}
	}
}
