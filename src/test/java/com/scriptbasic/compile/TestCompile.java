package com.scriptbasic.compile;

import com.scriptbasic.Engine;
import com.scriptbasic.context.CompilerContext;
import org.junit.jupiter.api.Test;

public class TestCompile {

    @Test
    void testCompile() throws Exception {
        final var engine = new Engine();
        engine.load("""
                'i = 14
                while i > 0
                  PRINT i
                 ' i = i - 1
                wend
                """);
        System.out.println(engine.toJava(new CompilerContext()));
    }
}
