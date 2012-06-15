package com.scriptbasic.interfaces;

/**
 * Generic factory interface to create and get objects, as factories do.
 * 
 * @author Peter Verhas
 * @date Jun 7, 2012
 */
public interface Factory {

    /**
     * Call this method to create or replace an instance of the class defined by
     * the interface {@code interf4ce}. The object will be created calling the
     * default constructor of the class {@code cl4ss}.
     * <p>
     * If the class can not be instantiated then the implementation will throw
     * {@see BasicInterpreterInternalError}.
     * <p>
     * To avoid other code to create instance of the class out of the
     * {@code Factory} the class should have only one default constructor and it
     * should not be public. If the class has public constructor or has more
     * than one constructors the method will throw {@see
     * BasicInterpreterInternalError}
     * <p>
     * See also {@see FactoryManaged}
     * 
     * @param interf4ce
     *            the interface that will be used in subsequent calls to {@see
     *            #get(Class)}. Note that this is an error to pass a class that
     *            is not an interface and implementations may throw {@see
     *            BasicInterpreterInternalError} in such a case.
     * @param cl4ss
     *            the class that represents the interface. An instance of this
     *            class will be created by the factory.
     */
    <T extends FactoryManaged> void create(Class<T> interf4ce,
            Class<? extends T> cl4ss);

    /**
     * Get an instance of the interface. Some of the implementation define the
     * class that implements the interface and if there was no preceding call to
     * {@see #create(Class, Class)} then instantiate the class.
     * 
     * @param klass
     *            the interface. It is an error to pass a class that is not an
     *            interface. In that case the implementation may throw {@see
     *            BasicInterpreterInternalError}.
     * @return the instance implementing the interface.
     */
    <T extends FactoryManaged> T get(Class<T> klass);

    /**
     * Clean the factory. Remove all references to the objects that were created
     * by the factory.
     * <p>
     * When the factory creates thread singletons (thread local instances) then
     * this is important to call this method to release the objects before the
     * thread finishes.
     */
    void clean();
}
