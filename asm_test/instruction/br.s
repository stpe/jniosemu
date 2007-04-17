# r1 = 1
# r2 = 2
# r3 = 3
# r4 = 0
# r5 = 5
# r6 = 6


 .data
 .global main

 .text

main:
	movi r1, 1
	movi r2, 2
	br 0
test1:	movi r3, 3
	br test3
	movi r4, 4
test2:	movi r5, 5
	br 4
test3: br -12
test4: movi r6, 6