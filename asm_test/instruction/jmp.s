# r2 = 2
# r3 = 6
# r4 = 6
# r5 = 5

 .data
 .global main

 .text

main:	movia r1, test1
	movi r4, 6
	jmp r1
	movi r2, 2
test1: 	addi r3, r3, 3
	beq r3, r4, test2
	jmp r1
test2: 	movi r5, 5
