# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 0
# r5 = 1
# r6 = 0
# r7 = 0
# r8 = 1

	.data
	.global main

	.text
main:	movi r1, 2
	movi r2, -2
	cmplti r3, r1, 2
	cmplti r4, r1, -2
	cmplti r5, r1, 3
	cmplti r6, r2, -2
	cmplti r7, r2, -3
	cmplti r8, r2, -1
