package com.scriptbasic.syntax;

import junit.framework.TestCase;

import com.scriptbasic.exceptions.CommandCanNotBeCreatedException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.utility.FactoryUtilities;
import com.scriptbasic.utility.SyntaxExceptionUtility;

public class AuxTests extends TestCase {

    @SuppressWarnings("static-method")
    public void testExceptions() {
        new SyntaxExceptionUtility();
        new FactoryUtilities();
        new CommandCanNotBeCreatedException("bla bla", null);
        new KeywordNotImplementedException("hukk");
        final SyntaxException c = new GenericSyntaxException("hull", (Throwable)null);
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
            assertTrue("throwSyntaxException did not throw exception", false);
        } catch (SyntaxException se) {
            // OK
        }
        try {
            SyntaxExceptionUtility.throwSyntaxException("",
                    new BasicLexicalElement());
            assertTrue("throwSyntaxException did not throw exception", false);
        } catch (SyntaxException se) {
            // OK
        }
    }

}
