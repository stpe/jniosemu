###############################################
# Example: ledadv.s
# Light led when changing dipswitch or pressing
# a button. If both the dipswitch is changed
# and button pressed the led is turned off
###############################################

		.data
		.equ LEDADDR, 0x810
		.equ BUTTONADDR, 0x840
		.equ DIPSWITCHADDR, 0x850

		.global main
		.text
main:		movia r12, LEDADDR
		movia r13, DIPSWITCHADDR
		movia r14, BUTTONADDR

loop:		ldb r8, 0(r13)
		ldb r9, 0(r14)
		xor r8, r8, r9
		stb r8, 0(r12)
		br loop