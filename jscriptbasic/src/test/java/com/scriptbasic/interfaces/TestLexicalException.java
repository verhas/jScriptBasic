package com.scriptbasic.interfaces;

import java.io.IOException;

import com.scriptbasic.exceptions.LexicalException;

import junit.framework.TestCase;

public class TestLexicalException extends TestCase {
    public TestLexicalException(final String testName) {
        super(testName);
    }

    public void test() throws IOException {
        class TestedLexicalException extends LexicalException {

            private static final long serialVersionUID = 1L;

            @Override
            public String getFileName() {
                return null;
            }

            @Override
            public int getLineNumber() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            public TestedLexicalException(final Throwable arg0) {
                super(arg0);
            }

            public TestedLexicalException() {
                super();
            }
        }

        new TestedLexicalException();
        new TestedLexicalException(new Exception());

    }
}
