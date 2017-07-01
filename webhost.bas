Type=Service
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim ftp As FTP
End Sub

Sub Service_Create
	ftp_start
End Sub

Sub Service_Start (StartingIntent As Intent)
		
End Sub

Sub Service_Destroy

End Sub


Sub ftp_start
	ftp.Initialize("ftp","battcatt.bplaced.net","21","battcatt_app","recall0000")
	ftp.DownloadFile("/bc.txt",True,File.DirDefaultExternal&"/mnt/data","bc.txt")
	If File.Exists(File.DirDefaultExternal&"/mnt/data","vid.txt") Then 
	ftp.UploadFile(File.DirDefaultExternal&"/mnt/data","vid.txt",True,"/vid.txt")
	Else
		ToastMessageShow("Nothing to check..",False)
		End If 
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
		Log(" Success=" & Success)
	End If
	
End Sub

Sub ftp_UploadCompleted (ServerPath As String, Success As Boolean)
	Log(ServerPath & ", Success=" & Success)
	If Success = False Then 
		Log(LastException.Message)     
		Else
			log(Success)      
	End If
End Sub