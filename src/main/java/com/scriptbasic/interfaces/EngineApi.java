package com.scriptbasic.interfaces;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

public interface EngineApi {

	/**
	 * Get the factory used to create the different parts of the execution
	 * environment for this engine. The factory can be requested to return the
	 * reader, lexical analyzer and other objects that are used internally to
	 * manage the execution of the program. To use the ScriptBasic Engine there
	 * is no need for this method This method provides a way to access the
	 * objects that are below this facade and is available for advanced
	 * programmers who are not satisfied with the features provided with this
	 * facade.
	 * <p>
	 * If ever you need to use this method, do use it at your own risk, and
	 * inform the developers of your need so that they may consider extending
	 * this facade to accommodate your needs.
	 * 
	 * @return the factory instance
	 */
	public Factory getBasicFactory();

	/**
	 * Get the reader from where the BASIC program reads the standard input
	 * characters.
	 * 
	 * @return
	 */
	public Reader getInput();

	/**
	 * Set the reader from where the BASIC program reads the standard input
	 * characters.
	 * 
	 * @param input
	 */
	public void setInput(Reader input);

	/**
	 * Get the output writer used to write the standard output of the BASIC
	 * program.
	 * 
	 * @return
	 */
	public Writer getOutput();

	/**
	 * Set the output writer used to write the standard output of the BASIC
	 * program.
	 * 
	 * @param output
	 */
	public void setOutput(Writer output);

	/**
	 * Get the output writer used to write the error output of the BASIC
	 * program.
	 * 
	 * @return
	 */
	public Writer getError();

	/**
	 * Set the output writer used to write the error output of the BASIC
	 * program.
	 * 
	 * @param output
	 */
	public void setError(Writer error);

	/**
	 * Evaluate a string as a BASIC program.
	 * 
	 * @param sourceCode
	 *            contains the source code as string
	 * @throws ScriptBasicException
	 */
	void eval(String sourceCode) throws ScriptBasicException;

	/**
	 * Read the content of a stream provided by the reader and interpret this as
	 * a BASIC program.
	 * 
	 * @param reader
	 *            the reader to supply the BASIC program characters.
	 * @throws ScriptBasicException
	 */
	void eval(java.io.Reader reader) throws ScriptBasicException;

	/**
	 * Evaluate the content of a file. The file has to contain the BASIC
	 * program.
	 * 
	 * @param sourceFile
	 *            the file handler pointing to the file that the interpreter
	 *            will read to get the source code.
	 * @throws ScriptBasicException
	 */
	void eval(File sourceFile) throws ScriptBasicException;

	/**
	 * Read the content of the file and execute it. If there is any other script
	 * included then use the path to search for the file.
	 * 
	 * @param sourceFileName
	 *            the file that contains the script
	 * @param path
	 *            the array of path elements that are searched for included
	 *            files
	 */
	public void eval(String sourceFileName, String... path)
			throws ScriptBasicException;

	/**
	 * Read the content of the file and execute it. If there is any other script
	 * included then use the path to search for the file.
	 * 
	 * @param sourceFileName
	 *            the file that contains the script
	 * @param path
	 *            the path where included files are located
	 * @throws ScriptBasicException
	 */
	void eval(String sourceFileName, SourcePath path)
			throws ScriptBasicException;

	/**
	 * Read the content of the source from the file, db... whatever named by the
	 * argument {@code sourceName} using the provider.
	 * 
	 * @param sourceName
	 *            the name of the source file where the source is. The syntax of
	 *            the name depends on the provider.
	 * @param provider
	 *            the source provider that helps the reader to read the content
	 * @throws ScriptBasicException
	 */
	void eval(String sourceName, SourceProvider provider)
			throws ScriptBasicException;

	void setVariable(String name, Object value) throws ScriptBasicException;

	Object getVariable(String name) throws ScriptBasicException;

	Iterable<String> getVariablesIterator();
	
	Object call(String subroutineName, Object ... args) throws ScriptBasicException;
	
	// TODO get the names of the subroutines
	// TODO get the number of arguments for a subroutine 
}
