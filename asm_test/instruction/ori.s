# r1 = 0b0011
# r2 = 0b0110
# r3 = 0b0111
# r4 = 0b0111
# r5 = 0b1010

 .data
 .global main

 .text

main:
	movi r1, 0b0011
	movi r2, 0b0110
	ori r3, r1, 0b0110
	ori r4, r2, 0b0011
	ori r5, r0, 0b1010
