sentence "the $expression is the same as $expression" call myequals
the 13+2 is the same as 15

sentence "message $expression unless $expression is true" call assert
message "we have a problem" unless 1=1 is true


sentence "true that 1 $expression has 1 result" call nullsub
true that 1 2*2 has 1 result


sentence ". this is $expression $ expression" call expression

.this is "no way" $expression

sub expression(xp)
  if xp <> "no way" then
    PRINT "Problem"
  endif
endsub

sub myequals(a,b)
  if a = 15 and b = 15 then
    PRINT "OK"
  else
    PRINT "CALLED BUT THE VALUES ARE", a, " AND ", b
  endif
endsub

sub nullsub(a)
endsub