# r1 = -1
# r2 = 6
# r3 = 4
# r4 = 66
# r5 = 0x0FFFFFFF
# r6 = 1

 .data
 .global main

 .text

main:
	movia r1, -1
	movia r2, 0b0110
	movia r3, 4
	movia r4, 66
	srl r5, r1, r3
	srl r6, r2, r4
	


