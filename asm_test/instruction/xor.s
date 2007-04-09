# r1 = 0x0F0F0F0F
# r2 = 0x0000FFFF
# r3 = 0x0F0FF0F0
# r4 = 0x0F0FF0F0
# r5 = 0x0000FFFF

 .data
 .global main

 .text

main:
	movia r1, 0x0F0F0F0F
	movia r2, 0x0000FFFF
	xor r3, r1, r2
	xor r4, r2, r1
	xor r5, r0, r2
