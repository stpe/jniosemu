	.data
	.global main
	.equ SERIAL, 0x860

	.text
main:	movia r8, SERIAL
input:	ldw r9, 8(r8)
	andi r9, r9, 0b10000000
	beq r9, r0, input
	ldw r10, 0(r8)
output:	ldw r9, 8(r8)
	andi r9, r9, 0b01000000
	beq r9, r0, output
	stw r10, 4(r8)
	br input
