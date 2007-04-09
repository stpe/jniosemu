# r1 = 0xF
# r2 = 0xFF
# r3 = 0xFF0
# r4 = 0xF
# r5 = 0xF
# r6 = 0
# r7 = 0xF0
# r8 = 0xF0

 .data
 .global main

 .text

main:
	movia r1, 0xF
	movia r2, 0xFF
	movia r3, 0xFF0
	and r4, r1, r2
	and r5, r2, r1
	and r6, r1, r3
	and r7, r2, r3
	and r8, r3, r2
