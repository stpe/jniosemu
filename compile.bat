@echo off

CALL clean.bat

javac.exe -Xlint:deprecation -d bin/ jniosemu\*.java jniosemu\editor\*.java jniosemu\emulator\*.java jniosemu\emulator\compiler\*.java jniosemu\emulator\compiler\macro\*.java jniosemu\emulator\memory\*.java jniosemu\emulator\register\*.java jniosemu\instruction\*.java jniosemu\instruction\compiler\*.java jniosemu\instruction\emulator\*.java

PAUSE
