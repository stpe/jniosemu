# r1 = 12
# r2 = 6
# r3 = 3
# r4 = 1

 .data
 .global main

 .text

main:
	movia r1, 0b1100
	movia r2, 0b0110
	srai r3, r1, 2
	srai r4, r2, 66
	


