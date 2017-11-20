package com.scriptbasic.test.auxilliary;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.factories.ThreadLocalFactoryFactory;
import com.scriptbasic.utility.*;
import com.scriptbasic.utility.functions.UtilityFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class tests that a utility class conforms certain best practice rules.
 * <p>
 * These rules are the followings:
 * <p>
 * <ul>
 * <li>The class should have only a single, no-arg, private constructor.
 * <li>The constructor should throw exception of some type.
 * <li>The class should not implement any instance methods, not even inherit any (except those of Object.class).
 * </ul>
 *
 * @author Peter Verhas
 */
public class TestFactoryClasses {

    private static void test(final Class<?> utilityClass) {
        checkConstructor(utilityClass);
        assertThatTheMethodsAreStatic(utilityClass);
    }

    private static void checkConstructor(Class<?> utilityClass) {
        final Constructor<?>[] constructors = utilityClass
                .getDeclaredConstructors();

        assertThereIsOnlyOneConstructor(utilityClass, constructors);
        assertThatTheConstructorIsPrivate(utilityClass, constructors[0]);
        assertThatTheConstructorHasNoArguments(utilityClass, constructors[0]);
        assertThatTheConstructorThrowsException(utilityClass, constructors[0]);
    }

    private static void assertThatTheMethodsAreStatic(Class<?> utilityClass) {
        Method[] declaredMethods = utilityClass.getDeclaredMethods();
        Method[] methods = utilityClass.getMethods();
        assertThatTheMethodsAreStatic(utilityClass, declaredMethods);
        assertThatTheMethodsAreStatic(utilityClass, methods);
    }

    private static boolean methodIsNotInheritedFromObject(Method method) {
        return method.getDeclaringClass() != Object.class;
    }

    private static boolean methodIsNotStatic(Method method) {
        return !Modifier.isStatic(method.getModifiers());
    }

    private static void assertThatTheMethodsAreStatic(Class<?> utilityClass,
                                                      Method[] methods) {
        for (Method method : methods) {
            if (methodIsNotInheritedFromObject(method)
                    && methodIsNotStatic(method)) {
                reportThatTheMethodIsNotStatic(utilityClass, method);
            }
        }
    }

    private static void reportThatTheMethodIsNotStatic(Class<?> utilityClass,
                                                       Method method) {
        Assert.fail("Method " + method.getName() + "() in class "
                + utilityClass.getName() + " is not static");
    }

    private static void assertThatTheConstructorThrowsException(
            final Class<?> utilityClass, final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("The constructor of the class "
                    + utilityClass.getName() + " does not throw exception.");
        } catch (final Exception e) {
            reportThatEverythingIsOk(utilityClass);
        }

    }

    private static void assertThatTheConstructorHasNoArguments(
            final Class<?> utilityClass, final Constructor<?> constructor) {
        Class<?>[] argumentClasses = constructor.getParameterTypes();
        if (argumentClasses != null && argumentClasses.length > 0) {
            reportThatTheConstructorIsNotTheDefault(utilityClass);
        }
    }

    private static void assertThatTheConstructorIsPrivate(final Class<?> utilityClass,
                                                          final Constructor<?> constructor) {
        if (!Modifier.isPrivate(constructor.getModifiers())) {
            reportThatTheConstructorIsNotPrivate(utilityClass);
        }
    }

    private static void assertThereIsOnlyOneConstructor(
            final Class<?> utilityClass, final Constructor<?>[] constructors) {
        if (constructors == null || constructors.length == 0) {
            reportThatThereIsNoConstructor(utilityClass);
        }
        if (constructors.length > 1) {
            reportThatTheTooManyConstructors(utilityClass);
        }
    }

    private static void reportThatTheConstructorIsNotTheDefault(Class<?> utilityClass) {
        Assert.fail("The constructor of the class " + utilityClass.getName()
                + " has arguments.");
    }

    private static void reportThatTheConstructorIsNotPrivate(
            final Class<?> utilityClass) {
        Assert.fail("The constructor of the class " + utilityClass.getName()
                + " is not private.");
    }

    private static void reportThatTheTooManyConstructors(final Class<?> utilityClass) {
        Assert.fail("The class " + utilityClass.getName()
                + " has too many constructors.");
    }

    private static void reportThatThereIsNoConstructor(final Class<?> utilityClass) {
        Assert.fail("The class " + utilityClass.getName()
                + " has no constructor at all.");
    }

    private static void reportThatEverythingIsOk(final Class<?> utilityClass) {
    }

    @Test
    public void testFactories() {
        test(SingletonFactoryFactory.class);
        test(FactoryFactory.class);
        test(ThreadLocalFactoryFactory.class);
        test(CastUtility.class);
        test(CharUtils.class);
        test(ExpressionUtility.class);
        test(FactoryUtility.class);
        test(HookRegisterUtility.class);
        test(KlassUtility.class);
        test(LexUtility.class);
        test(MethodRegisterUtility.class);
        test(NumberUtility.class);
        test(ReflectionUtility.class);
        test(RightValueUtility.class);
        test(UtilityFunctions.class);
        test(SyntaxExceptionUtility.class);
    }

}
