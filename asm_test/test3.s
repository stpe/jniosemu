#####################################################################
# test3.s: Never ending loop
#####################################################################

		.data

		.global main
		.text

main:		movi r4, 0 	# Move 0 to register r4


add:		addi r4, r4, 1	# Add 1 to value in r4 and write it to r4
		br add		# Branch back to r4 (never ending loop)
