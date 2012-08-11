/**
 *
 */
package com.scriptbasic.utility;
import java.lang.reflect.Method;
import com.scriptbasic.Function;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.ExtensionInterfaceVersion;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
/**
 * @author Peter Verhas
 * date Jul 22, 2012
 *
 */
public class MethodRegisterUtility implements ExtensionInterfaceVersion {
    public MethodRegisterUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }
    private static Logger LOG = LoggerFactory
            .getLogger(MethodRegisterUtility.class);
    static class FunctionLoadParameters {
        Class<?> klass;
        String methodName;
        String alias;
        Class<?>[] parameterTypes;
        Function annotation;
        Class<?>[] classifications;
        void initParameters(Method method, Class<?> klassPar) {
            klass = klassPar;
            methodName = method.getName();
            parameterTypes = method.getParameterTypes();
            annotation = method.getAnnotation(Function.class);
        }
        void setAlias() {
            alias = annotation.alias();
            if (alias.length() == 0) {
                alias = methodName;
            }
        }
        void setMethodName() {
            if (annotation.substituteMethod().length() > 0) {
                methodName = annotation.substituteMethod();
            }
        }
        void setClass() {
            if (annotation.substituteClass() != Function.class) {
                klass = annotation.substituteClass();
            }
        }
        void setClassification() {
            classifications = annotation.classification();
            if (classifications.length == 1
                    && classifications[0] == Function.class) {
                classifications = null;
            }
        }
        boolean versionIsCompatible() {
            long requiredVersion = annotation.requiredVersion();
            if (annotation.requiredVersion() > EXTENSION_INTERFACE_VERSION) {
                LOG.error(
                        "The method {} can not be registered, because it requires the interface version {} and the implemented version is {}.",
                        new Object[] { methodName, requiredVersion,
                                EXTENSION_INTERFACE_VERSION });
            }
            return requiredVersion <= EXTENSION_INTERFACE_VERSION;
        }
        void register(ExtendedInterpreter interpreter) {
            if (versionIsCompatible()) {
                if (classificationsAllowRegistering(interpreter,
                        classifications)) {
                    interpreter.registerJavaMethod(alias, klass, methodName,
                            parameterTypes);
                } else {
                    LOG.info(
                            "Classification prevents the registration of the method {}",
                            methodName);
                }
            }
        }
        boolean isFunctionAnnotated() {
            return annotation != null;
        }
    }
    public static void registerFunctions(Class<?> klass,
            ExtendedInterpreter interpreter) {
        FunctionLoadParameters method = new FunctionLoadParameters();
        for (Method methodObj : klass.getMethods()) {
            method.initParameters(methodObj, klass);
            if (method.isFunctionAnnotated()) {
                method.setAlias();
                method.setMethodName();
                method.setClass();
                method.setClassification();
                method.register(interpreter);
            }
        }
    }
    /**
     * @param classifications
     * @return
     */
    private static boolean classificationsAllowRegistering(
            ExtendedInterpreter interpreter, Class<?>[] classifications) {
        // TODO use the SecurityManager to control the access to the different
        // Java methods
        // Configuration config = interpreter.getConfiguration();
        // for (Class<?> classification : classifications) {
        // if ("deny".equals(config.getConfigValue("classification."
        // + classification.getName(), "allow")))
        // return false;
        // }
        return true;
    }
}