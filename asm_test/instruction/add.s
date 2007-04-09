# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 0
# r5 = 2
# r6 = -2

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	add r3, r1, r2
	add r4, r2, r1
	add r5, r1, r0
	add r6, r2, r0
