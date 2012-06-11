package com.scriptbasic.factories;

import com.scriptbasic.interfaces.Factory;


public class FactoryFactory {
    private static Factory singleton = new BasicFactory();

    public static Factory getFactory() {
        return singleton;
    }
}
