/**
 * 
 */
package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.lexer.BasicLexicalElement;

import junit.framework.TestCase;

/**
 * @author Peter Verhas
 * date Jul 1, 2012
 * 
 */
public class Test extends TestCase {
    @SuppressWarnings("serial")
	public static void test() {
        GeneralAnalysisException gae = new GeneralAnalysisException() {
        };
        gae.setFileName("fileName");
        gae.setLineNumber(13);
        LexicalElement le = new BasicLexicalElement();
        gae.setLocation(le);
        new CommandFactoryException();
        new CommandFactoryException("");
        new CommandFactoryException((Exception) null);
        new CommandFactoryException("", (Throwable) null);
    }
}
