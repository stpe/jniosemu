; Name of our application
Name "JNiosEmu"

; The file to write
OutFile "jniosemu-setup.exe"

; Set the default Installation Directory
InstallDir "$PROGRAMFILES\JNiosEmu"

; Set the text which prompts the user to enter the installation directory
DirText "Please choose a directory to which you'd like to install JNiosEmu."

; ----------------------------------------------------------------------------------
; *************************** SECTION FOR INSTALLING *******************************
; ----------------------------------------------------------------------------------

Var javapath

Section "" ; A "useful" name is not needed as we are not installing separate components

; Set output path to the installation directory. Also sets the working
; directory for shortcuts
SetOutPath $INSTDIR\

File C:\svnwork\trunk\installers\jniosemu.jar

WriteUninstaller $INSTDIR\Uninstall.exe

; //////////

Call GetJRE
Pop $javapath

; ///////////////// CREATE SHORT CUTS //////////////////////////////////////

CreateDirectory "$SMPROGRAMS\JNiosEmu"

CreateShortCut "$SMPROGRAMS\JNiosEmu\Run JNiosEmu.lnk" "$javapath" "-jar $INSTDIR\jniosemu.jar"
CreateShortCut "$SMPROGRAMS\JNiosEmu\Uninstall JNiosEmu.lnk" "$INSTDIR\Uninstall.exe"

; ///////////////// END CREATING SHORTCUTS //////////////////////////////////

; //////// CREATE REGISTRY KEYS FOR ADD/REMOVE PROGRAMS IN CONTROL PANEL /////////

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JNiosEmu" "DisplayName"\
"JNiosEmu (remove only)"

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\JNiosEmu" "UninstallString" \
"$INSTDIR\Uninstall.exe"

; //////////////////////// END CREATING REGISTRY KEYS ////////////////////////////

MessageBox MB_OK "Installation was successful."

SectionEnd

; ----------------------------------------------------------------------------------
; ************************** SECTION FOR UNINSTALLING ******************************
; ----------------------------------------------------------------------------------

Section "Uninstall"
; remove all the files and folders
Delete $INSTDIR\*

RMDir $INSTDIR

; now remove all the startmenu links
Delete "$SMPROGRAMS\JNiosEmu\Run JNiosEmu.lnk"
Delete "$SMPROGRAMS\JNiosEmu\Uninstall JNiosEmu.lnk"
RMDIR "$SMPROGRAMS\JNiosEmu"

; Now delete registry keys
DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\JNiosEmu"

SectionEnd


Function GetJRE
;
;  Find JRE (javaw.exe)
;  1 - in .\jre directory (JRE Installed with application)
;  2 - in JAVA_HOME environment variable
;  3 - in the registry
;  4 - assume javaw.exe in current dir or PATH
 
  Push $R0
  Push $R1
 
  ClearErrors
  StrCpy $R0 "$EXEDIR\jre\bin\javaw.exe"
  IfFileExists $R0 JreFound
  StrCpy $R0 ""
 
  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound
 
  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCpy $R0 "$R0\bin\javaw.exe"
 
  IfErrors 0 JreFound
  StrCpy $R0 "javaw.exe"
        
 JreFound:
  Pop $R1
  Exch $R0
FunctionEnd