package jniosemu.emulator.register;

public class RegisterException extends RuntimeException {
  public RegisterException() {
    super();
  }

  public RegisterException(String aRegister) {
    super(aRegister + ": Don't exists");
  }

  public RegisterException(int aIndex) {
    super("r" + aIndex + ": Can't be used");
  }
}
