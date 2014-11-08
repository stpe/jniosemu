#####################################################################
# test11.s: Test logic operations
#####################################################################
	.data
	.global main
	.text
main:
	movi	r1, 2
	movi	r2, -2
	and	r3, r2, r1		# 2
	andhi	r4, r2, 0x11100111	# 0x11100000
	andi	r5, r2, 0x11100111	# 0x00000110
	and	r6, r4, r5		# 0
	nor	r7, r2, r1		# 0xFFFFFFFD
	or	r8, r4, r5		# 0x11100110
	orhi	r9, r4, r2		# 0xFFFF0000
	ori	r10, r4, r2		# 0x0000FFFE
	xor	r11, r4, r5		# 0x11100110
	xor	r12, r1, r2		# 0xFFFFFFFC
	xorhi	r13, r1, -2		# 0x00000002
	xorhi	r14, r4, -2		# 0x1110FFFE
	xori	r15, r5, -2		# 0x0000FEEE
	xori	r16, r1, -2		# 0x0000FFFD