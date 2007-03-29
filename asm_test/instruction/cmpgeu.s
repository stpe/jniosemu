# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 0
# r5 = 1
# r6 = 1

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	cmpgeu r3, r1, r1
	cmpgeu r4, r1, r2
	cmpgeu r5, r2, r2
	cmpgeu r6, r2, r1
	
