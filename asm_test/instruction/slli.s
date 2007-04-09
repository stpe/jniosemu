# r1 = 3
# r2 = 6
# r3 = 12
# r4 = 12

 .data
 .global main

 .text

main:
	movia r1, 0b0011
	movia r2, 0b0110
	slli r3, r2, 1
	slli r4, r1, 2
	


