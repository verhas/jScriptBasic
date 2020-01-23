' Test functions with numeric parameter
if year(0)=1899 then print "1" else print "E1"
if month(0)=12 then print "2" else print "E2"
if day(0)=30 then print "3" else print "E3"

' Test functions with string parameter
d="2020-02-24"
if year(d)=2020 then print "4" else print "E4"
if month(d)=2 then print "5" else print "E5"
if day(d)=24 then print "6" else print "E6"

' Test functions with date parameter
ds=DateSerial(year(d),month(d),day(d))
if year(ds)=2020 then print "7" else print "E7"
if month(ds)=2 then print "8" else print "E8"
if day(ds)=24 then print "9" else print "E9"

' Test date aritmetics
ds2=DateSerial(year(d),month(d),day(d))+1
if day(ds2)=25 then print "a" else print "Ea"
ds3=ds2-1
if day(ds3)=24 then print "b" else print "Eb"

' Compare dates
if ds=ds3 then print "c" else print "Ec"
if ds<>ds2 then print "d" else print "Ed"
if ds<ds2 then print "e" else print "Ee"
if ds2>ds3 then print "f" else print "Ef"
