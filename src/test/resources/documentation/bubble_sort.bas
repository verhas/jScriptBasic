a[0] = 3
a[1] = 2
a[2] = 7
a[3] = -1
a[4] = 58
a[5] = 0
a[6] = 99

for i=0 to length(a)-1
for j=0 to length(a)-1
 if a[i] < a[j] then
   x = a[i]
   a[i] = a[j]
   a[j] = x
 endif
next j
next i

for i=0 to length(a)-1
  print a[i],"\n"
next i