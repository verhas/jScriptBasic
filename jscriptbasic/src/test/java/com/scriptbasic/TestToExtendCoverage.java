package com.scriptbasic;

import com.scriptbasic.lexer.BasicKeywordRecognizer;
import com.scriptbasic.lexer.BasicLexicalException;

import junit.framework.TestCase;

/**
 * This test just calls some trivial code to increase the test coverage.
 * <p>
 * This may seem stupid, but it has a very good reason: it eliminate the need to
 * check manually the coverage and see what is not covered.
 * 
 * @author Peter Verhas
 * @date Jun 8, 2012
 */
public class TestToExtendCoverage extends TestCase {
    public void testCover() {
        BasicLexicalException bela = new BasicLexicalException();
        bela.fileName();
        bela.lineNumber();
        bela.position();
        new BasicLexicalException("");
        new BasicLexicalException((Throwable) null);
        new BasicLexicalException("", null);
        BasicKeywordRecognizer.setKeywords(BasicKeywordRecognizer.getKeywords());
        
    }
}
