@echo off
setlocal
set GRADLE_VERSION=8.11.1
set DIST=%USERPROFILE%\.gradle\wrapper\dists\z9tether-gradle-%GRADLE_VERSION%
set ZIP=%DIST%\gradle-%GRADLE_VERSION%-bin.zip
set DIR=%DIST%\gradle-%GRADLE_VERSION%
if not exist "%DIR%\bin\gradle.bat" (
  if not exist "%DIST%" mkdir "%DIST%"
  if not exist "%ZIP%" powershell -Command "Invoke-WebRequest -Uri https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip -OutFile '%ZIP%'"
  powershell -Command "Expand-Archive -Force '%ZIP%' '%DIST%'"
)
call "%DIR%\bin\gradle.bat" %*
