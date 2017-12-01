package com.scriptbasic.utility;


import java.util.function.Function;

public class ConvertUtils {

    public static <T, R> Function<T, R> $(final ExceptionalFunction<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> T cast(final Object obj, final Class<T> t) {
        return (T) obj;
    }

    public interface ExceptionalFunction<T, R> {
        R apply(T r) throws Exception;
    }

}
