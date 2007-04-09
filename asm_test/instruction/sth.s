# r2 = 0x00004321

 .data
test: 	.word 0x87654321

 .global main

 .text

main:
	movia r1, test
	ldw r2, 0(r1)
	sth r2, 0(r1)
	sth r0, 2(r1)
	ldw r2, 0(r1)
	

	
