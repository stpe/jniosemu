# r3 = 4
# r4 = -4
# r5 = 1
# r6 = 0x3FFFFFFF
# r7 = -1
# r8 = 0xC0000001

 .data
 .global main

 .text

main:
	movia r1, 0x40000000
	movia r2, -0x40000000
	movi r3, 4
	movi r4, -4
	mulxsu r5, r1, r3
	mulxsu r6, r1, r4
	mulxsu r7, r2, r3
	mulxsu r8, r2, r4
	
