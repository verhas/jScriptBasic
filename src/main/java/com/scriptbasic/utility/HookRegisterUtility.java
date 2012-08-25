/**
 * 
 */
package com.scriptbasic.utility;

import java.util.List;

import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.InterpreterHook;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

/**
 * @author Peter Verhas
 * @date Aug 4, 2012
 * 
 */
public class HookRegisterUtility {
    private HookRegisterUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    private static Logger LOG = LoggerFactory
            .getLogger(HookRegisterUtility.class);

    /**
     * Create hook instances and chain them up based on the configuration file.
     * The configuration contains {@code hook.0}, {@code hook.1} ... properties
     * keys and their value is the name of the class that implements the {@see
     * com.scriptbasic.interface.InterpreterHook} interface.
     * <p>
     * The method loads the classes, creates a new instance of each configured
     * class and registers the new object in the interpreter.
     * <p>
     * Note that registering does not include the call to the method {@see
     * InterpreterHook#init()}. It is executed when all the hook objects are
     * chained and the {@code init()} invocation already uses the normal
     * chaining.
     * 
     * @param interpreter
     */
    public static void registerHooks(ExtendedInterpreter interpreter) {
        List<String> hookClassNameList = interpreter.getConfiguration()
                .getConfigValueList("hook");
        if (hookClassNameList != null) {
            for (String hookClassName : hookClassNameList) {
                InterpreterHook hook = null;
                try {
                    hook = (InterpreterHook) Class.forName(hookClassName)
                            .newInstance();
                } catch (InstantiationException | IllegalAccessException
                        | ClassNotFoundException e) {
                    LOG.error("Can not register the hook configured as '"
                            + hookClassName + "'");
                }
                if (hook != null) {
                    interpreter.registerHook(hook);
                }
            }
        }
    }
}
