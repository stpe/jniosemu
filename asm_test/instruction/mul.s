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
	mul r5, r1, r3
	mul r6, r1, r4
	mul r7, r2, r3
	mul r8, r2, r4
	mul r9, r3, r4
	mul r10, r4, r4
	mul r11, r3, r3
	
