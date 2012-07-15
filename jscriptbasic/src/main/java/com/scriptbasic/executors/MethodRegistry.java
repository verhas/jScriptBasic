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
 * Keep a registry of methods. The Java methods (either class or object method)
 * can be called from BASIC. The problem is that Java methods can be overloaded
 * and BASIC functions can not. To overcome this issue the BASIC program has to
 * declare a function name for each method it wants to call. For example:
 * 
 * <pre>
 * method sin from java.lang.Math is (double) use as sinus
 * </pre>
 * 
 * Here the method {@code sin} is to be used from the java package
 * {@code java.lang.Math} and the alias is {@code sinus}. The
 * {@code use as sinus} part is optional. In that case the alias of the function
 * is the original name of the function.
 * <p>
 * If there are two methods of the same name, with different signature, you have
 * to define different aliases for the different methods.
 * <p>
 * Not that you can use the same alias for different methods in different
 * packages.
 * <p>
 * Also note that you can also use aliases for Class names.
 * <p>
 * The registry keeps track of the name of the method, the alias, the class and
 * the signature. Thus whenever there is a function call in BASIC to call a Java
 * method in a certain class this registry will know which signature to use for
 * the certain alias.
 * 
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public class MethodRegistry {

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

    /**
     * Get the method for a given alias used to name a method in a specific Java
     * class.
     * 
     * @param klass
     *            the class where the method is.
     * @param alias
     *            the alias with which the method was registered for a specific
     *            signature.
     * @return the located method that match the name and the signature that was
     *         registered.
     * @throws ExecutionException
     *             if the method can not be accessed, e.g. there is no such
     *             method or accessing the method violates java security
     */
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

    /**
     * Register an item in the global registry so that the program can use it
     * without specifying the class that the method belongs to. This makes it
     * possible to use these methods as simple functions in the BASIC program.
     * On the other hand this mechanism does not protect from name collisions.
     * If two different methods from two different classes use the same alias
     * that they can not be used as function at the same time. In that case the
     * class alias also have to be used in the BASIC program.
     * <p>
     * This method register the item containing the name of the method and also
     * the class and the argument types if a function with that name was not
     * registered yet. If there was already a function with the name registered
     * then it removes the previous registry from the store and registers
     * {@code null} as item, so that neither of he methods can later be used as
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

    /**
     * Register a java method.
     * 
     * @param alias
     *            the alias of the method. This is the name how the BASIC
     *            program will refer to the method.
     * @param klass
     *            the class in which the method is implemented
     * @param methodName
     *            the Java name of the method
     * @param argumentTypes
     *            the argument classes that form the signature of the method
     *            together with the name of the method.
     */
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
