/**
 * 
 */
package com.scriptbasic.utility;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.Function;
import com.scriptbasic.executors.MethodRegistry;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.ExtensionInterfaceVersion;

/**
 * @author Peter Verhas
 * @date Jul 22, 2012
 * 
 */
public class MethodRegisterUtility implements ExtensionInterfaceVersion {
    public MethodRegisterUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    private static Logger LOG = LoggerFactory
            .getLogger(MethodRegisterUtility.class);

    public static void registerFunctions(Class<?> klass,
            ExtendedInterpreter interpreter) {
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            Class<?>[] argumentTypes = method.getParameterTypes();
            Function annotation = method.getAnnotation(Function.class);
            if (annotation != null) {
                String alias = annotation.alias();
                if (alias.length() == 0) {
                    alias = methodName;
                }
                if (annotation.substituteMethod().length() > 0) {
                    methodName = annotation.substituteMethod();
                }
                if (annotation.substituteClass() != Function.class) {
                    klass = annotation.substituteClass();
                }
                Class<?>[] classifications = annotation.classification();
                long requiredVersion = annotation.requiredVersion();
                if (requiredVersion > EXTENSION_INTERFACE_VERSION) {
                    LOG.error(
                            "The method {} can not be registered, because it requires the interface version {} and the implemented version is {}.",
                            new Object[] { methodName, requiredVersion,
                                    EXTENSION_INTERFACE_VERSION });
                } else if (classificationsAllowRegistering(classifications)) {
                    interpreter.registerJavaMethod(alias, klass, methodName,
                            argumentTypes);
                } else {
                    LOG.info(
                            "Classification prevents the registration of the method {}",
                            method.getName());
                }
            }
        }
    }

    /**
     * @param classifications
     * @return
     */
    private static boolean classificationsAllowRegistering(
            Class<?>[] classifications) {
        // TODO implement a logic that lets the user configure which methods may
        // be registered and which are those that are not to be registered
        return true;
    }
}
