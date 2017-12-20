# SENTENCE

To make ScriptBasic  business user friendly it is possible to define sentences that are converted to function
calls in the BASIC interpreter. That way the business users can program almost like writing English sentences and
what sentences mean what is defined in BASIC.

To define a sentence you can write

```
sentence "the sentence string" call methodName
```

The syntactical definition of the sentence is a string literal. The keyword `call`  separates the syntactical definition
of the sentence from the actual definition what to do. A sentence, as the definition suggests, will always call a
method as defined at the end of the command.

The method can be a `sub` or a Java defined method.

The syntax definition string should be a sentence containg words, special characters and placeholders.
The words, special characters and placeholders should be separated in the definition by one or more space.

The placeholder can be `$expression` as written here, a dollar sign immediately followed by the word "expression".

When ScriptBasic fails to syntax analyze a line as a final effort it tries ro match the line against the sentences
which are defined in the source code.

A sentence definition should precede it's use.

Matching the line against a sentence definition succeeds if the characters, words are following each other as
defined in the `sentence` commands and the `$expression` placeholders match expressions.

When a sentence is matched the syntax analyzer creates a method call to the named mathod using the expressions as
arguments. The expressions are passed from left to right.