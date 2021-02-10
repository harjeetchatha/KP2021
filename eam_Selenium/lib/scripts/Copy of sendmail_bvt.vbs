Dim sCurPath
sCurPath = CreateObject("Scripting.FileSystemObject").GetAbsolutePathName(".")
sCurPath = Replace(sCurPath, "lib\scripts", "")
resFile = sCurPath + "\res\TestReport.txt"

MsgBox resFile, 40