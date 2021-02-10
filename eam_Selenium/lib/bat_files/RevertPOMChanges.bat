Echo ***********************   Reverting POM changes   **********************
 
java -classpath %CD% MergeResults "target\surefire-reports\EAMM Test Automation\TEST-TestSuite.xml" "target\surefire-reports\TEST-TestSuite.xml" "target\surefire-reports\TEST-TestSuite.xml"

move /y "target\surefire-reports\testng-results.xml" "Reports_Backup\"

copy "lib\pom_backup.xml" "pom.xml"

Echo ***********************   Reverting POM changes completed **********************