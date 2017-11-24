package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.MethodRegistry;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicMethodRegistry implements MethodRegistry {

    private Map<String, RegistryItem> registry = new HashMap<>();
    private Map<String, RegistryItem> globalRegistry = new HashMap<>();

    private static String formKey(String alias, Class<?> klass) {
        return alias + "#" + klass.getName().replaceAll("\\$", ".");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.executors.MethodRegistry#getJavaMethod(java.lang.Class,
     * java.lang.String)
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
                } catch (Exception e) {
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
            } catch (Exception e) {
                throw new BasicRuntimeException("Method '" + item.methodName
                        + "' from class '" + item.klass
                        + "' can not be accessed", e);
            }
        }
        return method;
    }

    /**
     * Compare two global registry items to check if they define the same static
     * java method.
     * <p>
     * Check that the item in the global registry of Java functions that can be
     * called from BASIC is the same for this alias as the one provided now. In
     * that case the METHOD declaration in the BASIC code is a harmless double
     * definition. On the other hand one alias can not be used for several java
     * methods. If the class of the method, the name of the method or the
     * argument list class type is not the same then the two definitions are not
     * the same.
     *
     * @param a one of the items
     * @param b the other item to be compared
     * @return {@code true} if the two items define the same Java method.
     */
    private boolean definitionIsTheSame(RegistryItem a, RegistryItem b) {
        return a.methodName.equals(b.methodName) && a.klass.equals(b.klass)
                && Arrays.equals(a.args, b.args);
    }

    /**
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
     * then it throws a BasicRuntimeException explaining the name collision.
     * <p>
     * Even though the functionality of evaluation of function calls is not
     * implemented in class or in this package it is good to note that if a
     * BASIC subroutine is defined with the same name as an alias then that will
     * rule.
     *
     * @param alias
     * @param item
     * @throws BasicRuntimeException
     */
    private void registerGlobal(String alias, RegistryItem item)
            throws BasicRuntimeException {
        if (globalRegistry.containsKey(alias)
                && !definitionIsTheSame(globalRegistry.get(alias), item)) {
            throw new BasicRuntimeException("The BASIC function alias '"
                    + alias
                    + " can not be defined to point to the Java method '"
                    + item.toString() + " because it is already defined to be "
                    + globalRegistry.get(alias).toString());
        } else {
            globalRegistry.put(alias, item);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.executors.MethodRegistry#registerJavaMethod(java.lang
     * .String, java.lang.Class, java.lang.String, java.lang.Class)
     */
    @Override
    public void registerJavaMethod(String alias, Class<?> klass,
                                   String methodName, Class<?>[] argumentTypes)
            throws BasicRuntimeException {
        RegistryItem item = new RegistryItem();
        item.methodName = methodName;
        item.klass = klass;
        item.args = argumentTypes.clone();
        registry.put(formKey(alias, klass), item);
        registerGlobal(alias, item);
    }

    private static class RegistryItem {
        private String methodName;
        private Class<?> klass;
        private Class<?>[] args;

        public String toString() {
            return new StringBuilder(klass + "." + methodName + "(")
                    .append(Arrays.stream(args)
                            .map(Class::getName)
                            .collect(Collectors.joining(",")))
                    .append(")").toString();
        }
    }

}
