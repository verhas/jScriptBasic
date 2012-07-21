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

    // TODO separate this utility class from the one that can be used to
    // register the methods and make the other one configurable via properties,
    // XML and API

    // TODO create function to instantiate an object

    // TODO create mathematical functions for DOUBLE arguments and values

    public static Long abs(Long a) {
        return Math.abs(a);
    }

    // TODO move the functions to a separate class and create code that
    // registers all the function with annotations for a class

    // TODO create function that returns null

    // TODO create functions for regular expression handling

    // TODO create functions using JODA time to handle date and time

    /**
     * Register a static method as BASIC function so that the method can be
     * called from BASIC program. The alias used in BASIC is the same as the
     * name of the method.
     * 
     * @param methodRegistry
     * @param name
     * @param argClasses
     */
    private static void registerFunction(MethodRegistry methodRegistry,
            String name, Class<?>... argClasses) {
        registerFunction(methodRegistry, name, name, argClasses);
    }

    /**
     * Register a static method as BASIC function so that the method can be
     * called from BASIC programs.
     * 
     * @param methodRegistry
     * @param name
     * @param alias
     * @param argClasses
     */
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
