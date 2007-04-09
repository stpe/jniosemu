# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 10
# r5 = 5
# r6 = 6

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	blt r2, r1, test1
	movi r3, 3
test1:	addi r4, r6, 4
	blt r1, r0, test2
	movi r5, 5
test2:	movi r6, 6
	blt r4, r5, test1
