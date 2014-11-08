#####################################################################
# test6.s: Testing register and branching
#####################################################################

		.data
		.global main

		.text
main:		movi r1, 1
		movi r2, 2
		blt r1, r2, hoppA
		movi r3, 3
hoppB:		br end
		movi r4, 4
hoppA:		movi r2, 0
		blt r2, r1, hoppB

end:		addi r1, r1, 0