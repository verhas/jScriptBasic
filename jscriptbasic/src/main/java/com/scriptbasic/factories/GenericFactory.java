package com.scriptbasic.factories;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.interfaces.FactoryManaged;

/**
 * A {@see FactoryFactory} that creates and manages new instances of classes for
 * the different threads.
 * 
 * @author Peter Verhas
 * @date Jun 8, 2012
 */
public class GenericFactory extends AbstractFactory {

    private static Map<Class<? extends FactoryManaged>, FactoryManaged> newMap() {
        return new HashMap<Class<? extends FactoryManaged>, FactoryManaged>();
    }

    private Map<Class<? extends FactoryManaged>, FactoryManaged> analyzerMap = newMap();

    @Override
    public void clean() {
        analyzerMap=newMap();
    }

    /**
     * This method is used by the {@see AbstractFactory} to store created
     * objects.
     */
    <T extends FactoryManaged> void set(Class<T> klass, T analyzer) {
        analyzerMap.put(klass, analyzer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FactoryManaged> T get(Class<T> klass) {
        assertInterface(klass);
        // TODO how to alter it to avoid this unchecked cast
        return (T) analyzerMap.get(klass);
    }

}
