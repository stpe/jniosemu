# r1 = 2
# r2 = -2
# r3 = 3
# r4 = 0
# r5 = 4
# r6 = 6

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	movi r5, 4
	beq r1, r2, test2
	movi r3, 3
test1:	addi r4, r4, 4
	beq r4, r5, test1
	movi r6, 6
test2:	movi r4, 0
