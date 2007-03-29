	.data
	.global main
	.equ TIMER, 0x820

	.text
main:	movia r8, TIMER
	movi r9, 0x5
	stw r9, 8(r8)
	movi r9, 0x6
	stw r9, 4(r8)
	nop
	nop
	nop
	nop
	nop
	nop
	stw r0, 16(r8)
	ldw r10, 16(r8)
	stw r0, 0(r8)
	ldw r11, 0(r8)
	
