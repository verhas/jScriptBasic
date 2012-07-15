/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.executors.MethodRegistry;

/**
 * Static methods in this class are registered in the interpreter when the
 * interpreter starts. The interpreter calls the static method {@see
 * #registerFunctions(MethodRegistry)} and that function registers the methods
 * in this class with their own name so that BASIC programs can call the
 * functions like BASIC built in functions.
 * 
 * @author Peter Verhas
 * @date Jul 15, 2012
 * 
 */
public class RuntimeUtility {
    private RuntimeUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    public static Long abs(Long a) {
        return Math.abs(a);
    }

    /**
     * @param methodRegistry
     */
    public static void registerFunctions(MethodRegistry methodRegistry) {
        methodRegistry.registerJavaMethod("abs", RuntimeUtility.class, "abs",
                new Class<?>[] { Long.class });
    }
}
