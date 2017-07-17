Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'datacount Code Module created by trOw
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	
End Sub

Sub Globals

	Dim kvdata As KeyValueStore
	Dim i As Intent
	Dim la As Label
	Dim button As Button
	Private mcl As MaterialColors
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.Color=Colors.ARGB(220,255,255,255)
	button.Initialize("button")
	la.Initialize("la")
	la.Enabled=True
	la.Visible=True
	button.Enabled=True
	button.Visible=True
	Activity.AddView(la,20dip,30%y,300dip,150dip)
	Activity.AddView(button,35%x,70%y,120dip,50dip)
	button.Text="zurück"
	button.TextSize=15
	button.Color=mcl.md_light_blue_300
	la.Text="Deinstallation beendet!"
	la.TextSize=20
	la.Gravity=Gravity.CENTER
	
	
		
		
	kvdata.Initialize(File.DirDefaultExternal,"datastore_data")
	
		
End Sub

Sub Activity_Resume
	
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub button_click
	Activity.Finish
End Sub

Sub start
	If kvdata.ContainsKey("data") Then 
		i.Initialize("android.intent.action.DELETE","package:"&kvdata.GetSimple("data"))
		StartActivity(i)
	End If

	Return
End Sub

