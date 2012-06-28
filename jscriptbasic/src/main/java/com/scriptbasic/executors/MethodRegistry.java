/**
 * 
 */
package com.scriptbasic.executors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;

/**
 * @author Peter Verhas
 * @date Jun 28, 2012
 * 
 */
public class MethodRegistry {

    private static String formKey(String alias, Class<?> klass) {
        return alias + "#" + klass.getCanonicalName();
    }

    private static class RegistryItem {
        String methodName;
        Class<?> klass;
        Class<?>[] args;
    }

    private Map<String, RegistryItem> registry = new HashMap<>();

    /**
     * @param klass
     * @param methodName
     * @return
     * @throws ExecutionException
     */
    public Method getJavaMethod(Class<?> klass, String alias)
            throws ExecutionException {
        RegistryItem item = registry.get(formKey(alias, klass));
        Method method = null;
        if (item != null) {
            try {
                method = item.klass.getMethod(item.methodName, item.args);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new BasicRuntimeException("Method '" + item.methodName
                        + "' from class '" + item.klass
                        + "' can not be accessed", e);
            }
        }
        return method;
    }

    /**
     * @param alias
     * @param klass
     * @param methodName
     * @param argumentTypes
     */
    public void registerJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
        RegistryItem item = new RegistryItem();
        item.methodName = methodName;
        item.klass = klass;
        item.args = argumentTypes.clone();
        registry.put(formKey(alias, klass), item);
    }

}
