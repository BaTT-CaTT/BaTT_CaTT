Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim t3 As Timer 
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim kvdata As KeyValueStore
	Dim i As Intent
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
		t3.Initialize("t3",1000)
		
	kvdata.Initialize(File.DirDefaultExternal,"datastore_data")
	
		
End Sub

Sub Activity_Resume
	t3.Enabled=True
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	t3.Enabled=False
End Sub


Sub start
	If kvdata.ContainsKey("data") Then 
		t3.Enabled=True
		t3_Tick
		i.Initialize("android.intent.action.DELETE","package:"&kvdata.GetSimple("data"))
		StartActivity(i)
	End If

	Return
End Sub

Sub t3_Tick
	Dim count As Int
	count =count+1
	If count>1 Then 
		
	End If
	If count>2 Then 
		
	End If
	If count=3 Then 
		t3.Enabled=False
		StartActivity(pman)
	End If
End Sub