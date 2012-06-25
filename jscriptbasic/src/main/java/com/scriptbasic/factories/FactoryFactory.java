package com.scriptbasic.factories;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;

public final class FactoryFactory {
    private FactoryFactory() {
        throw new BasicInterpreterInternalError(
                "Do not even try to instantiate "
                        + FactoryFactory.class.getCanonicalName());
    }

    private static Factory singleton = new BasicFactory();

    public static Factory getFactory() {
        return singleton;
    }
}
