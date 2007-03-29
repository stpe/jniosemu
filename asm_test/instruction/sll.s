# r1 = 0b0011
# r2 = 0b0110
# r3 = 2
# r4 = -2
# r5 = 0b1100
# r6 = 0b0001

 .data
 .global main

 .text

main:
	movia r1, 0b0011
	movia r2, 0b0110
	movi r3, 2
	movi r4, -2
	sll r5, r1, r3
	sll r6, r2, r4
	


