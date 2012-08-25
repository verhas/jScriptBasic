' START SNIPPET: whole
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


' END SNIPPET: whole