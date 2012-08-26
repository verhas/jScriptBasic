/**
 * 
 */
package com.scriptbasic.rightvalues;

import junit.framework.TestCase;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.RightValueUtility;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 * 
 */
public class TestConversions extends TestCase {

    public static void testRightValueUtility() throws Exception {
        RightValue rv;
        rv = RightValueUtility.createRightValue(new Short((short) 3));
        rv = RightValueUtility.createRightValue("apple");
        try {
            RightValueUtility.convert2Integer(rv);
            assertFalse(true);
        } catch (BasicRuntimeException e) {
        }
        rv = RightValueUtility.createRightValue('a');
        rv = RightValueUtility.createRightValue(true);
        try {
            RightValueUtility.getValueObject(new BasicArrayValue());
            assertFalse(true);
        } catch (BasicInterpreterInternalError e) {
        }
    }

    public static void testConversion() throws Exception {
        RightValue rv = new BasicJavaObjectValue(null);
        assertFalse(BasicBooleanValue.convert(rv));
        rv = new BasicJavaObjectValue(Boolean.TRUE);
        assertTrue(BasicBooleanValue.convert(rv));
        rv = new BasicJavaObjectValue(new Integer(6000));
        assertTrue(BasicBooleanValue.convert(rv));
        rv = new BasicStringValue("");
        assertFalse(BasicBooleanValue.convert(rv));
        rv = new BasicStringValue(null);
        assertFalse(BasicBooleanValue.convert(rv));
        assertFalse(rv.isArray());
        rv = new BasicStringValue("apple");
        try {
            BasicJavaObjectValue.convert(rv);
            assertTrue(false);
        } catch (Exception e) {

        }
        rv = new BasicStringValue("10.3");
        assertEquals(10.3, BasicDoubleValue.convert(rv), 0.00001);
        rv = new BasicStringValue(null);
        assertNull(BasicDoubleValue.convert(rv));
        rv = new BasicLongValue(null);
        assertNull(BasicDoubleValue.convert(rv));
        rv = new BasicJavaObjectValue(new Double(10.3));
        assertEquals(10.3, BasicDoubleValue.convert(rv), 0.00001);
        rv = new BasicBooleanValue(true);
        assertEquals(1.0, BasicDoubleValue.convert(rv), 0.00001);
        rv = new BasicBooleanValue(false);
        assertEquals(0.0, BasicDoubleValue.convert(rv), 0.00001);
        rv = new BasicJavaObjectValue(new TestConversions());
        try {
            BasicDoubleValue.convert(rv);
            assertTrue(false);
        } catch (Exception e) {

        }
        rv = new BasicJavaObjectValue("apple");
        assertEquals("apple", BasicStringValue.convert(rv));
        rv = new BasicBooleanValue(true);
        assertEquals("true", BasicStringValue.convert(rv));
        rv = new BasicBooleanValue(false);
        assertEquals("false", BasicStringValue.convert(rv));
        rv = new BasicStringValue("apple");
        assertEquals("apple", BasicStringValue.convert(rv));
        rv = new BasicArrayValue();
        try {
            BasicStringValue.convert(rv);
            assertTrue(false);
        } catch (Exception e) {

        }
        rv = new BasicBooleanValue(true);
        assertEquals(new Long(1L), BasicLongValue.convert(rv));
        rv = new BasicBooleanValue(false);
        assertEquals(new Long(0L), BasicLongValue.convert(rv));
        rv = new BasicStringValue("1300");
        assertEquals(new Long(1300L), BasicLongValue.convert(rv));
        rv = new BasicStringValue(null);
        assertEquals((Long) null, BasicLongValue.convert(rv));
        rv = new BasicDoubleValue(1300.0);
        assertEquals(new Long(1300L), BasicLongValue.convert(rv));
        rv = new BasicJavaObjectValue(new Long(1300));
        assertEquals(new Long(1300L), BasicLongValue.convert(rv));
        rv = new BasicArrayValue();
        try {
            BasicLongValue.convert(rv);
            assertTrue(false);
        } catch (Exception e) {

        }
    }
}
