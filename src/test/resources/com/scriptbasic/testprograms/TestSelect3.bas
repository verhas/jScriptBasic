'
' This program is used to test the functionality 
' of select with recursive call
'

sub calculate(n)
  let result = 0
  select n
  case 0: result = 0
  case 1: result = 1
  case else
    result = calculate(n-1)+ calculate(n-2)
  end select
  return result
endsub

print calculate(0)
print " "
print calculate(1)
print " "
print calculate(2)
print " "
print calculate(3)
print " "
print calculate(4)
print " "
print calculate(5)
print " "
print calculate(6)
