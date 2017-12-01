# ScriptBasic for Java Documentation

## introduction

These documents will help you to get acquainted with the application and how to use it.

* [Introduction to the application](intro.md)

* [The BASIC language implemented](basic.md) a simple and short language definition and not a BASIC
programming tutorial

* [How to install SB4J](install.md) ... just put it on the classpath

* [How to configure](configure.md) ... no need to

* [Design considerations](design.md) ... there were none (just kidding)

* [Name of the game](name.md)

* [Releases](releases.md)

## advanced

These documents will give you more detail into the topics, like embedding the interpreter
into a host application, writing hook classes that use the service programming interface
of the interpreter and so on.

* [Index of the advanced topics](advanced/index.md)

* [How to extend ScriptBasic for Java with external Java Methods](advanced/extend.md)

* [How to embed the interpreter as s standard scripting engine](advanced/jsr223tutorial.md)
This should quite standard as defined by the standard JSR223, but here it is. The extra
information, which is a bit more than a standard JSR223 tutorial is how the interpreter
binds the global variables to the JSR defined contexts.

* [How to embed the interpreter using the native ScriptBasic API](advanced/nativeapi.md) 