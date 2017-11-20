/**
 *
 */
package com.scriptbasic.executors;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.MethodRegistry;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNull;

/**
 * @author Peter Verhas
 * date June 30, 2012
 */

public class TestMethodRegistry {

    @Test
    public void returnsNullForNonRegisteredMethod() throws ExecutionException {
        MethodRegistry mr = new BasicMethodRegistry();
        assertNull(mr.getJavaMethod(Object.class, "alias"));
    }

    @Test
    public void registersMethods() throws ExecutionException {
        MethodRegistry mr = new BasicMethodRegistry();
        mr.registerJavaMethod("alias", Object.class, "noSuchMethod",
                new Class[]{Object.class});
        mr.registerJavaMethod("sinus", java.lang.Math.class, "sin",
                new Class<?>[]{double.class});
        @SuppressWarnings("unused")
        Method m = mr.getJavaMethod(java.lang.Math.class, "sinus");
    }

}
