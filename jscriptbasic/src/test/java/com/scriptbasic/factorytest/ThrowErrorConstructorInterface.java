package com.scriptbasic.factorytest;

import com.scriptbasic.interfaces.FactoryManaged;
/**
 * A test interface that can not be handled by the factory. This is needed to unit
 * test the error handling of the factory.
 * 
 * @author Peter Verhas
 * @date Jul 9, 2012
 * 
 */
public interface ThrowErrorConstructorInterface extends FactoryManaged {

}
