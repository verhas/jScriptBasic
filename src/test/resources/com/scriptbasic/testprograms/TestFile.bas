
file = open("test.txt","w")
printfln file,"hello"
close file

file = open("test.txt","r")
line = readLine(file)
eof = readLine(file)
close file
assert "eof is not undef",isUndef(eof)

assert "file read was not the same as written", line = "hello"
deleteFile "test.txt"

file = open("test.binary.file","wb")
buffer = byteBuffer(100)
for i=0 to 99
 setByte buffer , i , i
next i
write file, buffer
close file

file = open("test.binary.file","rb")
buffer = read(file,100)
close file

for i=0 to 99
  if i <> getByte(buffer,i) then
    assert("The byte "+i+" is "+getByte(buffer,i),false)
  endif
next i

deleteFile "test.binary.file"

fileList = listFiles(".")
files = ""
for i = 0 to length(fileList)-1
 files = files + fileList[i]
 files = files +","
next i

file = open("file.list.txt","w")
printfln file,files
close file
deleteFile "file.list.txt"
 
a[0] = 1
a[1] = 2
z = length(a)
assert "z has to be two", z = 2 
assert "String length does not work", length("apple") = 5