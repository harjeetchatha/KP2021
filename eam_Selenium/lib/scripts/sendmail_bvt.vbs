Dim fso, ts, passed,failed,total,objText,resFile,i,line
Dim tcArr(200)

Const ForReading = 1
Dim sCurPath
sCurPath = CreateObject("Scripting.FileSystemObject").GetAbsolutePathName(".")
resFile = sCurPath + "\res\TestReport.txt"

passed=0
failed=0
total=0
i=0


Set fso = CreateObject("Scripting.FileSystemObject")
Set ts = fso.OpenTextFile(resFile, ForReading)

Do Until ts.AtEndOfStream
strNextLine = ts.Readline
total = total+1
tcArr(i) = strNextLine
If (InStr (strNextLine, "Passed")) Then
passed=passed+1
Else
failed=failed+1
tcArr(i) = "* " + strNextLine

End If
i=i+1
Loop
ts.close

objText = Join (Array(_
"" _
,"" _
,"EAMM  Build Verification Test Cases Execution completed. Please find results as below"_
, "" _
, "" _
, "" _
,"__________________________________________________" _
, ""_
,"  Total Executed= "+ CStr(total) _
, "" _
,"  Total Passed  = "+ CStr(passed) _
, "" _
,"  Total Failed  = "+ cStr(failed) _
, "" _
,"__________________________________________________" _
, ""_
, ""), vbCrLf)




For Each line In tcArr

objText = Join (Array(_
objText _
, line _
, ""), vbCrLf)
Next




Set objEmail = CreateObject("CDO.Message")


objEmail.From = "hpesala@prokarma.com"
objEmail.To = "harikrishna.x.pesala@kp.org"
'objEmail.CC ="eamm_ta_results@kp.org"
'objEmail.To = "harikrishna.x.pesala@kp.org" & ";" & "Harjeet.X.Chatha@kp.org" & ";" & "Nagarathna.R.Dutt@kp.org" & ";" & "Vamsi.K.Padala@kp.org"


objEmail.Subject = "EAMM  Build Verification Test Results - " & CStr(passed) & " Pass/ " & CStr(failed) & " Fail - " & Date()
objEmail.Textbody = objText



objEmail.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusing")=2
objEmail.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserver")="smtp.office365.com"
'objMessage.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpusessl")=true
objEmail.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserverport")=587
'objEmail.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusername")="hpesala@prokarma.com"
'objEmail.Configuration.Fields.Item("http://schemas.microsoft.com/cdo/configuration/sendpassword")=""

objEmail.Configuration.Fields.Update
objEmail.Send
