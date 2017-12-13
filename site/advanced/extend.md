# How to extend ScriptBasic for Java with external Java Methods

## METHOD and USE

ScriptBasic for Java can call Java methods from the BASIC source code. To do so the BASIC program
can use the `use` and `method` commands. This way the BASIC source code expresses its wish
to use some Java code. Doing it the other way around the embedding Java application can register
some methods that will be available to the BASIC program without `use` or `method` declarations.

The general recommendation is that the embedding application should define the methods that are
available for the interpreter and it should not be up to the BASIC code to reach out without control
to the Java environment. There is one scenario when the use of the `USE ... METHOD` command is
justified. When the hosting application does not need the high security, the BASIC programs are
trusted to a higher level and the extra methods provided as a library for the BASIC programs are
provided sepearately from the host application. In that case if the host application allows the
use of the command `USE ... METHOD` the extra methods for the BASIC can be provided as
JAR filed to be put on the class path or on the module path and some header files with the declaration
of the callable methods.

## Registering extension classes

To register a method into the BASIC runtime and thus make it available for the BASIC program to call,
ScriptBasic for Java provides a method `registerExtension(Class<?> klass)` in the `ScriptBasic` interface. Using
this method the embedding application can register static methods from a class. The embedding application has to
issue a single call and it will register all the methods that are appropriately annotated in that class.


## Automatic Registration

External modules can register extension classes automatically. ScriptBasic defines the interface
`com.scriptbasic.spi.ClassSetProvider`. An extension module (either a real Java 9 module or only a JAR file)
can implement this interface, and decrare the implementing class in the file
`META-INF/services/com.scriptbasic.spi.ClassSetProvider`
so that the Java ServiceLoader mechanism can find the class. The interface defines one method:

```
Set<Class<?>> provide();
```

The implementation should return the set of the classes that contains the static methods with the `BasicFunction`
annotation. The implementation of this method is usually nothing more than

```
@Override
    public Set<Class<?>> provide() {
        return Set.of(... list of the classes containing the extension methods ...);
    }
```

a method returning a constant set. ScriptBasic will find all these classes and register them automatically if the JAR file is on the
class path.

Note that ScriptBasic module declaration does not `requires` any extension class containing JAR file
therefore it is not a good solution to declare the implementation of the interface `ClassSetProvider`
as `provides com.scriptbasic.spi.ClassSetProvider with ...` in the `module-info.java` because the
service loader does not find the implementation in the modules that are not required by the module `scriptbasic`.


## BasicFunction Annotation

A method from a class is registered and becomes available for the BASIC program if it is annotated with the
annotation `com.scriptbasic.api.BasicFunction` and if its classification is configured to allow the method to be
used by the BASIC program. (About this a bit later.)

For example we can have the following class in an application:

``` 
public static class TestExtensionClass {
	@BasicFunction(alias = "javaFunction",
	               classification = java.lang.Long.class,
	               requiredVersion = 1)
	public static Long fiftyFive() {
		return 55L;
	}
}
```
 This class contains a single `static` method that is very simple: it only returns a `Long` value: 55. This method
 is annotated with the annotation `@BasicFunction` that signals for the registering process that this method is a candidate
 for being called from BASIC.
 
 The parameters for the annotation are all optional. They are the followings:

* `String alias` is the name of the function to be used in the BASIC program. If this
  is not defined then the name of the Java method will be used. It is recommended to
  name the Java method as the name of the BASIC method, but in some cases it is not
  possible. For example if you want to name a method to the name `void()` or `default()`.
  It is also handy to define an alias when the method is substituted. See `substituteClass`
  in the followings.

* `Class substituteClass` defines a class instead of the actual one that contains the method
  to be used from BASIC. It may happen that the you want to register methods that are not in your
  hands. In such a case the method can not be instrumented using
  annotations. Using this annotation parameter you can specify an
  alternative class where the method you want to register instead of the
  annotated one is. You can find examples of use of this parameter in the ScriptBasic
  class `com.scriptbasic.utility.functions.MathFunctions`. This class defines
  the mathematical functions, but instead of implementing all methdos that are already
  implemented in `java.lang.Math` the implementation is annotated so that the interpreter
  knows that the actual method with the same name is there. For example

```
@BasicFunction(substituteClass = java.lang.Math.class, classification = com.scriptbasic.classification.Math.class)
    static public double acos(final double x) {
        return 0.0;
    }
```

   could also be implemented as

```
@BasicFunction(classification = com.scriptbasic.classification.Math.class)
    static public double acos(final double x) {
        return java.lang.Math.acos(x);
    }
```

but the first version is cleaner and more declarative and contains one level less of method
calls when the method is actually invoked.

* `String substituteMethod` Using this parameter you can specify an alternative method instead of the
  annotated one. This parameter is usually used together with the `substituteClass`
  argument when the annotated method, which is only a placeholder anyway, can not be
  named the same as the one the BASIC interpreter should call. For example you collect
  substitued decralrations into a single file and you reference multiple classes with
  the different `substituteClass` annotation parameter some of which contain identically
  named methods. In this case it is inevitable to use the `alias` parameter as well.

