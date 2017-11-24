package com.scriptbasic.rightvalues;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.RightValueUtility;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas date Jul 13, 2012
 */
public class TestConversions {

    @Test(expected = BasicRuntimeException.class)
    public void rightValueUtilityThrowsExceptionConvertingArbitraryStringToNumber() throws Exception {
        RightValue rv;
        rv = RightValueUtility.createRightValue("apple");
        RightValueUtility.convert2Integer(rv);
    }

    @Test
    public void rightValueUtilityReturnsBasicArrayValue() {
        BasicArrayValue bav = new BasicArrayValue();
        assertEquals(bav, RightValueUtility.getValueObject(bav));
    }

    @Test
    public void nullBasicBooleanValueConvertsToFalse() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(null);
        assertFalse(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void trueBasicBooleanValueConvertsToTrue() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(Boolean.TRUE);
        assertTrue(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void nonZeroIntegerBasicBooleanValueConvertsToTrue() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(6000);
        assertTrue(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void emtyStringBasicBooleanValueConvertsToFalse() throws Exception {
        final RightValue rv = new BasicStringValue("");
        assertFalse(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void nullBasicStringValueDoesNotConvertsToArray() throws Exception {
        final RightValue rv = new BasicStringValue(null);
        assertFalse(rv.isArray());
    }

    @Test(expected = BasicRuntimeException.class)
    public void arbitraryStringBasicBooleanValueConversionToBasicJavaObjectValueThrowsException() throws Exception {
        final RightValue rv = new BasicStringValue("apple");
        BasicJavaObjectValue.asObject(rv);
    }

    @Test
    public void arbitraryStringBasicBooleanValueConvertsToDouble() throws Exception {
        final RightValue rv = new BasicStringValue("10.3");
        assertEquals(10.3, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void nullStringBasicBooleanValueConvertsToNullDouble() throws Exception {
        final RightValue rv = new BasicStringValue(null);
        assertNull(BasicDoubleValue.asDouble(rv));
    }

    @Test
    public void nullBasicLongValueConvertsToNullDouble() throws Exception {
        final RightValue rv = new BasicLongValue(null);
        assertNull(BasicDoubleValue.asDouble(rv));
    }

    @Test
    public void basicDoubleValueConvertsToDouble() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(10.3);
        assertEquals(10.3, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void basicBooleanTrueValueConvertsTo_1_0_Double() throws Exception {
        final RightValue rv = new BasicBooleanValue(true);
        assertEquals(1.0, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void basicBooleanFalseConvertsTo_0_0_Double() throws Exception {
        final RightValue rv = new BasicBooleanValue(false);
        assertEquals(0.0, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test(expected = BasicRuntimeException.class)
    public void arbitraryObjectConversionThrowsExceptionConvertingToDouble() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(new Object());
        BasicDoubleValue.asDouble(rv);
    }

    @Test
    public void basicObjectValueStringContentConvertsToString() throws Exception {
        final RightValue rv = new BasicJavaObjectValue("apple");
        assertEquals("apple", BasicStringValue.asString(rv));
    }

    @Test
    public void basicBooleanTrueConvertsToStringLiteral_true() throws Exception {
        final RightValue rv = new BasicBooleanValue(true);
        assertEquals("true", BasicStringValue.asString(rv));
    }

    @Test
    public void basicBooleanFalseConvertsToStringLiteral_false() throws Exception {
        final RightValue rv = new BasicBooleanValue(false);
        assertEquals("false", BasicStringValue.asString(rv));
    }

    @Test
    public void basicStringValueConvertsToString() throws Exception {
        final RightValue rv = new BasicStringValue("apple");
        assertEquals("apple", BasicStringValue.asString(rv));
    }

    @Test(expected = BasicRuntimeException.class)
    public void basicArrayValueNullConvertingToStringThrowsException() throws Exception {
        final RightValue rv = new BasicArrayValue(null);
        BasicStringValue.asString(rv);
    }

    @Test
    public void basicBooleanValueTrueConvertsToLong_1() throws Exception {
        final RightValue rv = new BasicBooleanValue(true);
        assertEquals(Long.valueOf(1L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicBooleanValueFalseConvertsToLong_0() throws Exception {
        final RightValue rv = new BasicBooleanValue(false);
        assertEquals(Long.valueOf(0L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicStringValueContainingDecimalIntegerConvertsToLong() throws Exception {
        final RightValue rv = new BasicStringValue("1300");
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicStringValueNullConvertsTobasicLongValueNull() throws Exception {
        final RightValue rv = new BasicStringValue(null);
        assertEquals(null, BasicLongValue.asLong(rv));
    }

    @Test
    public void basicDoubleValueConvertsToLong() throws Exception {
        final RightValue rv = new BasicDoubleValue(1300.0);
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicJavaObjectLongConvertsToLong() throws Exception {
        final RightValue rv = new BasicJavaObjectValue(1300L);
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test(expected = BasicRuntimeException.class)
    public void basicArrayValueNullConvertingToLongThrowsException() throws Exception {
        final RightValue rv = new BasicArrayValue(null);
        BasicLongValue.asLong(rv);
    }
}
