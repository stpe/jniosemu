package jniosemu.emulator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.Thread;

import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.emulator.compiler.CompilerException;
import jniosemu.emulator.io.IOManager;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.emulator.Instruction;

/**
 * Managing the emulation
 */
public class EmulatorManager implements EventObserver
{
	/**
	 * Program counter address
	 */
	private int pc = MemoryManager.PROGRAMSTARTADDR;
	/**
	 * EventManager that is used
	 */
	private EventManager eventManager;
	/**
	 * MemoryManager that is used
	 */
	private MemoryManager memory;
	/**
	 * RegisterManager that is used
	 */
	private RegisterManager register;
	/**
	 * IOManager that is used
	 */
	private IOManager io = null;
	/**
	 * Emulator that is used
	 */
	private Emulator emulator;
	/**
	 * True if the emulator is running
	 */
	private boolean running = false;
	/**
	 * True if emulation ended
	 */
	private boolean ended = true;
	/**
	 * Current program
	 */
	private Program program;
	/**
	 * Breakpoints
	 */
	private Hashtable<Integer, Integer> breakpoints = new Hashtable<Integer, Integer>();
	/**
	 * Thread that the emulation runs in
	 */
	private Thread runningThread = null;

	/**
	 * Init EmulatorManager
	 *
	 * @post emulator and eventManager is set and eventlistener is added
	 * @calledby JNiosEmu.main()
	 * @calls Emulator(), EventManager.addEventObserver()
	 */
	public EmulatorManager(EventManager eventManager) {
		this.emulator = new Emulator(this);
		this.eventManager = eventManager;

		EventManager.EVENT[] events = {
			EventManager.EVENT.COMPILER_COMPILE,
			EventManager.EVENT.EMULATOR_PAUSE,
			EventManager.EVENT.EMULATOR_STEP,
			EventManager.EVENT.EMULATOR_RESET,
			EventManager.EVENT.EMULATOR_RUN,
			EventManager.EVENT.EMULATOR_BREAKPOINT_TOGGLE
		};

		eventManager.addEventObserver(events, this);
	}

	/**
	 * Return the PC address
	 *
	 * @calledby Emulator
	 *
	 * @return PC address
	 */
	public int readPC() {
		return this.pc;
	}

	/**
	 * Change PC address
	 *
	 * @calledby Emulator
	 *
	 * @param value  PC address value
	 * @throws EmulatorException  If value modulus 4 is equal to 0
	 */
	public void writePC(int value) throws EmulatorException {
		if (value % 4 != 0)
			throw new EmulatorException("Program counter address must be a multiplier of 4");

		this.pc = value;
	}

	/**
	 * Run instructions as long as there are any or program is stopped
	 *
	 * @calledby update()
	 * @calls startEvent, stopEvent(), step()
	 */
	public void runAll() {
		this.running = true;
		this.startEvent();

		boolean nextInstruction = false;
		int instruction = 0;
		do {
			if (instruction % 1000 == 0)
				this.register.resetState();

			nextInstruction = this.step();

			if (instruction % 1000 == 0)
				this.pcChange();

			instruction++;
		} while (nextInstruction && this.running);

		this.pcChange();

		this.running = false;
		this.stopEvent();
	}

	/**
	 * Run one instruction
	 *
	 * @calledby update()
	 * @calls startEvent, stopEvent(), step()
	 */
	public void runOne() {
		this.running = true;
		this.startEvent();

		this.register.resetState();
		this.step();
		this.pcChange();

		this.running = false;
		this.stopEvent();
	}

	/**
	 * Run next instruction
	 *
	 * @pre program must be loaded
	 * @post update pc to next instruction
	 * @checks If Instruction.run() throws an error send EVENTID_RUNTIME_ERROR
	 * @calledby runAll(), runOne()
	 * @calls MemoryManager.readInt(), InstructionManager.get(), Instruction.run(), pcChange(), EventManager.sendEvent()
	 *
	 * @return True if the emulation can continue
	 */
	public boolean step() {
		int lastPc = this.pc;

		try {
			int opCode = this.memory.readInt(this.pc);
			if (opCode == 0) {
				this.pcChange();
				this.ended = true;
				return false;
			}

			Instruction instruction = InstructionManager.get(opCode);
			instruction.run(this.emulator);
			this.pc += 4;
		} catch (EmulatorException e) {
			this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_ERROR, e.getMessage());
			this.ended = true;
			return false;
		}

		if (this.pc == lastPc || this.ended) {
			this.pc = 0;
			this.pcChange();
			this.ended = true;
			return false;
		}

