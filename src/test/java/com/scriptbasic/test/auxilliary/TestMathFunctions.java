package com.scriptbasic.test.auxilliary;

import org.junit.Assert;

import org.junit.Test;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.utility.functions.UtilityFunctions;

import java.lang.reflect.InvocationTargetException;

import static com.scriptbasic.utility.functions.MathFunctions.*;

public class TestMathFunctions {
	@Test
	public void testExMethods() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, ExecutionException, NoSuchMethodException, InvocationTargetException {

		double x = 0.0;
		double y = 1.0;

		abs(13L);
		abs(-13L);
		abs(13.0);
		abs(-13.0);

		try {
			abs(55);
			Assert.fail("MathFunctions.abs accepted int argument");
		} catch (ExecutionException e) {

		}

		copySign(x, y);
		exp(x);
		expm1(x);
		floatF(13.3);
		Assert.assertNull(floatF("1122.3"));
		integer(55L);
		Assert.assertNull(integer("1122.3"));
		getExponent(x);
		max(11.2, 11.2);
		max(55.5, 66.6);
		max(66.6, 55.5);
		max(1L, 1L);
		max(13L, 14L);
		max(14L, 13L);
		Assert.assertNull(max(13, 14));
		min(11.2, 11.2);
		min(55.5, 66.6);
		min(66.6, 55.5);
		min(1L, 1L);
		min(13L, 14L);
		min(14L, 13L);
		Assert.assertNull(min(13, 14));
		IEEEremainder(x, y);
		log1p(x);
		random();
		rint(x);
		scalb(x, 1);
		signum(x);
		sin(x);
		sinh(x);
		sqrt(x);
		toRadians(x);
		toDegrees(x);
		acos(x);

		asin(x);

		atan(x);

		atan2(x, y);

		cbrt(x);

		ceil(x);

		cos(x);

		cosh(x);

		floor(x);

		hypot(x, y);

		log(x);

		log10(x);

		pow(x, y);

		round(x);

		tan(x);

		tanh(x);

		UtilityFunctions.undef();
	}
}
