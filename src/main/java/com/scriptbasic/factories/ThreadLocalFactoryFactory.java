package com.scriptbasic.factories;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.NoInstance;

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
        NoInstance.isPossible();
    }

    private static ThreadLocal<Factory> threadLocalFactory = ThreadLocal.withInitial(() -> FactoryFactory.getFactory());

    public static Factory getFactory() {
        return threadLocalFactory.get();
    }
}
