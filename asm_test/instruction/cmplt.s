# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 1
# r5 = 0
# r6 = 1

	.data
	.global main

	.text
main:	movi r1, 2
	movi r2, -2
	cmplt r3, r1, r2
	cmplt r4, r2, r1
	cmplt r5, r0, r0
	cmplt r6, r0, r1
