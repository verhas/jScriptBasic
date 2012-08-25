'
' Sub testing
'

sub apple
 global a
 a = a + 1
 b = 2
 return a
endsub
a = 1
b = 1
c = apple()
print a,b