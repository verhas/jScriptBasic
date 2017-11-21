test_string = "123"
If length(test_string) <> 3 Then
  print "string length does not work"
EndIf

if isDefined(length(undef)) then
  print "error undef length is not zero"
endif

bb = byteBuffer(50)
if length(bb) <> 50 then
 print "error bytebuffer length does not work, it is" + length(bb)
endif