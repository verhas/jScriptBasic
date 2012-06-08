package com.scriptbasic.syntax;

import junit.framework.TestCase;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.lexer.BasicLexicalElement;

public class AuxTests extends TestCase {

    public void testExceptions() {
        new CommandCanNotBeCreatedException("bla bla", null);
        new KeywordNotImplemented("hukk");
        final SyntaxException c = new GenericSyntaxException("hull", null);
        c.setLocation(new BasicLexicalElement());
        c.fileName();
        c.lineNumber();
        c.position();
        new BasicRuntimeException();
        new BasicRuntimeException("");
        new BasicRuntimeException((Exception) null);
        new BasicRuntimeException("", null);
    }

}
