package com.scriptbasic.interfaces;

import java.io.IOException;

import junit.framework.TestCase;

public class TestLexicalException extends TestCase {
    public TestLexicalException(final String testName) {
        super(testName);
    }

    public void test() throws IOException {
        class TestedLexicalException extends LexicalException {

            private static final long serialVersionUID = 1L;

            @Override
            public String fileName() {
                return null;
            }

            @Override
            public int lineNumber() {
                return 0;
            }

            @Override
            public int position() {
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
