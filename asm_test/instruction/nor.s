# r1 = 0xFFFF0000
# r2 = 0x0000FFFF
# r3 = 0x00000000
# r4 = 0x0000FFFF
# r5 = 0xFFFF0000

 .data
 .global main

 .text

main:
	movia r1, 0xFFFF0000
	movia r2, 0x0000FFFF
	nor r3, r1, r2
	nor r4, r1, r0
	nor r5, r0, r2
