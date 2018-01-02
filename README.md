# ScriptBasic for Java

[![Build Status](https://travis-ci.org/verhas/jScriptBasic.svg)](https://travis-ci.org/verhas/jScriptBasic)

ScriptBasic for Java is a BASIC interpreter that can be embedded into Java programs. With SB4J you can
script your application (or let the users to script) in good old BASIC. You can start in the JVM an
interpreter and execute for example  

```
PRINT "hello, I am BASIC"
PRINT "\nthe numbers from 1 to 10 are\n"
FOR I=1 to 10
  PRINT I,"\n"
NEXT I
```

SB4J has all the BASIC language features, assignments, loops, subroutines, global and local variables and
no `GOTO` statement. Seriously. 

To embed the interpreter into your application you need to use SB4J as a dependency

```
  <dependency>
    <groupId>com.scriptbasic</groupId>
    <artifactId>jscriptbasic</artifactId>
    <version>1.0.5</version>
  </dependency>
``` 

and then use the JSR223 defined scripting interface or use the ScriptBasic native integration API.

The simplest way is to

```
     ScriptBasic.getEngine().eval("PRINT \"hello world\"");
```

get an execution engine and `eval()` the program source. There are other possibilities. You can
specify the file where the source code is, a `java.io.Reader` to read the source code and
there are even more advanced possibilities.

The BASIC language contains all the usual BASIC features, subroutines, local and global variables,
conditional statements, loops and so on. (No GOTO!) Your host program can directly call subroutines, read
and write global variables, provide methods implemented in Java to be called from BASIC code.

The interpreter can safely be integrated to applications as the BASIC programs can not access
arbitrary objects and call Java methods at their will and there are other features that help
controlling the scripts in the application. The language is a "no freak out" language, so you
can put the programming possibility into the hands of users who would not ever touch Python, Groovy
or some other programming language. 

[Documentation](https://github.com/verhas/jScriptBasic/wiki/index)