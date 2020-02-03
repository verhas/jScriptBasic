package com.scriptbasic.rightvalues;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.utility.RightValueUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Verhas date Jul 13, 2012
 */
@SuppressWarnings("ConstantConditions")
public class TestConversions {

    @Test()
    public void rightValueUtilityThrowsExceptionConvertingArbitraryStringToNumber() {
        final RightValue rv;
        rv = RightValueUtility.createRightValue("apple");
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                RightValueUtility.convert2Integer(rv));
    }

    @Test
    public void rightValueUtilityReturnsBasicArrayValue() {
        final var bav = new BasicArrayValue();
        assertEquals(bav, RightValueUtility.getValueObject(bav));
    }

    @Test
    public void nullBasicBooleanValueConvertsToFalse() throws BasicRuntimeException {
        final var rv = new BasicJavaObjectValue(null);
        assertFalse(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void trueBasicBooleanValueConvertsToTrue() throws BasicRuntimeException {
        final var rv = new BasicJavaObjectValue(Boolean.TRUE);
        assertTrue(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void nonZeroIntegerBasicBooleanValueConvertsToTrue() throws BasicRuntimeException {
        final var rv = new BasicJavaObjectValue(6000);
        assertTrue(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void emtyStringBasicBooleanValueConvertsToFalse() throws BasicRuntimeException {
        final var rv = new BasicStringValue("");
        assertFalse(BasicBooleanValue.asBoolean(rv));
    }

    @Test
    public void nullBasicStringValueDoesNotConvertsToArray() {
        final var rv = new BasicStringValue(null);
        assertFalse(rv.isArray());
    }

    @Test()
    public void arbitraryStringBasicBooleanValueConversionToBasicJavaObjectValueThrowsException() {
        final var rv = new BasicStringValue("apple");
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                BasicJavaObjectValue.asObject(rv));
    }

    @Test
    public void arbitraryStringBasicBooleanValueConvertsToDouble() throws Exception {
        final var rv = new BasicStringValue("10.3");
        assertEquals(10.3, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void nullStringBasicBooleanValueConvertsToNullDouble() throws Exception {
        final var rv = new BasicStringValue(null);
        assertNull(BasicDoubleValue.asDouble(rv));
    }

    @Test
    public void nullBasicLongValueConvertsToNullDouble() throws Exception {
        final var rv = new BasicLongValue(null);
        assertNull(BasicDoubleValue.asDouble(rv));
    }

    @Test
    public void basicDoubleValueConvertsToDouble() throws Exception {
        final var rv = new BasicJavaObjectValue(10.3);
        assertEquals(10.3, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void basicBooleanTrueValueConvertsTo_1_0_Double() throws Exception {
        final var rv = new BasicBooleanValue(true);
        assertEquals(1.0, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test
    public void basicBooleanFalseConvertsTo_0_0_Double() throws Exception {
        final var rv = new BasicBooleanValue(false);
        assertEquals(0.0, BasicDoubleValue.asDouble(rv), 0.00001);
    }

    @Test()
    public void arbitraryObjectConversionThrowsExceptionConvertingToDouble() {
        final var rv = new BasicJavaObjectValue(new Object());
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                BasicDoubleValue.asDouble(rv));
    }

    @Test
    public void basicObjectValueStringContentConvertsToString() throws Exception {
        final var rv = new BasicJavaObjectValue("apple");
        assertEquals("apple", BasicStringValue.asString(rv));
    }

    @Test
    public void basicBooleanTrueConvertsToStringLiteral_true() throws Exception {
        final var rv = new BasicBooleanValue(true);
        assertEquals(ScriptBasicKeyWords.KEYWORD_TRUE, BasicStringValue.asString(rv));
    }

    @Test
    public void basicBooleanFalseConvertsToStringLiteral_false() throws Exception {
        final var rv = new BasicBooleanValue(false);
        assertEquals(ScriptBasicKeyWords.KEYWORD_FALSE, BasicStringValue.asString(rv));
    }

    @Test
    public void basicStringValueConvertsToString() throws Exception {
        final var rv = new BasicStringValue("apple");
        assertEquals("apple", BasicStringValue.asString(rv));
    }

    @Test()
    public void basicArrayValueNullConvertingToStringThrowsException() {
        final var rv = new BasicArrayValue();
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                BasicStringValue.asString(rv));
    }

    @Test
    public void basicBooleanValueTrueConvertsToLong_1() throws Exception {
        final var rv = new BasicBooleanValue(true);
        assertEquals(Long.valueOf(1L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicBooleanValueFalseConvertsToLong_0() throws Exception {
        final var rv = new BasicBooleanValue(false);
        assertEquals(Long.valueOf(0L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicStringValueContainingDecimalIntegerConvertsToLong() throws Exception {
        final var rv = new BasicStringValue("1300");
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicStringValueNullConvertsTobasicLongValueNull() throws Exception {
        final var rv = new BasicStringValue(null);
        assertNull(BasicLongValue.asLong(rv));
    }

    @Test
    public void basicDoubleValueConvertsToLong() throws Exception {
        final var rv = new BasicDoubleValue(1300.0);
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test
    public void basicJavaObjectLongConvertsToLong() throws Exception {
        final var rv = new BasicJavaObjectValue(1300L);
        assertEquals(Long.valueOf(1300L), BasicLongValue.asLong(rv));
    }

    @Test()
    public void basicArrayValueNullConvertingToLongThrowsException() {
        final var rv = new BasicArrayValue();
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                BasicLongValue.asLong(rv));
    }
}
