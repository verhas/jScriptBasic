/**
 * 
 */
package com.scriptbasic.utility;

/**
 * @author Peter Verhas
 * @date Jun 30, 2012
 * 
 */
public final class CastUtility {
    private CastUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    private static class Byte {
        static java.lang.Byte cast(Object object) {
                return  ((Number) object).byteValue();
        }
    }

    private static class Short {
        static java.lang.Short cast(Object object) {
                return  ((Number) object).shortValue();
        }
    }

    private static class Integer {
        static java.lang.Integer cast(Object object) {
                return  ((Number) object).intValue();
        }
    }

    private static class Long {
        static java.lang.Long cast(Object object) {
                return  ((Number) object).longValue();
        }
    }

    private static class Float {
        static java.lang.Float cast(Object object) {
                return  ((Number) object).floatValue();
        }
    }

    private static class Double {
        static java.lang.Double cast(Object object) {
            return ((Number) object).doubleValue();
        }
    }

    /**
     * Convert an object to another object so that the result does have the type
     * {@code castTo}.
     * <p>
     * This is not a generic solution but does some handy conversion like Long
     * to Integer so that the BASIC programs can easily call Java methods that
     * accept int, float etc.
     * <p>
     * Casting is done on best effort. If the class is unknown to the utility
     * then the original object is returned and it is up to the higher level
     * code to recognize the class mis-alignment.
     * 
     * @param object
     *            the object to convert.
     * @param castTo
     *            the class that the result has to belong to.
     * @return the converted object.
     */
    public static Object cast(Object object, Class<?> castTo) {
        Object result = object;
        try {
            switch (castTo.getName()) {
            case "byte":
            case "java.lang.Byte":
                result = Byte.cast(object);
                break;
            case "short":
            case "java.lang.Short":
                result = Short.cast(object);
                break;
            case "int":
            case "java.lang.Integer":
                result = Integer.cast(object);
                break;
            case "long":
            case "java.lang.Long":
                result = Long.cast(object);
                break;
            case "float":
            case "java.lang.Float":
                result = Float.cast(object);
                break;
            case "double":
            case "java.lang.Double":
                result = Double.cast(object);
                break;
            default:
                break;
            }
        } catch (ClassCastException cce) {

        }
        return result;
    }
}
