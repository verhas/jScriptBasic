package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.lexer.BasicLexicalElement;
import org.junit.Test;

/**
 * @author Peter Verhas
 * date Jul 1, 2012
 */
public class ExceptionTest {
    @SuppressWarnings("serial")
    @Test
    public void test() {
        GeneralAnalysisException gae = new GeneralAnalysisException() {
        };
        gae.setFileName("fileName");
        gae.setLineNumber(13);
        LexicalElement le = new BasicLexicalElement();
        gae.setLocation(le);
        new CommandFactoryException();
        new CommandFactoryException("");
        new CommandFactoryException((Exception) null);
        new CommandFactoryException("", null);
    }
}
