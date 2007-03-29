# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 0
# r5 = 1
# r6 = 0
# r7 = 0
# r8 = 1
# r9 = 0
# r10 = 0

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	cmpgei r3, r1, 2
	cmpgei r4, r1, -2
	cmpgei r5, r1, 1
	cmpgei r6, r1, 3

        cmpgei r7, r2, 2
        cmpgei r8, r2, -2
        cmpgei r9, r2, 1
        cmpgei r10, r2, 3

	
