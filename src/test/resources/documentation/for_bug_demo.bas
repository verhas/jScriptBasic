' For loop end and step values are evaluated only once at the start of the execution of the loop.
' The calculated values are stored in objects that are associated with the actual command instance.
' When the same loop starts again these expressions are evaluated again and then the variables
' are overwritten. This can happen in case of recursive calls before the loop finishes. This is
' a structural bug in ScriptBasic and is demonstrated by the following test code.
'
' If ever this gets fixed the documentation of the FOR loop also has to be modified, because this
' bug is documented there.
'

' true once and then set to false so we call
' the subroutine 'a' only once recursively
z = true

' the original start finish values
start = 1
finish = 5

sub a
' we access these global variables, have to be declared
' otherwise we modify local variables
global z,start,finish, delta

' first time '1 to 5'
' before the recursive call these are modified '2 to 3'
' the first execution will also finish at 3 as the finish value
' is overwritten during the recursive call
for i=start to finish
  print i
  ' new start value when calling recursive
  start = 2
  ' new finish value when calling recursive
  finish = 3
  if z then
    ' set to false so this is the only recursive
    z = false
    call a
    ' recursive call already evaluated the end value
    ' to 3, this assignment does not change anything
    finish = 4
  endif
next
endsub

call a