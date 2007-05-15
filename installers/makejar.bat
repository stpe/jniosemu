@echo off

REM keypass: _iAhje4_b!6ys
REM storepass: 9!!lgr_z87fRr

set tmppath=C:\jartmp\
set svnpath=C:\svnwork\trunk\
set installpath=%svnpath%installers\
set jarname=jniosemu.jar
set nsisexe=C:\Program\NSIS\makensis.exe

REM ----------------------------------

mkdir %tmppath%

REM ----------------------------------
REM - Make JAR
REM ----------------------------------

cd %svnpath%
xcopy jniosemu\*.class %tmppath%jniosemu\ /S /EXCLUDE:%installpath%excludes.txt
xcopy graphics %tmppath%graphics\ /S
xcopy %installpath%MANIFEST.MF %tmppath%META-INF\

jar cMf %installpath%%jarname% -C %tmppath% .

REM ----------------------------------
REM - Sign JAR
REM ----------------------------------

cd %installpath%

jarsigner -keystore jniosemustore -storepass 9!!lgr_z87fRr -keypass _iAhje4_b!6ys %jarname% jniosemu

REM ----------------------------------
REM - NSIS
REM ----------------------------------

if not exist %nsisexe% goto skipnsis

%nsisexe% /V3 /NOCD %installpath%windows\jniosemu.nsi

:skipnsis

REM ----------------------------------

rmdir /S /Q %tmppath%

