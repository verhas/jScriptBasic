package com.scriptbasic.interfaces;

/**
 * Interface to manage a BASIC subroutine. An instance of this interface can be
 * acquired calling the method {@link EngineApi#getSubroutine(String)}. It is
 * also possible to get all the subroutines calling the method {@link
 * EngineApi#getSubroutines()}.
 * 
 * @author Peter Verhas
 * 
 */
public interface Subroutine {
	/**
	 * 
	 * @return the number of arguments the subroutine expects when invoked
	 */
	int getNumberOfArguments();

	/**
	 * Get the name of the subroutine. Even though the instance of the interface
	 * is usually acquired knowing the name of the subroutine this method can be
	 * used to access this information.
	 * 
	 * @return the name of the subroutine
	 */
	String getName();

	/**
	 * Call the subroutine.
	 * 
	 * @param args
	 *            the arguments passed to the subroutine
	 * @return the return value of the subroutine
	 * @throws ScriptBasicException
	 *             when an BASIC error happens during the execution of the
	 *             subroutine
	 */
	Object call(Object... args) throws ScriptBasicException;

	/**
	 * Call a subroutine without any argument.
	 * 
	 * @return the return value of the subroutine
	 * @throws ScriptBasicException
	 */
	Object call() throws ScriptBasicException;
}
