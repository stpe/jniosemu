# r2 = -1
# r3 = 6

 .data
test: 	.byte -1
 	.byte 6

 .global main

 .text

main:
	movia r1, test
	ldb r2, 0(r1)
	ldb r3, 1(r1)
	
