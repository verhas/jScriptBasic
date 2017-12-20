sentence "the $expression is the same as $expression" call myequals
the 13+2 is the same as 15

sentence "message $expression unless $expression is true" call assert


message "we have a problem" unless 1=1 is true

sub myequals(a,b)
  if a = 15 and b = 15 then
    PRINT "OK"
  else
    PRINT "CALLED BUT THE VALUES ARE", a, " AND ", b
  endif
endsub

