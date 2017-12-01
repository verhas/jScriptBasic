package com.scriptbasic.utility.functions;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.utility.NoInstance;

public class ErrorFunctions {
    private ErrorFunctions() {
        NoInstance.isPossible();
    }

    /**
     * Throw an exception causing BASIC runtime error. This method can be called
     * directly from BASIC to stop the interpreter with an error condition.
     *
     * @param s the message in the error
     * @throws ExecutionException always
     */
    @BasicFunction(classification = com.scriptbasic.classification.Utility.class)
    static public void error(final String s) throws ExecutionException {
        throw new BasicRuntimeException(s);
    }

    /**
     * Throw an exception causing BASIC runtime error if the assertion fails.
     * This can be used in the BASIC program to test preconditions and stop the
     * execution of the BASIC program if the condition fails. For example you
     * can write
     * <p>
     * <pre>
     * assert("test that the parameter starts with the required prefix", startsWith(s,prefix))
     * </pre>
     * <p>
     * In the example above if the variable {@code s} starts with the string
     * contained in the variable {@code prefix} then the execution of the BASIC
     * program goes on, otherwise the function causes BASIC runtime error.
     *
     * @param s
     * @param b
     * @throws ExecutionException
     */
    @BasicFunction(classification = com.scriptbasic.classification.Utility.class, alias = "assert")
    static public void asserT(final String s, final Boolean b) throws ExecutionException {
        if (!b) {
            throw new BasicRuntimeException(s);
        }
    }

}
