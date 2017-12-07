package com.scriptbasic.executors;

import com.scriptbasic.Engine;
import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasic;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.Context;
import com.scriptbasic.spi.Interpreter;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class TestInterpreterAwareMethodCall {


    static ScriptBasic sb;

    @BasicFunction(alias = "callMe",classification = Test.class)
    public static void getFirstArgument(Interpreter interpreter, int one, long two) throws NoSuchFieldException, IllegalAccessException {
        assertEquals(1, one);
        assertEquals(2, two);
        Field ctxF = Engine.class.getDeclaredField("ctx");
        ctxF.setAccessible(true);
        Context ctx = (Context)ctxF.get(sb);
        assertEquals(ctx.interpreter,interpreter);
    }

    @Test
    public void passesInterpreterAsFirstArgument() throws ScriptBasicException {
        sb = ScriptBasic.getEngine();
        sb.registerExtension(this.getClass());
        sb.eval("callMe 1,2");
    }

}
