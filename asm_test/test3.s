#####################################################################
# test3.s: Oändlig loop
#####################################################################

		.data
		
		.global main
		.text

main:		movi r4, 0 	# Flytta 0 till register r4
		
	
add:		addi r4, r4, 1	# Addera 1 till värdet i r4 och skriv det till r4
		br add		# Hoppa tillbaka till r4 (oändlig loop)
