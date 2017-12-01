package com.scriptbasic.utility;

import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.InterpreterHook;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

import java.lang.reflect.Constructor;

import static com.scriptbasic.utility.ConvertUtils.$;
import static com.scriptbasic.utility.ConvertUtils.cast;

/**
 * @author Peter Verhas
 * date Aug 4, 2012
 */
public class HookRegisterUtility {
    private static Logger LOG = LoggerFactory.getLogger();

    private HookRegisterUtility() {
        NoInstance.isPossible();
    }

    /**
     * Create hook instances and chain them up based on the configuration file.
     * The configuration contains {@code hook.0}, {@code hook.1} ... properties
     * keys and their value is the name of the class that implements the {@link
     * InterpreterHook} interface.
     * <p>
     * The method loads the classes, creates a new instance of each configured
     * class and registers the new object in the interpreter.
     * <p>
     * Note that registering does not include the call to the method {@link
     * InterpreterHook#init()}. It is executed when all the hook objects are
     * chained and the {@code init()} invocation already uses the normal
     * chaining.
     *
     * @param interpreter the interpter in which the hooks are registered
     */
    public static void registerHooks(final ExtendedInterpreter interpreter) {
        interpreter.getConfiguration().getConfigValueStream("hook")
                .map($(Class::forName))
                .map($(Class::getDeclaredConstructor))
                .map($(Constructor::newInstance))
                .map(x -> cast(x, InterpreterHook.class))
                .forEach(interpreter::registerHook);
    }
}