* `long requiredVersion` The interface version of ScriptBasic for Java required for this
  method to properly function. The different versions of ScriptBasic may provide different
  functionalities for the methods registered as functions. When a method
  needs a specific interface version the interpreter may alter its
  behavior. If a method requires an interface version that is higher than the version
  of the executing interpreter then the interpreter can not work together
  with the extension and thus will signal error.
  If a method requires a version that is smaller that the actual version of
  the executing interpreter then the interpreter may decide if it can work
  with the extension. There may be three different cases foreseen:
  * The interpreter can work seamless with an extension that was designed for an earlier version.
  * The interpreter can not support the outdated extension.
  * The interpreter mimics the interface of an older version of it provided towards the extension.
  The version specified in this annotation is the version of the
  'interface' the interpreter provides for the extensions. This 'interface'
  is not literally a Java interface, rather the collection of all the
  interfaces and behaviors that the interpreter exhibits towards the
  extensions.
  If different versions of ScriptBasic for Java share the same behavior
  regarding the extensions and thus the two versions are 100% compatible
  regarding extension support and usage then the two different versions of
  the interpreter will have the same extension interface version. Therefore
  you should expect this interface version to change rather infrequent.
  A later version of SB4J should not have a smaller interface version.
  The actual value of the actual version is defined as a constant:
  `ExtensionInterfaceVersion#EXTENSION_INTERFACE_VERSION`


* `Class[] classification` the array of the classification classes that define if a method
   is to be used from BASIC or not. The fact that the methods are there does not implicitly
   means that the method is available from BASIC. The interpreter can be configured to
   vote on the different classification classes and these votes add up plus and minus
   and only the methods that have a non-negative classification value are available for the
   BASIC programs.

Classification helps the security system of ScriptBasic for Java. You can classify a method into several groups.
These classification groups classify the nature of the method and help the installation to decide if the invocation
of a certain method is allowed or not. For example you can classify methods to be `Constant` when the method
just returns a constant value. Actually this is how `undef()` is implemented. Other methods may be classified as
`Math` to denote that they perform mathematical functions.

When an instance of ScriptBasic is running the interpreter reads the configuration file `sb4j.properties` as
described in [chapter configuration](../configure.md). This configuration file may level the classifications
as allowed and denied and based on that the ScriptBasic runtime can decide if a method is available for the BASIC
program or not.

The classification groups are denoted by Java classes. However these classes have nothing to do with
the actual classification. It is important to understand that the classes listed as classification do not
play any special role. We could use strings just as well instead of java classes. The reason we decided to use
java classes is to lessen the human errors that come from misspelling. Using Java classes you can have auto fill
in your favorite IDE and you also get compilation error in case of misspelling. These
IDEs also help you displaying the JavaDoc of the classes while you use them and JavaDoc is a good place to describe
the details of the interfaces or classes that are used for classification.

It is recommended to create Java interfaces to be used as classification with names that express the actual
classification group. It is not a must. You can use `java.lang.Integer` to classify a method, however
it has drawbacks: looks meaningless and you have no easy way to document the meaning of the classification.

```
//repeated code sample defining javaFunction()
public static class TestExtensionClass {
	@BasicFunction(alias = "javaFunction",
	               classification = java.lang.Long.class,
	               requiredVersion = 1)
	public static Long fiftyFive() {
		return 55L;
	}
}
```

The declared function can be called after that from BASIC the following way:

```
sub aPie
  return javaFunction()
endsub
```

The Java test code you can find in the test directory that test this feature contains the following
lines:
 
```
ScriptBasic engine = ScriptBasic.getEngine();
engine.registerExtension(TestExtensionClass.class);
engine.load("Sub aPie\nreturn javaFunction()\nEndSub\n");
engine.execute();
Long z = (Long) engine.getSubroutine("aPie").call();
Assert.assertEquals((Long) 55L, z);
```

Note that extension methods should be `static`. If a non-static method is annotated in the registered class using the
annotation `@BasicFunction` then the registering process will throw exception. The return value and the arguments can be
any Java type. The interpreter will convert the BASIC value passed as actual parameter doing its best. Thus you can
accept `int`, `long` and other primitive parameters, just as well as `Integer`, `Long` and so on. When a
function returns some complex object the interpreter will use it without conversion and if it is stored in a variable
then it is possible to pass the object to other Java methods that expect the type.

If the first argument of the Java method is `com.scriptbasic.spi.Interpreter` then
the interpreter will pass the `Interpreter` object as first argument to the method.
In this case the `requiredVersion` should not be less than `3`.

BASIC internally uses `Long` to store integer values, `Double` to store floating point values,
`Boolean` to store boolean values. When you write a method that is specifically designed to be an extension function
it is recommended that you use these classes instead of `Integer`, `int`, `Float`, `float`, `double`
and so on.

Generally speaking you need not care about the internal data structures of ScriptBasic for Java when you write an extension
method. The only exception is when you accept an array as an argument and when you return an array as an argument.

When the extension function handles an array as an argument the actual Java type should be compatible with
`BasicArray` interface.

Similarly when you return an array it has to be `BasicArray`. The elements of the array can be any Java object that will be
converted by the interpreter when the array element is used in an expression in the BASIC program. This is different
from returning a primitive value, which will be converted when the method returns. In this case the array in the
BASIC program will contain Java objects and the conversion will happen only later when the value is used.

For more information please look at the documentation of [BasicArray](BasicArray.md)







 