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
 * date Aug 4, 2012
 *
 */
public class HookRegisterUtility {
    public HookRegisterUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }
    private static Logger LOG = LoggerFactory
            .getLogger(HookRegisterUtility.class);
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