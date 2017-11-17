package com.scriptbasic.factories;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.NoInstance;

/**
 * This utility class manages instances of the {@link BasicFactory} class.
 * <p>
 * The class {@link BasicFactory} is not singleton, however this class creates a
 * single instance and manages that single instance.
 * 
 * @author Peter Verhas
 * date Aug 1, 2012
 * 
 */
public final class FactoryFactory {
    private FactoryFactory() {
        NoInstance.isPossible();
    }

    /**
     * Get a new instance of the factory.
     * 
     * @return a new instance.
     */
    public static Factory getFactory() {
        return FactoryServiceLoader.loadFactory();
    }
}
