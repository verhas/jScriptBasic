package com.scriptbasic.factories;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
/**
 * This utility class manages instances of the {@see BasicFactory} class.
 * <p>
 * The class {@see BasicFactory} is not singleton, however this class creates a
 * single instance and manages that single instance.
 *
 * @author Peter Verhas
 * date Aug 1, 2012
 *
 */
public final class FactoryFactory {
    private FactoryFactory() {
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + FactoryFactory.class.getCanonicalName());
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