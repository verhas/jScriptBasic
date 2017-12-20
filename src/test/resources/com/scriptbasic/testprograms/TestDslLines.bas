sentence "for the ( $expression ) and ( $expression )" call nullsub

for the (6) and (7)

sentence "assert that $expression is the same as $expression" call assertEquals
assert that 13+2 is the same as 15

sentence "message $expression unless $expression is true" call assert
message "we have a problem" unless 1=1 is true


sentence "$expression is the answer" call isTheAnswer
42 is the answer


sub isTheAnswer( a )
  if a <> 42 then
    error a + " is not the answer"
  endif
endsub


sentence "$expression is exactly the answer" call isTheAnswer
42 is exactly the answer

sentence "for example $expression " call isTheAnswer
for example 42


sentence "true that 1 $expression has 1 result" call nullsub
true that 1 2*2 has 1 result


sentence ". this is $expression" call expression
.this is "a string expression"


sentence "$expression compareTo $expression" call assertEquals

13+2 compareTo 11+4

sentence "$expression" call printFunction




66+13

sub printFunction(e)
PRINT e
endsub

PRINT "OK"

sub expression(xp)
  if xp <> "a string expression" then
    PRINT "Problem"
  endif
endsub

sub assertEquals(a,b)
 if a <> b then
   error "equality assertion failed"
 endif
endsub

sub nullsub(a,b)
endsub