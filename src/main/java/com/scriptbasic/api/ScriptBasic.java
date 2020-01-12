package com.scriptbasic.api;

import com.scriptbasic.Engine;
import com.scriptbasic.readers.SourcePath;
import com.scriptbasic.readers.SourceProvider;
import com.scriptbasic.spi.InterpreterHook;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

/**
 * ScriptBasic for Java embedding interface.
 * <p>
 * This interface defines the native methods that can be used to embed
 * ScriptBasic into applications. Using the methods of this API you can load,
 * execute BASIC programs, define how the interpreter can get access to other
 * sources that are included by the script, set and get global variables and
 * call subroutines.
 * <p>
 * Deprecated methods are replaced by fluent api methods that return "this". The deprecated
 * method are not for removal. They are not to be used by callers but they are to be implemented
 * by implementing classes. These methods are invoked from the fluent API method default implementations
 * in this interface. That way the builder pattern like fluent api does not need to be implemented by
 * each class that implements the interface.
 *
 * @author Peter Verhas
 */
public interface ScriptBasic {

    Class<FileHandlingFunctions> fileHandlingFunctionsClass = FileHandlingFunctions.class;

    static ScriptBasic engine() {
        return new Engine();
    }

    /**
     * @param alias         the name of the method as it can be used in the BASIC program
     * @param klass         the class that contains the method
     * @param methodName    the name of the method
     * @param argumentTypes the types of the arguments
     * @throws ScriptBasicException when a function is double defined and not an identical manner
     */
    void registerFunction(final String alias,
                          final Class<?> klass,
                          final String methodName,
                          final Class<?>... argumentTypes) throws ScriptBasicException;

    /**
     * Use this method to register a Java function so that it can be called from the BASIC program.
     * For example:
     * <pre>
     *     engine.function("myMethod")
     *           .alias("BASICNAME")
     *           .klass(WhereItIsImplemented.class)
     *           .arguments(String.class);
     * </pre>
     * <p>
     * In the fluent API this method call should be followed/chained by calls to {@link FunctionBuilder#klass(Class)},
     * optionally by {@link FunctionBuilder.FunctionBuilder2#alias(String)} and finally the chain must be closed with
     * {@link FunctionBuilder.FunctionBuilder2#arguments(Class[])}. The method {@code #function(String)} as well as
     * {@link FunctionBuilder#klass(Class)},and {@link FunctionBuilder.FunctionBuilder2#alias(String)} return a
     * {@link FunctionBuilder} instance used only for the method chaining and the last call in the chain
     * {@link FunctionBuilder.FunctionBuilder2#arguments(Class[])} returns to the top level {@link ScriptBasic} object.
     *
     * @param methodName the name of the method in the Java class. If the method {@link FunctionBuilder.FunctionBuilder2#alias(String)}
     *                   is not invoked in this chain then the name specified here as argument will also be used
     *                   as the name to be used in th BASIC program.
     * @return a {@link FunctionBuilder} to use for further method chaining.
     */
    default FunctionBuilder function(final String methodName) {
        return new FunctionBuilder(methodName, this);
    }

    /**
     * Get the reader from where the BASIC program reads the standard input
     * characters.
     *
     * @return return value
     */
    Reader getInput();

    /**
     * Set the reader from where the BASIC program reads the standard input
     * characters.
     *
     * @param input the input to set
     */
    void setInput(Reader input);

    default ScriptBasic input(final Reader input) {
        setInput(input);
        return this;
    }

    /**
     * Get the output writer used to write the standard output of the BASIC
     * program.
     *
     * @return the output
     */
    Writer getOutput();

    /**
     * Set the output writer used to write the standard output of the BASIC
     * program.
     *
     * @param output parameter
     */
    void setOutput(Writer output);

    default ScriptBasic output(final Writer output) {
        setOutput(output);
        return this;
    }

    /**
     * Get the output writer used to write the error output of the BASIC
     * program.
     *
     * @return the error output writer
     */
    Writer getErrorOutput();

    /**
     * Set the output writer used to write the error output of the BASIC
     * program.
     *
     * @param error the error output
     */
    void setErrorOutput(Writer error);

    default ScriptBasic error(final Writer error) {
        setErrorOutput(error);
        return this;
    }

    /**
     * Load a string as a BASIC program.
     *
     * @param sourceCode contains the source code as string
     * @return this
     * @throws ScriptBasicException when the code cannot be loaded
     */
    ScriptBasic load(String sourceCode) throws ScriptBasicException;

    /**
     * Read the content of a stream provided by the reader and interpret this as
     * a BASIC program. This method does not execute the code.
     *
     * @param reader the reader to supply the BASIC program characters.
     * @return this
     * @throws ScriptBasicException when the code cannot be loaded
     */
    ScriptBasic load(Reader reader) throws ScriptBasicException;

