package com.scriptbasic.exceptions;

import com.scriptbasic.lexer.BasicLexicalElement;
import org.junit.jupiter.api.Test;

/**
 * @author Peter Verhas
 * date Jul 1, 2012
 */
@SuppressWarnings("ThrowableNotThrown")
public class ExceptionTest {
    @SuppressWarnings("serial")
    @Test
    public void test() {
        final var gae = new GeneralAnalysisException() {
        };
        gae.setFileName("fileName");
        gae.setLineNumber(13);
        final var le = new BasicLexicalElement();
        gae.setLocation(le);
        new CommandFactoryException();
        new CommandFactoryException("");
        new CommandFactoryException((Exception) null);
        new CommandFactoryException("", null);
    }
}
