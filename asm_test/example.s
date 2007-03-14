#####################################################################
# example.s: Program för test1
#####################################################################

.data
A: .word 0
B: .word 0
C: .word 0

.text
MOVIA r8, A 		#adressen A kopieras till reg r8
LDW r9, 0(r8) 		#värdet A kopieras till reg r9
MOVIA r8, B 		#adressen B kopieras till reg r8
LDW r10, 0(r8) 		#värdet B kopieras till reg r10
ADD r11, r9, r10 	#r11 <= r9 + r10
MOVIA r8, C 		#adressen C kopieras till reg r8
STW r11, 0(r8) 		#resultatet i r11 kopieras 
			#till minnet på adress C
