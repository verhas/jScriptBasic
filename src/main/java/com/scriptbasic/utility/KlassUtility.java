package com.scriptbasic.utility;

import com.scriptbasic.api.BasicRuntimeException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.Magic;
import com.scriptbasic.interfaces.NoAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Peter Verhas date June 28, 2012
 */
public final class KlassUtility {
    final static private String SETTER_PREFIX = "set";
    final static private int SETTER_PREFIX_LEN = SETTER_PREFIX.length();

    private KlassUtility() {
        NoInstance.isPossible();
    }

    /**
     * Set the field of a Java object. This method is called when a BASIC code has access to an object and
     * assignes value to some of the fields of the Java object.
     * <p>
     * TOPO: implement ScriptBasic magic bean setting mechanism as a first resort and if that does not work then
     * setter and if there is no setter then direct field access.
     *
     * @param object
     * @param fieldName
     * @param valueObject
     * @throws BasicRuntimeException
     */
    public static void setField(final Object object, final String fieldName,
                                final Object valueObject) throws BasicRuntimeException {
        if (object != null && object instanceof NoAccess) {
            throw new BasicRuntimeException("The filed '" +
                    fieldName +
                    "' is not allowed to be write in object of the type '" +
                    object.getClass().getName());
        }
        final Class<?> klass = object.getClass();
        if (Magic.Setter.class.isAssignableFrom(klass)) {
            final Magic.Setter magicBean = (Magic.Setter) object;
            magicBean.set(fieldName, valueObject);
        } else {
            try {
                final Field field = getField(klass, fieldName);
                final Method setter = getSetter(field);
                if (setter != null) {
                    setter.invoke(object, valueObject);
                } else {
                    final Class<?> fieldClass = field.getType();
                    final Object typeConvertedValueObject = CastUtility.cast(valueObject, fieldClass);
                    field.set(object, typeConvertedValueObject);
                }
            } catch (final Exception e) {
                throw new BasicRuntimeException("Object access of type "
                        + object.getClass() + " can not set field '" + fieldName
                        + "'", e);
            }
        }
    }

    /**
     * Get the value of a field of an object and return it.
     * <p>
     * TODO implement the following algorithm:
     * ScriptBasic magic bean access as a first resort when the object's class implements scriptbasic magic bean if.
     * <p>
     * 2. If there is a getter for the field, use that
     * <p>
     * 3. If there is no getter starting with name 'get' but the
     * field is declared as boolean or Boolean then use the getter that starts
     * with 'is'
     * <p>
     * 4. Use the field.
     * <p>
     * Current implementation uses the field.
     * <p>
     * TODO implement ScriptBasic magic bean functionality
     * access as a last resort calling the method INVENT_NAME passing the field name as argument
     * if the class of the object implements the interface
     * INVENT_INTERFACE_NAME. The names should include the word BASIC, or better
     * SCRIPTBASIC. Perhaps ScriptBasicMagicBean or something.
     *
     * @param object
     * @param fieldName
     * @return
     * @throws BasicRuntimeException
     */
    public static Object getField(final Object object, final String fieldName)
            throws BasicRuntimeException {
        if (object != null && object instanceof NoAccess) {
            throw new BasicRuntimeException("The filed '" +
                    fieldName +
                    "' is not allowed to be read in object of the type '" +
                    object.getClass().getName());
        }
        final Class<?> klass = object.getClass();
        if (Magic.Getter.class.isAssignableFrom(klass)) {
            final Magic.Getter magicBean = (Magic.Getter) object;
            return magicBean.get(fieldName);
        } else {
            final Object result;
            try {
                final Field field = getField(klass, fieldName);
                final Method getter = getGetter(field);
                if (getter != null) {
                    result = getter.invoke(object);
                } else {
                    result = field.get(object);
                }
            } catch (final Exception e) {
                throw new BasicRuntimeException("Object access of type "
                        + object.getClass() + " can not access field '" + fieldName
                        + "'", e);
            }
            return result;
        }
    }

