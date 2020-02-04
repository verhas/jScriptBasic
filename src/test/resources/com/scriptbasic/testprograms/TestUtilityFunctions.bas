' test function cdbl
assert("cdbl_1", cdbl(2)=2.0)
assert("cdbl_2", cdbl(2.5)=2.5)
assert("cdbl_3", cdbl("2")=2.0)
assert("cdbl_4", cdbl("2.5")=2.5)

' test function clng
assert("clng_1", clng(2)=2)
assert("clng_2", clng("2")=2)
assert("clng_3", clng(true)=1)

print "DONE"
