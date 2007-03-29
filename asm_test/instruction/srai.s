# r1 = 0b0011
# r2 = 0b0110
# r3 = 0b1100
# r4 = 0b0001

 .data
 .global main

 .text

main:
	movia r1, 0b0011
	movia r2, 0b0110
	srai r3, r1, 2
	srai r4, r2, 0xFFFFFFFE
	


