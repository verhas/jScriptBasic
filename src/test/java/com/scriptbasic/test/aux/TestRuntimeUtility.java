package com.scriptbasic.test.aux;

import org.junit.Test;

import com.scriptbasic.utility.RuntimeUtility;

public class TestRuntimeUtility {
    @Test
    public void testExMethods() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Object o = RuntimeUtility.newObject("java.lang.Object");

        double d, x = 0.0;
        d = RuntimeUtility.acos(x);

        d = RuntimeUtility.asin(x);

        d = RuntimeUtility.atan(x);

        d = RuntimeUtility.atan2(x, x);

        d = RuntimeUtility.cbrt(x);

        d = RuntimeUtility.ceil(x);

        d = RuntimeUtility.cos(x);

        d = RuntimeUtility.cosh(x);

        d = RuntimeUtility.floor(x);

        d = RuntimeUtility.hypot(x);

        d = RuntimeUtility.log(x);

        d = RuntimeUtility.log10(x);

        d = RuntimeUtility.pow(x);

        d = RuntimeUtility.round(x);

        d = RuntimeUtility.tan(x);

        d = RuntimeUtility.tanh(x);

        Object on = RuntimeUtility.nullFunction();
    }
}
