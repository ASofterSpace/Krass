@echo off

cd /D %~dp0

java -classpath "%~dp0\bin" -Xms16m -Xmx1024m -Djava.security.policy=C:\home\prog\asofterspace\Krass\test.policy com.asofterspace.krass.Krass %*

pause
