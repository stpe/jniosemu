# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 1
# r5 = 1
# r6 = 0

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	cmpge r3, r1, r1
	cmpge r4, r1, r2
	cmpge r5, r2, r2
	cmpge r6, r2, r1
	
