		.data
tal:		.word	3
		.global main

		.text
main:		movi r1, 5
		movia r2, tal
		load r3, 0(r2)

		add r4, r1, r3