assert "two undef values compared should result true", a = b
assert "undef compared to anything else is false" , not ( a = 1 )
assert "anything compared to undef is false" , not ( 1 = a )
PRINT "OK"