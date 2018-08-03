package com.scriptbasic.syntax;


import com.scriptbasic.exceptions.CommandCanNotBeCreatedException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.utility.SyntaxExceptionUtility;
import org.junit.Test;

import static org.junit.Assert.fail;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AuxTests {

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testExceptions() {
        new CommandCanNotBeCreatedException("bla bla", null);
        new KeywordNotImplementedException("hukk");
        final var c = new BasicSyntaxException("hull",
                (Throwable) null);
        c.setLocation(new BasicLexicalElement());
        c.getFileName();
        c.getLineNumber();
        c.getPosition();
        new BasicRuntimeException();
        new BasicRuntimeException("");
        new BasicRuntimeException((Exception) null);
        new BasicRuntimeException("", null);
        try {
            SyntaxExceptionUtility.throwSyntaxException("", null);
            fail("throwSyntaxException did not throw exception");
        } catch (final SyntaxException se) {
            // OK
        }
        try {
            SyntaxExceptionUtility.throwSyntaxException("",
                    new BasicLexicalElement());
            fail("throwSyntaxException did not throw exception");
        } catch (final SyntaxException se) {
            // OK
        }
    }
}
