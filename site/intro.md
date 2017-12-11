# What is ScriptBasic for Java

ScriptBasic for Java is a scripting implementation of the classic [basic](./basic.md) language.
Using this interpreter you can extend you application with BASIC scripting possibilities. There are many
scripting languages that can be embedded into Java application. Different languages have different
capabilities and different target users' group. ScriptBasic for Java targets the applications that are
to be extended using scripts by users who are not really programmers.

## No freak out language 
 
Business analysts, domain experts who use an application can learn Groovy, JavaScript, Python or some other
scripting language to program the scripting possibilities of the application. They probably will not learn.
In case of BASIC this learning is not needed. If anything is known already then BASIC is.
This also means that the "freak out" factor is minimal. Your prospective users may freak out hearing that
the application can be scripted using Groovy, JavaScript or Python which all are considered geek languages.
In case of BASIC this does not happen. If the prospective users have minimal affinity to programming
they have some experience using Excel and its BASIC, Visual Basic or just any other BASIC generally.

For the same reason the language implemented is very simple. It implements only a very limited set
of commands and operators. Each command is implemented that is needed to create embedded scripts but nothing more.
You can assign values to variables, loop, execute conditional code (`IF` statement), print to the standard output
and call subroutines. You can handle arrays. Anything else you need in a BASIC script? To communicate with the
embedding host application
more than just printing to the output the program can call Java static methods or instance methods in case an
object is stored in a BASIC variable and if the host application made that possible. To do that the usual
`object.method(params)` format can be used, or just `method(params)` when it is not ambiguous.
 
## Control remains in the hand of the application

Script languages may pose security risks if not properly configured. When you start a Python, JavaScript of Groovy 
script it can access the application Java objects, methods and so on. You have to pay great attention to execute other
language scripts securely setting up `SecurityManager` to avoid a hacking script run amok.

BASIC programs executed by the ScriptBasic interpreter can not do that unless the host application explicitly made
some of the methods and objects available for the code. The host application can even limit the number of steps, or
the maximum time
a BASIC program can execute and then terminate the execution with error, which is very handy when a BASIC script
gets into an infinite loop or endless recursion.

## Standard Interface

To embed ScriptBasic for Java into a host application the host application can use the standard
JSR-223 interface. This way you can embed just any scripting language easily without further programming.
As a matter of fact the `main()` method of the class `com.scriptbasic.main.CommandLine` uses solely
this interface and as a side effect you can use the command line version of ScriptBasic for Java to execute
JavaScript files utilizing the JRE built in JavaScript engine.

If an application properly uses the JRS-223 interface, integrating jScriptBasic as another scripting language
to the application is simply copying the `jscriptbasic-x.y.z.jar` file on the classpath and restart the application.
 
## Command line tool

You can also execute BASIC programs from the command line. All you have to do is issue the command line
 
```
java -jar jscriptbasic-${project.version}.jar basicProgram.sb
```        

and the program in the file `basicProgram.sb` will execute. (Note that the line above uses the latest version
`${project.version}` which may not be the latest released version. If it finishes with the postfix `-SNAPSHOT`
then the version is a development version. Instead use the name of the JAR file you downloaded.)
 

