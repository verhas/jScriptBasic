package com.scriptbasic.factories;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;

/**
 * Using the standard {@see java.util.ServiceLoader} this utility class loads a
 * new instance of the implementation of the interface {@see Factory}.
 * <p>
 * 
 * @author Peter Verhas
 * date Aug 1, 2012
 * 
 */
public final class FactoryServiceLoader {
    private FactoryServiceLoader() {
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + FactoryServiceLoader.class.getCanonicalName());
    }

    /**
     * Load and create a new instance of the implementation of the interface
     * {@see Factory}. If there are multiple implementations then the first one
     * will be loaded. (The first as it is loaded by the standard Java
     * ServiceLoader class.)
     * 
     * @return a new {@see Factory} instance or {@code null} if there is no
     *         implementation of the interface {@see Factory}.
     */
    public static Factory loadFactory() {
        ServiceLoader<Factory> loader = ServiceLoader.load(Factory.class);
        Iterator<Factory> iterator = loader.iterator();
        Factory factory = null;
        if (iterator.hasNext()) {
            factory = iterator.next();
        }
        return factory;
    }
}
