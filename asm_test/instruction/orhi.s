# r1 = 0xF00F00FF
# r2 = 0x0F0F0F0F
# r3 = 0xF0FF00FF
# r4 = 0x0FFF0F0F
# r5 = 0x00F00000

 .data
 .global main

 .text

main:
	movia r1, 0xF00F00FF
	movia r2, 0x0F0F0F0F
	orhi r3, r1, 0xFF
	orhi r4, r2, 0xFF
	orhi r5, r0, 0x0F0
