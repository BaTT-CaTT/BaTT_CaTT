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
	Dim bat20,bat40,bat60,bat80,bat100,batlow,usb0,usb1,usb2,usb3,usbc,ulow As Bitmap
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
	bat20.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (5).png")
	bat40.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (4).png")
	bat60.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (3).png")
	bat80.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (2).png")
	bat100.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (1).png")
	usb1.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (16).png")
	usb0.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (9).png")
	usb2.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (8).png")
	usb3.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (7).png")
	batlow.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (10).png")
	ulow.Initialize(File.DirAssets,"Battery Icons - Colorful 128px (14).png")
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
		rv.SetImage("im1",usb1)
		rv.SetVisible("im1",True)
		If Level<=100 Then
			rv.SetImage("im1",usb0)
		End If
		If Level<=50 Then
			rv.SetImage("im1",usb2)
		End If
			If Level <=5 Then
				rv.SetImage("im1",usb3)
			End If
		Else
		rv.SetVisible("im1",True)
		If Level<=100 Then 
			rv.SetImage("im1",bat100)
		End If
		If Level<=80 Then 
			rv.SetImage("im1",bat80)
		End If
		If Level<=60 Then 
			rv.SetImage("im1",bat60)
		End If
		If Level<=40 Then 
		rv.SetImage("im1",bat40)
	End If
	If Level <=20 Then 
				rv.SetImage("im1",bat20)
	End If
	If Level < 15 Then 
		rv.SetImage("im1",batlow)
		rv.SetTextColor("label3",Colors.ARGB(150,255,255,255))
		rv.SetTextSize("lablel3",18)
		rv.SetText("label2","ACHTUNG:")
		rv.SetText("label3","Akku fast leer! "&Level)
		rv.SetVisible("im1",True)
	End If
	end if 
End Sub