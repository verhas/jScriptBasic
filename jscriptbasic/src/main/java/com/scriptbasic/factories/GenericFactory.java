package com.scriptbasic.factories;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.interfaces.FactoryManaged;

/**
 * A {@see FactoryFactory} that creates and manages new instances of classes for
 * the different threads.
 * 
 * @author Peter Verhas
 * @date June 8, 2012
 */
public class GenericFactory extends AbstractFactory {

    private transient Map<Class<? extends FactoryManaged>, FactoryManaged> analyzerMap = new HashMap<>();

    @Override
    public void clean() {
        analyzerMap = new HashMap<>();
    }

    /**
     * This method is used by the {@see AbstractFactory} to store created
     * objects.
     */
    @Override
    <T extends FactoryManaged> void set(final Class<T> klass, final T analyzer) {
        analyzerMap.put(klass, analyzer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FactoryManaged> T get(final Class<T> klass) {
        assertInterface(klass);
        return (T) analyzerMap.get(klass);
    }

}
