sub abs(a)
  if a < 0 then
    return -a-0.0
  endif
  return a+0.0
endsub  
a = -1
b = abs( a )
print b, acos(0.0)