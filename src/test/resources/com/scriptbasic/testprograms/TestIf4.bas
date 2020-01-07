'
' This program is used in unit testing the jScriptBasic interpreter to test the functionality of the
' single line if-then-else
'
print "1"
If True Then if False then print "2" else print "3"
If True Then if False then print "4" else print "5" else print "6"
If False Then if False then print "7" else print "8" else print "9"
print "10"
