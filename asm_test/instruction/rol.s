# r1 = 0xF00F00FF
# r2 = 0x00FF0FF0
# r3 = 0b01001000
# r4 = 0b11010100
# r5 = 0x0F00FFF0
# r6 = 0xFF0FF000
# r7 = 0x00F00FFF
# r8 = 0x00000000

 .data
 .global main

 .text

main:
	movia r1, 0xF00F00FF
	movia r2, 0x00FF0FF0
	movia r3, 0b01001000
	movia r4, 0b11010100
	rol r5, r1, r3
	rol r6, r2, r3
	rol r7, r1, r4
	rol r8, r0, r4
	


