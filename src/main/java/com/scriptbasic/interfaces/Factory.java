package com.scriptbasic.interfaces;

/**
 * Generic factory interface to create and get objects, as factories usually do.
 *
 * @author Peter Verhas date Jun 7, 2012
 */
public interface Factory {

    /**
     * Get an instance of the interface. Calling the method multiple times with
     * the same argument should return the same instance on the same factory.
     * Implementation are encouraged to check that the parameter argument is an
     * interface and not an ordinary class.
     *
     * @param klass     the interface. It is an error to pass a class that is not an
     *                  interface and is not implemented in the concrete factory.
     *                  In that case the implementation may throw {@link
     *                  com.scriptbasic.errors.BasicInterpreterInternalError}.
     * @param arguments optional arguments for the instantiation of the class.
     * @return the instance implementing the interface.
     */
    <T> T get(Class<T> klass, Object... arguments);

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
