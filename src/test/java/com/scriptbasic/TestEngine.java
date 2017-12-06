package com.scriptbasic;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasic;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.api.Subroutine;
import com.scriptbasic.interfaces.SourcePath;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.interfaces.SourceReader;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestEngine {

    @Test(expected = ScriptBasicException.class)
    public void throwsExceptionWhenInputIsTotalGarbage() throws ScriptBasicException {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("ajdjkajkladsadsadjkls");
    }

    @Test
    public void testEvalString() throws Exception {
        // START SNIPPET: helloWorldString
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("print \"hello world\"");
        // END SNIPPET: helloWorldString
    }

    @Test
    public void testEvalStringSW() throws Exception {
        // START SNIPPET: helloWorldStringSW
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        engine.eval("print \"hello world\"");
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldStringSW
    }

    @Test
    public void testEvalReader() throws Exception {
        // START SNIPPET: helloWorldReader
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        StringReader sr = new StringReader("print \"hello world\"");
        engine.eval(sr);
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldReader
    }

    @Test
    public void testEvalFile() throws Exception {
        // START SNIPPET: helloWorldFile
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        File file = new File(getClass().getResource("hello.bas").getFile());
        engine.eval(file);
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldFile
    }

    @Test
    public void testEvalPath() throws Exception {
        // START SNIPPET: helloWorldPath
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        String path = new File(getClass().getResource("hello.bas").getFile())
                .getParent();
        engine.eval("include.bas", path);
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldPath
    }

    public void documentOnlyForTheSnippetNotCalledEver() throws Exception {
        // START SNIPPET: helloWorldPathMultiple
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("include.bas", ".", "..", "/usr/include/scriptbasic");
        // END SNIPPET: helloWorldPathMultiple
    }

    @Test
    public void testEvalSourcePath() throws Exception {
        // START SNIPPET: helloWorldSourcePath
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        String path = new File(getClass().getResource("hello.bas").getFile())
                .getParent();
        SourcePath sourcePath = new BasicSourcePath();
        sourcePath.add(path);
        engine.eval("include.bas", sourcePath);
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldSourcePath
    }

    @Test
    public void testEvalSourceProvider() throws Exception {
        // START SNIPPET: helloWorldSourceProvider
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        SourceProvider provider = new SourceProvider() {
            private Map<String, String> source = new HashMap<>();

            {
                source.put("hello.bas", "print \"hello world\"");
                source.put("include.bas", "include \"hello.bas\"");
            }

            @Override
            public SourceReader get(String sourceName, String referencingSource)
                    throws IOException {
                return get(sourceName);
            }

            @Override
            public SourceReader get(String sourceName) throws IOException {
                final SourceReader reader = new GenericSourceReader(new StringReader(source.get(sourceName)), this, sourceName);
                return new GenericHierarchicalSourceReader(reader);
            }
        };
        engine.eval("include.bas", provider);
        sw.close();
        assertEquals("hello world", sw.toString());
        // END SNIPPET: helloWorldSourceProvider
    }

    @Test
    public void testSetGlobalVariable() throws Exception {
        // START SNIPPET: setGlobalVariable
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        engine.setVariable("a", 13);
        engine.eval("print a + \"hello world\"");
        sw.close();
        assertEquals("13hello world", sw.toString());
        // END SNIPPET: setGlobalVariable
    }

    @Test
    public void testGetGlobalVariable() throws Exception {
        // START SNIPPET: getGlobalVariable
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("a = \"hello world\"");
        String a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        // END SNIPPET: getGlobalVariable
    }

    @Test
    public void testListGlobalVariable() throws Exception {
        // START SNIPPET: listGlobalVariable
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("a = \"hello world\"\nb=13");
        String varnames = "";
        for (String varname : engine.getVariablesIterator()) {
            varnames += varname;
        }
        Assert.assertTrue(varnames.indexOf('a') != -1);
        Assert.assertTrue(varnames.indexOf('b') != -1);
        assertEquals(2, varnames.length());
        // END SNIPPET: listGlobalVariable
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSubroutineCallWOArgumentsWORetvalLocalVarIsLocal()
            throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie\na = \"hello world\"\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        engine.getSubroutine("applePie").call((Object[]) null);
        a = (String) engine.getVariable("a");
        assertNull(a);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSubroutineCallWOArgumentsWORetval() throws Exception {
        // START SNIPPET: subroutineCallWOArgumentsWORetval
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie\nglobal a\na = \"hello world\"\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        engine.getSubroutine("applePie").call((Object[]) null);
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        // END SNIPPET: subroutineCallWOArgumentsWORetval
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSubroutineCallWArgumentsWORetval() throws Exception {
        // START SNIPPET: subroutineCallWArgumentsWORetval
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nglobal a\na = b\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        engine.getSubroutine("applePie").call("hello world");
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        // END SNIPPET: subroutineCallWArgumentsWORetval
    }

    @Test
    public void testSubroutineCallWOArgumentsWORetvalOO() throws Exception {
        // START SNIPPET: subroutineCallWOArgumentsWORetvalOO
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie\nglobal a\na = \"hello world\"\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        Subroutine applePie = engine.getSubroutine("applePie");
        applePie.call((Object[]) null);
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        // END SNIPPET: subroutineCallWOArgumentsWORetvalOO
    }

    @Test
    public void testSubroutineCallWArgumentsWORetvalOO() throws Exception {
        // START SNIPPET: subroutineCallWArgumentsWORetvalOO
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nglobal a\na = b\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        Subroutine applePie = engine.getSubroutine("applePie");
        applePie.call("hello world");
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        // END SNIPPET: subroutineCallWArgumentsWORetvalOO
    }

    @SuppressWarnings("deprecation")
    @Test(expected = ScriptBasicException.class)
    public void testSubroutineCallWArgumentsWRetval1() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nglobal a\na = b\nreturn 6\nEndSub");
        engine.getSubroutine("applePie").call("hello world", "mama");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSubroutineCallWArgumentsWRetval2() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b,c)\nglobal a\na = c\nreturn 6\nEndSub");
        String a = (String) engine.getVariable("a");
        engine.getSubroutine("applePie").call("hello world");
        a = (String) engine.getVariable("a");
        assertNull(a);
    }

    @Test
    public void testSubroutineCallWArgumentsWRetval() throws Exception {
        // START SNIPPET: subroutineCallWArgumentsWRetval
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nglobal a\na = b\nreturn 6\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        @SuppressWarnings("deprecation")
        Long ret = (Long) engine.getSubroutine("applePie").call("hello world");
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        assertEquals((Long) 6L, ret);
        // END SNIPPET: subroutineCallWArgumentsWRetval
    }

    @Test
    public void testSubroutineCallWArgumentsWRetvalOO() throws Exception {
        // START SNIPPET: subroutineCallWArgumentsWRetvalOO
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nglobal a\na = b\nreturn 6\nEndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        Subroutine applePie = engine.getSubroutine("applePie");
        Long ret = (Long) applePie.call("hello world");
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        assertEquals((Long) 6L, ret);
        // END SNIPPET: subroutineCallWArgumentsWRetvalOO
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSubroutineList() throws Exception {
        // START SNIPPET: subroutineList
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nEndSub\nsub anotherSubroutine\nEndSub\n");
        final AtomicInteger i = new AtomicInteger(0);
        engine.getSubroutines().forEach((s) -> i.incrementAndGet());
        assertEquals(2, i.get());
        assertEquals(1, engine.getSubroutine("applePie").getNumberOfArguments());
        assertEquals(0, engine.getSubroutine("anotherSubroutine").getNumberOfArguments());
        // END SNIPPET: subroutineList
    }

    @Test
    public void testSubroutineCallWArgumentsWRetval2007() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("" +
                "sub applePie(b,c)\n" +
                "global a\n" +
                "a = c\n" +
                "return 6\n" +
                "EndSub");
        Subroutine sub = engine.getSubroutine("applePie");
        sub.call("hello world");
        final String a = (String) engine.getVariable("a");
        assertNull(a);
    }

    @Test
    public void testSubroutineCallWArgumentsWRetval007() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.load("" +
                "sub applePie(b)\n" +
                "global a\n" +
                "a = b\n" +
                "return 6\n" +
                "EndSub");
        String a = (String) engine.getVariable("a");
        assertNull(a);
        Subroutine sub = engine.getSubroutine("applePie");
        Long returnValue = (Long) sub.call("hello world");
        a = (String) engine.getVariable("a");
        assertEquals("hello world", a);
        assertEquals((Long) 6L, returnValue);
    }

    @Test
    public void testSubroutineList007() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.load("sub applePie(b)\nEndSub\nsub anotherSubroutine\nEndSub\n");
        int i = 0;
        for (@SuppressWarnings("unused")
                Subroutine sub : engine.getSubroutines()) {
            i++;
        }
        assertEquals(2, i);
        Subroutine applePie = engine.getSubroutine("applePie");
        assertEquals(1, applePie.getNumberOfArguments());
        Subroutine anotherSubroutine = engine
                .getSubroutine("anotherSubroutine");
        assertEquals(0, anotherSubroutine.getNumberOfArguments());
    }

    @Test
    public void getsTheSubroutineByName() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("sub applePie(b)\nEndSub\n");
        Subroutine applePie = engine.getSubroutine("applePie");
        assertEquals("applePie", applePie.getName());
    }

    @Test(expected = ScriptBasicException.class)
    public void throwsExceptionWhenSubroutineIsUndefined() throws ScriptBasicException {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("call applePie");
    }

    @Test(expected = ScriptBasicException.class)
    public void throwsExceptionWhenFileDoesNotExist() throws ScriptBasicException {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval(new File("this file is totally nonexistent"));
    }

    @Test(expected = ScriptBasicException.class)
    public void throwsExceptionWhenSourceProviderDoes() throws ScriptBasicException {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.eval("kakukk.bas", new SourceProvider() {

            @Override
            public SourceReader get(String sourceName, String referencingSource)
                    throws IOException {
                throw new IOException();
            }

            @Override
            public SourceReader get(String sourceName) throws IOException {
                throw new IOException();
            }
        });
    }

    @Test
    public void testLoadString() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.load("print \"hello world\"");
        engine.execute();
    }

    @Test
    public void testLoadStringSW() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        engine.load("print \"hello world\"");
        engine.execute();

        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadReader() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        StringReader sr = new StringReader("print \"hello world\"");
        engine.load(sr);
        engine.execute();

        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadFile() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        File file = new File(getClass().getResource("hello.bas").getFile());
        engine.load(file);
        engine.execute();

        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadPath() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        String path = new File(getClass().getResource("hello.bas").getFile())
                .getParent();
        engine.load("include.bas", path);
        engine.execute();

        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadSourcePath() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        String path = new File(getClass().getResource("hello.bas").getFile())
                .getParent();
        SourcePath sourcePath = new BasicSourcePath();
        sourcePath.add(path);
        engine.load("include.bas", sourcePath);
        engine.execute();
        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadSourceProvider() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(10);
        engine.setOutput(sw);
        SourceProvider provider = new SourceProvider() {
            private Map<String, String> source = new HashMap<>();

            {
                source.put("hello.bas", "print \"hello world\"");
                source.put("include.bas", "include \"hello.bas\"");
            }

            @Override
            public SourceReader get(String sourceName, String referencingSource)
                    throws IOException {
                return get(sourceName);
            }

            @Override
            public SourceReader get(String sourceName) throws IOException {
                final SourceReader reader = new GenericSourceReader(new StringReader(source.get(sourceName)), this, sourceName);
                return new GenericHierarchicalSourceReader(reader);
            }
        };
        engine.load("include.bas", provider);
        engine.execute();
        sw.close();
        assertEquals("hello world", sw.toString());
    }

    @Test
    public void testLoadStringSWMultipleExecute() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        StringWriter sw = new StringWriter(22);
        engine.setOutput(sw);
        engine.load("print \"hello world\"");
        engine.execute();
        engine.execute();
        sw.close();
        assertEquals("hello worldhello world", sw.toString());
    }

    @Test(expected = ScriptBasicException.class)
    public void testNoLoadStringSWMultipleExecute() throws Exception {
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.execute();
    }

    @Test
    public void testRegisterExtension() throws Exception {
        // START SNIPPET: testExtensionMethod
        ScriptBasic engine = ScriptBasic.getEngine();
        engine.load("Sub aPie\nreturn javaFunction()\nEndSub\n");
        engine.registerExtension(TestExtensionClass.class);
        engine.execute();
        Long z = (Long) engine.getSubroutine("aPie").call();
        assertEquals((Long) 55L, z);
        // END SNIPPET: testExtensionMethod
    }

    // END SNIPPET: testExtensionClass

    // START SNIPPET: testExtensionClass
    public static class TestExtensionClass {
        @BasicFunction(alias = "javaFunction", classification = java.lang.Long.class, requiredVersion = 1)
        public static Long fiftyFive() {
            return 55L;
        }
    }
}
