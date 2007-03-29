# r2 = 255
# r3 = 6

 .data
test: 	.byte -1
 	.byte 6

 .global main

 .text

main:
	movia r1, test
	ldbu r2, 0(r1)
	ldbu r3, 1(r1)
	
