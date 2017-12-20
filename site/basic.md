# The BASIC language interpreted by ScriptBasic for Java

The language implemented byScriptBasic for Java is a simple BASIC language.
There are only a very few commands implemented
to keep the language simple, easy to learn. The following commands are implemented:

* [Variable assignment](./basic/let.md), `LET` and expression with operators

* `IF`/`THEN`/`ELSEIF`/`ELSE`/`ENDIF` [conditional commands](./basic/if.md)

* `SUB`/`CALL`/`RETURN` [subroutine and call](./basic/sub.md) using formal arguments and local variables

* `FOR`/`TO`/`STEP`/`NEXT` [loop](./basic/for.md), using integer or float loop variable.
  Default step is 1. Checked at the start of the loop.

* `WHILE`/`WEND` [loop](./basic/while.md)

* `PRINT` command to [send output](./basic/print.md) to the standard output stream

* `GLOBAL`/`LOCAL` [variable declaration](./basic/global.md)

* `USE`/`METHOD` [declaration](./basic/method.md) to use Java methods and objects from the BASIC code

* `INCLUDE` / `IMPORT` [to include](basic/include.md) basic code into the program

* 'SENTENCE' [to define DSL sentences](basic/sentence.md)

The operators implemented in the language are

* `-`, `+` and `not` unary operators.

* `.` (dot) to access Java object fields (1)

* `^` power operator (2)

* `*` multiply (3)

* `/` division (floating point) (3)

* `%` modulo calculation (3)

* `div` integer divide (3)

* `+` binary add operator, works on integer values, float values and concatenates strings (4)

* `-` binary minus (4)

* `=` equality,` <` 'less than', `> `'greater than', `>=`
  'greater or equal', `<=` 'less or equal', ` <> `'not equal' operators that compares values (5)

* `and` logical (short circuit) and operator (6)

* `or` logical (short circuit) or operator (6)

The operators have the usual precedences, as listed at the end of the lines above. You can use `(` and `)` parentheses to
change the order of the evaluation of the operations. You can also use the parenthesis to call declared Java methods and to call
BASIC defined subroutines.

[Arrays](./basic/arrays.md) can be accessed using the `[` and `]` brackets. Multidimensional array indices are separated by `,` comma. There is
no array of arrays.

[File handling](./basic/file.md) is available via an extension class. This extension class is not loaded by default. The
embedding application should register this extension class. The command line version does register this class.