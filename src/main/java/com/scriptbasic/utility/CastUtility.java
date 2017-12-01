package com.scriptbasic.utility;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.RightValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Verhas date Jun 30, 2012
 */
public final class CastUtility {
    private static final Map<String, java.lang.Integer> castTypeNames = new HashMap<>();

    static {
        int i = 1;
        for (String name : new String[]{"byte", "short", "int", "long",
                "float", "double"}) {
            castTypeNames.put(name, i);
            if (i == 3) {
                name = "java.lang.Integer";
            } else {
                name = "java.lang." + name.substring(0, 1).toUpperCase()
                        + name.substring(1);
            }
            castTypeNames.put(name, i);
            i++;
        }
    }

    private CastUtility() {
        NoInstance.isPossible();
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
     * @param object the object to convert.
     * @param castTo the class that the result has to belong to.
     * @return the converted object.
     */
    public static Object cast(final Object object, final Class<?> castTo) {
        Object result = object;
        try {
            if (castTypeNames.get(castTo.getName()) != null) {
                switch (castTypeNames.get(castTo.getName())) {
                    case 1:
                        result = Byte.cast(object);
                        break;
                    case 2:
                        result = Short.cast(object);
                        break;
                    case 3:
                        result = Integer.cast(object);
                        break;
                    case 4:
                        result = Long.cast(object);
                        break;
                    case 5:
                        result = Float.cast(object);
                        break;
                    case 6:
                        result = Double.cast(object);
                        break;
                    default:
                        break;
                }
            }
        } catch (final ClassCastException cce) {
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Object toObject(final RightValue rightValue) {
        return rightValue == null ? null
                : ((AbstractPrimitiveRightValue<Object>) rightValue).getValue();
    }

    private static class Byte {
        static java.lang.Byte cast(final Object object) {
            return ((Number) object).byteValue();
        }
    }

    private static class Short {
        static java.lang.Short cast(final Object object) {
            return ((Number) object).shortValue();
        }
    }

    private static class Integer {
        static java.lang.Integer cast(final Object object) {
            return ((Number) object).intValue();
        }
    }

    private static class Long {
        static java.lang.Long cast(final Object object) {
            return ((Number) object).longValue();
        }
    }

    private static class Float {
        static java.lang.Float cast(final Object object) {
            return ((Number) object).floatValue();
        }
    }

    private static class Double {
        static java.lang.Double cast(final Object object) {
            return ((Number) object).doubleValue();
        }
    }
}
