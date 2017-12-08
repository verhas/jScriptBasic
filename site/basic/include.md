# Including other files

ScritpBasic programs can include other BASIC programs into the
file. The syntax is

```
INCLUDE "otherProgram.bas"
```

The string has to specify the name of the file that contains the BASIC program
to be included. There is no special assumption on the extensions. The file name
is usually relative to the actual source but it can also be absolute path.

When a host application allows the codes to include other BASIC programs it
is possible to provide the souce code through some reading constructs that
reach BASIC programs only in certain directories, or read from database
or from any character source. For more information on this
you can read ["Source Reading"](../advanced/sourceReading.md).