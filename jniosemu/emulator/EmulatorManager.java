package jniosemu.emulator;

import java.util.Hashtable;
import jniosemu.emulator.compiler.Compiler;
import jniosemu.emulator.compiler.CompilerException;
import jniosemu.emulator.memory.MemoryBlock;
import jniosemu.emulator.memory.MemoryException;
import jniosemu.emulator.memory.MemoryManager;
import jniosemu.emulator.register.RegisterManager;
import jniosemu.events.EventManager;
import jniosemu.events.EventObserver;
import jniosemu.instruction.InstructionManager;
import jniosemu.instruction.emulator.Instruction;

/** Managing the emulation */
public class EmulatorManager implements EventObserver {
  /** The different speed that is possible to run the emulator in */
  public static enum SPEED {
    SLOW,
    NORMAL,
    FAST,
    ULTRA
  };
  /** Program counter address */
  private int pc = MemoryManager.PROGRAMSTARTADDR;
  /** EventManager that is used */
  private EventManager eventManager;
  /** MemoryManager that is used */
  private MemoryManager memory = null;

  private MemoryBlock variableMemory = null;
  /** RegisterManager that is used */
  private RegisterManager register;
  /** Emulator that is used */
  private Emulator emulator;
  /** True if the emulator is running */
  private boolean running = false;
  /** True if emulation ended */
  private boolean ended = true;
  /** Current program */
  private Program program = null;

  private SourceCode latestSourceCode = null;
  /** Current speed */
  private SPEED speed = SPEED.NORMAL;

  private boolean stepOver = false;

