package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.LexicalException;

public class TestLexicalException {


    public void test() {
        class TestedLexicalException extends LexicalException {

            private static final long serialVersionUID = 1L;

            public TestedLexicalException(final Throwable arg0) {
                super(arg0);
            }

            public TestedLexicalException() {
                super();
            }

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
        }

        //noinspection ThrowableNotThrown
        new TestedLexicalException();
        //noinspection ThrowableNotThrown
        new TestedLexicalException(new Exception());

    }
}
