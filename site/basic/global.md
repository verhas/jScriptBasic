# `LOCAL` and `GLOBAL`

 Variables in ScriptBasic for Java need not be declared. They are created automatically when the
 program execution sees a variable. When a variable is used in a subroutine, however it can be either
 a local or a global variable. The interpreter can not derive the intention of the programmer and
 for this reason there are two commands `LOCAL` and `GLOBAL` that can be used to declare
 variables.
 
 Inside a subroutine all variables are local unless defined global.
 
 A variable can be declared to be global inside a subroutine or in the main program code. `LOCAL`
 and `GLOBAL` declarations can be used in subroutines only before the first executable command.
 In the main program you can use `GLOBAL` anywhere.
 
 Lets have a look at the following program:
 
```
' Program demonstrating global
' and local variables

gVar1 = "global"
gVar2 = "global"
anyVar = "global"
arg = "global"

GLOBAL myLocalVar2

sub mySub(arg)

  LOCAL anyVar, myLocalVar
  GLOBAL gVar2

  anyVar = "local"
  arg = "local"
  gVar1 = "local"
  gVar2 = "local"
  myLocalVar1 = "local"
  myLocalVar2 = "local"

endsub


mySub "argument"

print "gVar1=",gVar1
print "\n"
print "gVar2=",gVar2
print "\n"
print "anyVal=",anyVar
print "\n"
print "arg=",arg
print "\n"
print "myLocalVar1=",myLocalVar1
print "\n"
print "myLocalVar2=",myLocalVar2
print "\n"
```

 The output of the program is


```
gVar1=global
gVar2=local
anyVal=global
arg=global
myLocalVar1=undef
myLocalVar2=undef
```

Only the variable `gVar2` was modified to hold the string `"local"` in the
subroutine, because that is the only variable declared to be global. All other variables
including the argument is local.

## Practice

Treating all variables local unless declared global is a safe practice. This prevents a subroutine
to accidentally overwrite the value of a global variable.

Even though it is not required to declare every variable, it is a good practice to declare variables
either to be global and local. ScriptBasic for Java has an internal feature that makes it possible
to programmatically configure the interpreter to alter the behavior above. It can be forced to
treat any non declared variable global inside subroutines.

The other possible programmatic configuration is to treat any non declared variable an error. To make the
interpreter usable this way the `GLOBAL` keyword can be used in the main program code and you can
declare any local variable as `LOCAL` inside a subroutine.
