# r1 = 0xF00F00FF
# r2 = 0x00FF0FF0
# r3 = 72
# r4 = 212
# r5 = 0xFFF00F00
# r6 = 0xF000FF0F
# r7 = 0xFF00F00F
# r8 = 0x00000000

 .data
 .global main

 .text

main:
	movia r1, 0xF00F00FF
	movia r2, 0x00FF0FF0
	movia r3, 0b01001000
	movia r4, 0b11010100
	ror r5, r1, r3
	ror r6, r2, r3
	ror r7, r1, r4
	ror r8, r0, r4
	


