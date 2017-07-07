Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region
#Extends: android.support.v7.app.AppCompatActivity
	
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private kvs4,kvs4sub As KeyValueStore
	Private Panel1 As Panel
	Private sublist As ScrollView
	Private Label1 As Label
	Private Label2 As Label
	Private Label3 As Label
	Private Label4 As Label
	Private ACToolBarLight1 As ACToolBarLight
	Private ToolbarHelper As ACActionBar
	Dim cc1,cc2,cc3,cc4,cc5,cc6,cc7,cc8,cc9,cc10,cc11,cc12,cc13,cc14,cc15,cc16 As ColorDrawable
	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16 As Int
	Dim mcl As MaterialColors
	Dim dev As PhoneEvents
	'Private ACSwitch1 As ACSwitch
	'Private ACSwitch2 As ACSwitch
	'Private ACSwitch3 As ACSwitch
	Private ACSpinner1 As ACSpinner
	Private ACButton1 As ACButton
	Dim popa As ACPopupMenu
	Private Panel2 As Panel
	Dim icon As Bitmap
	Private ac As AppCompat
	Dim parser As SaxParser
	Dim colist As List
	Dim nativeMe As JavaObject
	Dim cs As CSBuilder
	Private cb1 As ACCheckBox
	Private Panel3 As Panel

	Private arb As ACSwitch
	'Private ui As UISwitch
	Dim uon,uoff As Bitmap 
	Private Label5 As Label
	Dim AutoUpdate As Boolean
	
End Sub 

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("6")
	'Activity.Color=Colors.ARGB(200,255,255,255)
	'Activity.AddMenuItem3("Info","pci",LoadBitmap(File.DirAssets, "Rss.png"),True)
	kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
	kvs4sub.Initialize(File.DirDefaultExternal, "datastore_sub_4")
	dev.Initialize("dev")
	If FirstTime Then
		parser.Initialize
	End If
	uon=LoadBitmap(File.DirAssets,"son.png")
	uoff=LoadBitmap(File.DirAssets,"soff.png")
	Dim bd As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets,"ic_data_usage_black_48dp.png"))
	'icon.Initialize(File.DirAssets,"ic_snooze_black_18dp.png")
	popa.Initialize("popa",Panel3)
	colist.Initialize
	'arb.Visible=False
'	ui.Initialize(Me,"ui","33333333",90,45,uon,uoff)
'	Activity.AddView(ui.asView,375,60,90,45)
'	ACToolBarLight1.SetAsActionBar
'	ac.SetElevation(ACToolBarLight1,4dip)
'	ToolbarHelper.Initialize
'	ToolbarHelper.ShowUpIndicator=False
'	ToolbarHelper.Hide

	'Panel2.RemoveView
	

	popa.AddMenuItem(0,"Save",bd)
	nativeMe.InitializeContext
	c1=mcl.md_light_blue_A700
	c2=mcl.md_amber_A700
	c3=mcl.md_white_1000
	c4=mcl.md_teal_A700
	c5=mcl.md_deep_purple_A700
	c6=mcl.md_red_A700
	c7=mcl.md_indigo_A700
	c8=mcl.md_blue_A700
	c9=mcl.md_orange_A700
	c10=mcl.md_grey_700
	c11=mcl.md_green_A700
	c12=mcl.md_black_1000
	c13=mcl.md_yellow_A700
	c14=mcl.md_cyan_A700
	c15=mcl.md_blue_grey_700
	c16=mcl.md_light_blue_A700
	
	cc1.Initialize(c1,5dip)
	cc2.Initialize(c2,5dip)
	cc3.Initialize(c3,5dip)
	cc4.Initialize(c4,5dip)
	cc5.Initialize(c5,5dip)
	cc6.Initialize(c6,5dip)
	cc7.Initialize(c7,5dip)
	cc8.Initialize(c8,5dip)
	cc9.Initialize(c9,5dip)
	cc10.Initialize(c10,5dip)
	cc11.Initialize(c11,5dip)
	cc12.Initialize(c12,5dip)
	cc13.Initialize(c13,5dip)
	cc14.Initialize(c14,5dip)
	cc15.Initialize(c15,5dip)
	cc16.Initialize(c16,5dip)

	
	ACSpinner1.Prompt="Wähle Farbe"
	
	ACSpinner1.Add2("light blue",cc1)
	ACSpinner1.Add2("Amber",cc2)
	ACSpinner1.Add2("White(Arctic)",cc3)'-<<<<<<change on main bardata and textflow!
	ACSpinner1.Add2("teal",cc4)
	ACSpinner1.Add2("purple(dark)",cc5)
	ACSpinner1.Add2("red",cc6)
	ACSpinner1.Add2("indigo",cc7)
	ACSpinner1.Add2("blue",cc8)
	ACSpinner1.Add2("orange",cc9)
	ACSpinner1.Add2("grey",cc10)
	ACSpinner1.Add2("green",cc11)
	ACSpinner1.Add2("Black(Ultra)",cc12)'-<<<<<<change on main bardata and textflow!
	ACSpinner1.Add2("yellow",cc13)
	ACSpinner1.Add2("cyan",cc14)
	ACSpinner1.Add2("blue grey",cc15)
	ACSpinner1.Color=Colors.Transparent
	'ACButton1.ButtonColor=Colors.ARGB(180,255,255,255)
	
	If StateManager.RestoreState(Activity, "Main", 0) = False Then
		'set the default values
		arb.Checked=True
		ACSpinner1_ItemClick(0,0)
	End If
	AutoUpdate = StateManager.GetSetting2("AutoUpdate", False)

	

	store_check
