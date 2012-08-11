'
' Thisprogram demonstrates that the loop start value, end value and the step value is evaluated when the
' loop starts
'

startV = 1.2
endV = 3.4
stepV = 0.1
count = 0
for d=startV to endV step stepV
  count = count + 1
  stepV = stepV-0.1
next
print count
