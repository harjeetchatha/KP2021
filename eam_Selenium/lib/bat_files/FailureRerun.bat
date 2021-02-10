Echo ***********************   Starting Backup of FirstRerun   **********************

SET DAY=%DATE:~4,2%
SET MTH=%DATE:~7,2%
SET YR=%DATE:~10,4%
SET HR=%TIME:~0,2%
SET HR0=%TIME:~0,1%
IF "%HR0%"==" " SET HR=0%TIME:~1,1%
SET MIN=%TIME:~3,2%
SET SEC=%TIME:~6,2%
SET MYDATE=%YR%%MTH%%DAY%-%HR%%MIN%%SEC%

IF EXIST "Reports_Backup\" GOTO RepBakpExist
mkdir "Reports_Backup\"
:RepBakpExist

mkdir "Reports_Backup\RegressionFirstRun_%MYDATE%"

xcopy /E /I "target\surefire-reports" "Reports_Backup\RegressionFirstRun_%MYDATE%"
copy /y "target\surefire-reports\TEST-TestSuite.xml" "target\surefire-reports\EAMM Test Automation\TEST-TestSuite.xml"

Echo ***********************   Starting Failed Rerun   **********************

mvn test -P rerun

Echo ***********************   Completed Failed Rerun   **********************