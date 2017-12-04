# Record handling in ScriptBasic

A function `newRecord()` is implemented in ScriptBasic that returns a special Java objects.
Java objects when passed to the BASIC program can be used from BASIC. A BASIC program can access
the fields of the object or can call methods on the object using the dot notation that is
known from Java. Access is only allowed if the Java code can access the field or can call the
method.

In case of the object returned by the BASIC funtion `newRecord()` the object behaves
magically. You can use any field name you want either to read and to write.
What ever value you write into a field you will get back when you read the same
field. The fields are undefined by default, however when they are assigned in
mutli-level, they will automatically become new records. Thus you can write

```
R = newRecord()
R.alma = 3                        ' field is automatically created
print R.alma
R.alma = newRecord()              ' field value is overwritten
r.alma.korte = "haho"             ' field of the record in the field gets value
print r.alma.korte
r.z.k.l.t = 55                    ' field z, k, l and t are magically created
print r.z.k.l.t
REM can be used through 'get' and 'put'
print r.get("z").get("k").get("l").t
r.set("sss","sss")
r.set("xxx")
print r.sss
```


The fields can be reched via the methods `set(name,value)` and `get(name)` when the BASIC program
wants to access a field by the name using calculated string and not hardcoded into the BASIC program.

The actual object behind the record functionality is `com.scriptbasic.utility.MagicBean`. The
host Java program can directly use that object. The rest of this page describes the Java side details
of object handling in ScriptBasic.

## Java side handling of records

The access (read and write) to object fields is implemented in a very
flexible way in ScriptBasic. When the BASIC program tries to write the
field of an object, like

```
record.myField = 42
```

then the interpreter does the following steps:

1. First it checks if the object is an instance of the interface
`com.scriptbasic.interfaces.Magic.Setter`. If it is then the interface
defined `set` method is used to set the field. It is up to the implementation
to do whatever it needs to do with this value.

1. If the object's class does not implement the interface then the
interpreter checks if the object has a setter method for the field. For the
example above the setter method would be `setMyField()`. If there is
such method then it is invoked. (To be precise, the interpter looks for the
method `setMyField()` that has the same argument type as the field. The
Java code can not get along with a method named `setSomething()` without
actually having a field `something`.)

1. If the setter is not there then the field is set directly.

The second and the third step (only one of them at a time) is performed
via reflection calls. In these calls the accessibility of the field and the
method is not overridden. The setter should be callable by the BASIC interpter
as well as the field has to be accessible (in case there is no setter). This
does not only mean that the access should be public but also the package that
contains the class of the object should be opened in the module-inf file
for the ScriptBasic interpreter module.

Reading the value of a field is executed the same way. The following steps are performed

1. First it checks if the object is an instance of the interface
   `com.scriptbasic.interfaces.Magic.Getter`. If it is then the interface
   defined `set` method is used to get the field. It is up to the implementation
   to do whatever it needs to get the value.

1. If the object's class does not implement the interface then the
   interpreter checks if the object has a getter method for the field.
   For the example above the getter method would be `getMyField()` or
   `isMyField()` in case the field is `boolean` or `Boolean`. If there is
   such method then it is invoked. (To be precise, the interpter looks for the
   method `getMyField()` if the field is not boolean type and first looks for
   `isMyField()` if the field is boolean type and then if there is no
   'is' method then it makes a second try with the 'get' method.
   The Java code can not get along with a method named `getSomething()` without
   actually having a field `something`.)

1. Finally if there was no getter for the field then the interpreter will
   access the field directly.

The second and the third steps (only one of them at a time) is performed
using reflection. This is almost identical as it is with the setters and
the same rules are valid.