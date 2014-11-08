#####################################################################
# example.s: Program for test1
#####################################################################

.data
A: .word 0
B: .word 0
C: .word 0

.text
MOVIA r8, A 		#address A copied to reg r8
LDW r9, 0(r8) 		#value A copied to reg r9
MOVIA r8, B 		#address B copied to reg r8
LDW r10, 0(r8) 		#value B copied to reg r10
ADD r11, r9, r10 	#r11 <= r9 + r10
MOVIA r8, C 		#address C copied to reg r8
STW r11, 0(r8) 		#result in r11 copied
			        #to memory on address C
