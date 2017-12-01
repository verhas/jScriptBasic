package com.scriptbasic.factorytest;

import com.scriptbasic.errors.BasicInterpreterInternalError;

/**
 * A test class that can not be handled by the factory. This is needed to unit
 * test the error handling of the factory.
 * 
 * @author Peter Verhas
 * date Jul 9, 2012
 * 
 */
public final class ThrowErrorConstructorClass implements
        ThrowErrorConstructorInterface {

    private ThrowErrorConstructorClass() {
        throw new BasicInterpreterInternalError(null);
    }

}
