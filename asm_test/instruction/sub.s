# r1 = 2
# r2 = -2
# r3 = 4
# r4 = -4
# r5 = 2
# r6 = -2

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	sub r3, r1, r2
	sub r4, r2, r1
	sub r5, r1, r0
	sub r6, r2, r0
