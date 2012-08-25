/**
 * 
 */
package com.scriptbasic.executors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.MethodRegistry;

public class BasicMethodRegistry implements MethodRegistry {

    private static String formKey(String alias, Class<?> klass) {
        return alias + "#" + klass.getName().replaceAll("\\$", ".");
    }

    private static class RegistryItem {
        private String methodName;
        private Class<?> klass;
        private Class<?>[] args;
    }

    private Map<String, RegistryItem> registry = new HashMap<>();
    private Map<String, RegistryItem> globalRegistry = new HashMap<>();

    /* (non-Javadoc)
     * @see com.scriptbasic.executors.MethodRegistry#getJavaMethod(java.lang.Class, java.lang.String)
     */
    @Override
    public Method getJavaMethod(Class<?> klass, String alias)
            throws ExecutionException {
        Method method = null;
        if (klass == null) {
            method = getJavaMethod(alias);
        } else {
            RegistryItem item = registry.get(formKey(alias, klass));
            if (item != null) {
                try {
                    method = item.klass.getMethod(item.methodName, item.args);
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new BasicRuntimeException("Method '"
                            + item.methodName + "' from class '" + item.klass
                            + "' can not be accessed", e);
                }
            }
        }
        return method;
    }

    private Method getJavaMethod(String alias) throws ExecutionException {
        RegistryItem item = globalRegistry.get(alias);
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

    /*
     * Register an item in the global registry so that the program can use it
     * without specifying the class that the method belongs to. This makes it
     * possible to use these methods as simple functions in the BASIC program.
     * On the other hand this mechanism does not protect from name collisions.
     * If two different methods from two different classes use the same alias
     * then they can not be used as functions at the same time. In that case the
     * class alias also have to be used in the BASIC program.
     * <p>
     * This method registers the item containing the name of the method and also
     * the class and the argument types if a function with that name was not
     * registered yet. If there was already a function with the name registered
     * then it removes the previous registry from the store and registers
     * {@code null} as item, so that none of the methods can later be used as
     * simple functions.
     * <p>
     * Even though the functionality of evaluation of function calls is not
     * implemented in class or in this package if is good to note that if a
     * BASIC subroutine is defined with the same name as an alias then that will
     * rule.
     * 
     * @param alias
     * @param item
     */
    private void registerGlobal(String alias, RegistryItem item) {
        if (globalRegistry.containsKey(alias)) {
            globalRegistry.put(alias, null);
        } else {
            globalRegistry.put(alias, item);
        }
    }

    /* (non-Javadoc)
     * @see com.scriptbasic.executors.MethodRegistry#registerJavaMethod(java.lang.String, java.lang.Class, java.lang.String, java.lang.Class)
     */
    @Override
    public void registerJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
        RegistryItem item = new RegistryItem();
        item.methodName = methodName;
        item.klass = klass;
        item.args = argumentTypes.clone();
        registry.put(formKey(alias, klass), item);
        registerGlobal(alias, item);
    }

}
