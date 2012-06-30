/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.exceptions.GenericSyntaxException;

/**
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public final class KlassUtility {
    private KlassUtility() {
        UtilityUtility.assertUtilityClass();
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
