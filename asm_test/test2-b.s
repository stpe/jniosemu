#####################################################################
# test2-b.s: Enkelt program som lägger ihop två tal med ett fel
#####################################################################


		.data
tal:		.word	3
		.global main

		.text
main:		movi r1, 5
		movia r2, tal
		load r3, 0(r2)		# Felaktig instruktion

		add r4, r1, r3
