'
' Sub testing
'

sub apple
 global a
 local c, d, e
 a = a + 1
 b = 2
 c = 7
endsub

a = 1
b = 1
c = apple()
print a,b,c