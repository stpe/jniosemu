# r1 = 0xFFFF0000
# r2 = 0x0000FFFF
# r3 = 0xFFFFFFFF
# r4 = 0xFFFF0000
# r5 = 0x0000FFFF

 .data
 .global main

 .text

main:
	movi r1, 0xFFFF0000
	movi r2, 0x0000FFFF
	or r3, r1, r2
	or r4, r1, r0
	or r5, r0, r2