End Sub

Sub Activity_Resume
	'AutoUpdate
	store_check
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		StateManager.SaveState(Activity, "Main")
	End If
	StateManager.SaveSettings
	Activity.Finish
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub


Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
		SetAnimation.setanimati("extra_in", "extra_out")
			StateManager.SaveState(Activity, "Main")
		End If
		StateManager.SaveSettings
	Return(True)
End Sub

Sub ACButton1_Click
	StateManager.SaveSettings
	Activity.Finish
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub popa_ItemClicked (Item As ACMenuItem)
	If Item = popa.GetItem(0) Then
		Activity.Finish
		SetAnimation.setanimati("extra_in", "extra_out")
	End If
End Sub


Sub arb_CheckedChange(Checked As Boolean) 
	If Checked=True Then
		kvs4sub.DeleteAll
		StartService(Starter)
	'StartService(hw)
		StartService(webhost)
		Log("start")
		ToastMessageShow("Services started..",False)
'		'StopService(hw)
		StateManager.SaveSettings
	Else
		If Checked=False Then
			kvs4sub.DeleteAll
			kvs4sub.PutSimple("off",Checked)
			StopService(Starter)
			StopService(webhost)
			Log("Service Stop")
			ToastMessageShow("Services closed!",False)
		End If
		StateManager.SaveSettings
	End If
	'AutoUpdate
End Sub


Sub tab_settings
	Dim liv As ListView
	Dim draw As BitmapDrawable
	Dim bdraw As Bitmap
	bdraw.Initialize(File.DirAssets,"ic_folder_special_black_48dp.png")
	draw.Initialize(bdraw)
	liv.Initialize("liv")
	liv.Enabled=True
	liv.AddSingleLine("Text")
	liv.AddTwoLines("Text","Sub text..")	
End Sub

