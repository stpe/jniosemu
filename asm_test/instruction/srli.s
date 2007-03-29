# r1 = -1
# r2 = 0b0110
# r3 = 0xFFFFFFF0
# r4 = 0b0001

 .data
 .global main

 .text

main:
	movia r1, -1
	movia r2, 0b0110
	srl r3, r1, 4
	srl r4, r2, 0xFFFFFFFE
	


