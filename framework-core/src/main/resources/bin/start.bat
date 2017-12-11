@echo off & setlocal enabledelayedexpansion


cd ..
set SERVER_HOME=%CD%
cd bin
@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal
set LIB_JARS=""
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i

cd ..\deploy
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\deploy\%%i
cd ..\bin
set JAVA_OPTS=-Xms4G -Xmx8G -Xss256k -XX:PermSize=128m -XX:MaxPermSize=512m -XX:+UseParallelGC -XX:+UseParallelOldGC
set JAVA_OPTS=%JAVA_OPTS% -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000
rem set JAVA_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y %JAVA_OPTS%
echo ===============================================================================
echo.
echo   Server Bootstrap Environment
echo.
echo   SERVER_HOME: %SERVER_HOME%
echo.
echo   JAVA_HOME: %JAVA_HOME%
echo.
echo   JAVA_OPTS: %JAVA_OPTS%
echo.
echo ===============================================================================
echo.
java %JAVA_OPTS% -classpath ../;%LIB_JARS% com.framework.rpc.server.boot.Main
goto end
:end
pause