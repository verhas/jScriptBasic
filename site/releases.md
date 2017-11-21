# Releases of ScriptBasic for Java

This document describes the history of ScriptBasic listing the different releases and the release notes for each of them.

ScriptBasic for Java is a language and an implementation at the same time. This way there is no separate language and
implementation version. The language does not live without the implementation and there is no formal defintion for the
language (even though the language itself is very simple).

The version structure of the interpreter has three levels and follow the usual maven suggested version structure. The version
of a release is `M.m.b` where `M` is the major version, `m` is the minor version and `b` is the bug fix version.

* `M` increases if there is some significant change in the software and the new version has significant new features.

* `m` increases if some smaller features are added to the interpreter.

* `b` is increased if there is some bugfix but the features of the language and the interpreter did not change.


## 1.0.0-SNAPSHOT
The current development version. This is not a release version.

## 1.0.5-SNAPSHOT
Version that supports Java 9. 