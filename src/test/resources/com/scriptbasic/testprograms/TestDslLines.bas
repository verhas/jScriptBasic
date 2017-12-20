sentence "the $expression is the same as $expression" call myequals
the 13+2 is the same as 15

sub myequals(a,b)
  if a = 15 and b = 15 then
    PRINT "OK"
  else
    PRINT "CALLED BUT THE VALUES ARE", a, " AND ", b
  endif
endsub

