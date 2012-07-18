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

    // TODO create mathematical functions for DOUBLE arguments and values

    public static Long abs(Long a) {
        return Math.abs(a);
    }

    // TODO create functions for regular expression handling

    private static void registerFunction(MethodRegistry methodRegistry,
            String name, Class<?>... argClasses) {
        registerFunction(methodRegistry, name, name, argClasses);
    }

    private static void registerFunction(MethodRegistry methodRegistry,
            String name, String alias, Class<?>... argClasses) {
        methodRegistry.registerJavaMethod(name, RuntimeUtility.class, alias,
                argClasses);
    }

    /**
     * @param methodRegistry
     */
    public static void registerFunctions(MethodRegistry methodRegistry) {
        registerFunction(methodRegistry, "abs", Long.class);
    }
}
