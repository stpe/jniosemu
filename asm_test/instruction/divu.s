# r1 = 2
# r2 = -2
# r3 = -1
# r4 = 1
# r5 = 0
# r6 = 2147483647
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
	divu r5, r1, r2
	divu r6, r2, r1
	divu r7, r1, r4
	divu r8, r4, r1
	divu r9, r0, r1
	movi r10, 5
	divu r10, r1, r0
	movi r10, 10
