# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 0
# r5 = 0
# r6 = 1
# r7 = 1
# r8 = 1

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	cmpgeui r3, r1, 2
	cmpgeui r4, r1, -2
	cmpgeui r5, r1, 3
	cmpgeui r6, r2, 2
	cmpgeui r7, r2, -2
	cmpgeui r8, r2, 3
	
