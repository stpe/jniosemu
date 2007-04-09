# r1 = 3
# r2 = 6
# r3 = 1
# r4 = 1
# r5 = 3
# r6 = 0

 .data
 .global main

 .text

main:
	movi r1, 3
	movi r2, 6
	movi r3, 1
	sra r4, r1, r3
	sra r5, r2, r3
	sra r6, r2, r1
	


