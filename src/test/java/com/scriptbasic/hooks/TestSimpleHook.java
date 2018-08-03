package com.scriptbasic.hooks;

import com.scriptbasic.spi.SimpleHook;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.executors.BasicInterpreter;
import com.scriptbasic.interfaces.AnalysisException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestSimpleHook {

    @Test
    public void testExMethods() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, AnalysisException {
        final Method[] methods = SimpleHook.class.getDeclaredMethods();
        final var simpleHook = new SimpleHook() {
        };
        final var ctx = ContextBuilder.from("");
        final var interpreter = new BasicInterpreter(ctx);
        simpleHook.setInterpreter(interpreter);
        final var nullHook = new NullHook();
        simpleHook.setNext(nullHook);
        simpleHook.init();
        for (final Method method : methods) {
            final var name = method.getName();
            if (!name.equals("setNext") && !name.equals("setInterpreter")) {
                method.setAccessible(true);
                final var paramsNum = method.getParameterTypes().length;
                final Object[] obj = new Object[paramsNum];
                method.invoke(simpleHook, obj);
            }
        }
    }
}
