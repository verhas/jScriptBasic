package com.scriptbasic.utility.functions;

import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.utility.NoInstance;

/**
 * Utility class to register the functions implemented in Java and that are
 * available to BASIC programs.
 *
 * @author Peter Verhas
 */
public class BasicRuntimeFunctionRegisterer {
    private static final Class<?>[] basicRuntimeFunctionClasses = new Class<?>[]{
            ErrorFunctions.class, StringFunctions.class,
            UtilityFunctions.class, MathFunctions.class,};

    private BasicRuntimeFunctionRegisterer() {
        NoInstance.isPossible();
    }

    /**
     * Registers the functions that are implemented in Java into the interpreter
     * passed as argument.
     *
     * @param interpreter the interpreter to register the functions into.
     */
    public static void registerBasicRuntimeFunctions(final Interpreter interpreter) {
        for (final Class<?> klass : basicRuntimeFunctionClasses) {
            interpreter.registerFunctions(klass);
        }
    }
}
