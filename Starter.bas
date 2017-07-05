Type=Service
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: true
	'#ExcludeFromLibrary: True
	#StartCommandReturnValue: android.app.Service.START_STICKY
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
		'Dim ra As OperatingSystem
		Dim sNotif As Notification
      Dim device As PhoneEvents
	Dim list1 As List
	 Dim kvs2,kvs3,kvs4,kvstemp,kvsvolt As KeyValueStore 
	 Private date,time,tt As String
	 Dim bat As Batut
	Dim level1 As Int
	Dim kl,dt,nl As List
	Dim cs As CSBuilder
	Dim dir As String=File.DirDefaultExternal&"/mnt/cache"
	Dim labex As String
	Dim sql As SQL
	Dim m As Map
	dim bat as Batut
End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	
   sNotif.Initialize
   bat.Initialize
   sNotif.Light=False
   sNotif.Icon= "bat100"
   sNotif.SetInfo("Bat-Cat","Welcome",Main)
   sNotif.Sound = False
   sNotif.Vibrate=False  
   sNotif.Notify(1)   
   Service.StartForeground(1,sNotif)
	device.Initialize("device")
	list1.Initialize
	m.Initialize
	'File.WriteList(File.DirDefaultExternal&"/mnt/cache","sstats.txt",list1)
	date=DateTime.Date(DateTime.Now)
	
	sql.Initialize(File.DirRootExternal, "1.db", True)
	tt=time

	'level1 =bat.BatteryInformation(0)
	kl.Initialize
	dt.Initialize
	nl.Initialize
	kvs2.Initialize(File.DirDefaultExternal, "datastore_2")
	kvs3.Initialize(File.DirDefaultExternal, "datastore_3")
	kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
	kvstemp.Initialize(File.DirDefaultExternal, "datastore_temp")
	kvsvolt.Initialize(File.DirDefaultExternal, "datastore_volt")
End Sub

Sub Service_Start (StartingIntent As Intent)
	ToastMessageShow("Status: Service Start..",False)
	If File.Exists(File.DirDefaultExternal&"/mnt/cache","lvl2.txt") Then
		'ToastMessageShow("Welcome!",False)
		'ListView1.Clear
		'lis.Add("App StartActivity "&date&" - "&time)
	Else
		File.MakeDir(File.DirDefaultExternal, "mnt/cache")
	
		File.WriteString(File.DirDefaultExternal&"/mnt/cache","lvl2.txt",level1)
	
		'ToastMessageShow("BC loaded..! "&date&", "&time,False)
	End If
	

End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	ToastMessageShow("Status: Error Service Stop!",False)
	Return True
End Sub


Sub Service_Destroy
	ToastMessageShow("Status: Service End..",False)
End Sub