  private String currentDir = null;
  /** Breakpoints */
  private Hashtable<Integer, Integer> breakpoints = new Hashtable<Integer, Integer>();
  /** Thread that the emulation runs in */
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
      EventManager.EVENT.CURRENT_DIRECTORY,
      EventManager.EVENT.EMULATOR_PAUSE,
      EventManager.EVENT.EMULATOR_STEP,
      EventManager.EVENT.EMULATOR_STEP_OVER,
      EventManager.EVENT.EMULATOR_TOGGLE_STEP_OVER,
      EventManager.EVENT.EMULATOR_RESET,
      EventManager.EVENT.EMULATOR_RUN,
      EventManager.EVENT.EMULATOR_BREAKPOINT_TOGGLE,
      EventManager.EVENT.EMULATOR_SPEED,
      EventManager.EVENT.MEMORY_REQUEST_UPDATE,
      EventManager.EVENT.VARIABLE_REQUEST_UPDATE
    };

    eventManager.addEventObserver(events, this);
  }

  /**
   * Return the PC address
   *
   * @calledby Emulator
   * @return PC address
   */
  public int readPC() {
    return this.pc;
  }

  /**
   * Change PC address
   *
   * @calledby Emulator
   * @param value PC address value
   * @throws EmulatorException If value modulus 4 is equal to 0
   */
  public void writePC(int value) throws EmulatorException {
    if (value % 4 != 0)
      throw new EmulatorException("Program counter address must be a multiplier of 4");

    this.pc = value;
  }

  public void initRun(final boolean all, final boolean stepOver) {
    this.running = false;
    this.ended = false;

    if (this.runningThread != null && this.runningThread.isAlive()) {
      try {
        this.runningThread.join(1000);
      } catch (InterruptedException e) {
      }
    }

    this.runningThread =
        new Thread(
            new Runnable() {
              public void run() {
                execRun(all, stepOver);
              }
            });
    this.runningThread.setPriority(Thread.MIN_PRIORITY);
    this.runningThread.start();
  }

  public void execRun(boolean all, boolean stepOver) {
    this.running = true;
    this.startEvent();

    int nextInstruction = 0;
    int instructionCount = 0;
    int endPc;

    do {
      if (stepOver || (all && this.stepOver)) {
        endPc = this.pc;
        nextInstruction = this.step(true, true);
        if (nextInstruction == 2 && this.running) {
          endPc += 4;
          while (this.step(false, false) > 0 && this.running && this.pc != endPc)
            ;
        }
      } else {
        nextInstruction = this.step(true, false);
      }

      switch (this.speed) {
        case SLOW:
          this.pcChange();
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
          }
          break;
        case NORMAL:
          this.pcChange();
          break;
        case FAST:
          if ((++instructionCount) % 1009 == 0) this.pcChange();
          break;
      }
    } while (nextInstruction > 0 && this.running && all);

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
   * @calls MemoryManager.readInt(), InstructionManager.get(), Instruction.run(), pcChange(),
   *     EventManager.sendEvent()
   * @return True if the emulation can continue
   */
  public int step(boolean reset, boolean firstStepOver) {
    int lastPc = this.pc;

    if (reset) {
      this.register.resetState();
      this.memory.resetState();
    }

    Instruction instruction;
    try {
      int opCode = this.memory.readInt(this.pc);
      if (opCode == 0) {
        this.ended = true;
        return 0;
      }

      instruction = InstructionManager.get(opCode);
      instruction.run(this.emulator);
      this.pc += 4;
    } catch (Exception e) {
      this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_ERROR, e.getMessage());
      this.ended = true;
      return 0;
    }

    if (this.pc == lastPc) {
      this.ended = true;
      return 0;
    }

    if (this.breakpoints.containsKey(this.pc)) {
      return 0;
    }

    if (firstStepOver
        && (instruction instanceof jniosemu.instruction.emulator.CallInstruction
            || instruction instanceof jniosemu.instruction.emulator.CallrInstruction)) {
      return 2;
    }

    return 1;
  }

  /**
   * Helpfunction that returns the current MemoryManager
   *
   * @calledby Emulator
   * @return Current MemoryManager
   */
  public MemoryManager getMemoryManager() {
    return this.memory;
  }

  /**
   * Helpfunction that returns the current RegisterManager
   *
   * @calledby Emulator
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

  /** Used for debuggin */
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
   * @calls Compiler(), Compiler.compile(), Compiler.link(), EventManager.sendEvent(),
   *     Program.toggleBreakpoint, load()
   * @param lines Sourcecode of the program
   */
  public void compile(String lines) {
    Compiler compiler = new Compiler(lines, this.currentDir);

    Program program = null;
    try {
      compiler.compile();
      program = compiler.link();
    } catch (CompilerException e) {
      this.eventManager.sendEvent(EventManager.EVENT.COMPILER_ERROR, e.getMessage());
      this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_CLEAR);
      return;
    }

    this.program = program;

    // Add old breakpoints to this program
    /*
    for (Integer lineNumber: this.breakpoints.values())
    	this.program.toggleBreakpoint(lineNumber.intValue());
    */

    this.reset();
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
   * @calls RegisterManager(), MemoryManager.reset(), Program.getStartAddr(),
   *     EventManager.sendEvent(), pcChange()
   */
  public void load() {
    if (this.memory == null) {
      this.memory =
          new MemoryManager(
              this.eventManager,
              this.program.getBinaryProgram(),
              this.program.getBinaryVariables(),
              this.program.getSourceCode());
    } else {
      this.memory.reset(
          this.program.getBinaryProgram(),
          this.program.getBinaryVariables(),
          this.program.getSourceCode());
    }

    for (MemoryBlock memoryBlock : this.memory.getMemoryBlocks()) {
      if (memoryBlock.getStart() == MemoryManager.VARIABLESTARTADDR)
        this.variableMemory = memoryBlock;
    }

    this.pc = this.program.getStartAddr();
    this.register = new RegisterManager();

    this.ended = false;
    this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_READY);
    this.eventManager.sendEvent(EventManager.EVENT.VARIABLE_VECTOR, this.program.getVariables());

    this.pcChange();
  }

  public void reset() {
    this.running = false;
    this.ended = false;

    if (this.runningThread != null && this.runningThread.isAlive()) {
      try {
        this.runningThread.join(1000);
      } catch (InterruptedException e) {
      }

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
   * @param lineNumber Line to toggle breakpoint
   */
  public void toggleBreakpoint(int lineNumber) {
    if (this.latestSourceCode == null) return;

    int addr = this.latestSourceCode.getAddress(lineNumber);
    if (this.latestSourceCode.toggleBreakpoint(lineNumber)) {
      this.breakpoints.put(addr, lineNumber);
    } else {
      this.breakpoints.remove(addr);
    }

    this.eventManager.sendEvent(EventManager.EVENT.EMULATOR_BREAKPOINT_UPDATE, lineNumber);
  }

  /**
   * Listen for events and acts on them
   *
   * @calledby COMPILER_COMPILE, EMULATOR_RUN, EMULATOR_PAUSE, EMULATOR_STEP, EMULATOR_RESET,
   *     EMULATOR_BREAK_POINT_TOGGLE
   * @calls compile(), runAll(), pause(), runOne(), load(), toggleBreakpoint()
   * @param eventIdentifier Event identifier
   * @param obj Argument depending of which event
   */
  public void update(EventManager.EVENT eventIdentifier, Object obj) {
    switch (eventIdentifier) {
      case COMPILER_COMPILE:
        this.compile((String) obj);
        break;
      case CURRENT_DIRECTORY:
        this.currentDir = (String) obj;
        break;
      case EMULATOR_STEP:
        this.initRun(false, false);
        break;
      case EMULATOR_STEP_OVER:
        this.initRun(false, true);
        break;
      case EMULATOR_RUN:
        this.initRun(true, false);
        break;
      case EMULATOR_PAUSE:
        this.pause();
        break;
      case EMULATOR_RESET:
        this.reset();
        break;
      case EMULATOR_TOGGLE_STEP_OVER:
        this.stepOver = !this.stepOver;
        break;
      case EMULATOR_BREAKPOINT_TOGGLE:
        this.toggleBreakpoint(((Integer) obj).intValue());
        break;
      case EMULATOR_SPEED:
        this.setSpeed((SPEED) obj);
        break;
      case MEMORY_REQUEST_UPDATE:
        if (this.memory != null)
          this.eventManager.sendEvent(
              EventManager.EVENT.MEMORY_CHANGE, this.memory.getMemoryBlocks());
        break;
      case VARIABLE_REQUEST_UPDATE:
        if (this.program != null)
          this.eventManager.sendEvent(
              EventManager.EVENT.VARIABLE_VECTOR, this.program.getVariables());
        if (this.variableMemory != null)
          this.eventManager.sendEvent(EventManager.EVENT.VARIABLE_CHANGE, this.variableMemory);
        break;
    }
  }

  public void setSpeed(SPEED speed) {
    this.speed = speed;
  }

  /**
   * If pc is changed this method is called and all events that should be sent are sent.
   *
   * @calledby load(), step()
   */
  private void pcChange() {
    MemoryBlock block = null;
    try {
      block = this.memory.getBlock(this.pc);
    } catch (MemoryException e) {
    }

    if (block != null) {
      SourceCode sourceCode = this.memory.getBlock(this.pc).getSourceCode();
      if (sourceCode != this.latestSourceCode) {
        this.latestSourceCode = sourceCode;
        this.eventManager.sendEvent(EventManager.EVENT.PROGRAM_CHANGE, this.latestSourceCode);
      }
    }

    this.eventManager.sendEvent(EventManager.EVENT.PROGRAMCOUNTER_CHANGE, Integer.valueOf(this.pc));
    this.eventManager.sendEvent(EventManager.EVENT.REGISTER_CHANGE, this.register.get());
    this.eventManager.sendEvent(EventManager.EVENT.MEMORY_CHANGE, this.memory.getMemoryBlocks());
    this.eventManager.sendEvent(EventManager.EVENT.VARIABLE_CHANGE, this.variableMemory);
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
   * @checks If program ended send EVENTID_EMULATION_END
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

  public void run() {}
}
