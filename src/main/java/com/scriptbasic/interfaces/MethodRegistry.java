package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;

import java.lang.reflect.Method;

/**
 * <p>
 * Keep a registry of methods. The Java methods (either class or object method)
 * can be called from interpreted script. The problem is that Java methods can
 * be overloaded and BASIC functions can not. To overcome this issue the BASIC
 * program has to declare a function name for each method it wants to call. For
 * example:
 * </p>
 * <pre>
 * method sin from java.lang.Math is (double) use as sinus
 * </pre>
 * <p>
 * Here the method {@code sin} is to be used from the java package
 * {@code java.lang.Math} and the alias is {@code sinus}. The
 * {@code use as sinus} part is optional. If this is missing then the name of
 * the method will be used as the name of the BASIC function and no alias is
 * used. If this part is defined then the alias will be used as the name of the
 * function.
 * </p>
 * <p>
 * If there are two methods of the same name, with different signature, you have
 * to define different aliases for the different methods.
 * </p>
 * <p>
 * You can use the same alias for different methods in different packages. This
 * can be done because the class name or alias for the class name or the
 * variable holding the Java object reference stands in front of the method
 * name/alias when the registered method is called. This distinguishes the
 * different methods of different class even if the aliases or the method names
 * are the same.
 * </p>
 * <p>
 * Also note that you can also use aliases for Class names defined in the
 * statement {@code use}.
 * </p>
 * The registry keeps track of the
 * <ul>
 * <li>name of the method,  
 * <li>the alias,
 * <li>the class and
 * <li>the signature.
 * </ul>
 * <p>
 * Thus whenever there is a function call in BASIC to call a Java method in a
 * certain class this registry will know which signature to use for the certain
 * alias.
 * </p>
 * Aliases can be global or class specific. When an alias is defined for a
 * single method then the alias can be used without the class specification.
 * This appears in the BASIC code as a simple function call. When a method is
 * registered in the MethodRegistry it is registered in the normal registry as
 * well as in the global registry. When registering a method in the global
 * registry the implementation checks if there is already a method registered
 * with that alias. If there is then the alias is not global, can not be used
 * without an object or class reference and it is removed from the global
 * registry.
 *
 * @author Peter Verhas date June 28, 2012
 */
public interface MethodRegistry {

    /**
     * Get the method for a given alias used to name a method in a specific Java
     * class.
     *
     * @param klass the class where the method is. If this parameter is
     *              {@code null} then the class will be fetched from the global
     *              registry of methods.
     * @param alias the alias with which the method was registered for a specific
     *              signature.
     * @return the located method that match the name and the signature what was
     * registered.
     * @throws ScriptBasicException if the method can not be accessed, e.g. there is no such
     *                            method or accessing the method violates java security (e.g.
     *                            the method is private)
     */
    Method getJavaMethod(Class<?> klass, String alias)
            throws ScriptBasicException;

    /**
     * Register a java method.
     *
     * @param alias         the alias of the method. This is the name how the BASIC
     *                      program will refer to the method. Although the BASIC source
     *                      code makes it optional to provide an alias it is not optional
     *                      here. It may be same as the string contained in the parameter
     *                      {@code methodName} but it should be passed here and should not
     *                      be {@code null}
     * @param klass         the class in which the method is implemented.
     * @param methodName    the Java name of the method.
     * @param argumentTypes the argument classes that form the signature of the method
     *                      together with the name and class of the method.
     * @throws BasicRuntimeException is thrown if the registration of the alias is not unique in
     *                               the actual interpreter. You can not register a name for a
     *                               method and then later the same alias for a different method.
     */
    void registerJavaMethod(String alias, Class<?> klass,
                            String methodName, Class<?>[] argumentTypes)
            throws BasicRuntimeException;

}
