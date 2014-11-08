#####################################################################
# test5.s: Program that reads/writes stack and memory
#####################################################################

	.data
var:	.word 5

	.macro PUSH reg		# Macro for PUSH
	addi sp, sp, -4
	stw \reg, 0(sp)
	.endm

	.macro POP reg		# Macro for POP
	ldw \reg, 0(sp)
	addi sp, sp, 4
	.endm

	.global main

	.text
main:	movia	r8, var		# Move adress of variable var to r8
	ldw	r9, 0(r8)	# Move value of variable var to r9
	addi	r9, r9, 2	# Add 2 to value of r9
	stw	r9, 0(r8)	# Move value of r8 to variable var

	PUSH r9			# pushes r9 on stack
	POP r10			# pops stack and puts it in r10
				# If r9 equals r10 this will work
				# Also verify that sp has same value before PUSH as after POP
