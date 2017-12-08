# Providing source to the interpreter

The engine provides some very simple methods to provide the source
code to eecute. The simplest is the `eval(String)` method that
directly evaluates the string as a BASIC prorgram. Applications,
however, need more complex ways, especially when we have
BASIC programs that are more complex and contain some `INCLUDE`
statements.

If you look at the interface `ScriptBasic` in the API it defines
a bunch of overloaded `load()` methods. They all load some
code from different sources makinf the interpreter ready to
invlode the `execute()` method.

## Simple source provision

The simplest versions are

```
void load(String sourceCode) throws ScriptBasicException;
void load(Reader reader) throws ScriptBasicException;
void load(File sourceFile) throws ScriptBasicException;
```

These are very simple and straightforward ways to give the code to
the interpreter. The first one is actually the one used by the
before mentioned `eval(String)` method. The second gets the
code from a `java.io.Reader` and the last one from a `java.io.File`.

If you don't want BASIC programs to include other BASIC program
code fragments and you have the BASIC programs in a location that
is not available as `File` then the simplest way is to implement
a `java.io.Reader` that reads the code from where it is (database, network etc)
and provides it to the lexical analyser. If you need `INCLUDE` in your
code and the code is not in `File` then you should read on and
implement two interfaces as discussed below.

## Source with path

These are still simple possibilities that are easy to use and to
implement the code that makes use of them. The interfacing becomes
more complex when we need to limit the BASIC programs so that they
can not just `INCLUDE` other BASIC code from anywhere that is
readable for the host application. To provide a defined set of
directories to the interpreter where to include from we can use the
method

```
    void load(String sourceFileName, SourcePath path)
            throws ScriptBasicException;
```

This method also gets a file name, but it also needs a `SourcePath`.
The `SourcePath` interface is defined in the API package of ScriptBasic
and it is hardly more than a `Iterable<String>`. (Well, if you look at
the interface it also has a `void add(String path)` method to add
a new string to the path.) The interpreter will search the directories
defined in the source path to find the file and whenever an `INCLUDE`
statement is processed the source path will be consulted again.

If you do not want to implement the interface `SourcePath` then
there is a convenience method

```
    void load(String sourceFileName, String... path)
            throws ScriptBasicException;
```

that accepts the elements of the source path as a second `String[]`
argument or as a series of `String` arguments.

## Source from anywhere

With this most of the applications can live with, after all the BASIC
code is usually on the disk somewhere. But not always. It is possible to
create a host application embedding ScriptBasic that fetches the BASIC
program code from just anywhere it wants. The actual store can be
database, something over the network, anything. It is not a must that
the host has to have the BASIC code in files.

To do just that the host application has to implement two simple
interfaces and use the method

```
    void load(String sourceName, SourceProvider provider)
            throws ScriptBasicException;
```

gets the source code from a `SourceProvider`. The interface `SourceProvider`
is as simple as it can be. It is in the API package of the interpreter
and it defines two methods

```
    SourceReader get(String sourceName) throws IOException;
    SourceReader get(String sourceName, String referencingSource) throws IOException;
```

Both of these methods should be implemented by the host application
using this feature that they return an implementation instance
of the interface `SourceReader` or `HierarchicalSourceReader` in case
there is need to process included files. Eventually the host application
should also implement this interface to provide it. This is the
part where the host application should implement the access to the
real source code and read it from a file, a database, from a wiki page,
or from any other service. There are three methods in this interface

```
    Integer get();
    void unget(Integer character);
    SourceProvider getSourceProvider();
```

`get()` should provide the next available character from the BASIC source code
for the the lexical analysis and it should return `null` when there are no more
characters. `unget(Integer)` should accept a character that was read by the
lexical analyzer but it could not work with. Netx time the method `get()` should
return this character again. A few times the interpreter reads ahead more
than one characters and then `unget`s them, but never more than a BASIC program line.

The last method `getSourceProvider()` should return the `SourceProvider`
instance that can be used to look for other, indluded files. This is
usually the same as the one that retuned the `SourceReader` itself.

To support the inclusion of other files the source reader implementation
should implement the `HierarchicalSourceReader`, which itself extends the
`SourceReader` and adds an extra method

```
    void include(SourceReader reader);
```

The implementation of this method should set aside the current source and
should start to return characters delegating the `get()` calls to the
new source reader until it provides characters. After that it should continue
with the previous source provider. If you decided that you want to
write a hierarchical source provider consult the source code of the
class `GenericHierarchicalSourceReader` or you can just implement a
non-hierarchical source reader and use the available
`GenericHierarchicalSourceReader` as


With the `SourceProvider` and `SourceReader` implemenatation the host
application has full control over the source code location but also
over the access control of it. Some `SourceProvider` may behave differently
for different users, different BASIC programs that include others or
just based on anything.

This way the host program can embed the interpreter in a simple and easy and
fast way in case there are no special needs and the same time there is
no limit where to fetch the source code.