# r1 = 2
# r2 = -2
# r3 = 2
# r4 = 1
# r5 = 0
# r6 = 0
# r7 = 0

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	movi r3, 2
	cmpne r4, r1, r2
	cmpne r5, r1, r3
	cmpne r6, r1, r1
	cmpne r7, r2, r2
	
