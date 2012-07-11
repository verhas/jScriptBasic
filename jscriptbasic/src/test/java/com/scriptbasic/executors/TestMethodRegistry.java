/**
 * 
 */
package com.scriptbasic.executors;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;

/**
 * @author Peter Verhas
 * @date June 30, 2012
 * 
 */
public class TestMethodRegistry extends TestCase {
    @Test(expected = BasicRuntimeException.class)
    public static void testRegistry() throws ExecutionException {
        MethodRegistry mr = new MethodRegistry();
        mr.registerJavaMethod("alias", Object.class, "noSuchMethod",
                new Class[] { Object.class });
        try {
            mr.getJavaMethod(Object.class, "alias");
            assertTrue(false);
        } catch (ExecutionException e) {
        }
        mr.registerJavaMethod("sinus", java.lang.Math.class, "sin",
                new Class<?>[] { double.class });
        @SuppressWarnings("unused")
        Method m = mr.getJavaMethod(java.lang.Math.class, "sinus");
    }

}
