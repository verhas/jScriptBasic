package com.scriptbasic.test.aux;

import org.junit.Test;

import com.scriptbasic.utility.RuntimeUtility;

public class TestRuntimeUtility {
	@Test
	public void testExMethods() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		RuntimeUtility.newObject("java.lang.Object");

		double x = 0.0;
		RuntimeUtility.acos(x);

		RuntimeUtility.asin(x);

		RuntimeUtility.atan(x);

		RuntimeUtility.atan2(x, x);

		RuntimeUtility.cbrt(x);

		RuntimeUtility.ceil(x);

		RuntimeUtility.cos(x);

		RuntimeUtility.cosh(x);

		RuntimeUtility.floor(x);

		RuntimeUtility.hypot(x);

		RuntimeUtility.log(x);

		RuntimeUtility.log10(x);

		RuntimeUtility.pow(x);

		RuntimeUtility.round(x);

		RuntimeUtility.tan(x);

		RuntimeUtility.tanh(x);

		RuntimeUtility.nullFunction();
	}
}
