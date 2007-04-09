# r1 = 2
# r2 = -2
# r3 = 0
# r4 = 1
# r5 = 1
# r6 = 0
# r7 = 0
# r8 = 0

	.data
	.global main

	.text
main:	movi r1, 2
	movi r2, -2
	cmpltui r3, r1, 2
	cmpltui r4, r1, -2
	cmpltui r5, r1, 3
	cmpltui r6, r2, -2
	cmpltui r7, r2, -3
	cmpltui r8, r2, -1
