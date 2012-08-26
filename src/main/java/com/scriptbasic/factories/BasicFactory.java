package com.scriptbasic.factories;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;

/**
 * Implementing the interface {@see com.scriptbasic.interfaces.Factory} this
 * class instantiates the objects using the JDK standard {@see
 * java.util.ServiceLoader}. A single factory instance will maintain a single
 * instance of each class and subsequent call on the same factory object to the
 * method {@see #get(Class)} will return the same class instance for the same
 * interface.
 * <p>
 * The implementation also checks that the argument passed to the method is an
 * interface and not a class.
 * 
 * @author Peter Verhas
 * date June 8, 2012
 * 
 */
public class BasicFactory implements Factory {
    /**
     * Asserts that the parameter is an interface and not a class. If the
     * assertion fails a runtime exception is thrown.
     * 
     * @param interfAce
     *            the interface to check
     */
    private static void assertInterface(
            Class<? extends FactoryManaged> interfAce) {
        if (!interfAce.isInterface()) {
            throw new IllegalArgumentException("The class " + interfAce
                    + " is not an interface, can not be used in this factory.");
        }
    }

    /**
     * Load a new instance of a class that implements the {@code interfAce}. If
     * the load is successful the new object is registered in the factory and
     * also the factory is set in the object.
     * 
     * @param interfAce
     *            is the interface for which we need an implementation.
     */
    private <T extends FactoryManaged> void load(Class<T> interfAce) {
        ServiceLoader<T> loader = ServiceLoader.load(interfAce);
        Iterator<T> iterator = loader.iterator();
        if (iterator.hasNext()) {
            T object = iterator.next();
            store.set(interfAce, object);
            object.setFactory(this);
        } else {
            throw new BasicInterpreterInternalError("Can not instantiate "
                    + interfAce);
        }
    }

    final private ManagedObjectsStore store = new ManagedObjectsStore();

    /**
     * {@inheritDoc}
     * 
     * The implementation checks that the argument is an interface and throws
     * IllegalArgumentException if the argument is a class.
     */
    @Override
    public <T extends FactoryManaged> T get(final Class<T> interfAce) {
        assertInterface(interfAce);
        T object = store.get(interfAce);
        if (object == null) {
            load(interfAce);
            object = store.get(interfAce);
        }
        return object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Factory#clean()
     */
    @Override
    public void clean() {
        store.clean();
    }

}
