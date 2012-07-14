'
' Sub testing
'

sub apple
 global a
 a = a + 1
 b = 2
endsub

a = 1
b = 1
c = apple()
print a,b