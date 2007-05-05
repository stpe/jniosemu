@echo off

CALL clean.bat

D:\program\java\jdk1.5.0_11\bin\javac.exe -d ../../bin/ jniosemu\*.java jniosemu\editor\*.java jniosemu\emulator\*.java jniosemu\emulator\compiler\*.java jniosemu\emulator\compiler\macro\*.java jniosemu\emulator\memory\*.java jniosemu\emulator\register\*.java jniosemu\instruction\*.java jniosemu\instruction\compiler\*.java jniosemu\instruction\emulator\*.java

PAUSE
