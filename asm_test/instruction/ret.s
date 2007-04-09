# r1 = 2
# r2 = 2
# r3 = 3


 .data
 .global main

 .text

main:
	movi r1, 2
	movi r2, -2
test1:	call sub
	beq r1, r2, test2
	br test1

sub:	addi r2, r2, 1
	ret

test2:	movi r3, 3
