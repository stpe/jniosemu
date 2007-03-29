# r1 = 2
# r2 = -2
# r3 = 3
# r4 = 10
# r5 = 4
# r6 = 6

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	bgeu r1, r2, test1
	movi r3, 3
test1:	addi r4, r6, 4
	bgeu r0, r1, test2
	movi r5, 4
test2:	movi r6, 6
	bgeu r5, r4, test1
