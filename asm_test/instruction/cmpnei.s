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
	cmpnei r4, r1, -2
	cmpnei r5, r1, 2
	cmpnei r6, r1, 2
	cmpnei r7, r2, -2
	
