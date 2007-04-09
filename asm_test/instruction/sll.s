# r1 = 3
# r2 = 6
# r3 = 1
# r4 = 2
# r5 = 6
# r6 = 24

 .data
 .global main

 .text

main:
	movia r1, 0b0011
	movia r2, 0b0110
	movi r3, 1
	movi r4, 2
	sll r5, r1, r3
	sll r6, r2, r4
	


