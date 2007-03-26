	.data
	.global main

	.text
main:	movi r1, 10000
loop:	subi r1, r1, 1
	beq r1, r0, klar
	br loop
klar:	movi r2, 2