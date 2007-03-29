# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 4
# r5 = 0
# r6 = 6
# r7 = 7

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	bne r1, r2, test1
	movi r3, 3
test1:	movi r4, 4
	bne r0, r5, test2
	movi r6, 6
test2:	movi r7, 7
