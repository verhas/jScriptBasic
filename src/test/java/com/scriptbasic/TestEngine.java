package com.scriptbasic;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasic;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.readers.SourceProvider;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEngine {
    private static final String NL = "\n";

    @Test
    @DisplayName("engine throws exception when input is total garbage")
    public void throwsExceptionWhenInputIsTotalGarbage() {
        assertThrows(ScriptBasicException.class, () ->
            ScriptBasic.engine().eval("ajdjkajkladsadsadjkls"));
    }

    @Test
    @DisplayName("it is possible to set a global variable before execution")
    public void testSetGlobalVariable() throws Exception {
        // START SNIPPET: setGlobalVariable
        final var output = Output.get();
        try (output) {
            ScriptBasic.engine().output(output).variable("a").is(13)
                .eval("print a + \"hello world\"");
        }
        output.is("13hello world");
        // END SNIPPET: setGlobalVariable
    }

    @Test
    @DisplayName("it is possible to get a global variable after execution")
    public void testGetGlobalVariable() throws Exception {
        // START SNIPPET: getGlobalVariable
        final var engine = ScriptBasic.engine().eval("a = \"hello world\"");
        final var a = engine.variable(String.class, "a");
        assertEquals("hello world", a);
        // END SNIPPET: getGlobalVariable
    }

    @Test
    @DisplayName("it is possible to get a global variable after execution")
    public void testGetGlobalVariableFailingTypeConversion() throws Exception {
        // START SNIPPET: getGlobalVariable
        final var engine = ScriptBasic.engine().eval("a = \"hello world\"");
        assertThrows(ScriptBasicException.class, () ->
            engine.variable(Long.class, "a"));
        // END SNIPPET: getGlobalVariable
    }

    @Test
    @DisplayName("engine can list the names of the global variables after execution")
    public void testListGlobalVariable() throws Exception {
        // START SNIPPET: listGlobalVariable
        final var names = new StringBuilder();
        for (final String name :
            ScriptBasic.engine().eval("a = \"hello world\"\nb=13")
                .variables()) {
            names.append(name);
        }
        final var s = names.toString();
        assertAll(
            () -> assertTrue(s.contains("a")),
            () -> assertTrue(s.contains("b")),
            () -> assertEquals(2, s.length())
        );
        // END SNIPPET: listGlobalVariable
    }

    @Test
    @DisplayName("subroutine local variables are not available")
    public void testSubroutineLocalVarIsLocal()
        throws Exception {
        final var engine = ScriptBasic.engine();
        engine.eval("sub applePie\na = \"hello world\"\nEndSub");
        assertNull(engine.variable(String.class, "a"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    @DisplayName("subroutine local variables are not available even after subroutine was called")
    public void testSubroutineCallWOArgumentsWORetvalLocalVarIsLocal()
        throws Exception {
        final var engine = ScriptBasic.engine();
        engine.eval("sub applePie\na = \"hello world\"\nEndSub");
        final var emptyArgumentList = (Object[]) null;
        engine.subroutine(Void.class, "applePie").call(emptyArgumentList);
        assertNull(engine.variable(String.class, "a"));
    }

    @Test
    @DisplayName("no arg subroutine declared global variable is available after calling subroutine")
    public void testSubroutineCallWOArgumentsWORetval() throws Exception {
        // START SNIPPET: subroutineCallWOArgumentsWORetval
        final var engine = ScriptBasic.engine()
            .eval("sub applePie\nglobal a\na = \"hello world\"\nEndSub");
        assertNull(engine.subroutine("applePie").call());
        assertEquals("hello world", engine.variable(String.class, "a"));
        // END SNIPPET: subroutineCallWOArgumentsWORetval
    }

    @Test
    @DisplayName("no arg subroutine declared global variable is available before calling the subroutine")
    public void testSubroutineCaxllWOArgumentsWORetval() throws Exception {
        final var engine = ScriptBasic.engine();
        engine.eval("sub applePie\nglobal a\na = \"hello world\"\nEndSub");
        assertNull(engine.variable(String.class, "a"));
    }

    @Test
    @DisplayName("engine throws exception when code calls undefined subroutine")
    public void throwsExceptionWhenSubroutineIsUndefined() {
        final var engine = ScriptBasic.engine();
        assertThrows(ScriptBasicException.class, () ->
            engine.eval("call applePie"));
    }

    @Test
    @DisplayName("engine throws exception when source file does not exist")
    public void throwsExceptionWhenFileDoesNotExist() {
        final var engine = ScriptBasic.engine();
        assertThrows(ScriptBasicException.class, () ->
            engine.eval(new File("this file is totally nonexistent")));
    }

    @Test
    @DisplayName("engine throws exception when then underlying source provider does")
    public void throwsExceptionWhenSourceProviderDoes() {
        assertThrows(ScriptBasicException.class, () ->
            ScriptBasic.engine().eval("kakukk.bas", new SourceProvider() {

                @Override
                public SourceReader get(final String sourceName, final String referencingSource)
                    throws IOException {
                    throw new IOException();
                }

                @Override
                public SourceReader get(final String sourceName) throws IOException {
                    throw new IOException();
                }
            }));
    }

    @Test
    @DisplayName("engine can load hello world string and it can execute it")
    public void testLoadString() throws Exception {
        final var engine = ScriptBasic.engine();
        engine.load("print \"hello world\"");
        engine.execute();
    }

    @Test
    @DisplayName("program from string creates output")
    public void testLoadStringSW() throws Exception {
        final var output = Output.get();
        try (output) {
            ScriptBasic.engine().output(output).load("print \"hello world\"").execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("program from reader creates output")
    public void testLoadReader() throws Exception {
        final var output = Output.get();
        final var reader = new StringReader("print \"hello world\"");
        try (output) {
            ScriptBasic.engine().output(output).load(reader).execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("program from file creates output")
    public void testLoadFile() throws Exception {
        final var output = Output.get();
        final var file = new File(getClass().getResource("hello.bas").getFile());
        try (output) {
            ScriptBasic.engine().output(output).load(file).execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("pogram loaded with path creates output")
    public void testLoadPath() throws Exception {
        final var output = Output.get();
        final var path = new File(
            getClass().getResource("hello.bas").getFile())
            .getParent();
        try (output) {
            ScriptBasic.engine().output(output).load("include.bas", path).execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("program loaded with SourcePath creates output")
    public void testLoadSourcePath() throws Exception {
        final var output = Output.get();
        final var path = new File(
            getClass().getResource("hello.bas").getFile())
            .getParent();
        final var sourcePath = new BasicSourcePath();
        sourcePath.add(path);
        try (output) {
            ScriptBasic.engine().output(output).load("include.bas", sourcePath).execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("program with SourceProvider creates output")
    public void testLoadSourceProvider() throws Exception {
        final var output = Output.get();
        final var provider = new SourceProvider() {
            private final Map<String, String> source = Map.of(
                "hello.bas", "print \"hello world\"",
                "include.bas", "include \"hello.bas\"");

            @Override
            public SourceReader get(final String sourceName, final String referencingSource) {
                return get(sourceName);
            }

            @Override
            public SourceReader get(final String sourceName) {
                return new GenericHierarchicalSourceReader(
                    new GenericSourceReader(
                        new StringReader(source.get(sourceName)),
                        this,
                        sourceName));
            }
        };
        try (output) {
            ScriptBasic.engine().output(output).load("include.bas", provider).execute();
        }
        output.is("hello world");
    }

    @Test
    @DisplayName("program loaded once can be executed multiple times")
    public void testLoadStringSWMultipleExecute() throws Exception {
        final var output = Output.get();
        try (output) {
            ScriptBasic.engine().output(output).load("print \"hello world\"")
                .execute()
                .execute();
        }
        output.is("hello worldhello world");
    }

    @Test
    @DisplayName("empty engine should not be executed")
    public void testNoLoadStringSWMultipleExecute() {
        assertThrows(ScriptBasicException.class, () ->
            ScriptBasic.engine().execute());
    }

    @Test
    @DisplayName("Java function can be registered and called from BASIC")
    public void testRegisterExtension() throws Exception {
        // START SNIPPET: testExtensionMethod
        final var engine = ScriptBasic
            .engine()
            .load("Sub aPie\n" +
                "return javaFunction()\n" +
                "EndSub")
            .registerExtension(TestExtensionClass.class)
            .execute();
        final var z = engine.subroutine(Long.class, "aPie").call();
        assertEquals((Long) 55L, z);
        // END SNIPPET: testExtensionMethod
    }

    /**
     * A simple helper class that asserts the collected string.
     */
    private static class Output extends StringWriter {
        public static Output get() {
            return new Output();
        }

        public void is(final String result) {
            assertEquals(result, this.toString());
        }
    }

    // START SNIPPET: testExtensionClass
    public static class TestExtensionClass {
        @BasicFunction(alias = "javaFunction", classification = java.lang.Long.class, requiredVersion = 1)
        public static Long fiftyFive() {
            return 55L;
        }
    }

    @Nested
    @DisplayName("engine can eval")
    class EngineCanEval {

        @Test
        @DisplayName("a simple one liner hello world code")
        public void testEvalString() throws Exception {
            // START SNIPPET: helloWorldString
            ScriptBasic.engine().eval("print \"hello world\"");
            // END SNIPPET: helloWorldString
        }

        @Test
        @DisplayName("a simple one liner hello world code")
        public void testEvalStringSW() throws Exception {
            // START SNIPPET: helloWorldStringSW
            final var output = Output.get();
            try (output) {
                ScriptBasic.engine().output(output).eval("print \"hello world\"");
            }
            output.is("hello world");
            // END SNIPPET: helloWorldStringSW
        }

        @Test
        @DisplayName("multiple statement on one line")
        public void testEvalStringMP() throws Exception {
            final var output = Output.get();
            try (output) {
                ScriptBasic.engine().output(output).eval("a=\"hello world\" : print a");
            }
            output.is("hello world");
        }

        @Nested
        @DisplayName("source coming from a")
        class SourceComingFrom {

            @Test
            @DisplayName("StringReader")
            public void testEvalReader() throws Exception {
                // START SNIPPET: helloWorldReader
                final var output = Output.get();
                final var sr = new StringReader("print \"hello world\"");
                try (output; sr) {
                    ScriptBasic.engine().output(output).eval(sr);
                }
                output.is("hello world");
                // END SNIPPET: helloWorldReader
            }

            @Test
            @DisplayName("file")
            public void testEvalFile() throws Exception {
                // START SNIPPET: helloWorldFile
                final var output = Output.get();
                final var file = new File(getClass().getResource("hello.bas").getFile());
                try (output) {
                    ScriptBasic.engine().output(output).eval(file);
                }
                output.is("hello world");
                // END SNIPPET: helloWorldFile
            }

            @Test
            @DisplayName("file name and path")
            public void testEvalPath() throws Exception {
                // START SNIPPET: helloWorldPath
                final var output = Output.get();
                final var path = new File(getClass().getResource("hello.bas").getFile())
                    .getParent();
                try (output) {
                    ScriptBasic.engine().output(output).eval("include.bas", path);
                }
                output.is("hello world");
                // END SNIPPET: helloWorldPath
            }

            public void documentOnlyForTheSnippetNotCalledEver() throws Exception {
                // START SNIPPET: helloWorldPathMultiple
                ScriptBasic.engine()
                    .eval("include.bas", ".", "..", "/usr/include/scriptbasic");
                // END SNIPPET: helloWorldPathMultiple
            }

            @Test
            @DisplayName("file name and SourcePath")
            public void testEvalSourcePath() throws Exception {
                // START SNIPPET: helloWorldSourcePath
                final var output = Output.get();
                final var path = new File(
                    getClass().getResource("hello.bas").getFile())
                    .getParent();
                final var sourcePath = new BasicSourcePath();
                sourcePath.add(path);
                try (output) {
                    final var engine = ScriptBasic.engine().output(output).eval("include.bas", sourcePath);
                }
                output.is("hello world");
                // END SNIPPET: helloWorldSourcePath
            }

            @Test
            @DisplayName("SourceProvider implementation")
            public void testEvalSourceProvider() throws Exception {
                // START SNIPPET: helloWorldSourceProvider
                final var provider = new SourceProvider() {
                    private final Map<String, String> source = Map.of(
                        "hello.bas", "print \"hello world\"",
                        "include.bas", "include \"hello.bas\"");

                    @Override
                    public SourceReader get(final String sourceName, final String referencingSource) {
                        return get(sourceName);
                    }

                    @Override
                    public SourceReader get(final String sourceName) {
                        final var reader = new GenericSourceReader(new StringReader(source.get(sourceName)), this, sourceName);
                        return new GenericHierarchicalSourceReader(reader);
                    }
                };
                final var output = Output.get();
                try (output) {
                    final var engine = ScriptBasic.engine().output(output).eval("include.bas", provider);
                }
                output.is("hello world");
                // END SNIPPET: helloWorldSourceProvider
            }
        }
    }
    // END SNIPPER: testExtensionClass

    @Nested
    @DisplayName("declaring and calling a subroutine")
    class SubroutineTests {

        @Test
        @DisplayName("with arguments can also define global variables")
        public void testSubroutineCallWArgumentsWORetval() throws Exception {
            // START SNIPPET: subroutineCallWArgumentsWORetval
            final var engine = ScriptBasic.engine();
            engine.eval("sub applePie(b)\nglobal a\na = b\nEndSub");
            assertNull(engine.variable(String.class, "a"));
            engine.subroutine(Void.class, "applePie").call("hello world");
            assertEquals("hello world", engine.variable(String.class, "a"));
            // END SNIPPET: subroutineCallWArgumentsWORetval
        }

        @Test
        @DisplayName("without argument global variable is set to constant value")
        public void testSubroutineCallWOArgumentsWORetvalOO() throws Exception {
            // START SNIPPET: subroutineCallWOArgumentsWORetvalOO
            final var engine = ScriptBasic.engine()
                .eval("sub applePie" + NL +
                    "global a" + NL +
                    "a = \"hello world\"" + NL +
                    "EndSub");
            assertNull(engine.variable(Void.class, "a"));
            final var applePie = engine.subroutine(String.class, "applePie");
            applePie.call((Object[]) null);
            assertEquals("hello world", engine.variable(String.class, "a"));
            // END SNIPPET: subroutineCallWOArgumentsWORetvalOO
        }

        @Test
        @DisplayName("with argument but no return value it sets the global variable")
        public void testSubroutineCallWArgumentsWORetvalOO() throws Exception {
            // START SNIPPET: subroutineCallWArgumentsWORetvalOO
            final var engine = ScriptBasic.engine();
            engine.eval("sub applePie(b)" + NL +
                "global a" + NL +
                "a = b" + NL +
                "EndSub");
            assertGlobalVariableIsNotDefined(engine, "a");
            engine.subroutine(Void.class, "applePie").call("hello world");
            assertEquals("hello world", engine.variable(String.class, "a"));
            // END SNIPPET: subroutineCallWArgumentsWORetvalOO
        }

        @Test
        @DisplayName(" with too many arguments will throw exception")
        public void testSubroutineCallWArgumentsWRetval1() throws Exception {
            final var engine = ScriptBasic.engine();
            engine.eval("sub applePie(b)" + NL +
                "global a" + NL +
                "a = b" + NL +
                "return 6" + NL +
                "EndSub");
            assertThrows(ScriptBasicException.class, () ->
                engine.subroutine(Long.class, "applePie").call("hello world", "mama"));
        }

        @Test
        @DisplayName("with too few arguments is okay, extra arguments are undefined")
        public void testSubroutineCallWArgumentsWRetval2() throws Exception {
            final var engine = ScriptBasic.engine();
            engine.eval("sub applePie(b,c)\nglobal a\na = c\nreturn 6\nEndSub");
            engine.subroutine(Long.class, "applePie").call("hello world");
            assertNotNull(engine.variable(Object.class, "a"));
        }

        @Test
        @DisplayName("with argument and return value")
        public void testSubroutineCallWArgumentsWRetval() throws Exception {
            // START SNIPPET: subroutineCallWArgumentsWRetval
            final var engine = ScriptBasic.engine();
            engine.eval(
                "sub applePie(b)" + NL +
                    "global a" + NL +
                    "a = b" + NL +
                    "return 6" + NL +
                    "EndSub");
            assertGlobalVariableIsNotDefined(engine, "a");
            final var ret = engine.subroutine(Long.class, "applePie").call("hello world");
            assertEquals("hello world", engine.variable(String.class, "a"));
            assertEquals((Long) 6L, ret);
            // END SNIPPET: subroutineCallWArgumentsWRetval
        }

        @Test
        @DisplayName("with return value can set global variable after it was called")
        public void testSubroutineCallWArgumentsWRetvalOO() throws Exception {
            // START SNIPPET: subroutineCallWArgumentsWRetvalOO
            final var engine = ScriptBasic.engine();
            engine.eval("sub applePie(b)\nglobal a\na = b\nreturn 6\nEndSub");
            assertGlobalVariableIsNotDefined(engine, "a");
            final var applePie = engine.subroutine(Long.class, "applePie");
            final var ret = applePie.call("hello world");
            assertEquals("hello world", engine.variable(String.class, "a"));
            assertEquals((Long) 6L, ret);
            // END SNIPPET: subroutineCallWArgumentsWRetvalOO
        }

        @Test
        @DisplayName("engine can list subroutines and return them by name even before execution")
        public void testSubroutineList() throws Exception {
            // START SNIPPET: subroutineList
            final var engine = ScriptBasic.engine().eval(
                "sub applePie(b)" + NL +
                    "EndSub" + NL +
                    "sub anotherSubroutine" + NL +
                    "EndSub\n");
            final var numberOfSubroutines = new AtomicInteger(0);
            engine.subroutines().forEach((s) -> numberOfSubroutines.incrementAndGet());
            assertAll(
                () -> assertEquals(2, numberOfSubroutines.get()),
                () -> assertEquals(1, engine.subroutine(Void.class, "applePie").numberOfArguments()),
                () -> assertEquals(0, engine.subroutine(Void.class, "anotherSubroutine").numberOfArguments()));
            // END SNIPPET: subroutineList
        }

        @Test
        @DisplayName("results the return value but leaves global variable undefined if there is not enough argument")
        public void testSubroutineCallWArgumentsWRetval2007() throws Exception {
            final var engine = ScriptBasic.engine().eval(
                "sub applePie(b,c)" + NL +
                    "global a" + NL +
                    "a = c" + NL +
                    "return 6" + NL +
                    "EndSub");
            final var sub = engine.subroutine(null, "applePie");
            sub.call("hello world");
            assertNotNull(engine.variable(Object.class, "a"));
        }

        @Test
        @DisplayName("results the return value and also sets global variable")
        public void testSubroutineCallWArgumentsWRetval007() throws Exception {
            final var engine = ScriptBasic.engine().load("" +
                "sub applePie(b)" + NL +
                "global a" + NL +
                "a = b" + NL +
                "return 6" + NL +
                "EndSub");
            assertGlobalVariableIsNotDefined(engine, "a");
            final var sub = engine.subroutine(Long.class, "applePie");
            final var returnValue = sub.call("hello world");
            assertAll(
                () -> assertEquals("hello world", engine.variable(String.class, "a")),
                () -> assertEquals((Long) 6L, returnValue));
        }

        private void assertGlobalVariableIsNotDefined(final ScriptBasic engine, final String name) throws ScriptBasicException {
            assertNull(engine.variable(Object.class, name));
        }

        @Test
        @DisplayName("can be accessed as object by its name")
        public void getsTheSubroutineByName() throws Exception {
            final var engine = ScriptBasic.engine().eval(
                "sub applePie(b)" + NL +
                    "EndSub\n");
            final var applePie = engine.subroutine(Void.class, "applePie");
            assertEquals("applePie", applePie.name());
        }
    }
}
