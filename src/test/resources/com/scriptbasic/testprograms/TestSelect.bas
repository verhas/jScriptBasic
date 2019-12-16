'
' This program is used in unit testing the jScriptBasic interpreter to test the functionality of the
' command select case ... end select
'
v = "1"

' Test select with case keyword
select case v
case "0": print "0"
case "1": print "1"
case "2": print "2"
end select

' Test expression in select
' test case else statement
select v+"1"
case "1": print "0"
case "2", "11": print "1"
case else: print "2"
end select

' test case else statement usage
select v
case "0": print "0"
case else: print "1"
end select

' test case with to statement
select case v
case "-": print "-"
case "0" to "2": print "1"
case "3": print "3"
end select

' test case with is statement
select case v
case is "0": print "0"
case is "1": print "1"
case is "2": print "2"
end select


print "1"
