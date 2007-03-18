	.data
	.global main
	.text
main:   
	movi	r1, 2
	movi	r2, -2
	div	r3, r2, r1		# -1
	divu	r4, r2, r1		# 0x7FFFFFFF
	mul	r5, r1, r2		# -4
	mul	r6, r2, r0		# 0
	muli	r7, r1, 0xFFFFFFFF	# 0xFFFFFFFE
	sub	r8, r1, r2		# 4
	sub	r9, r1, r0		# 2
	subi	r10, r1, 2		# 0
	add	r11, r1, r2		# 0
	addi	r12, r1, 0xFFFFFFFF	# 1