		return !this.breakpoints.containsKey(this.pc);
	}

	/**
	 * Helpfunction that returns the current MemoryManager
	 *
	 * @calledby Emulator
	 *
	 * @return Current MemoryManager
	 */
	public MemoryManager getMemoryManager() {
		return this.memory;
	}

	/**
	 * Helpfunction that returns the current RegisterManager
	 *
	 * @calledby Emulator
	 *
	 * @return Current RegisterManager
	 */
	public RegisterManager getRegisterManager() {
		return this.register;
	}

	/**
	 * Get the current compiled program
	 *
	 * @return Current program
	 */
	public Program getProgram() {
		return this.program;
	}

	/**
	 * Used for debuggin
	 */
	public void dump() {
		this.register.dump();
		this.memory.dump();
	}

	/**
	 * Compile the sourcecode
	 *
	 * @post Event should be sent. Both if an error has occured or not.
	 * @checks If an error occured during compile or link send COMPILER_ERROR
	 * @calledby update()
	 * @calls Compiler(), Compiler.compile(), Compiler.link(), EventManager.sendEvent(), Program.toggleBreakpoint, load()
	 *
	 * @param lines Sourcecode of the program
	 */
	public void compile(String lines) {
		Compiler compiler = new Compiler(lines);

		this.program = null;
		try {
			compiler.compile();
			this.program = compiler.link();
		} catch (CompilerException e) {
			this.eventManager.sendEvent(EventManager.EVENT.COMPILER_ERROR, e.getMessage());
			return;
		}

		// Add old breakpoints to this program
		for (Integer lineNumber: this.breakpoints.values())
			this.program.toggleBreakpoint(lineNumber.intValue());

		this.load();
	}

	/**
	 * Pause the emulation
	 *
	 * @calledby update()
	 */
	public void pause() {
		this.running = false;
	}

	/**
	 * Reset the emulation
	 *
	 * @calledby update()
	 * @calls RegisterManager(), MemoryManager(), IOManager(), Program.getStartAddr(), EventManager.sendEvent(), pcChange()
	 */
	public void load() {
		this.memory = new MemoryManager(program.getBinaryProgram(), program.getBinaryVariables());
		
		if (this.io == null) {
			this.io = new IOManager(this.memory, this.eventManager);
		} else {
			this.io.reset(this.memory);
		}

		this.pc = this.program.getStartAddr();
		this.register = new RegisterManager();

		this.ended = false;
		this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_READY, this.program);

		this.pcChange();
	}

	public void reset() {
		this.running = false;
		this.ended = false;

		if (this.runningThread != null && this.runningThread.isAlive()) {
			try {
				this.runningThread.join(1000);
			} catch (InterruptedException e) {}

			this.runningThread = null;
		}

		this.load();
	}

	/**
	 * Toggle breakpoint
	 *
	 * @post Add breakpoint to breakpoints and update Program
	 * @calledby update()
	 * @calls Program.toggleBreakpoint(), EMULATOR_BREAKPOINT_TOGGLE
	 *
	 * @param lineNumber  Line to toggle breakpoint
	 */
	public void toggleBreakpoint(int lineNumber) {
		int addr = this.program.getAddress(lineNumber);
		if (this.program.toggleBreakpoint(lineNumber)) {
			this.breakpoints.put(addr, lineNumber);
		} else {
			this.breakpoints.remove(addr);
		}

		this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_BREAKPOINT_TOGGLE, lineNumber);
	}

	/**
	 * Listen for events and acts on them
	 *
	 * @calledby COMPILER_COMPILE, EMULATOR_RUN, EMULATOR_PAUSE, EMULATOR_STEP, EMULATOR_RESET, EMULATOR_BREAK_POINT_TOGGLE
	 * @calls compile(), runAll(), pause(), runOne(), load(), toggleBreakpoint()
	 *
	 * @param eventIdentifier Event identifier
	 * @param obj Argument depending of which event
	 */
	public void update(EventManager.EVENT eventIdentifier, Object obj)
	{
		switch(eventIdentifier) {
			case COMPILER_COMPILE:
				this.compile((String)obj);
				break;
			case EMULATOR_STEP:
				this.runOne();
				break;
			case EMULATOR_RUN:
				this.running = false;
				this.ended = false;

				if (this.runningThread != null && this.runningThread.isAlive()) {
					try {
						this.runningThread.join(1000);
					} catch (InterruptedException e) {}
				}

				this.runningThread = new Thread(new Runnable() {
					public void run() {
						runAll();
					}
				});
				this.runningThread.setPriority(Thread.MIN_PRIORITY);
				this.runningThread.start();
				break;
			case EMULATOR_PAUSE:
				this.pause();
				break;
			case EMULATOR_RESET:
				this.reset();
				break;
			case EMULATOR_BREAKPOINT_TOGGLE:
				this.toggleBreakpoint(((Integer)obj).intValue());
				break;
		}
	}

	/**
	 * If pc is changed this method is called and all events that should be sent are sent.
	 *
	 * @calledby load(), step()
	 */
	private void pcChange() {
		this.eventManager.sendEvent(EventManager.EVENT.PROGRAMCOUNTER_CHANGE, Integer.valueOf(this.pc));
		this.eventManager.sendEvent(EventManager.EVENT.REGISTER_CHANGE, this.register.get());
	}

	/**
	 * Sends event when compilation starts end
	 *
	 * @calledby runAll(), runOne()
	 * @calls EVENTID_EMULATION_START
	 */
	private void startEvent() {
		this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_START);
	}

	/**
	 * Sends event and set status when the emulation ended
	 *
	 * @checks  If program ended send EVENTID_EMULATION_END
	 * @calledby runAll(), runOne()
	 * @calls EVENTID_EMULATION_END, EVENTID_EMULATION_STOP
	 */
	private void stopEvent() {
		if (this.ended) {
			this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_END);
		} else {
			this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_STOP);
		}
	}
	
	public void run()
	{
		
	}
}
