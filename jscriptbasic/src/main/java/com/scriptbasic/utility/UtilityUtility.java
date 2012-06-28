/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;

/**
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public class UtilityUtility {
    private UtilityUtility() {
        assertUtilityClass();
    }

    static void assertUtilityClass() {
        throw new BasicInterpreterInternalError(
                "Should not instantiate utility class");
    }

}