    /**
     * Evaluate the content of a file. The file has to contain the BASIC
     * program. This method does not execute the code.
     *
     * @param sourceFile the file handler pointing to the file that the interpreter
     *                   will read to get the source code.
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic load(File sourceFile) throws ScriptBasicException;

    /**
     * Read the content of the file and execute it. If there is any other script
     * included then use the path to search for the file. This method does not
     * execute the code.
     *
     * @param sourceFileName the file that contains the script
     * @param path           the array of path elements that are searched for included
     *                       files
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic load(String sourceFileName, String... path) throws ScriptBasicException;

    /**
     * Read the content of the file and execute it. If there is any other script
     * included then use the path to search for the file. This method does not
     * execute the code.
     *
     * @param sourceFileName the file that contains the script
     * @param path           the path where included files are located
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic load(String sourceFileName, SourcePath path)
            throws ScriptBasicException;

    /**
     * Read the content of the source from the file, db... whatever named by the
     * argument {@code sourceName} using the provider. This method does not
     * execute the code.
     *
     * @param sourceName the name of the source file where the source is. The syntax of
     *                   the name depends on the provider.
     * @param provider   the source provider that helps the reader to read the content
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic load(String sourceName, SourceProvider provider)
            throws ScriptBasicException;

    /**
     * Evaluate a string as a BASIC program.
     *
     * @param sourceCode contains the source code as string
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(String sourceCode) throws ScriptBasicException;

    /**
     * Read the content of a stream provided by the reader and interpret this as
     * a BASIC program.
     *
     * @param reader the reader to supply the BASIC program characters.
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(Reader reader) throws ScriptBasicException;

    /**
     * Evaluate the content of a file. The file has to contain the BASIC
     * program.
     *
     * @param sourceFile the file handler pointing to the file that the interpreter
     *                   will read to get the source code.
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(File sourceFile) throws ScriptBasicException;

    /**
     * Read the content of the file and execute it. If there is any other script
     * included then use the path to search for the file.
     *
     * @param sourceFileName the file that contains the script
     * @param path           the array of path elements that are searched for included
     *                       files
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(String sourceFileName, String... path)
            throws ScriptBasicException;

    /**
     * Read the content of the file and execute it. If there is any other script
     * included then use the path to search for the file.
     *
     * @param sourceFileName the file that contains the script
     * @param path           the path where included files are located
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(String sourceFileName, SourcePath path)
            throws ScriptBasicException;

    /**
     * Read the content of the source from the file, db... whatever named by the
     * argument {@code sourceName} using the provider.
     *
     * @param sourceName the name of the source file where the source is. The syntax of
     *                   the name depends on the provider.
     * @param provider   the source provider that helps the reader to read the content
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic eval(String sourceName, SourceProvider provider)
            throws ScriptBasicException;

    /**
     * Execute a previously loaded code.
     *
     * @return this
     * @throws ScriptBasicException in case of exception
     */
    ScriptBasic execute() throws ScriptBasicException;

    /**
     * Set the value of a global variable of the BASIC program.
     *
     * @param name  of the variable as it is used in the BASIC program
     * @param value the value of the variable. The value is converted
     *              automatically to be a BASIC value.
     * @throws ScriptBasicException in case of exception
     * @deprecated use {@link #variable(String)}, not for removal.
     */
    @Deprecated()
    void setVariable(String name, Object value) throws ScriptBasicException;

    /**
     * define a
     *
     * @param name parameter
     * @return return value
     */
    default VariableBuilder variable(final String name) {
        return new VariableBuilder(name, this);
    }

    /**
     * Get the value of a global variable after the BASIC program was executed.
     *
     * @param name of the variable
     * @return the value of the variable converted to Java. Thus there is no
     * need to deal with ScriptBasic internal classes. If the variable
     * contains an integer then this method will return a {@code Long},
     * if it is a string then it will be a {@code String} and so on.
     * @throws ScriptBasicException in case of exception
     * @deprecated use the version that has a second argument specifying the required type.
     * Migration can be done in two steps: 1.) add an {@code Object.class} second argument and keep
     * the cast, 2.) use it properly.
     */
    @Deprecated(forRemoval = true)
    Object getVariable(String name) throws ScriptBasicException;

