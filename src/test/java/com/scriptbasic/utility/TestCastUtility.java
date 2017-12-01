package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.BasicSyntaxException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * @author Peter Verhas
 * date Jul 1, 2012
 */
public class TestCastUtility {
    @Test
    public void test() throws Exception {
        assertEquals(CastUtility.cast(13.5, Byte.class), (byte) 13);
        assertEquals(CastUtility.cast(13.5, Short.class), (short) 13);
        assertEquals(CastUtility.cast(13.5, Integer.class), 13);
        assertEquals(CastUtility.cast(13.5, Long.class), (long) 13);
        assertEquals(CastUtility.cast(13.5, Float.class), (float) 13.5);
        assertEquals(CastUtility.cast(13.5, Double.class), 13.5);
        assertEquals(CastUtility.cast(13.5, byte.class), (byte) 13);
        assertEquals(CastUtility.cast(13.5, short.class), (short) 13);
        assertEquals(CastUtility.cast(13.5, int.class), 13);
        assertEquals(CastUtility.cast(13.5, long.class), (long) 13);
        assertEquals(CastUtility.cast(13.5, float.class), (float) 13.5);
        assertEquals(CastUtility.cast(13.5, double.class), 13.5);

        assertEquals(CastUtility.cast("DEADBEEF", Byte.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", Short.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", Integer.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", Long.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", Float.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", Double.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", byte.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", short.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", int.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", long.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", float.class), "DEADBEEF");
        assertEquals(CastUtility.cast("DEADBEEF", double.class), "DEADBEEF");

        assertEquals(CastUtility.cast("DEADBEEF", Class.class), "DEADBEEF");

        try {
            NoInstance.isPossible();
            fail();
        } catch (BasicInterpreterInternalError e) {
        }

        KlassUtility.forNameEx("byte");
        KlassUtility.forNameEx("short");
        KlassUtility.forNameEx("char");
        KlassUtility.forNameEx("int");
        KlassUtility.forNameEx("long");
        KlassUtility.forNameEx("float");
        KlassUtility.forNameEx("double");
        KlassUtility.forNameEx("boolean");
        try {
            KlassUtility.forNameEx("beef.dead.beef");
            fail();
        } catch (BasicSyntaxException e) {
        }
        try {
            KlassUtility.forName("beef.dead.beef");
            fail();
        } catch (ClassNotFoundException e) {
        }
    }
}
