# r1 = 0x20004
# r2 = 0x20018

 .data
 .global main

 .text

main:
	nextpc r1
	nop
	nop
	nop
	nop
	nextpc r2
