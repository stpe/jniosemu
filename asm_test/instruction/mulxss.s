# r3 = 4
# r4 = -4
# r5 = 1
# r6 = -1
# r7 = -1
# r8 = 1

 .data
 .global main

 .text

main:
	movia r1, 0x40000000
	movia r2, -0x40000000
	movi r3, 4
	movi r4, -4
	mulxss r5, r1, r3
	mulxss r6, r1, r4
	mulxss r7, r2, r3
	mulxss r8, r2, r4
	
