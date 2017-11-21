# ScriptBasic for Java Arrays

The language implemented by ScriptBasic for Java can handle arrays. The arrays are
allocated on the fly and are indexed from zero up to their limits. If you try to index
an array above the actual limit the array is automatically extended.

The following program creates an array and sorts it with the simplest possible bubble sort:
 
```
	              a[0] = 3
	              a[1] = 2
	              a[2] = 7
	              a[3] = -1
	              a[4] = 58
	              a[5] = 0
	              a[6] = 99
	              
	              for i=0 to length(a)-1
	              for j=0 to length(a)-1
	               if a[i] < a[j] then
	                 x = a[i]
	                 a[i] = a[j]
	                 a[j] = x
	               endif
	              next j
	              next i
	              
	              for i=0 to length(a)-1
	                print a[i],"\n"
	              next i
```
 
 For safety reasons you can not use a variable to store a non array value that
 already stores an array.
 For example the following code will result error:
 
```
			' This code will not work
			a[13] = 2
			a = 55
```

 This will prevent programming errors and avoid some misbehaving scripts.
 On the other hand you can use a variable to hold an array that was storing some value before:
 
```
			' This code works fine
			a = 55
			a[13] = 2
```

 The only value you can assign to a variable that is already an array is the undefined value. To
 do that is very simple, you can write
 
```
			' This code will work assuming that
			' the variable 'undef' does not have any value
			a[13] = 2
			a = undef
			a = 55
```

 Note that the word `undef` is not a reserved word, or a special constant. It is simply a
 variable that was not used before, and no value was assigned to it, therefore its value is undefined.
 
 The length of an array can be retrieved using the function `length()`. Note that arrays are
 indexed from zero up. This means that the index values that hold meaningful values are from `0` to
 `length(array)-1`. When you use an index that is larger that `length(array)-1` the array will
 automatically be extended and all undefined elements will hold the undefined value. Therefore it is very
 easy to allocate really great amount of memory in ScriptBasic for Java using some large index values when
 setting or reading array elements.
 