jScriptBasic
============

ScriptBasic for Java. This implementation is a total rewrite of the ScriptBasic ideas in Java.
There is no common code base, or even common BASIC syntax. The original ScriptBasic that we soon will
officially call ScriptBasic Classic is written in C and has many features encoded in it that jScriptBasic
need not have. ScriptBasic was intended to be embedded into native applications, while jSriptBasic is to be
embedded into Java application.

Because of this jScriptBasic is not compatible with ScriptBasic Classic (SBC as a short notation). (Also jScriptBasic
also bears the technical name sb4j, that stands for ScriptBasic for Java.)

- SBC contains a wast amount of code to manage memory. sb4j relies on Java memory management.

- SBC contains a lot of functions implemented in the language. sb4j does not implement these. Programs running in sb4j can use the methods of the Java runtime.
  
  