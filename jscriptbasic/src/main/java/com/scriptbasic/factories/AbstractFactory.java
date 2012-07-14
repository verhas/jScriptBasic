package com.scriptbasic.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;

/**
 * An abstract implementation of the {@see FactoryFactory} interface that
 * implements the method {@see #create(Class, Class)}. Concrete subclasses of
 * this class should implement the method {@see #set(Class, FactoryManaged)} to
 * support the method {@see #create(Class, Class)}
 * <p>
 * Extending classes can not override the method {@see #create(Class, Class)}.
 * If the concrete class has its own implementation of {@see #create(Class,
 * Class)} then the class has to extend some other class that implements the
 * interface {@see Factory}, or itself should implement the interface {@see
 * Factory}
 * 
 * @author Peter Verhas
 * @date June 8, 2012
 */
@SuppressWarnings("javadoc")
public abstract class AbstractFactory implements Factory {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FactoryManaged> void create(Class<T> interf4ce,
            Class<? extends T> klass) {
        assertInterface(interf4ce);
        assertPattern(klass);
        try {
            Constructor<? extends T> constructor = (Constructor<? extends T>) klass
                    .getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            T object;
            if (parameterTypes.length == 0) {
                object = constructor.newInstance();
            } else {
                object = constructor.newInstance(new Object[] { this });
            }
            set(interf4ce, object);
            object.setFactory(this);
        } catch (Exception e) {
            throw new BasicInterpreterInternalError("Can not instantiate "
                    + klass, e);
        }
    }

    protected static void assertPattern(Class<? extends FactoryManaged> klass) {
        Constructor<?>[] constructors = klass.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new BasicInterpreterInternalError("The class " + klass
                    + " has too many constructors");
        }
        Constructor<?> constructor = constructors[0];
        if (Modifier.isPublic(constructor.getModifiers())) {
            throw new BasicInterpreterInternalError("The class " + klass
                    + " has public constructor");
        }
    }

    protected static void assertInterface(Class<? extends FactoryManaged> klass) {
        if (!klass.isInterface()) {
            throw new BasicInterpreterInternalError("The class " + klass
                    + " is not an interface.");
        }
    }

    /**
     * Concrete implementation of this abstract method should store the object
     * {@code object} associated with the class {@code interf4ce}. Since the
     * storage of the objects is implementation dependent and different
     * implementation of the interface {@see Factory} store the objects and the
     * classes different ways this method is abstract here.
     * 
     * @param interf4ce
     * @param object
     */
    abstract <T extends FactoryManaged> void set(Class<T> interf4ce, T object);

}
