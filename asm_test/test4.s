#####################################################################
# test4.s: Tänder två led
#####################################################################

	.data
	.global main
	
	.text

main:	
	movia		r8, 0x850		# Flytta dipswitch adressen till register r8
	ldw			r9, 0(r8)		# Läs in från minnet till r9
	movia		r8, 0x810		# Flytta led adressen till register r8
	stw			r9, 0(r8)		# Skriv det binära värdet till minnet (tänd lampor)
	
	
