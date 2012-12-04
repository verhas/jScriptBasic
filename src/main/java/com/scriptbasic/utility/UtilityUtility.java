 /**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;

/**
 * @author Peter Verhas
 * date June 28, 2012
 * 
 */
public final class UtilityUtility {
    private UtilityUtility() {
        throwExceptionToEnsureNobodyCallsIt();
    }

    public static void throwExceptionToEnsureNobodyCallsIt() {
        throw new BasicInterpreterInternalError(
                "Should not instantiate utility class");
    }

}
