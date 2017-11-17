package com.scriptbasic.test.auxilliary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Assert;

/**
 * This class helps testing that a utility class conforms certain best practice
 * rules.
 * 
 * These rules are the followings:
 * 
 * <ul>
 * <li>The class should have only a single, no-arg, private constructor.
 * <li>The constructor should throw exception of some type.
 * <li>The class should not implement any instance methods, except those
 * inherited from the {@code Object} class.
 * </ul>
 * 
 * @author Peter Verhas
 * 
 */
public class UtilityClassDefaultConstructorTester {

    private UtilityClassDefaultConstructorTester() {
        throw new IllegalArgumentException(
                UtilityClassDefaultConstructorTester.class.getName()
                        + " can not be instantiated");
    }

    public static void test(final Class<?> utilityClass) {
        checkConstructor(utilityClass);
        checkMethods(utilityClass);
    }

    private static void checkConstructor(Class<?> utilityClass) {
        final Constructor<?>[] constructors = utilityClass
                .getDeclaredConstructors();

        checkTheNumberOfConstructors(utilityClass, constructors);
        checkConstructorIsPrivate(utilityClass, constructors[0]);
        checkConstructorHasNoArguments(utilityClass, constructors[0]);
        checkConstructorThrowsException(utilityClass, constructors[0]);
    }

    private static void checkMethods(Class<?> utilityClass) {
        Method[] declaredMethods = utilityClass.getDeclaredMethods();
        Method[] methods = utilityClass.getMethods();
        checkMethodsAreStatic(utilityClass, declaredMethods);
        checkMethodsAreStatic(utilityClass, methods);
    }

    private static boolean methodIsNotInheritedFromObject(Method method) {
        return method.getDeclaringClass() != Object.class;
    }

    private static boolean methodIsNotStatic(Method method) {
        return !Modifier.isStatic(method.getModifiers());
    }

    private static void checkMethodsAreStatic(Class<?> utilityClass,
            Method[] methods) {
        for (Method method : methods) {
            if (methodIsNotInheritedFromObject(method)
                    && methodIsNotStatic(method)) {
                signalMethodIsNotStatic(utilityClass, method);
            }
        }
    }

    private static void signalMethodIsNotStatic(Class<?> utilityClass,
            Method method) {
        Assert.fail("Method " + method.getName() + "() in class "
                + utilityClass.getName() + " is not static");
    }

    private static void checkConstructorThrowsException(
            final Class<?> utilityClass, final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("The constructor of the class "
                    + utilityClass.getName() + " does not throw exception.");
        } catch (final Exception e) {
            signalOk(utilityClass);
        }

    }

    private static void checkConstructorHasNoArguments(
            final Class<?> utilityClass, final Constructor<?> constructor) {
        Class<?>[] argumentClasses = constructor.getParameterTypes();
        if (argumentClasses != null && argumentClasses.length > 0) {
            signalConstructorIsNotTheDefault(utilityClass);
        }
    }

    private static void checkConstructorIsPrivate(final Class<?> utilityClass,
            final Constructor<?> constructor) {
        if (!Modifier.isPrivate(constructor.getModifiers())) {
            signalConstructorIsNotPrivate(utilityClass);
        }
    }

    private static void checkTheNumberOfConstructors(
            final Class<?> utilityClass, final Constructor<?>[] constructors) {
        if (constructors == null || constructors.length == 0) {
            signalNoConstructor(utilityClass);
        }
        if (constructors.length > 1) {
            signalTooManyConstructors(utilityClass);
        }
    }

    private static void signalConstructorIsNotTheDefault(Class<?> utilityClass) {
        Assert.fail("The constructor of the class " + utilityClass.getName()
                + " has arguments.");
    }

    private static void signalConstructorIsNotPrivate(
            final Class<?> utilityClass) {
        Assert.fail("The constructor of the class " + utilityClass.getName()
                + " is not private.");
    }

    private static void signalTooManyConstructors(final Class<?> utilityClass) {
        Assert.fail("The class " + utilityClass.getName()
                + " has too many constructors.");
    }

    private static void signalNoConstructor(final Class<?> utilityClass) {
        Assert.fail("The class " + utilityClass.getName()
                + " has no constructor at all.");
    }

    private static void signalOk(final Class<?> utilityClass) {
    }
}
