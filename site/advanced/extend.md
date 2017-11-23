# How to extend ScriptBasic for Java with external Java Methods

 ScriptBasic for Java can call Java methods from the BASIC source code. To do so the BASIC program
 should use the `use` and `method` commands. This way the BASIC source code expresses its wish
 to use some Java code. Doing it the other way around the embedding Java application can register
 some methods that will be available to the BASIC program without `use` or `method` declarations.
 
 To register a method into the BASIC runtime and thus make it available for the BASIC program to call,
 ScriptBasic for Java provides a method `registerExtension(Class<?> klass)` in the `EngineApi` interface. Using
 this method the embedding application can register static methods from a class. The embedding application has to
 issue a single call and it will register all the methods that are appropriately annotated.
 
 A method from a class is registered and becomes available for the BASIC program if it is annotated with the
 annotation `com.scriptbasic.Function` and if its classification is configured to allow the method to be
 used by the BASIC program.
 
 For example we can have the following class in an application:

``` 
	public static class TestExtensionClass {
		@Function(alias = "javaFunction", classification = java.lang.Long.class, requiredVersion = 1)
		public static Long fiftyFive() {
			return 55L;
		}
	}
```
 This class contains a single `static` method that is very simple: it only returns a `Long` value: 55. This method
 is annotated with the annotation `@Function` that signals for the registering process that this method is a candidate
 for being called from BASIC.
 
 The parameters for the annotation are all optional. The arguments are defined in the 
 [JavaDoc API](../apidocs/com/scriptbasic/Function.md).
 
 The declared function can be called after that from BASIC the following way:
 
```
		EngineApi engine = EngineApi.getEngine();
		engine.registerExtension(TestExtensionClass.class);
		engine.load("Sub aPie\nreturn javaFunction()\nEndSub\n");
		engine.execute();
		Long z = (Long) engine.getSubroutine("aPie").call();
		Assert.assertEquals((Long) 55L, z);
```

 Note that extension methods should be `static`. If a non-static method is annotated in the registered class using the
 annotation `@Function` then the registering process will throw exception. The return value and the arguments can be
 any Java type. The interpreter will convert the BASIC value passed as actual parameter doing its best. Thus you can
 accept `int`, `long>> and other primitive parameters, just as well as `Integer`, `Long` and so on. When a
 function returns some complex object the interpreter will use it without conversion and if it is stored in a variable
 then it is possible to pass the object to other Java methods that expect the type.
 
 Because BASIC internally uses `Long` to store integer values, `Double` to store floating point values,
 `Boolean` to store boolean values, if you write a method that is specifically designed to be an extension function
 it is recommended that you use these classes instead of `Integer`, `int`, `Float`, `float`, `double`
 and so on.
 
 Generally speaking you need not care about the internal data structures of ScriptBasic for Java when you write an extension
 method. The only exception is when you accept an array as an argument and when you return an array as an argument.
 
 When the extension function handles an array as an argument the actual Java type should be compatible with
 `BasicArrayValue`. Simply saying: it can be `BasicArrayValue` or `Object`.
 
 When you return an array it has to be `BasicArrayValue`. Note that the elements of the array need not be
 an object that implements `RightValue` as defined in the interpreter. It can be any Java object that will be
 converted by the interpreter when the array element is used in an expression in the BASIC program. This is different
 from returning a primitive value, which will be converted when the method returns. In this case the array in the
 BASIC program will contain Java objects and the conversion will happen only later when the value is used.
 
 For more information on how to use the `BasicArrayValue` have a look at the
 [JavaDoc API](../apidocs/com/scriptbasic/executors/rightvalues/BasicArrayValue.md).

 ## alias

 The `alias` of a method is the name of the BASIC function. In the example above the `alias` "javaFunction" is used and accordingly
 this name is used in the BASIC program. If the annotation parameter `alias` is not used then the actual name of the Java
 method will be used as the name of the function for the BASIC program.
 
 Using `alias` can be handy in several cases. One case is when multiple methods have the same name. It is possible because
 Java does method overloading and different methods accepting different arguments may share the name. On the other hand in
 BASIC this is not possible. Using alias you can assign different names to the different argument versions of these methods.
 
 Another use of the parameter `alias` is to define a BASIC friendly name to the method that is more appealing or more
 common for the BASIC programmers.

 ## requiredVersion

 `requiredVersion` is an integer value denoting the required version of the embedding interface of ScriptBasic.
 In the current version `1.0.3` of ScriptBasic for Java this is 1. The default version for this parameter is 1.
 This parameter is described in detail in the [JavaDoc API](../apidocs/com/scriptbasic/Function.md).
 
 ScriptBasic version 1.0.4 adds functionality to the interface, thus the interface version number in this version is
 already 2.
 
 For more information on the differences between the versions see the page [versions](./requiredVersion.md).

 ## substitueClass

 Using the annotations you can register methods that are in different classes. It can happen that the class you want to make
 usable by the BASIC program is defined in a package that you import into your project and you do not have the source code or
 simply you do not want to modify the source code inserting annotation to reserve maintenance ability. In such a case you can
 create a method in the class you register and you can annotate specifying the `substituteClass` and `substituteMethod`.
 If that case the actual name, parameters, return type of the method annotated is ignored and the class and method specified
 in the annotations are used instead.
 
 You can see examples of the use of this annotation in the Scriptbasic for Java source code in the utility class
 `com.scriptbasic.utility.RuntimeUtility` where this annotation is used to declare some of the methods of the class
 `java.lang.Math` usable from BASIC.

 ## substitueMethod

 Using this annotation you can specify an alternate method instead of the one annotated. In this case the actual
 name, parameters, return type of the method annotated is ignored and the one specified in the annotation is used. You can
 use this annotation along with `substituteClass` or without that. In the latter case you can give alternative names
 of the same method. The names, which are the names of the Java methods or are defined in the annotation `alias` are
 interchangeably used in the BASIC program.

 ## classification

 Classification helps the security system of ScriptBasic for Java. You can classify a method into several groups. 
 These classification groups classify the nature of the method and help the installation to decide if the invocation
 of a certain method is allowed or not. For example you can classify methods to be `Constant` when the method
 just returns a constant value. Actually this is how `undef()` is implemented. Other methods may be classified as
 `Math` to denote that they perform mathematical functions.

 When an instance of ScriptBasic is running the interpreter reads the configuration file `sb4j.properties` as
 described in [chapter configuration](../configure.md). This configuration file may level the classifications
 as allowed and denied and based on that the ScriptBasic runtime can decide if a mathod is available for the BASIC
 program or not.
 
 The classification groups are denoted by Java classes. However these classes have nothing to do with
 the actual classification. It is important to understand that the classes listed as classification do not
 play any special role. We could use strings just as well instead of java classes. The reason we decided to use
 java classes is to lessen the human errors that come from misspelling. Using Java classes you can have auto fill
 in your favorite IDE (e.g.: NetBeans or Eclipse) and you also get compilation error in case of misspelling. These
 IDEs also help you displaying the JavaDoc of the classes while you use them and JavaDoc is a good place to describe
 the details of the interfaces or classes that are used for classification.
 
 It is recommended to create Java interfaces to be used as classification with names that express the actual
 classification group. It is not a must. You can use `java.lang.Integer` to classify a method, however
 it has drawbacks: seems to be meaningless and you have no easy way to document the meaning of the classification.



 