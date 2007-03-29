# r1 = 2
# r2 = -2
# r3 = 2
# r4 = 0
# r5 = 1
# r6 = 1
# r7 = 1

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	movi r3, 2
	cmpeq r4, r1, r2
	cmpeq r5, r1, r3
	cmpeq r6, r1, r1
	cmpeq r7, r2, r2
	
