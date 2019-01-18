package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.BasicSyntaxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Peter Verhas
 * date Jul 1, 2012
 */
public class TestCastUtility {
    @Test
    public void test() throws Exception {
        assertEquals('A', CastUtility.cast(65.3, Character.class));
        assertEquals((byte) 13, CastUtility.cast(13.5, Byte.class));
        assertEquals((short) 13, CastUtility.cast(13.5, Short.class));
        assertEquals(13, CastUtility.cast(13.5, Integer.class));
        assertEquals((long) 13, CastUtility.cast(13.5, Long.class));
        assertEquals((float) 13.5, CastUtility.cast(13.5, Float.class));
        assertEquals(13.5, CastUtility.cast(13.5, Double.class));
        assertEquals((byte) 13, CastUtility.cast(13.5, byte.class));
        assertEquals((short) 13, CastUtility.cast(13.5, short.class));
        assertEquals(13, CastUtility.cast(13.5, int.class));
        assertEquals((long) 13, CastUtility.cast(13.5, long.class));
        assertEquals((float) 13.5, CastUtility.cast(13.5, float.class));
        assertEquals(13.5, CastUtility.cast(13.5, double.class));

        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Byte.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Short.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Integer.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Long.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Float.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Double.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", byte.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", short.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", int.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", long.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", float.class));
        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", double.class));

        assertEquals("DEADBEEF", CastUtility.cast("DEADBEEF", Class.class));

        try {
            NoInstance.isPossible();
            fail();
        } catch (final BasicInterpreterInternalError e) {
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
        } catch (final BasicSyntaxException e) {
        }
        try {
            KlassUtility.forName("beef.dead.beef");
            fail();
        } catch (final ClassNotFoundException e) {
        }
    }
}
