		.data
sec:		.word 5
		.global main
		.macro BIGNOP
		nop
		nop
		nop
		nop
		.endm
		.text
subcall:	movi r2, 2
		ret

main:		movi r1, 1
		BIGNOP
		call subcall
		movi r3, 3
		movia r4, sec
		ldw r5, 0(r4)
		blt r3, r5, hopp
		movi r6, 6
hopp:		movi r7, 7
