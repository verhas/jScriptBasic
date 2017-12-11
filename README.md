# ScriptBasic for Java

[![Build Status](https://travis-ci.org/verhas/jScriptBasic.svg)](https://travis-ci.org/verhas/jScriptBasic)

ScriptBasic for Java is a BASIC interpreter that can be embedded into Java programs. To embed the
interpreter into your application you need to use SB4J as a dependency

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
     ScriptBasic.getEngine().eval("print \"hello world\"");
```

get an execution engine and `eval()` the program source. There are other possibilities. You can
specify the file where the source code is, a `java.io.Reader` to read the source code and
there are even more advanced possibilities.

The BASIC language contains all the usual BASIC features, subroutines, local and global variables,
conditional statements, loops and so on. Your host program can directly call subroutines, read
and write global variables, provide methods implemented in Java to be called from BASIC code.

The interpreter can safely be integrated to applications as the BASIC programs can not access
arbitrary objects and call Java methods at their will and there are other features that help
controlling the scripts in the application. The language is a "no freak out" language, so you
can put the programming possibility into the hands of users who would not ever touch Python, Groovy
or some other programming language. 

[Documentation](site/index.md)