startV = 1
endV = 22
stepV = 1
count = 0
for d=startV to endV step stepV
  count = count + 1
  stepV = stepV + 1
  endV = 2
next
print count