Sub device_BatteryChanged (Level As Int, Scale As Int, Plugged As Boolean, Intent As Intent)
	Dim acc,ausb As Int
	Dim us,ac As String 
	acc=bat.BatteryInformation(10)
	ausb=bat.BatteryInformation(9)
	If acc = 1 Then 
		ac="AC - Kabel: "
		Else
			ac="USB - Kabel: "
	End If
	time=DateTime.Time(DateTime.Now)
	Dim temp,volt,volt1,temp2 As String
	volt1=Intent.GetExtra("voltage") /1000
	temp2=Intent.GetExtra("temperature") /10
	Dim status As Int = Intent.GetExtra("status")
	volt=Rnd(volt1,volt1+1)
	temp=Rnd(temp2,temp2+1)
	If kvsvolt.ListKeys.Size>15 Then
		
		kvsvolt.DeleteAll
		kvstemp.DeleteAll
		'ToastMessageShow("Reset",False)
	End If
	If kvs2.ListKeys.Size>25 Then 
		kvs2.DeleteAll
		ToastMessageShow("Reset",False)
	End If
	For g = 0 To temp2
		If temp2=g Then
			kvstemp.PutSimple(temp2,time)
			Log(time&" Put-> "&temp2&"C°")
		End If
	Next
	For vo = 2900 To Intent.GetExtra("voltage")
		If Intent.GetExtra("voltage")=vo Then 
			 kvsvolt.PutSimple(volt1,time)
			Log(time&" Put-> "&Intent.GetExtra("voltage")&"V")
		End If
	Next
	Dim rst,rl,rm As Int
	Dim val,hours,minutes As Int
	File.WriteString(File.DirDefaultExternal&"/mnt/cache","lvl.txt",Level)
	File.WriteString(File.DirDefaultExternal&"/mnt/cache","volt.txt",volt)
	If Plugged  Then
	
		For v = 0 To Scale 
			'nl.Add(v)
			If Level=v Then
				Log("Put-> "&v)
				kvs2.PutSimple(Level,time) 
			End If 
	Next
		rst=Scale-Level
		val = rst*Intent.GetExtra("voltage") /1000
		hours = Floor(val / 60)
		minutes = val Mod 60
		If Level=100 Then
			sNotif.Icon="batusb"
			sNotif.Sound=False
			sNotif.SetInfo(Level&"%",volt&" V | "&temp&"°C ",Main)
			sNotif.Notify(1)
			'sql1.ExecNonQuery("INSERT INTO stats VALUES (NULL,"& tt &"," & level1 &")")
			Service.StartForeground(1,sNotif)
		Else
		sNotif.Icon="batusb"
		sNotif.Sound=False
		sNotif.SetInfo(ac&Level&" %",volt&" V | "&temp&"°C | noch: "&hours&"h/"&minutes&"min",Main)
		sNotif.Notify(1)
		'sql1.ExecNonQuery("INSERT INTO stats VALUES (NULL,"& tt &"," & level1 &")")
		Service.StartForeground(1,sNotif)
		End If
	Else
		For v = 0 To Scale
			nl.Add(v)
			If Level=v Then
				Log("Put-> "&v)
				kvs2.PutSimple(Level,time)
			End If
		Next
		Dim days,sval As Int 
		sval =Intent.GetExtra("status")*60
		Log(sval)
		val = sval
		days=Floor(val/60/24)
		hours = Floor(val/60 Mod 24)
		minutes = val Mod 60
		If  Not (Level=0) Then 
			sNotif.SetInfo(Level&" %",volt&" V | "&temp&"°C | noch: "&days&"d "&hours&"h "&minutes&"m",Main)
			sNotif.Notify(1)
		End If
	If Level <= 100 Then
		'kvs2.PutSimple(Level,time)
			
		sNotif.Icon="bat100"
	End If
	If Level <= 80 Then
	
		sNotif.Icon="bat80"
	End If
	If Level <= 60 Then
		
		sNotif.Icon="bat60"
	End If
	If Level <= 40 Then
			
		sNotif.Icon="bat40"
	End If
	If Level<=20 Then 
		sNotif.Icon="bat20"
	End If
	If Level = 5 Then
		sNotif.Icon="bat5"
		sNotif.SetInfo("low Battery!: ","remain: "&Level&"% ",Main)
		sNotif.Notify(1)
	End If
	If Level = 4 Then
			sNotif.Icon="batlow"
			sNotif.SetInfo("low Power!: ","set on low Power: "&Level&"% ",Main)
			sNotif.Notify(1)
	End If 
	If temp >=41 Then
		sNotif.Icon="batheat"
		sNotif.SetInfo("Achtung: "&temp&"°C !","hier klicken um zum kühlen...",cool)
		sNotif.Notify(1)
		End If
	End If
	
End Sub


Sub minutes_hours ( ms As Int ) As String
	Dim val,hours,minutes As Int
	val = ms
	hours = Floor(val / 60)
	minutes = val Mod 60
	Return NumberFormat(hours, 1, 0) & ":" & NumberFormat(minutes, 2, 0)
End Sub