    /**
     * Returns a class based on its name just like the method
     * {@link java.lang.Class#forName(String)}. The search for the class is
     * extended. For example if this method is called
     * <p>
     * <pre>
     * Class&lt;?&gt; klass = KlassUtility.forName(&quot;a.b.c.d&quot;);
     * </pre>
     * <p>
     * the method tries to locate he class {@code a.b.c.d}. If it can not locate
     * that it will try to locate {@code a.b.c$d} because it may happen that
     * {@code a.b.c} is not a package but rather a class itself and the class to
     * be located is an inner class. This continues until the class is located
     * or until the last dot (starting from the end of the class name string) is
     * replaced by the character '{@code $}'. In other words the method will try
     * to locate the classes {@code a.b$c$d} and {@code a$b$c$d} until it finds
     * one.
     * <p>
     * If this algorithm can not load any class then the exception caught the
     * first time is thrown.
     *
     * @param s the name of the class to be loaded
     * @return the class finally loaded
     * @throws ClassNotFoundException the exception thrown by the class loader the first time if
     *                                the algorithm can not find any class to load.
     */
    public static Class<?> forName(final String s)
            throws ClassNotFoundException {
        final StringBuilder className = new StringBuilder(s);
        Class<?> klass = null;
        ClassNotFoundException firstCaughtException = null;
        while (klass == null) {
            try {
                klass = Class.forName(className.toString());
            } catch (final ClassNotFoundException ex) {
                if (firstCaughtException == null) {
                    firstCaughtException = ex;
                }
                final int lastDotPosition = className.lastIndexOf(".");
                if (lastDotPosition == -1) {
                    throw firstCaughtException;
                }
                className.setCharAt(lastDotPosition, '$');
            }
        }
        return klass;
    }

    /**
     * Returns a class based on its name just like the method
     * {@link java.lang.Class#forName(String)}. If the name of the class is
     * <p>
     * <pre>
     * byte
     * short
     * char
     * double
     * float
     * long
     * int
     * boolean
     * </pre>
     * <p>
     * then the method will return the primitive class named. Otherwise it calls
     * {@link #forName(String)} to load the class.
     *
     * @param s
     * @return
     * @throws BasicSyntaxException
     */
    public static Class<?> forNameEx(final String s)
            throws BasicSyntaxException {
        Class<?> klass = null;
        switch (s) {
            case "byte":
                klass = byte.class;
                break;
            case "short":
                klass = short.class;
                break;
            case "char":
                klass = char.class;
                break;
            case "double":
                klass = double.class;
                break;
            case "float":
                klass = float.class;
                break;
            case "long":
                klass = long.class;
                break;
            case "int":
                klass = int.class;
                break;
            case "boolean":
                klass = boolean.class;
                break;
            default:
                try {
                    klass = forName(s);
                } catch (final ClassNotFoundException e) {
                    throw new BasicSyntaxException("Can not get class " + s, e);
                }
                break;
        }
        return klass;
    }

    private static Method getSetter(final Field field) {
        final String fieldName = field.getName();
        final StringBuilder sb = new StringBuilder(SETTER_PREFIX).append(fieldName);
        sb.setCharAt(SETTER_PREFIX_LEN, Character.toUpperCase(sb.charAt(SETTER_PREFIX_LEN)));
        try {
            return field.getDeclaringClass().getMethod(sb.toString(), field.getType());
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    private static Method getGetter(final Field field) {
        final Method getGetter = getterPrefixed(field, "get");
        final Method isGetter;
        if (Boolean.class.isAssignableFrom(field.getType()) ||
                boolean.class.isAssignableFrom(field.getType())) {
            isGetter = getterPrefixed(field, "is");
        } else {
            isGetter = null;
        }
        if (isGetter != null) {
            return isGetter;
        }
        if (getGetter != null) {
            return getGetter;
        }
        return null;
    }

    private static Method getterPrefixed(final Field field, final String prefix) {
        final String fieldName = field.getName();
        final int prefixLength = prefix.length();
        final StringBuilder sb = new StringBuilder(prefix).append(fieldName);
        sb.setCharAt(prefixLength, Character.toUpperCase(sb.charAt(prefixLength)));
        try {
            return field.getDeclaringClass().getMethod(sb.toString());
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    private static Field getField(final Class<?> klass, final String fieldName) throws NoSuchFieldException {
        try {
            return klass.getField(fieldName);
        } catch (final NoSuchFieldException e) {
            return klass.getDeclaredField(fieldName);
        }
    }
}
