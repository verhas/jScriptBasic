package com.scriptbasic.coverage;

import com.scriptbasic.exceptions.BasicLexicalException;
import org.junit.Test;


/**
 * This test just calls some trivial code to increase the test coverage.
 * <p>
 * This may seem stupid, but it has a very good reason: it eliminates the need to
 * check manually the coverage every time to see what is not covered and to manually check
 * that all code needed is covered. This code covers the trivial code that does not need
 * to be covered and that way anything leading to less than 100% coverage is suspicious.
 * 
 * @author Peter Verhas
 * date Jun 8, 2012
 */
public class TestToExtendCoverage  {

    @Test
    public void testCover() {
        BasicLexicalException bela = new BasicLexicalException();
        bela.getFileName();
        bela.getLineNumber();
        bela.getPosition();
        new BasicLexicalException("");
        new BasicLexicalException((Throwable) null);
        new BasicLexicalException("", null);

    }
}
