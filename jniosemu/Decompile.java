package jniosemu;

import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.InstructionException;
import jniosemu.instruction.emulator.Instruction;

public class Decompile
{
	public static void main(String [] args) {
		// nr_uart_txhex
		// int[] instructions = {0xDEFFFD04, 0xDFC00215, 0xDF000115, 0xD839883A, 0xE1000015, 0xE1000017, 0x00204240, 0xDFC00217, 0xDF000117, 0xDEC00304, 0xF800283A};
		/*
		addi r27, r27, -12
		stw r31, 8(r27)
		stw r28, 4(r27)
		add r28, r27, r0
		stw r4, 0(r28)
		ldw r4, 0(r28)
		call 33033
		ldw r31, 8(r27)
		ldw r28, 4(r27)
		addi r27, r27, 12
		ret
		*/

		// nr_uart_txhex32
		// int[] instructions = {0xDEFFFD04, 0xDFC00215, 0xDF000115, 0xD839883A, 0xE1000015, 0xE0800017, 0x1005D43A, 0x00FFFFC4, 0x1884703A, 0x113FFFCC, 0x2120001C, 0x21200004, 0x00203900, 0xE0800017, 0x00FFFFC4, 0x1884703A, 0x113FFFCC, 0x2120001C, 0x21200004, 0x00203900, 0xDFC00217, 0xDF000117, 0xDEC00304, 0xF800283A};
		/*
		addi r27, r27, -12
		stw r31, 8(r27)
		stw r23, 4(r27)
		add r23, r27, r0
		stw r4, 0(r23)
		ldw r2, 0(r23)
		srai r2, r2, 16
		addi r3, r0, -1
		and r2, r3, r2
		andi r4, r2, -1
		xori r4, r4, -32768
		addi r4, r4, -32768
		# call 32996
		ldw r2, 0(r23)
		addi r3, r0, -1
		and r2, r3, r2
		andi r4, r2, -1
		xori r4, r4, -32768
		addi r4, r4, -32768
		# call 32996
		ldw r31, 8(r27)
		ldw r23, 4(r27)
		addi r27, r27, 12
		ret
		*/

		int[] instructions = {0xDEFFFB04, 0xDFC00415, 0xDF000315, 0xD839883A, 0xE100000D, 0xE0000115, 0xE0800117, 0x10800108, 0x1000181E, 0xE100000F, 0x00C000C4, 0xE0800117, 0x1885C83A, 0x10800124, 0x2085D83A, 0x108003CC, 0xE0800215, 0xE0800217, 0x10800290, 0x1000031E, 0xE0800217, 0x108001C4, 0xE0800215, 0xE0800217, 0x10800C04, 0xE0800215, 0xE1000217, 0x000B883A, 0x00202F80, 0xE0800117, 0x10800044, 0xE0800115, 0x003FE506, 0xDFC00417, 0xDF000317, 0xDEC00504, 0xF800283A};
		/*
		addi r27, r27, -20
		stw r31, 16(r27)
		stw r28, 12(r27)
		add r28, r27, r0
		sth r4, 0(r28)
		stw r0, 4(r28)
		ldw r2, 4(r28)
		cmpgei r2, r2, 4
		bne r2, r0, 96
		addi r3, r0, 3
		ldw r2, 4(r28)
		sub r2, r3, r2
		muli r2, r2, 4
		sra r2, r4, r2
		andi r2, r2, 15
		stw r2, 8(r28)
		ldw r2, 8(r28)
		cmplti r2, r2, 10
		bne r2, r0, 12
		ldw r2, 8(r28)
		addi r2, r2, 7
		stw r2, 8(r28)
		ldw r2, 8(r28)
		addi r2, r2, 48
		stw r2, 8(r28)
		ldw r4, 8(r28)
		add r5, r0, r0
		call 32958
		ldw r2, 4(r28)
		addi r2, r2, 1
		stw r2, 4(r28)
		br -108
		ldw r31, 16(r27)
		ldw r28, 12(r27)
		addi r27, r27, 20
		ret
		*/

		for (int instruction : instructions) {
			// System.out.println(instruction);
			try {
				Instruction ins = InstructionManager.get(instruction);
				System.out.println(ins);				
			} catch (InstructionException e) {}
		}
	}
}