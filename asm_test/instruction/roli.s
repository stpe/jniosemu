# r1 = 0xF00F00FF
# r2 = 0x00FF0FF0
# r3 = 72
# r4 = 212
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
	roli r5, r1, 0b00001000
	roli r6, r2, 0b00001000
	roli r7, r1, 0b00000100
	roli r8, r0, 0b00010100
	


