Type=Service
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Service Module created by trOw
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
	 Dim kvs2,kvs3,kvs4,kvstemp,kvsvolt,kvstime As KeyValueStore 
	 Private date,time,tt As String
	 Dim bat As Batut
	Dim level1 As Int
	Dim kl,dt,nl,logfile As List
	Dim cs As CSBuilder
	Dim dir As String=File.DirDefaultExternal&"/mnt/cache"
	Dim labex As String
	Dim sql As SQL
	Dim m As Map
	Dim bat As Batut
	Dim t6 As Timer 
	Dim count As Int
End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	t6.Initialize("t6",1000)
   sNotif.Initialize
   bat.Initialize
   logfile.Initialize
   sNotif.Light=False
   sNotif.Icon= "bat100"
   sNotif.SetInfo("Bat-Cat","Welcome",Main)
   sNotif.Sound = False
   sNotif.Vibrate=False  
   sNotif.Notify(1)   
   
	device.Initialize("device")
	list1.Initialize
	m.Initialize
	'File.WriteList(File.DirDefaultExternal&"/mnt/cache","log.txt",logfile)
	If File.Exists(File.DirDefaultExternal&"/mnt/cache","log.txt") Then 
		
		Else
			File.MakeDir(File.DirDefaultExternal,"mnt/cache")
			File.WriteList(dir,"log.txt",logfile)
	End If
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
	kvstime.Initialize(File.DirDefaultExternal,"datastore_time")
	Service.StartForeground(1,sNotif)
	
End Sub

Sub Service_Start (StartingIntent As Intent)
	'ToastMessageShow("Service Start..",False)
	Dim volt As String = bat.BatteryInformation(7)
		logfile.Add("App StartActivity "&date&" - "&time)
		logfile.Add(date&"/"&time&": "&level1&" | "&volt)
End Sub 

Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	ToastMessageShow("Status: Error Service Stop!",False)
	Return True
End Sub


Sub Service_Destroy
	'ToastMessageShow("Status: Service End..",False)
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
	Dim temp,volt,volt1,temp2 As Int
	volt1=Intent.GetExtra("voltage") /1000
	temp2=Intent.GetExtra("temperature") /10
	Dim status As Int = Intent.GetExtra("status")
	volt=Rnd(volt1,volt1+1)
	temp=Rnd(temp2,temp2+1)
	Dim rst,rl,rm As Int
	Dim val,hours,minutes As Int
	If Level < Scale Then
		kvs2.PutSimple(Level,date&"/"&time)
		data_cache
	End If
	If Plugged  Then
		ac_check
		rst=Scale-Level
		val = rst*Intent.GetExtra("voltage")/1000
		hours = Floor(val / 60)
		minutes = val Mod 60
		If Level=100 Then
			load_check
			sNotif.Icon="batusb"
			sNotif.Sound=False
			sNotif.SetInfo(Level&"%",volt&" V | "&temp&"°C ",Main)
			sNotif.Notify(1)
			Service.StartForeground(1,sNotif)

			
		Else
			sNotif.Icon="batusb"
			sNotif.Sound=False
			sNotif.SetInfo(ac&Level&" %",volt&" V | "&temp&"°C | noch: "&hours&"h - "&minutes&"min",Main)
			sNotif.Notify(1)
			Service.StartForeground(1,sNotif)
		End If
	Else
		
		Dim days,sval As Int
		sval =Intent.GetExtra("voltage")
		'Log(sval)
		val = Scale/sval*Level*1000
		days=Floor(val/60/24)
		hours = Floor(val/60 Mod 24)
		minutes = val Mod 60
		If  Not (Level=0) Then
			sNotif.SetInfo(Level&" %",volt&" V | "&temp&"°C | noch: "&days&"d "&hours&"h "&minutes&"m",Main)
			sNotif.Notify(1)
		End If
		If Level < 100 Then
			sNotif.Icon="bat100"
		End If
		If Level < 80 Then
	
			sNotif.Icon="bat80"
		End If
		If Level < 60 Then
		
			sNotif.Icon="bat60"
		End If
		If Level < 40 Then
			
			sNotif.Icon="bat40"
		End If
		If Level<20 Then
			sNotif.Icon="bat20"
		End If
		If Level =15  Then
			sNotif.Icon="bat5"
			sNotif.SetInfo(Level&" %",volt&" V | "&temp&"°C | ca: "&days&"d "&hours&"h "&minutes&"m",Main)
			sNotif.Light=True
			sNotif.Sound=True
			sNotif.Notify(1)
		End If
		If temp =50 Then
			logfile.Add(date&"/"&time&": "&temp&"C°")
			sNotif.Icon="batheat"
			sNotif.SetInfo("Achtung: "&temp&"°C !","hier klicken um zum kühlen...",cool)
			sNotif.Light=True
			sNotif.Sound=True
			sNotif.Notify(1)
		
		End If
	End If
End Sub


Sub value_check 
	Log("check...")
End Sub
Sub ac_check
	Dim acc,ausb As Int
	Dim us,ac As String
	acc=bat.BatteryInformation(10)
	ausb=bat.BatteryInformation(9)
	If acc = 1 Then
		ac="AC - Kabel: "
		logfile.Add(date&"/"&time&": "&ac&" angeschlossen.")
		kvstime.DeleteAll
		kvstime.PutSimple(date&"/"&time&" - AC",acc)
		'value_check
	Else
		ac="USB - Kabel: "
		logfile.Add(date&"/"&time&": "&ac&" angeschlossen.")
		kvstime.DeleteAll
		kvstime.PutSimple(date&"/"&time&" - USB",acc)
		'value_check
	End If
	
End Sub

Sub load_check
	Dim load As String = bat.BatteryInformation(0)
	If load=100 Then
		kvstime.DeleteAll
		kvstime.PutSimple(date&"/"&time&": "&load&"% AC",load)
		logfile.Add(date&"/"&time&": "&load&"% geladen.")
		'value_check
	End If
End Sub

Sub data_cache
	time=DateTime.Time(DateTime.Now)
	Dim temp,volt As Int
	volt=bat.BatteryInformation(7)/1000
	temp=bat.BatteryInformation(6)/10
	If kvs2.ListKeys.Size=15 Then
		kvsvolt.DeleteAll
		kvstemp.DeleteAll
		kvs2.DeleteAll
		'ToastMessageShow("BC Reload",False)
	End If
	For g = 0 To 60
		If g=temp Then
			kvstemp.PutSimple(g,time)
			Log(time&" Put-> "&temp&"C°")
		End If
	Next
	For vo = 2500 To 6000
		If vo=volt Then
			kvsvolt.PutSimple(vo,time)
			Log(time&" Put-> "&volt&"V")
		End If
	Next
End Sub



Sub minutes_hours ( ms As Int ) As String
	Dim val,hours,minutes As Int
	val = ms
	hours = Floor(val / 60)
	minutes = val Mod 60
	Return NumberFormat(hours, 1, 0) & ":" & NumberFormat(minutes, 2, 0)
End Sub
