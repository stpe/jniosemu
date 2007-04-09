# r1 = 0x00FFFFF0
# r2 = 0xFFFFFFFF
# r3 = 0x000FFF00
# r4 = 0x00FF0000
# r5 = 0x000F0000
# r6 = 0x00F00000
# r7 = 0x0FF00000
# r8 = 0x000F0000

 .data
 .global main

 .text

main:
	movia r1, 0x00FFFFF0
	movia r2, 0xFFFFFFFF
	movia r3, 0x000FFF00
	andhi r4, r1, 0xFF
	andhi r5, r2, 0xF
	andhi r6, r1, 0xFF0
	andhi r7, r2, 0xFF0
	andhi r8, r3, 0xFF
