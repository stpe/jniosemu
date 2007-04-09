# r1 = 0x0F0F0F0F
# r2 = 0x0000FFFF
# r3 = 0x0F0FF0F0
# r4 = 0x0000F0F0
# r5 = 0x000000FF

 .data
 .global main

 .text

main:
	movia r1, 0x0F0F0F0F
	movia r2, 0x0000FFFF
	xori r3, r1, 0xFFFF
	xori r4, r2, 0x0F0F
	xori r5, r0, 0x00FF
