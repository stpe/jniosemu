# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 0
# r5 = 1
# r6 = 0

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	cmpeqi r3, r1, 2
	cmpeqi r4, r1, -2
	cmpeqi r5, r2, 2
	cmpeqi r6, r2, -2
	
