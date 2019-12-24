'
' This program is used to test the functionality of the
' embedded command select case ... end select
'
v = "1"

' Test select with inner select
select case "1"
case "0": print "1"
case "1": 
  print "2"

  select case "1"
  case "0": print "3"
  case "1": print "4"
  case "2": print "5"
  end select

  print "6"

case "2": print "7"
end select

print "8"
