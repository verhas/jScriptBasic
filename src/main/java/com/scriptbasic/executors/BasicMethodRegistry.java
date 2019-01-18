package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.MethodRegistry;
import com.scriptbasic.spi.Interpreter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicMethodRegistry implements MethodRegistry {

    private final Map<String, RegistryItem> registry = new HashMap<>();
    private final Map<String, RegistryItem> globalRegistry = new HashMap<>();

    private static String formKey(final String alias, final Class<?> klass) {
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
    public Method getJavaMethod(final Class<?> klass, final String alias) {
        final Method method;
        final var item = getRegistryItem(klass, alias);
        if (item == null) {
            method = null;
        } else {//TODO check why ntelliJ analysis says that this is always null
            if (item.method == null) {
                final Class<?>[] args = new Class[item.args.length + 1];
                args[0] = Interpreter.class;
                System.arraycopy(item.args, 0, args, 1, item.args.length);
                method = Optional.ofNullable(getMethod(item.klass, item.methodName, args))
                        .orElseGet(() -> getMethod(item.klass, item.methodName, item.args));
            } else {
                method = item.method;
            }
        }
        return method;
    }

    private RegistryItem getRegistryItem(final Class<?> klass, final String alias) {
        final RegistryItem item;

        if (klass == null) {
            item = globalRegistry.get(alias);
        } else {
            item = registry.get(formKey(alias, klass));
        }
        return item;
    }

    private Method getMethod(final Class<?> klass, final String methodName, final Class<?>[] args) {
        try {
            return klass.getMethod(methodName, args);
        } catch (final NoSuchMethodException e) {
            return null;
        }
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
    private boolean definitionIsTheSame(final RegistryItem a, final RegistryItem b) {
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
     * @param alias parameter
     * @param item  parameter
     * @throws BasicRuntimeException in case of exception
     */
    private void registerGlobal(final String alias, final RegistryItem item)
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
    public void registerJavaMethod(final String alias, final Class<?> klass,
                                   final String methodName, final Class<?>[] argumentTypes)
            throws BasicRuntimeException {
        final var item = new RegistryItem();
        item.methodName = methodName;
        item.klass = klass;
        item.args = argumentTypes.clone();
        registry.put(formKey(alias, klass), item);
        registerGlobal(alias, item);
    }

    private static class RegistryItem {
        private final Method method = null;
        private String methodName;
        private Class<?> klass;
        private Class<?>[] args;

        public String toString() {
            return klass + "." + methodName + "(" +
                    Arrays.stream(args)
                            .map(Class::getName)
                            .collect(Collectors.joining(",")) +
                    ")";
        }
    }

}
