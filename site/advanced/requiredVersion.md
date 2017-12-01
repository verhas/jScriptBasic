# ScriptBasic for Java Interface Versions

ScriptBasic provides an interface for functions implemented in Java and callable 
from BASIC. The functions
implemented in Java need not implement any Java interface. In this terminology the 
"interface" ScriptBasic provides is not a Java
interface. This term refers to the way ScriptBasic calls these methods.

As ScriptBasic develops the way ScriptBasic handles these extension functions also develop and some new
features become available. If an extension relies on such an advanced feature
it will not work with earlier
versions of ScriptBasic that implements an earlier version of the interface.
Therefore ScriptBasic introduces
a simple `long` number that specifies the version of the interface.

This document describes the differences between the different interface versions.  

* 1L

 This is the initial version.
 
* 2L

 ScriptBasic 1.0.4 introduced the possibility for an extension to accept BASIC
 array as argument and also to
 return an array as value. If you need that feature you have to
 specify `requiredVersion=2L` in the
 annotation of the static method implementing the extension function.
 
 For example the method `length()` implemented in the class `UtilityFunctions` as
 
```
     @BasicFunction(classification = Utility.class, requiredVersion = 2L)
     public static Long length(Object arg) {
         if (arg == null) {
             return null;
         } else if (arg instanceof BasicArrayValue) {
             BasicArrayValue array = (BasicArrayValue) arg;
             return array.getLength();
         } else if (arg instanceof String) {
             String string = (String) arg;
             return (long) string.length();
         } else if (arg.getClass().isArray()) {
             return (long) Array.getLength(arg);
         }
         return null;
     }
```

This is because this basic function would not work with versions of ScriptBasic prior to
1.0.4