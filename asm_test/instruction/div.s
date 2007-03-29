# r1 = 2
# r2 = -2
# r3 = -1
# r4 = 1
# r5 = -1
# r6 = -1
# r7 = 2
# r8 = 0
# r9 = 0
# r10 = 5

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	movi r3, -1
	movi r4, 1
	div r5, r1, r2
	div r6, r2, r1
	div r7, r1, r4
	div r8, r4, r1
	div r9, r0, r1
	movi r10, 5
	div r10, r1, r0
	movi r10, 10
