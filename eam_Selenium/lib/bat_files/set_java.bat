@ECHO OFF
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_131
set MAVEN_HOME=C:\apache-maven-3.6.2
set PATH=%PATH%;%JAVA_HOME%\bin;%MAVEN_HOME%\bin;
echo %JAVA_HOME%
echo %MAVEN_HOME%
echo %PATH%

mvn --version
