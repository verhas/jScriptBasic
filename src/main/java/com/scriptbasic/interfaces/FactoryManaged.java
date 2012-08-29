package com.scriptbasic.interfaces;

/**
 * ScriptBasic for Java does not depend on a container by decision to allow the
 * using applications to use their own container and not to introduce any
 * discrepancy. Not having a container in a project of this size needs some
 * services that a simple factory implementation provides.
 * <p>
 * Any interface that defines methods for implementations that are supposed to
 * be managed by the factory should extend this interface. This will ensure that
 * the actual class implements the methods that are needed by the factory to
 * successfully manage the instances.
 * 
 * @author Peter Verhas
 * 
 */
public interface FactoryManaged {
	/**
	 * When a factory creates an instance of a class implementing this interface
	 * this method is called to register the factory that created the instance.
	 * 
	 * @param factory
	 *            the factory that manages the instance.
	 */
	void setFactory(final Factory factory);
}
