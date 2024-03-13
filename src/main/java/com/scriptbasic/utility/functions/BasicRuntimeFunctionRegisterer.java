package com.scriptbasic.utility.functions;

import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.spi.ClassSetProvider;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.utility.NoInstance;

import java.util.ServiceLoader;
import java.util.Set;

/**
 * Utility class to register the functions implemented in Java and that are
 * available to BASIC programs.
 *
 * @author Peter Verhas
 */
public class BasicRuntimeFunctionRegisterer {
    private static final Logger LOG = LoggerFactory.getLogger();
    private static final Set<Class<?>> basicRuntimeFunctionClasses = Set.of(
            ErrorFunctions.class, StringFunctions.class,
            UtilityFunctions.class, MathFunctions.class,
            DateFunctions.class);

    private BasicRuntimeFunctionRegisterer() {
        NoInstance.isPossible();
    }

    /**
     * Registers the functions that are implemented in Java into the interpreter
     * passed as argument.
     * <p>
     * Register all the functions that are provided on the classpath and the
     * ServiceLoader can find via ClassSetProvider interface.
     *
     * @param interpreter the interpreter to register the functions into.
     */
    public static void registerBasicRuntimeFunctions(final Interpreter interpreter) {
        for (final Class<?> klass : basicRuntimeFunctionClasses) {
            interpreter.registerFunctions(klass);
        }
        ServiceLoader.load(ClassSetProvider.class).forEach(provider ->
                provider.provide().forEach(klass -> {
                    LOG.debug("Registering class {}", klass);
                    interpreter.registerFunctions(klass);
                })
        );
    }
}
