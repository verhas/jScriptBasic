' test function cdbl
assert("cdbl_1", cdbl(2)=2.0)
assert("cdbl_2", cdbl(2.5)=2.5)
assert("cdbl_3", cdbl("2")=2.0)
assert("cdbl_4", cdbl("2.5")=2.5)

' test function clng
assert("clng_1", clng(2)=2)
assert("clng_2", clng("2")=2)
assert("clng_3", clng(true)=1)

' test function chr
assert("chr_1", chr(65)="A")
assert("chr_2", chr("65")="A")

' test function asc
assert("asc_1", asc("A")=65)
assert("asc_2", asc(2)=50)
assert("asc_3", asc("ABCD")=65)

' test date conversion
d = CDate("2020-01-31")
assert("cdbl_5", cdbl(d)=clng(d))

print "DONE"
