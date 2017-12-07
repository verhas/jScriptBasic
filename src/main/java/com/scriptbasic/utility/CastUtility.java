package com.scriptbasic.utility;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.spi.RightValue;

import java.util.List;
import java.util.function.Function;

/**
 * @author Peter Verhas date Jun 30, 2012
 */
public final class CastUtility {
    private static final List<P> converters = List.of(P.of(byte.class, Byte.class, object -> ((Number) object).byteValue()),
            P.of(short.class, Short.class, object -> ((Number) object).shortValue()),
            P.of(int.class, Integer.class, object -> ((Number) object).intValue()),
            P.of(long.class, Long.class, object -> ((Number) object).longValue()),
            P.of(float.class, Float.class, object -> ((Number) object).floatValue()),
            P.of(double.class, Double.class, object -> ((Number) object).doubleValue()),
            P.of(char.class, Character.class, object -> (char) ((Number) object).intValue())
    );

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
        try {
            return converters.stream()
                    .filter(p -> p.primitive == castTo || p.objectClass == castTo)
                    .map(p -> p.converter.apply(object))
                    .findFirst()
                    .orElse(object);
        } catch (final ClassCastException cce) {
            return object;
        }
    }

    @SuppressWarnings("unchecked")
    public static Object toObject(final RightValue rightValue) {
        return rightValue == null ? null
                : ((AbstractPrimitiveRightValue<Object>) rightValue).getValue();
    }

    private static class P {
        final Class<?> primitive, objectClass;
        final Function<Object, Object> converter;

        private P(Class<?> primitive, Class<?> objectClass, Function<Object, Object> converter) {
            this.primitive = primitive;
            this.objectClass = objectClass;
            this.converter = converter;
        }

        static P of(Class<?> primitive, Class<?> objectClass, Function<Object, Object> converter) {
            return new P(primitive, objectClass, converter);
        }
    }

}
