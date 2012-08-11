/**
 *
 */
package com.scriptbasic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * This annotation interface can be used in classes that define static java
 * methods to be called from BASIC code.
 * <p>
 * Each {@code public static} method that is supposed to be reachable from BASIC
 * should have the annotation {@code @Function}. The implementation of the
 * interface method {@see Interpreter#registerFunctions(Class)} registers all
 * annotated methods of the class specified as argument.
 * 
 * @author Peter Verhas date July 22, 2012
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Function {
    /**
     * The alias name of the function to be used in the BASIC program. If this
     * is not defined then the name of the method will be used.
     */
    String alias() default "";

    /**
     * The interface version of ScriptBasic for Java required for this method to
     * properly function.
     * <p>
     * The different versions of ScriptBasic may provide different
     * functionalities for the methods registered as functions. When a method
     * needs a specific interface version the interpreter may alter its
     * behavior.
     * <p>
     * If a method requires an interface version that is higher than the version
     * of the executing interpreter then the interpreter can not work together
     * with the extension and thus will signal error.
     * <p>
     * If a method requires a version that is smaller that the actual version of
     * the executing interpreter then the interpreter may decide if it can work
     * with the extension. There may be three different cases foreseen:
     * <ol>
     * <li>The interpreter can work seamless with an extension that was designed
     * for an earlier version.
     * <li>The interpreter can not support the outdated extension.
     * <li>The interpreter mimics the interface of an older version of it
     * provided towards the extension.
     * </ol>
     * <p>
     * The version specified in this annotation is the version of the
     * 'interface' the interpreter provides for the extensions. This 'interface'
     * is not literally a Java interface, rather the collection of all the
     * interfaces and behaviors that the interpreter exhibits towards the
     * extensions.
     * <p>
     * If different versions of ScriptBasic for Java share the same behavior
     * regarding the extensions and thus the two versions are 100% compatible
     * regarding extension support and usage then the two different versions of
     * the interpreter will have the same extension interface version. Therefore
     * you should expect this interface version to change rather infrequent.
     * <p>
     * A later version of SB4J should not have a smaller interface version.
     * <p>
     * Note that the time I write this documentation the interpreter is in
     * infancy and has a pre-version of 1.
     */
    long requiredVersion() default 1;

    /**
     * The different methods can be classified and the configuration of the
     * actual interpreter registers or deny the registration of certain methods
     * base don the classification.
     */
    Class<?>[] classification() default Function.class;

    /**
     * It may happen that the you want to register methods that are not in your
     * hands. In such a case the method can not be instrumented using
     * annotations. Using this annotation parameter you can specify an
     * alternative class where the method you want to register instead of the
     * annotated one is.
     */
    Class<?> substituteClass() default Function.class;

    /**
     * Using this parameter you can specify an alternative method instead of the
     * annotated one. See the documentation of {@see #substituteClass()}.
     */
    String substituteMethod() default "";
}
