' Test that a variable can not be used as normal variable if it already contains array value
A = 13
A = undef
A[5] = 13
