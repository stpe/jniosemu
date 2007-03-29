# r1 = 2
# r2 = -2
# r3 = 1
# r4 = 0
# r5 = 0
# r6 = 1

	.data
	.global main

	.text
main:	movi r1, 2
	movi r2, -2
	cmpltu r3, r1, r2
	cmpltu r4, r2, r1
	cmpltu r5, r0, r0
	cmpltu r6, r0, r1
