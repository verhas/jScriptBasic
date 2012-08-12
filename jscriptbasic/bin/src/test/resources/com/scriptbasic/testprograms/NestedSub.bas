sub outer

  print "outer\n"
  
  sub inner
    print "inner\n"
  endsub
  
  print "outer\n"
  
endsub

print "global\n"

call inner

print "global\n"
