package com.scriptbasic.test.auxilliary;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.utility.functions.UtilityFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.scriptbasic.utility.functions.MathFunctions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class TestMathFunctions {
    @Test
    public void testExMethods() throws
            ScriptBasicException {

        final var x = 0.0;
        final var y = 1.0;

        abs(13L);
        abs(-13L);
        abs(13.0);
        abs(-13.0);

        try {
            abs(55);
            Assertions.fail("MathFunctions.abs accepted int argument");
        } catch (final ScriptBasicException e) {

        }

        copySign(x, y);
        exp(x);
        expm1(x);
        floatF(13.3);
        Assertions.assertNull(floatF("1122.3"));
        integer(55L);
        Assertions.assertNull(integer("1122.3"));
        getExponent(x);
        max(11.2, 11.2);
        max(55.5, 66.6);
        max(66.6, 55.5);
        max(1L, 1L);
        max(13L, 14L);
        max(14L, 13L);
        Assertions.assertNull(max(13, 14));
        min(11.2, 11.2);
        min(55.5, 66.6);
        min(66.6, 55.5);
        min(1L, 1L);
        min(13L, 14L);
        min(14L, 13L);
        Assertions.assertNull(min(13, 14));
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