Sub store_check
	If kvs4sub.ContainsKey("off") Then
		arb.Checked=False
	
	Else
		arb.Checked=True
	
	End If
	
	If kvs4.ContainsKey("0")Then
		Log("AC_true->1")
		'act
		ACSpinner1.SelectedIndex=0
		Panel2.Color=c1
	End If
	If kvs4.ContainsKey("1")Then
		Log("AC_true->2")
		ACSpinner1.SelectedIndex=1
		Panel2.Color=c2
	Else
		'Activity.Color=c1
	End If
	If kvs4.ContainsKey("2")Then
		Log("AC_true->3")
		Panel2.Color=c3
		'CSpinner1.SelectedIndex=2
		'act
	Else
		'Activity.Color=c1
	End If
	If kvs4.ContainsKey("3")Then
		Log("AC_true->4")
		Panel2.Color=c4
		ACSpinner1.SelectedIndex=3
		'Activity.Color=c4
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("4")Then
		Log("AC_true->4")
		'act
		ACSpinner1.SelectedIndex=4
		Panel2.Color=c5
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("5")Then
		Log("AC_true->4")
		'Activity.Color=c6
		Panel2.Color=c6
		ACSpinner1.SelectedIndex=5
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("6")Then
		Log("AC_true->4")
		'act
		ACSpinner1.SelectedIndex=6
		Panel2.Color=c7
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("7")Then
		Log("AC_true->4")
		Panel2.Color=c8
		'act
		ACSpinner1.SelectedIndex=7
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("8")Then
		Log("AC_true->4")
		'act
		'Activity.Color=c9
		Panel2.Color=c9
		ACSpinner1.SelectedIndex=8
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("9")Then
		Log("AC_true->4")
		'Activity.Color=c10
		Panel2.Color=c10
	ACSpinner1.SelectedIndex=9
		'act
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("10")Then
		Log("AC_true->4")
		Panel2.Color=c11
		ACSpinner1.SelectedIndex=10
		'act
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("11")Then
		Log("AC_true->4")
		'act
		Panel2.Color=c12
		Label5.TextColor=Colors.White
		ACSpinner1.SelectedIndex=11
		'Activity.Color=c12
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("12")Then
		Log("AC_true->4")
		'act
		Panel2.Color=c13
		ACSpinner1.SelectedIndex=12
		'Activity.Color=c13
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("13")Then
		Log("AC_true->4")
		ACSpinner1.SelectedIndex=13
		Panel2.Color=c14
		'act
		'Activity.Color=c14
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("14")Then
		Log("AC_true->4")
		'act
		Panel2.Color=c15
		ACSpinner1.SelectedIndex=14
		'Activity.Color=c15
	Else
		'Activity.Color=c4
	End If
End Sub

Sub ACSpinner1_ItemClick (Position As Int, Value As Object)
	Value=ACSpinner1.SelectedIndex
	ACSpinner1.SelectedIndex=Position
	ACSpinner1.Prompt="Select Color:"
	 ACSpinner1.DropdownBackgroundColor= ACSpinner1.THEME_LIGHT
	If Position=0 Then
		kvs4.DeleteAll
		kvs4.PutSimple("0",Value)
		ToastMessageShow(Value,False)
		Panel2.Color=c16
		Log("Is in Store")
	End If
	If Position=1 Then
		kvs4.DeleteAll
		kvs4.PutSimple("1",Value)
		ToastMessageShow(Value,False)
		Log("New Store")
		Panel2.Color=c2
	End If
	If Position=2 Then
		kvs4.DeleteAll
		kvs4.PutSimple("2",Value)
		ToastMessageShow(Value,False)
		Log("Now Stored")
		Panel2.Color=c3
		Activity.Invalidate
	End If
	If Position=3 Then
		kvs4.DeleteAll
		kvs4.PutSimple("3",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c4
		Activity.Invalidate
	End If
	If Position=4 Then
		kvs4.DeleteAll
		kvs4.PutSimple("4",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c5
	End If
	If Position=5 Then
		kvs4.DeleteAll
		kvs4.PutSimple("5",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c6
	End If
	If Position=6 Then
		kvs4.DeleteAll
		kvs4.PutSimple("6",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c7
	End If
	If Position=7 Then
		kvs4.DeleteAll
		kvs4.PutSimple("7",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c8
	End If
	If Position=8 Then
		kvs4.DeleteAll
		kvs4.PutSimple("8",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c9
	End If
	If Position=9 Then
		kvs4.DeleteAll
		kvs4.PutSimple("9",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c10
	End If
	If Position=10 Then
		kvs4.DeleteAll
		kvs4.PutSimple("10",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c11
		Activity.Invalidate
	End If
	If Position=11 Then
		kvs4.DeleteAll
		kvs4.PutSimple("11",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c12
		Label5.TextColor=Colors.White
	End If
	If Position=12 Then
		kvs4.DeleteAll
		kvs4.PutSimple("12",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c13
	End If
	If Position=13 Then
		kvs4.DeleteAll
		kvs4.PutSimple("13",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c14
	End If
	If Position=14 Then
		kvs4.DeleteAll
		kvs4.PutSimple("14",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		Panel2.Color=c15
	End If
End Sub

