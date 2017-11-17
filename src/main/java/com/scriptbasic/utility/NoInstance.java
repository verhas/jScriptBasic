package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;

public class NoInstance {
    private NoInstance() {
        isPossible();
    }

    public static void isPossible() {
        final Class<?> klass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + klass.getCanonicalName());
    }

}
