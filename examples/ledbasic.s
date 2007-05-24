###############################################
# Example: ledbasic.s
# Animate the led by turning on one led at a
# time
###############################################

		.data
		.equ LEDADDR, 0x810

		.global main
		.text
main:		movia r8, 0x11111111
		movia r9, LEDADDR
loop:		stb r8, 0(r9)
		roli r8, r8, 1
		br loop