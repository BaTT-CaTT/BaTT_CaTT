Type=Service
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: false
	
#End Region

Sub Process_Globals
    Dim rv As RemoteViews
	Dim bat As PhoneEvents
	
End Sub

Sub Service_Create
	rv = ConfigureHomeWidget("wid", "rv", 30, "BATT-CaTT Widget")
	
	rv.SetTextColor("label3",Colors.ARGB(230,0,0,0))
	rv.SetTextColor("label2",Colors.ARGB(180,0,0,0))
	rv.SetTextColor("label1",Colors.ARGB(180,0,0,0))
	rv.SetTextSize("label3",15)
	rv.SetTextSize("label2",13)
	rv.SetTextSize("label1",13)
   
	rv.SetImage("im1",LoadBitmap(File.DirAssets, "Battery.png"))
	rv.SetVisible("im1",True)
	rv.SetVisible("Progressbar1",True)
	Dim pi As PhoneId
	bat.InitializeWithPhoneState("bat",pi)
End Sub

Sub Service_Start (StartingIntent As Intent)

    If rv.HandleWidgetEvents(StartingIntent) Then Return
End Sub

Sub rv_RequestUpdate
    rv.UpdateWidget
End Sub

Sub rv_Disabled
    StopService("")
End Sub

Sub Service_Destroy

End Sub

Sub im1_click
	StartActivity(Main)	
End Sub

Sub label1_click

End Sub

Sub bat_BatteryChanged (Level As Int, Scale As Int, Plugged As Boolean, Intent As Intent)
	Dim temp As String 
	temp=Intent.GetExtra("temperature")/10
	rv.SetProgress("Progressbar1",Level)
	rv.SetText("label1",temp&"°C")
	rv.SetText("label2",Intent.GetExtra("technology"))
	rv.SetText("label3",Level&"%")
	If Plugged=True Then 
		rv.SetImage("im1",LoadBitmap(File.DirAssets, "Battery.png"))
		rv.SetVisible("im1",True)
	End If
	If Level < 15 Then 
		rv.SetImage("im1",LoadBitmap(File.DirAssets, "Battery.png"))
		rv.SetTextColor("label3",Colors.ARGB(150,255,255,255))
		rv.SetTextSize("lablel3",18)
		rv.SetText("label2","ATTENTION!")
		rv.SetText("label3","Battery low! "&Level)
		rv.SetVisible("im1",True)
	End If
End Sub