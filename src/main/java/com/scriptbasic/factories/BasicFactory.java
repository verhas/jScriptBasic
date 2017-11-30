package com.scriptbasic.factories;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;

import java.util.Optional;

/**
 * This class instantiates an implementation of the interface as requested. The instantiation is
 * wired into the code of this class including instantiation of all the dependencies. This way
 * this class works as a small DI context.
 *
 * @author Peter Verhas
 * date November 29, 2017
 */
public class BasicFactory implements Factory {

    final private ManagedObjectsStore store = new ManagedObjectsStore();

    private <T> Optional<T> instantiate(Class<T> interfAce, Object... arguments) {
        return Optional.empty();
    }

    /**
     * Load a new instance of a class that implements the {@code interfAce}. If
     * the load is successful the new object is registered in the factory and
     * also the factory is set in the object.
     *
     * @param interfAce is the interface for which we need an implementation.
     */
    private <T> void load(Class<T> interfAce, Object... arguments) {
        Optional<T> object = instantiate(interfAce, arguments);
        if (object.isPresent()) {
            store.set(interfAce, object.get());
        } else {
            throw new BasicInterpreterInternalError("Can not instantiate "
                    + interfAce);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation checks that the argument is an interface and throws
     * IllegalArgumentException if the argument is a class.
     */
    @Override
    public <T> T get(final Class<T> interfAce, Object... arguments) {
        T object = store.get(interfAce);
        if (object == null) {
            load(interfAce, arguments);
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
