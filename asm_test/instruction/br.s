# r1 = 2
# r2 = 2
# r3 = 3


 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, 0
	br test1
test1:	beq r1, r2, test2
	movi r2, 2
	br test1
test2:	movi r3, 3
