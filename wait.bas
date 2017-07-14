Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'wait Code Module created by trOw
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
	
#End Region
	#Extends: android.support.v7.app.AppCompatActivity
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
		Private pak As PackageManager
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private crtext As String 
	Private mcl As MaterialColors
	Dim creditsMax As Int = 30000
	Dim creditsPos As Int = 0
	Dim tmr As Timer
	Private credits As CreditsRollView
	Private iv1 As ImageView
	Dim count As Int 
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("8")
	Activity.Color=mcl.md_light_blue_100
	crtext="Version: "&pak.GetVersionName("com.batcat")&", Integer: "&pak.GetVersionCode("com.batcat")&" Programmed in: Basic4A, Android and Java. Java is a free OpenSurce software and is subject to the free Creative public license. Android is under the google license, all associated names and content are protected by the google Inc. software agreement. For more information, visit www.google.com/license. All rights to the code and the design are reserved to BaTTCaTT and its owners..Code by D. Trojan, published by SuloMedia™ www.battcatt.bplaced.net for Recent News & Updates. All Rights Reserved BC ©2017 "&pak.GetApplicationLabel("com.batcat")
	credits.Text=crtext
	credits.TextColor=Colors.Black

	credits.EndScrollMult=1.0
	tmr.Initialize("timer",90)
	tmr.Enabled = True
	iv1.Visible=True
	iv1.SetVisibleAnimated(15000,False)
End Sub

Sub Activity_Resume
	credits.ScrollPosition = 0
	tmr.Enabled=True
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	
End Sub
Sub timer_Tick
	count=creditsPos


		If count < creditsMax Then
			creditsPos = creditsPos +1
		End If
		'Log((creditsPos/1000))
		If (creditsPos/400) >= 1 Then
			tmr.Enabled = False
		iv1.SetVisibleAnimated(20,True)
		StartActivity(Main)
		End If
		credits.ScrollPosition = (creditsPos/400)
		credits.DistanceFromText = 50dip
		credits.Angle = 20
		credits.Height = 100%y
		credits.Width = 100%x
		credits.TextSize = 30

End Sub
	
Sub rebound
	
	CallSubDelayed(pman,"app_manage")
End Sub


Sub iv1_Click
	
End Sub