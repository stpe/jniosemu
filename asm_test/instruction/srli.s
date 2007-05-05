# r1 = -1
# r2 = 0b0110
# r3 = 0x0FFFFFFF
# r4 = 0b0001

 .data
 .global main

 .text

main:
	movia r1, -1
	movia r2, 0b0110
	srli r3, r1, 0b00100
	srli r4, r2, 0b00010
	


