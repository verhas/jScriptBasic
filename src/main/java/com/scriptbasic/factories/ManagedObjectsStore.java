package com.scriptbasic.factories;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement the storage part of the factory. The objects managed by the factory
 * are stored in a hash map and when an object that was already created for a
 * certain interface is requested the existing instance will be given.
 *
 * @author Peter Verhas
 * date June 8, 2012
 */
public class ManagedObjectsStore {

    private final Map<Class<?>, Object> factoryManagedObjectMap = new HashMap<>();

    public void clean() {
        factoryManagedObjectMap.clear();
    }

    void set(final Class<?> klass,
             final Object factoryManagedObject) {
        factoryManagedObjectMap.put(klass, factoryManagedObject);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> klass) {
        return (T) factoryManagedObjectMap.get(klass);
    }

}
