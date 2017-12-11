# BasicArray

BasicArray is an interface defined to be used by extension functions.
The extension functions have to use this interface to get access to the
elements of an array that was passed by the BASIC program or to return a
value that is an array. The use of this interface is unfortunately
inevitable because arrays in Java are objects and it is possible to pass
objects to the BASIC program and from there to the Java extension and
it is indistinguishable when we need to convert an array to a BASIC
array and when should we pas it as a genuine Java "object".

It is also an important aspect that the methods getting an array from the
BASIC program as an argument can modify the elements of the array. It
would be ridiculously complex and ineffective to convert the array to
an `Object[]` array and back. Especially when we consider that the arrays
can have any dimensio, which means that any array element can also be an
array.

The `BasicArray` interface is defined in the SPI package of ScriptBasic,
which is exported and is available for the extension programs. It also
contains two static methods `create()` and `create(Object[] x)` that
can be and probably should be used by extension programs to create a
new array (unless you can reimplement the interface).

There is an example of the use of the methods in the extension class
`FileHandlingFunctions` that lists the files in a directory:

```
@BasicFunction(classification = com.scriptbasic.classification.File.class)
public static BasicArray listFiles(final String fileName)
        throws ScriptBasicException {
    final String[] files = new File(fileName).list();
    final BasicArray result = BasicArray.create(files);
    return result;
}
```

The method creates a list of `File` objects and then wraps it into a
`BasicArray` before it returns it.

There are three methods defined in the interface

* `void setArray(final Object[] array) throws ScriptBasicException;`
  Set the array object. This method is available as a convenience method
  for extension methods and is not used by the interpreter. This method can
  be used when the array is available from some calculation and it would be
  waste of resource to copy the elements of the array one by one calling
  `set(Integer, Object)`.

* `public void set(final Integer index, final Object object) throws ScriptBasicException;`
  Set the `index`-th element of the array. If the array is smaller than
  needed then it is automatically extended with undefined element so that
  the `index`-th element can be set.

* `Object get(final Integer index) throws ScriptBasicException;`
  Get the `index`-th element of the array. Note that this method does
  NOT convert the value to an ordinary Java object. Thus when calling this
  method from an extension method be prepared to convert the value to
  ordinary Java object yourself.

* `long getLength();`
  Get the length of the array. This is not the length of the underlying
  object array but the size that the BASIC program should feel.


