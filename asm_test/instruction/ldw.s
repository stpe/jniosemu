# r2 = -1
# r3 = 6

 .data
test: 	.word -1
 	.word 6

 .global main

 .text

main:
	movia r1, test
	ldw r2, 0(r1)
	ldw r3, 1(r1)
	
