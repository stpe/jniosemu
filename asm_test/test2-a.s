#####################################################################
# test2-a.s: Simple program that adds two values - with an error
#####################################################################


		.data
tal:		.word	3
		.global main

		.text
main:		movi r1, 5
		movia r2, tal
		load r3, 0(r2)		# Error!

		add r4, r1, r3
