package com.scriptbasic.factories;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;

/**
 * Implements a factory factory that returns a new instance for each thread.
 * <p>
 * When the thread does not have an instance then the code uses the utility
 * class {@link FactoryFactory} to get a new instance.
 * 
 * @author Peter Verhas
 * date Aug 1, 2012
 * 
 */
public final class ThreadLocalFactoryFactory {
    private ThreadLocalFactoryFactory() {
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + ThreadLocalFactoryFactory.class.getCanonicalName());
    }

    private static ThreadLocal<Factory> threadLocalFactory = new ThreadLocal<Factory>() {
        @Override
        protected Factory initialValue() {
            return FactoryFactory.getFactory();
        }
    };

    public static Factory getFactory() {
        return threadLocalFactory.get();
    }
}
