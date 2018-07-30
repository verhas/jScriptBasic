package com.scriptbasic.api;

/**
 * Interface to manage a BASIC subroutine. An instance of this interface can be
 * acquired calling the method {@link ScriptBasic#subroutine(String)}. It is
 * also possible to get all the subroutines calling the method {@link
 * ScriptBasic#subroutines()}.
 *
 * @author Peter Verhas
 */
public interface Subroutine<R> {
    /**
     * @return the number of arguments the subroutine expects when invoked
     */
    int numberOfArguments();

    /**
     * Get the name of the subroutine. Even though the instance of the interface
     * is usually acquired knowing the name of the subroutine this method can be
     * used to access this information.
     *
     * @return the name of the subroutine
     */
    String name();

    /**
     * Call the subroutine.
     *
     * @param args the arguments passed to the subroutine
     * @return the return value of the subroutine
     * @throws ScriptBasicException when an BASIC error happens during the execution of the
     *                              subroutine
     */
    R call(Object... args) throws ScriptBasicException;

    /**
     * Call a subroutine without any argument.
     *
     * @return the return value of the subroutine
     * @throws ScriptBasicException
     */
    R call() throws ScriptBasicException;
}
