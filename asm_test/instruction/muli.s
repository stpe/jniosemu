# r3 = 4
# r4 = -4
# r5 = 0
# r6 = 0
# r7 = 0
# r8 = 0
# r9 = -16
# r10 = 16
# r11 = 16

 .data
 .global main

 .text

main:
	movia r1, 0x40000000
	movia r2, -0x40000000
	movi r3, 4
	movi r4, -4
	muli r5, r1, 4
	muli r6, r1, -4
	muli r7, r2, 4
	muli r8, r2, -4
	muli r9, r3, -4
	muli r10, r4, -4
	muli r11, r3, 4
	
