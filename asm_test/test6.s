	.data
var:	.word 5

	.macro PUSH reg		#
	addi sp, sp, –4
	stw \reg, 0(sp)
	.endm

	.macro POP reg
	ldw \reg, 0(sp)
	addi sp, sp, 4
	.endm

	.global main

	.text
main:	movia	r8, var		# Flyttar adressen till variabeln var till r8
	ldw	r9, 0(r8)	# Flyttar värdet på variabeln var till r9
	addi	r9, r9, 2	# Adderar 2 till värdet på r9
	stw	r9, 0(r8)	# Flyttar värdet på r8 till variabeln var

	PUSH r9			# lägger r9 på stacken
	POP r10			# poppar värdet i stacken till r10
				# Om r9 = r10 så fungerar operationen
				# Kontrollera även att sp har samma värde innan PUSH som efter POP
