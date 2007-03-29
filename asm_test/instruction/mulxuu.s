# r3 = 4
# r4 = -4
# r5 = 1
# r6 = 0x3FFFFFFF
# r7 = 3
# r8 = 0xBFFFFFFD

 .data
 .global main

 .text

main:
	movia r1, 0x40000000
	movia r2, -0x40000000
	movi r3, 4
	movi r4, -4
	mulxuu r5, r1, r3
	mulxuu r6, r1, r4
	mulxuu r7, r2, r3
	mulxuu r8, r2, r4
	
