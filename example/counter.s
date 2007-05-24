###############################################
# Example: counter.s
# Countdown from 10000 to 0
###############################################

		.data
		.equ STARTVALUE, 10000

counter:	.word STARTVALUE
word:		.asciz "Test string"

		.global main
		.text
main:		movia r8, counter
		ldw r9, 0(r8)
loop:		subi r9, r9, 1
		bne r9, r0, loop
		stw r9, 0(r8)
