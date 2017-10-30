package com.scriptbasic.interfaces;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

import com.scriptbasic.Function;

/**
 * ScriptBasic for Java embedding interface.
 * <p>
 * This interface defines the native methods that can be used to embed
 * ScriptBasic into applications. Using the methods of this API you can load,
 * execute BASIC programs, define how the interpreter can get access to other
 * sources that are included by the script, set and get global variables and
 * call subroutines.
 * 
 * @author Peter Verhas
 * 
 */
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
	Factory getBasicFactory();

	/**
	 * Get the reader from where the BASIC program reads the standard input
	 * characters.
	 * 
	 * @return
	 */
	Reader getInput();

	/**
	 * Set the reader from where the BASIC program reads the standard input
	 * characters.
	 * 
	 * @param input
	 */
	void setInput(Reader input);

	/**
	 * Get the output writer used to write the standard output of the BASIC
	 * program.
	 * 
	 * @return
	 */
	Writer getOutput();

	/**
	 * Set the output writer used to write the standard output of the BASIC
	 * program.
	 * 
	 * @param output
	 */
	void setOutput(Writer output);

	/**
	 * Get the output writer used to write the error output of the BASIC
	 * program.
	 * 
	 * @return
	 */
	Writer getError();

	/**
	 * Set the output writer used to write the error output of the BASIC
	 * program.
	 * 
	 * @param error
	 */
	void setError(Writer error);

	/**
	 * Load a string as a BASIC program.
	 * 
	 * @param sourceCode
	 *            contains the source code as string
	 * @throws ScriptBasicException
	 */
	void load(String sourceCode) throws ScriptBasicException;

	/**
	 * Read the content of a stream provided by the reader and interpret this as
	 * a BASIC program. This method does not execute the code.
	 * 
	 * @param reader
	 *            the reader to supply the BASIC program characters.
	 * @throws ScriptBasicException
	 */
	void load(java.io.Reader reader) throws ScriptBasicException;

	/**
	 * Evaluate the content of a file. The file has to contain the BASIC
	 * program. This method does not execute the code.
	 * 
	 * @param sourceFile
	 *            the file handler pointing to the file that the interpreter
	 *            will read to get the source code.
	 * @throws ScriptBasicException
	 */
	void load(File sourceFile) throws ScriptBasicException;

	/**
	 * Read the content of the file and execute it. If there is any other script
	 * included then use the path to search for the file. This method does not
	 * execute the code.
	 * 
	 * @param sourceFileName
	 *            the file that contains the script
	 * @param path
	 *            the array of path elements that are searched for included
	 *            files
	 */
	void load(String sourceFileName, String... path)
			throws ScriptBasicException;

	/**
	 * Read the content of the file and execute it. If there is any other script
	 * included then use the path to search for the file. This method does not
	 * execute the code.
	 * 
	 * @param sourceFileName
	 *            the file that contains the script
	 * @param path
	 *            the path where included files are located
	 * @throws ScriptBasicException
	 */
	void load(String sourceFileName, SourcePath path)
			throws ScriptBasicException;

	/**
	 * Read the content of the source from the file, db... whatever named by the
	 * argument {@code sourceName} using the provider. This method does not
	 * execute the code.
	 * 
	 * @param sourceName
	 *            the name of the source file where the source is. The syntax of
	 *            the name depends on the provider.
	 * @param provider
	 *            the source provider that helps the reader to read the content
	 * @throws ScriptBasicException
	 */
	void load(String sourceName, SourceProvider provider)
			throws ScriptBasicException;

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
	void eval(String sourceFileName, String... path)
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

	/**
	 * Execute a previously loaded code.
	 * 
	 * @throws ScriptBasicException
	 */
	void execute() throws ScriptBasicException;

	/**
	 * Set the value of a global variable of the BASIC program.
	 * 
	 * @param name
	 *            of the variable as it is used in the BASIC program
	 * @param value
	 *            the value of the variable. The value is converted
	 *            automatically to be a BASIC value.
	 * @throws ScriptBasicException
	 */
	void setVariable(String name, Object value) throws ScriptBasicException;

	/**
	 * Get the value of a global variable after the BASIC program was executed.
	 * 
	 * @param name
	 *            of the variable
	 * @return the value of the variable converted to Java. Thus there is no
	 *         need to deal with ScriptBasic internal classes. If the variable
	 *         contains an integer then this method will return a {@code Long},
	 *         if it is a string then it will be a {@code String} and so on.
	 * @throws ScriptBasicException
	 */
	Object getVariable(String name) throws ScriptBasicException;

	/**
	 * Get an iterator that iterates through the names of the global variables.
	 * 
	 * @return the iterator to fetch the names of the global variables one by
	 *         one.
	 */
	Iterable<String> getVariablesIterator();

	/**
	 * Get the subroutine object of a named subroutine. This object can later be
	 * used to call the subroutine after the code was executed.
	 * 
	 * @param subroutineName
	 *            the name of the subroutine for which the object is to be
	 *            fetched.
	 * @return the subroutine object.
	 * @throws ScriptBasicException
	 */
	Subroutine getSubroutine(String subroutineName) throws ScriptBasicException;

	/**
	 * Get all the subroutine objects in an iterator.
	 * 
	 * @return an iterator that can be used to access all subroutine objects.
	 */
	Iterable<Subroutine> getSubroutines();

	/**
	 * Call the named subroutine with the arguments.
	 * <p>
	 * It is recommended to call subroutines via a
	 * {@link com.scriptbasic.interfaces.Subroutine} object.
	 * 
	 * @param subroutineName
	 *            the name of the subroutine to be called.
	 * @param args
	 *            the arguments to be passed to the subroutine. Note that there
	 *            has to be that many arguments passed as many arguments are
	 *            needed by the subroutine. If there are less number of actual
	 *            arguments the rest of the arguments will be undefined. If you
	 *            pass more actual arguments than the subroutine expects you
	 *            will get an exception.
	 * @return the value returned by the subroutine. Note that this value is
	 *         already converted to Java object and not a raw ScriptBasic
	 *         {@code RightValue} but rather a {@code Long}, {@code Double},
	 *         {@code String} or similar.
	 * @throws ScriptBasicException
	 * @deprecated
	 */
	Object call(String subroutineName, Object... args)
			throws ScriptBasicException;

	/**
	 * Get the names of all subroutines.
	 * 
	 * @return the iterator that can be used to iterate through the names of all
	 *         the subroutines defined in the program.
	 */
	Iterable<String> getSubroutineNames();

	/**
	 * Get the number of expected argument of the named subroutine.
	 * <p>
	 * It is recommended to call subroutines via a
	 * {@link com.scriptbasic.interfaces.Subroutine} object.
	 * 
	 * @param subroutineName
	 *            the name of the subroutine.
	 * @return the number of arguments the subroutine expects.
	 * @throws ScriptBasicException
	 * @deprecated
	 */
	int getNumberOfArguments(String subroutineName) throws ScriptBasicException;

	/**
	 * Register the static methods of the class as BASIC functions. After the
	 * registration the methods can be called from BASIC just as if they were
	 * built-in functions in the language or just like if they were defined as
	 * BASIC subroutines.
	 * <p>
	 * The registration process uses only the methods that are annotated as
	 * {@link Function} and only if their {@link Function#classification()}
	 * parameter is not configured in the configuration file as forbidden.
	 * <p>
	 * Even though the static methods are called via reflection they have to be
	 * callable from the BASIC interpreter. Simply saying they have to be
	 * {@code public}.
	 * 
	 * @param klass
	 *            the class to parse.
	 * @throws ScriptBasicException
	 */
	void registerExtension(Class<?> klass) throws ScriptBasicException;
}
