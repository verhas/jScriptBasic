' Test ltrim, trim, rtrim functions
v = 1234
assert("ltrim_with_num", ltrim(v)="1234")
assert("trim_with_num", trim(v)="1234")
assert("rtrim_with_num", rtrim(v)="1234")

v = " 1234 "
assert("ltrim", ltrim(v)="1234 ")
assert("trim", trim(v)="1234")
assert("rtrim", rtrim(v)=" 1234")

' Test left, mid, right functions
v = 1234
assert("left_with_num", left(v,2)="12")
assert("mid_with_num", mid(v,2,3)="234")
assert("right_with_num", right(v,2)="34")
v = "0123456789"
PRINT left(v,2)
PRINT RIGHT(v,2)
PRINT Mid(v,2,3)

assert("strrev",strreverse("abc")="cba")
