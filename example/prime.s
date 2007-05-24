#       TestNextPrime (tested OK 13-DEC-2005/JWd)
#	This is a test-program that calls
#	subroutines NextPrime and hex2dec
#	NextPrime gets an integer i r4 
#	and returns the nearest bigger prime in r2
#	hex2dec translates content of r4 from hexadecimal
#	and prints as decimals using "putchar"

.equ    uart0, 0x860
.globl	main
.globl	TestNextPrime

.macro	push reg
	subi	sp, sp, 4
	stw	\reg, 0(sp)
.endm

.macro	pop reg
	ldw \reg, 0(sp)
	addi sp, sp, 4
.endm

.macro	putreg	reg
	mov	r4, \reg
	call	hexasc
	mov	r4, r2
	movia	r5, uart0
	call	nr_uart_txchar
.endm

	.text

###################################
# HEXASC
# In: r4, Ut: r2, Förstör: -
###################################

hexasc:	PUSH	r15
	andi	r2, r4, 0x0F		# maska fram 4 LSBits i returreg
	cmplei	r15, r2, 0x09		# r16 := 1 om 0..9
	beq	r15, r0, AtoF		# hoppa om 10-15
num:	addi	r2, r2, 0x30		#
	br	home
AtoF:	addi	r2, r2, 0x37


home:	POP	r15
	ret


###################################
# PRTEXT
# In: r4, Ut: r2, Förstör: r5, r10
###################################

prtext:	push	ra
	push	r8
	mov	r10, r4		# copy pointer
baa:	ldb	r4, 0(r10)	# load one char
	beq	r4, r0, foo	# jump if NUL
	movia	r5, uart0
	call	nr_uart_txchar	# print one char
	addi	r10, r10, 1	# point to next char
	br	baa		# next loop
foo:	pop	r8
	pop	ra
	ret			# return	

###################################
# MAIN
# Uses: r16 to r23 as a Caller
###################################
		
	.data
text1:	.asciz	"Primtal nr "
text2:	.asciz	" anses vara: "

	.text

main:   movia   r16, 1000       # start value in r16
	movia   r17, 0          # Number_of_Primes in r17

maa:    mov     r4, r16         # r16 <- NextPrime(r16)
	call	NextPrime
	mov     r16, r2
	addi    r17, r17, 1     # r17++



	movia	r4, text1
	# Skriv ut: "Primtal nr "
 
	call	prtext
	mov     r4, r17         # Skriv ut: 
	call	hex2dec
	movia	r4, text2	# Skriv ut: " anses vara "
	call	prtext
	mov     r4, r16         # Skriv ut: 
	call	hex2dec	
	movia	r5, uart0	# Skriv ut: "\n"
	movi	r4, 0xa		#
	call	nr_uart_txchar	#
	movia	r5, uart0	#
	movi	r4, 0xd		#
	call	nr_uart_txchar	#

	#movia   r4,100
	#call	delay

	br	maa

	ret


###################################
# MUL10
# In: r4, Ut: r2=10*r4, Förstör: -
###################################

mul10:	slli	r2, r4, 3	# 8A i r2
	add	r2, r2, r4	# 9A i r2
	add	r2, r2, r4	# 10 A i r2
	ret

###################################
# HEX2DEC
# In: r4, Ut: -, Förstör: ?
###################################

hex2dec:PUSH	r31		# save
	PUSH	r8
	PUSH	r10
	PUSH	r11
	PUSH	r12

	mov	r8, r4		# r8 = TALET IFRÅGA

	mov	r12, r0		# Ide: r12 = ANTALET SIFFROR i r8


	movi	r4, 1		# Ide: push 1, 10, 100, ..., 10^N
push10:	push	r4		# push next 10-potens
	addi	r12, r12, 1	# dvs vi har en siffra till
	call	mul10		# nästa 10-potens
	mov	r4, r2		# till r4
	ble	r4, r8, push10	# större än själva talet?
	
pop10:	pop	r10		# pop största 10-potens
	mov	r11, r0		# Ide: beräkna siffran i motsvarande position
sub10:	blt	r8, r10, done10	# while (digit>0)
	addi	r11, r11, 1	#   cnt++
	sub	r8, r8, r10	#   digit--
	br	sub10		
done10:	putreg	r11		# cout << digit
	subi	r12, r12, 1	# hoppa till nästa siffra (lägra 10-potens)
	bgt	r12, r0, pop10

	POP	r12		# restore
	POP	r11
	POP	r10
	POP	r8		
	POP	r31
	ret

###################################
# NextPrime
# In: r4, Ut: r2, Fvrstvr: r8, r9, r10, r11
###################################


NextPrime:
	mov	r8, r4

# increment to next odd integer
Pr1:	addi	r8, r8, 1	# addera 1
	andi	r9, r8, 1	# maska fram uddabiten
	bne	r9, r0, fyy
	addi	r8, r8, 1	# addera vid behov en till

fyy:	mov	r9, r8		# kopia till r9
	srli	r9, r9, 1	# dividera med 2, MAX
	movi	r10, 2		# DIV ges startvärde

Pr2:	mov	r11, r8		# Rem := TAL
Pr3:	sub	r11, r11, r10
	beq	r11, r0, Pr1	# REM=0 means no prime
	bgt	r11, r0, Pr3	# REM>0 means continue

	addi	r10, r10, 1	# increment DIV by 1
	blt	r10, r9, Pr2	# br if DIV < MAX

	mov	r2, r8		# return Prime in r2	
	ret

###################################
# DELAY
# In: r4, Förstör: -
###################################

delay:	PUSH	r8
	PUSH	r9
	PUSH	r10
	
	movia	r10, 16200
	mov	r8, r0	
dwait:	mov	r9, r0

dloop:	addi	r9, r9, 1
	blt	r9, r10, dloop

	addi	r8, r8, 1
	blt	r8, r4, dwait

	POP	r10
	POP	r9
	POP	r8
	ret
###################################
