/**
 * 
 */
package com.scriptbasic.utility;

import java.lang.reflect.Field;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;

/**
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public final class KlassUtility {
    private KlassUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    /**
     * @param object
     * @param fieldName
     * @param rightValue
     * @throws BasicRuntimeException
     */
    public static void setField(Object object, String fieldName,
            Object valueObject) throws BasicRuntimeException {
        Class<?> klass = object.getClass();
        try {
            Field field = klass.getField(fieldName);
            Class<?> fieldClass = field.getType();
            Object typeConvertedValueObject = CastUtility.cast(valueObject,
                    fieldClass);
            field.set(object, typeConvertedValueObject);
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Object access of type "
                    + object.getClass() + " can not set field '" + fieldName
                    + "'", e);
        }
    }

    /**
     * Get the value of a field of an object and return it.
     * 
     * TODO implement the following algorithm: 1. If there is a getter for the
     * field, use that 2. If there is no getter starting with name 'get' but the
     * field is declared as boolean or Boolean then use the getter that starts
     * with 'is' 3. Use the field.
     * <p>
     * Current implementation uses the field. TODO implement access as a last
     * resort calling the method INVENT_NAME passing the field name as argument
     * if the class of the object implements the interface
     * INVENT_INTERFACE_NAME. The names should include the word BASIC, or better
     * SCRIPTBASIC. Perhaps ScriptBasicMagicBean or something.
     * 
     * @param object
     * @param fieldName
     * @return
     * @throws ExecutionException
     */
    public static Object getField(Object object, String fieldName)
            throws ExecutionException {
        Class<?> klass = object.getClass();
        Object result = null;
        try {
            Field field = klass.getField(fieldName);
            result = field.get(object);
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Object access of type "
                    + object.getClass() + " can not access field '" + fieldName
                    + "'", e);
        }
        return result;
    }

    /**
     * calculate the getter name of the field. This is getXxx for the field xxx.
     * 
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unused")
    private static String getterName(String fieldName) {
        final String prefix = "get";
        StringBuilder sb = new StringBuilder(fieldName.length()
                + prefix.length());
        sb.append(prefix);
        sb.append(fieldName);
        sb.setCharAt(prefix.length(),
                Character.toUpperCase(sb.charAt(prefix.length())));
        return sb.toString();
    }

    /**
     * Returns a class based on its name just like the method {@see
     * java.lang.Class#forName(String)}. The search for the class is extended.
     * For example if this method is called
     * 
     * <pre>
     * Class&lt;?&gt; klass = KlassUtility.forName(&quot;a.b.c.d&quot;);
     * </pre>
     * 
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
     * @param s
     *            the name of the class to be loaded
     * @return the class finally loaded
     * @throws ClassNotFoundException
     *             the exception thrown by the class loader the first time if
     *             the algorithm can not find any class to load.
     */
    public static Class<?> forName(final String s)
            throws ClassNotFoundException {
        StringBuilder className = new StringBuilder(s);
        Class<?> klass = null;
        ClassNotFoundException firstCatchedException = null;
        while (klass == null) {
            try {
                klass = Class.forName(className.toString());
            } catch (ClassNotFoundException ex) {
                klass = null;
                if (firstCatchedException == null) {
                    firstCatchedException = ex;
                }
                int lastDotPosition = className.lastIndexOf(".");
                if (lastDotPosition == -1) {
                    break;
                }
                className.setCharAt(lastDotPosition, '$');
            }
        }
        if (klass == null) {
            throw firstCatchedException;
        }
        return klass;
    }

    /**
     * Returns a class based on its name just like the method {@see
     * java.lang.Class#forName(String)}. If the name of the class is
     * 
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
     * 
     * then the method will return the primitive class named. Otherwise it calls
     * {@see #forName(String)} to load the class.
     * 
     * @param s
     * @return
     * @throws GenericSyntaxException
     */
    public static Class<?> forNameEx(final String s)
            throws GenericSyntaxException {
        Class<?> klass = null;
        switch (s) {
        case "byte":
            klass = byte.class;
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
            } catch (ClassNotFoundException e) {
                throw new GenericSyntaxException("Can not get class " + s, e);
            }
        }
        return klass;
    }

}
