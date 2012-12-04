package com.scriptbasic.test.aux;

import org.junit.Test;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.factories.ThreadLocalFactoryFactory;
import com.scriptbasic.utility.CastUtility;
import com.scriptbasic.utility.CharUtils;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.HookRegisterUtility;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.LexUtility;
import com.scriptbasic.utility.MethodRegisterUtility;
import com.scriptbasic.utility.NumberUtility;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.RightValueUtility;
import com.scriptbasic.utility.SyntaxExceptionUtility;
import com.scriptbasic.utility.UtilityUtility;
import com.scriptbasic.utility.functions.UtilityFunctions;

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
        UtilityClassDefaultConstructorTester.test(UtilityUtility.class);

    }
}
