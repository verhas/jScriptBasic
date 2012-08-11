package com.scriptbasic.factories;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
/**
 * This utility class manages a single instance of an implementation of the
 * {@see Factory} interface.
 * <p>
 * The class {@see BasicFactory} is not singleton, however this class creates a
 * single instance and manages that single instance.
 *
 * @author Peter Verhas
 * date Aug 1, 2012
 *
 */
public final class SingletonFactoryFactory {
    private SingletonFactoryFactory() {
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + SingletonFactoryFactory.class.getCanonicalName());
    }
    private static Factory singleton = FactoryServiceLoader.loadFactory();
    /**
     * Get the single instance of the factory, managed by this utility class.
     *
     * @return the single instance.
     */
    public static Factory getFactory() {
        return singleton;
    }
}