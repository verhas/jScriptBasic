package com.scriptbasic.hooks;

import com.scriptbasic.executors.BasicExtendedInterpreter;
import com.scriptbasic.factories.Context;
import com.scriptbasic.factories.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestSimpleHook {

    @Test
    public void testExMethods() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, AnalysisException {
        Method[] methods = SimpleHook.class.getDeclaredMethods();
        SimpleHook simpleHook = new SimpleHook(){};
        Context ctx = ContextBuilder.from("");
        ExtendedInterpreter interpreter = new BasicExtendedInterpreter(ctx);
        simpleHook.setInterpreter(interpreter);
        NullHook nullHook = new NullHook();
        simpleHook.setNext(nullHook);
        simpleHook.init();
        for (Method method : methods) {
            String name = method.getName();
            if (!name.equals("setNext") && !name.equals("setInterpreter")) {
                method.setAccessible(true);
                int paramsNum = method.getParameterTypes().length;
                Object[] obj = new Object[paramsNum];
                method.invoke(simpleHook, obj);
            }
        }
    }
}
