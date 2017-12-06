package com.scriptbasic.utility;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.RightValue;

/**
 * @author Peter Verhas date Jun 30, 2012
 */
public final class CastUtility {
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
            final String className = castTo.getName();
            if ("byte".equals(className) || "java.lang.Byte".equals(className)) {
                result = ((Number) object).byteValue();
            } else if ("short".equals(className) || "java.lang.Short".equals(className)) {
                result = ((Number) object).shortValue();
            } else if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                result = ((Number) object).intValue();
            } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
                result = ((Number) object).longValue();
            } else if ("float".equals(className) || "java.lang.Float".equals(className)) {
                result = ((Number) object).floatValue();
            } else if ("double".equals(className) || "java.lang.Double".equals(className)) {
                result = ((Number) object).doubleValue();
            } else if ("char".equals(className) || "java.lang.Character".equals(className)) {
                result = (char) ((Number) object).intValue();
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

}
