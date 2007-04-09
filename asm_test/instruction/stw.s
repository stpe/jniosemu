# r2 = 0
# r3 = 7
# r4 = 0
# r5 = 7

 .data
test: 	.word -1
 	.word 6

 .global main

 .text

main:
	movia r1, test
	ldw r2, 0(r1)
	ldw r3, 4(r1)
	addi r2, r2, 1
	addi r3, r3, 1
	stw r2, 0(r1)
	stw r3, 4(r1)
	ldw r4, 0(r1)
	ldw r5, 4(r1)
	
