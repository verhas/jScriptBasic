## How to embed ScriptBasic for Java using the JSR223 standard interface

The JSR223 interface defines a few 
[interfaces and classes](http://java.sun.com/developer/technicalArticles/J2SE/Desktop/scripting/)
that are implemented by ScriptBasic for Java. If you
want to embed ScriptBasic for Java into your application in a standard way 
you can do that using these interfaces and classes that are defined in the Java runtime.

The descriptions in this document are very general and most of the statements are true unaltered if we talk about
some other scripting language.

To use anz scripting language you have to have the implementation of the interpreter.
In case of ScriptBasic for Java it is the `jscriptbasic-x.y.z.jar` file. 
Since the interfaces defined in JSR223 are included in the standard JDK the interpreter implementation is not a 
compilation time dependency. You can compile any program that wants to use some JSR223 compliant interpreter without
the actual implementation of it. When you run your code, however, the jar file should be included into the classpath.

When you need the interpreter during run time your code has to get an instance of the `javax.script.ScriptEngineManager`
some way, for example creating a new one using the operator `new`:
 
```
        ScriptEngineManager sem = new ScriptEngineManager();
``` 

As you can see from the package `javax.script` this class is part of the JDK.

If you use Sprint or some other DI framework, or container you can get the instance of the manager injected into your code.
When you have the instance you can use the next step to get an interpreter engine.
 
```
        ScriptEngine se = sem.getEngineByExtension(extension);
```

This engine should be used to execute the script. In the line above we asked the manager to select an interpreter for us
based on the extension of the script file name. This is only one possibility. You can ask the manager to give you an
interpreter
based on the name of the interpreted language or by the mime type of the script (in case you read the script from a
http stream and not from a file or from some other obscure source that does not represent any name and file extension).

When you ask the manager to give you an interpreter to a script based on the extension
of the script file you have to provide the extension without the dot. Thus you have to specify `sb` or `bas` as
argument to the method `getEngineByExtension()` and NOT `.sb` or `.bas`.

The script engine manager uses the standard Java service locator facility to load and find the appropriate script engine.
This means that at first it loads all `META-INF/services/javax.script.ScriptEngineFactory` resource files that are
loadable from the classpath. If you copied the jar file of ScriptBasic for Java onto the classpath then it will find the
file packaged with this name into this JAR file. The manager will use this file along with the other resources from the
runtime and the JAR files on the classpath and will read the content of each. The content of the file in case of
ScriptBasic for Java is
 
``` 
com.scriptbasic.javax.script.ScriptEngineFactory
```

 This is the fully qualified name of the Java class in ScriptBasic for Java that implements the `javax.script.ScriptEngineFactory`
 interface of the Java runtime. The script engine factory can be queried about the mime types, names of scripting languages and
 file name extensions the interpreter can handle and when your program asks for an interpreter to handle the script that has
 the extension `sb` it will know that the class `com.scriptbasic.javax.script.ScriptEngineFactory` can create one engine
 for you and the manager does call the ScriptBasic for Java script engine factory to create one.

 When you have the scripting engine you can use it to execute a script. The easiest way to do that is to call the `eval`
 method of the engine:
 
```
        se.eval("print \"hello world\"");
```
 
 Note that this method may throw `javax.script.ScriptException` therefore it is better to surround the call using a
 `try`/`catch` block.
 
To make something more complex than just executing a script, you can define a context that the script runs in. Using the
context you can provide input to the script, get output from the script (standard output, and error output) and you can
also access variables. You can set global BASIC variables before starting the script and you can read the values of the global
variables after the script was executed.

To have a context the engine should be used:
 
```
        ScriptContext context = se.getContext();
``` 
 
This call will return a context that you can manipulate before starting your script. To set the input and the output
you should have `PrintWriter` and `InputStreamReader` objects. The following code just wraps the Java standard
`System.out`, `System.err` and `System.in` to the scripting engine context:
 
```
        PrintWriter outWriter = new PrintWriter(System.out);
        context.setWriter(outWriter);
        PrintWriter errorWriter = new PrintWriter(System.err);
        context.setErrorWriter(errorWriter);
        context.setReader(new InputStreamReader(System.in));
        Reader reader = new FileReader(basicProgramFileName);
```         
 
Note that in the current version the interpreter does not provide any mean to write the error output or to
read the standard input. Later versions will provide features for that.

To set/get the global variables you should use the so called bindings of the context, that binds the values to the
names of the global variables.

The standard JSR223 defines two bindings: one engine scope and one global scope bindings. The values bound in the global scope
binding are available for all scripts. The values bound in the engine scope are available only to the scripts executed by the engine.
 
To get one of the scopes you have to 'get' it from the context:
 
```
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
```

and you can use `put` to store values into the bindings.
        
```
        bindings.put("B", Integer.valueOf(13));
        bindings.put("A", null);
``` 
 
To get the value of a global variable after the execution of the script you should call `get` on the bindings:
 
```
        Long z = (Long) bindings.get("A");
```
 
assuming that the type of the value in the variable `A` is `Long`. If you do not 'put' any value into the bindings
before the execution for the global variable `A` then you will not get back the value of the global variable from
the binding after the execution.
 
When a ScriptBasic script starts in Java the interpreter first copies the values from the global scope to the global variables table
of the interpreter. After this the interpreter copies the values from the engine scope to the variables table of the interpreter.
It also implies that if a variable is defined in the global and in the engine scope then the one defined in the engine scope
will override the value of the one defined in the global scope.

When the interpretation of the script is finished the interpreter overwrites the values of the engine scope binding and the
the global binding with the values of the same name from the interpreter variables table. It does not create any new binding.
If a binding, for example does not contain the key`A` then the interpreter will not create that key even if there is a
global BASIC variable named `A`. If you need the value of a global variable after the execution of the BASIC program
you have to set its value in the bindings before the execution of the program to something. If it is `undef` then set it
to `null`.

If you have the value defined both in the
global and in the engine binding then both will have the final value of the global variable, even though only the engine scope
is used in the scope as input.

If the execution of the script throws exception then the values are NOT copied into the bindings.

Later versions will develop other features of the JSR223 interface, like calling subroutines of a BASIC script repeatedly.

Some extra features, like executing a script that includes other scripts from disk, or from database, or some other script
repository needs the use of the native interface of ScriptBasic.
