#####################################################################
# test9.s: Testar mov instruktioner
#####################################################################
	.data
	.global main
	.text
main:   
	movi	r1,  2		# 2
	mov	r2, r1		# 2
	movhi	r3, 0x12345678	# 0x12340000
	movhi	r4, -2		# 0xFFFF0000
	movhi	r5, 2		# 0
	movi	r6, -2		# -2
	movia	r7, 2		# 2
	movia	r8, 0x12345678	# 0x12345678
	movui	r9, -2		# 0x0000FFFE
	movui	r10, 1		# 1