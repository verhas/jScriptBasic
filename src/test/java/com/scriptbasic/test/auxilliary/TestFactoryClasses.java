package com.scriptbasic.test.auxilliary;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.factories.ThreadLocalFactoryFactory;
import com.scriptbasic.utility.*;
import com.scriptbasic.utility.functions.UtilityFunctions;
import org.junit.Test;

public class TestFactoryClasses {
    @Test
    public void testFactories() {
        UtilityClassDefaultConstructorTester
                .test(SingletonFactoryFactory.class);
        UtilityClassDefaultConstructorTester.test(FactoryFactory.class);
        UtilityClassDefaultConstructorTester
                .test(ThreadLocalFactoryFactory.class);
        UtilityClassDefaultConstructorTester.test(CastUtility.class);
        UtilityClassDefaultConstructorTester.test(CharUtils.class);
        UtilityClassDefaultConstructorTester.test(ExpressionUtility.class);
        UtilityClassDefaultConstructorTester.test(FactoryUtility.class);
        UtilityClassDefaultConstructorTester.test(HookRegisterUtility.class);
        UtilityClassDefaultConstructorTester.test(KlassUtility.class);
        UtilityClassDefaultConstructorTester.test(LexUtility.class);
        UtilityClassDefaultConstructorTester.test(MethodRegisterUtility.class);
        UtilityClassDefaultConstructorTester.test(NumberUtility.class);
        UtilityClassDefaultConstructorTester.test(ReflectionUtility.class);
        UtilityClassDefaultConstructorTester.test(RightValueUtility.class);
        UtilityClassDefaultConstructorTester.test(UtilityFunctions.class);
        UtilityClassDefaultConstructorTester.test(SyntaxExceptionUtility.class);

    }
}
