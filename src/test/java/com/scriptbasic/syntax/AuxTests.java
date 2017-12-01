package com.scriptbasic.syntax;


import com.scriptbasic.exceptions.CommandCanNotBeCreatedException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.utility.SyntaxExceptionUtility;

import static org.junit.Assert.assertTrue;

public class AuxTests {


    public void testExceptions() {
        // new FactoryUtilities();
        new CommandCanNotBeCreatedException("bla bla", null);
        new KeywordNotImplementedException("hukk");
        final SyntaxException c = new BasicSyntaxException("hull",
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
