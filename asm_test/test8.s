#####################################################################
# test8.s: Testing compare operations
#####################################################################
	.data
	.global main
	.text
main:
	movi	r1,  1		# 1
	movi	r2,  -2		# -2
	cmpeq	r3, r0, r1	# 0
	cmpeqi	r4, r1, 1	# 1
	cmpge	r5, r0, r1	# 0
	cmpgei	r6, r1, 1	# 1
	cmpgeu	r7, r2, r1	# 1
	cmpgeui	r8, r2, -1	# 0
	cmpgt	r9, r1, r2	# 1
	cmpgti	r10, r0, -1	# 1
	cmpgtu	r11, r2, r1	# 0
	cmpgtui	r12, r2, -1	# 0
	cmple	r13, r2, r1	# 1
	cmplei	r14, r0, -1	# 0
	cmpleu	r15, r2, r1	# 0
	cmpleui	r16, r1, 1	# 1
	cmplt	r17, r2, r0	# 1
	cmplti	r18, r0, 0	# 0
	cmpltu	r19, r1, r0	# 0
	cmpltui	r20, r2, 0	# 0
	cmpne	r21, r2, r1	# 1
	cmpnei	r22, r2, -2	# 0
