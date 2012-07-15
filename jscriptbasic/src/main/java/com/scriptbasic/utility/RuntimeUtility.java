/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.executors.MethodRegistry;

/**
 * @author Peter Verhas
 * @date Jul 15, 2012
 * 
 */
public class RuntimeUtility {
    private RuntimeUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    public static Long abs(Long a) {
        return Math.abs(a);
    }

    /**
     * @param methodRegistry
     */
    public static void registerFunctions(MethodRegistry methodRegistry) {
        methodRegistry.registerJavaMethod("abs", RuntimeUtility.class, "abs",
                new Class<?>[] { Long.class });
    }
}
