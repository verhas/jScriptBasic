package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.Factory;


public class GenericNestedStructureHouseKeeper extends
        AbstractNestedStructureHouseKeeper {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    @Override
    protected Structure seekFrameError(Class<?> expectedType) {
        // TODO Auto-generated method stub
        return null;
    }


}
