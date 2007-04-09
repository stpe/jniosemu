# r1 = 0x0F0F0F0F
# r2 = 0x0000FFFF
# r3 = 0xF0F00F0F
# r4 = 0x0F0FFFFF
# r5 = 0x00FF0000

 .data
 .global main

 .text

main:
	movia r1, 0x0F0F0F0F
	movia r2, 0x0000FFFF
	xorhi r3, r1, 0xFFFF
	xorhi r4, r2, 0x0F0F
	xorhi r5, r0, 0x00FF