    /**
     * Get the value of a global variable after the BASIC program was executed.
     *
     * @param <T>  the type the caller expects the variable to be. Type conversion
     *             is done by the method and in case the value can not be cast to the
     *             type then the ClassCastException will be embedded into a
     *             ScriptBasicException. This method will not magically convert between
     *             Java types, even though it will convert from BASIC types to Java types
     *             (see below). This parameter is used only to cast the result.
     * @param type is the type class
     * @param name of the variable
     * @return the value of the variable converted to Java. Thus there is no
     * need to deal with ScriptBasic internal classes. If the variable
     * contains an integer then this method will return a {@code Long},
     * if it is a string then it will be a {@code String} and so on.
     * @throws ScriptBasicException when the variable cannot be retrieved, or has
     *                              a type that can not be converted to {@code T}.
     */
    <T> T variable(Class<T> type, String name) throws ScriptBasicException;


    /**
     * Same as {@link #variables()}.
     *
     * @return same as
     * @throws ScriptBasicException in case of exception
     */
    @Deprecated(forRemoval = true)
    Iterable<String> getVariablesIterator() throws ScriptBasicException;

    /**
     * Get an iterator that iterates through the names of the global variables.
     *
     * @return the iterator to fetch the names of the global variables one by
     * one.
     * @throws ScriptBasicException in case of exception
     */
    Iterable<String> variables() throws ScriptBasicException;

    /**
     * Get the subroutine object of a named subroutine. This object can later be
     * used to call the subroutine after the code was executed.
     *
     * @param type is the expected return value of the subroutine. If the subroutine does not return any value then
     *             specify {@code Void.class}. If it may return just any value, or may not return any value and the
     *             caller does not care then specify {@code Object.class} or just {@code null}. Note that this
     *             is an error if a subroutine returns a value when invoking {@link Subroutine#call()} but the subroutine
     *             object was created with {@code Void.class} argument.
     * @param name the name of the subroutine for which the object is to be
     *             fetched.
     * @param <T>  the type
     * @return the subroutine object.
     * @throws ScriptBasicException in case of exception
     */
    <T> Subroutine<T> subroutine(Class<T> type, String name) throws ScriptBasicException;

    /**
     * Convenience method with the same result as calling {@link #subroutine(Class, String)} with
     * {@code Object.class} as first argument.
     *
     * @param name the name of the subroutine
     * @param <T>  defaults to {@code Object.class}
     * @return the subroutine object
     * @throws ScriptBasicException in case of exception
     */
    <T> Subroutine<T> subroutine(String name) throws ScriptBasicException;

    /**
     * Get all the subroutine objects in an iterator.
     *
     * @return an iterator that can be used to access all subroutine objects.
     * @throws ScriptBasicException in case of exception
     */
    Iterable<Subroutine> subroutines() throws ScriptBasicException;

    /**
     * Register the static methods of the class as BASIC functions. After the
     * registration, the methods can be called from BASIC just as if they were
     * built-in functions in the language or just like if they were defined as
     * BASIC subroutines.
     * <p>
     * The registration process uses only the methods that are annotated as
     * {@link BasicFunction} and only if their {@link BasicFunction#classification()}
     * parameter is not configured in the configuration as forbidden.
     * <p>
     * Even though the static methods are called via reflection they have to be
     * callable from the BASIC interpreter. Simply saying they have to be
     * {@code public} and their packages exported to the BASIC interpreter.
     *
     * @param klass the class to parse.
     * @return this
     */
    ScriptBasic registerExtension(Class<?> klass);

    /**
     * Register an interpreter hook class.
     *
     * @param hook the hook instance to register
     * @return this
     */
    ScriptBasic registerHook(InterpreterHook hook);

    /**
     * @return the configuration object of the BASIC interpreter.
     */
    Configuration getConfiguration();

    class VariableBuilder {
        final private String name;
        final private ScriptBasic scriptBasic;

        private VariableBuilder(final String name, final ScriptBasic scriptBasic) {
            this.name = name;
            this.scriptBasic = scriptBasic;
        }

        public ScriptBasic is(final Object value) throws ScriptBasicException {
            scriptBasic.setVariable(name, value);
            return scriptBasic;
        }
    }

    class FunctionBuilder {

        private final String methodName;
        private final ScriptBasic scriptBasic;
        private String alias;

        private FunctionBuilder(final String methodName, final ScriptBasic scriptBasic) {
            this.methodName = methodName;
            this.alias = methodName;
            this.scriptBasic = scriptBasic;
        }


        public FunctionBuilder2 klass(final Class<?> klass) {
            return new FunctionBuilder2(klass);
        }

        public class FunctionBuilder2 {
            private final Class<?> klass;

            public FunctionBuilder2(final Class<?> klass) {
                this.klass = klass;
            }

            public FunctionBuilder2 alias(final String a) {
                alias = a;
                return this;
            }

            public ScriptBasic arguments(final Class<?>... argumentTypes) throws ScriptBasicException {
                scriptBasic.registerFunction(alias, klass, methodName, argumentTypes);
                return scriptBasic;
            }
        }
    }

}
