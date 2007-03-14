	.data
	.global main
	
	.text

main:	movia 	r8, 0x810	# Flytta adressen till register r8
	movi	r9, 0b1010	# Bestäm vilka leds som ska vara tända
	stw	r9, 0(r8)	# Skriv det binära värdet till minnet (tänd lampor)
	
	
