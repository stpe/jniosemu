# r1 = 0b0011
# r2 = 0b0110
# r3 = 2
# r4 = 0xFFFFFFFE
# r5 = 0b1100
# r6 = 0b0001

 .data
 .global main

 .text

main:
	movia r1, 0b0011
	movia r2, 0b0110
	movi r3, 2
	movia r4, 0xFFFFFFFE
	sra r5, r1, r3
	sra r6, r2, r4
	


