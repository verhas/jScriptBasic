# `WHILE` looping

The command `WHILE` is a looping construct. The format of the command is
 
```
WHILE  condition
  commands
WEND  
```
 
The loop evaluates the `condition` and if the condition is `true` then
it executes the commands. If the condition is false then the execution continues
after the command `WEND`.

When the command `WEND` is reached the execution jumps back to the command `WHILE`
and the looping starts again until the `condition` becomes `false`.