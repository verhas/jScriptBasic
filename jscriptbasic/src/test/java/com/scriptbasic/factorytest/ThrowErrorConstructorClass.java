package com.scriptbasic.factorytest;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
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
    /**
     * Since this class is never instantiated and never used, there is nothing
     * to be done here.
     */
    @Override
    public void setFactory(Factory factory) {
    }
}