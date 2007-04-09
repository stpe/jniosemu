# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 0
# r5 = 2
# r6 = -2

 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
	addi r3, r1, -2
	addi r4, r2, 2
	addi r5, r1, 0
	addi r6, r2, 0
