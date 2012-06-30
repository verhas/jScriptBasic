/**
 * 
 */
package com.scriptbasic.utility;

/**
 * @author Peter Verhas
 * @date Jun 30, 2012
 * 
 */
public class CastUtility {
    private CastUtility() {
        UtilityUtility.assertUtilityClass();
    }

    private static class Integer {
        static java.lang.Integer cast(Object object) {
            java.lang.Integer result = null;
            if (object instanceof Long) {
                result = ((Long) object).intValue();
            }
            return result;
        }
    }

    /**
     * Convert an object to another object so that the result does have the type
     * {@code castTo}.
     * <p>
     * This is not a generic solution but does some handy conversion like Long
     * to Integer so that the BASIC programs can easily call Java methods that
     * accept int, float etc.
     * 
     * @param object
     *            the object to convert.
     * @param castTo
     *            the class that the result has to belong to.
     * @return the converted object.
     */
    public static Object cast(Object object, Class<?> castTo) {
        Object result = object;
        if (castTo.equals(int.class) || castTo.equals(Integer.class)) {
            result = Integer.cast(object);
        }
        return result;
    }
}
