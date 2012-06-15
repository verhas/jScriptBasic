package com.scriptbasic.factorytest;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;

public final class ThrowErrorConstructorClass implements
        ThrowErrorConstructorInterface {

    private ThrowErrorConstructorClass() {
        throw new BasicInterpreterInternalError(null);
    }

    @Override
    public void setFactory(Factory factory) {
        // TODO Auto-generated method stub

    }

}
