package com.scriptbasic.coverage;

import com.scriptbasic.exceptions.BasicLexicalException;
import com.scriptbasic.lexer.BasicKeywordRecognizer;



/**
 * This test just calls some trivial code to increase the test coverage.
 * <p>
 * This may seem stupid, but it has a very good reason: it eliminate the need to
 * check manually the coverage and see what is not covered.
 * 
 * @author Peter Verhas
 * date Jun 8, 2012
 */
public class TestToExtendCoverage  {
    @SuppressWarnings("static-method")
    public void testCover() {
        BasicLexicalException bela = new BasicLexicalException();
        bela.getFileName();
        bela.getLineNumber();
        bela.getPosition();
        new BasicLexicalException("");
        new BasicLexicalException((Throwable) null);
        new BasicLexicalException("", null);
        new BasicKeywordRecognizer().setKeywords(new BasicKeywordRecognizer().getKeywords());
        
    }
}
