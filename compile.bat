@echo off

CALL clean.bat

D:\program\java\jdk1.5.0_11\bin\javac.exe *.java jniosemu\compiler\*.java jniosemu\compiler\macro\*.java jniosemu\emulator\*.java jniosemu\emulator\memory\*.java jniosemu\emulator\register\*.java jniosemu\instruction\*.java jniosemu\instruction\compiler\*.java jniosemu\instruction\emulator\*.java
