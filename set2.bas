Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Code Module created by trOw
'STATUS: Experimental
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	Private set As SettingUI
	Private mcl As MaterialColors
	Private kvs4,kvs4sub As KeyValueStore
	Private clist As List 
	Private cb As CheckBox
	Private cs As ACSpinner
	Private cc1,cc2,cc3,cc4,cc5,cc6,cc7,cc8,cc9,cc10,cc11,cc12,cc13,cc14,cc15,cc16 As ColorDrawable
	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16 As Int
	Dim panel2 As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
	kvs4sub.Initialize(File.DirDefaultExternal, "datastore_sub_4")
	Activity.LoadLayout("6_sub")
	clist.Initialize
	cb.Initialize("cb")
	cs.Initialize("cs")
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
	
	
	c_start
	set_set
	stor_check
End Sub

Sub Activity_Resume
	 stor_check
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Activity.Finish
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub c_start

	cs.Prompt="Wähle Farbe"
	cs.Add2("light blue",cc1)
	cs.Add2("Amber",cc2)
	cs.Add2("White(Arctic)",cc3)'-<<<<<<change on main bardata and textflow!
	cs.Add2("teal",cc4)
	cs.Add2("purple(dark)",cc5)
	cs.Add2("red",cc6)
	cs.Add2("indigo",cc7)
	cs.Add2("blue",cc8)
	cs.Add2("orange",cc9)
	cs.Add2("grey",cc10)
	cs.Add2("green",cc11)
	cs.Add2("Black(Ultra)",cc12)'-<<<<<<change on main bardata and textflow!
	cs.Add2("yellow",cc13)
	cs.Add2("cyan",cc14)
	cs.Add2("blue grey",cc15)
	clist.Add(c1)
	clist.Add(c2)
	clist.Add(c3)
	clist.Add(c4)
	clist.Add(c5)
	clist.Add(c6)
	clist.Add(c7)
	clist.Add(c8)
	clist.Add(c9)
	clist.Add(c10)
	clist.Add(c11)
	clist.Add(c12)
	clist.Add(c13)
	clist.Add(c14)
	clist.Add(c15)
End Sub


Sub set_set
	set.Initialize(Activity,mcl.md_blue_A400,"OpenSans.ttf","Settings Menu","Choose Options")
	set.AddCheckbox("cb","Service Icon",True)
	set.SetKeyString("cb",cb_CheckedChange(True))
	set.AddDivider
	For Each b As String In clist
	set.AddSpinner("cs","Select Backround Color:",clist.Get(b))
	Next
	set.AddDivider
	set.AddListview("lv","Test View",clist)
	set.AddDivider
	set.ApplyHeightPanel
End Sub

Sub cb_CheckedChange(Checked As Boolean)
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
End Sub
Sub stor_check
	If kvs4sub.ContainsKey("off") Then
		set.SetKeyBoolean("cb",False)
		cb_CheckedChange(False)
		
	Else
		set.SetKeyBoolean("cb",True)
	
	End If
	If kvs4.ContainsKey("0")Then
		Log("AC_true->1")
		'act
		set.SetKeyBoolean("cs",cs.SelectedIndex)
		''''Panel2.color=c1
	End If
	If kvs4.ContainsKey("1")Then
		Log("AC_true->2")
		cs.SelectedIndex=1
		''Panel2.color=c2
		Activity.Color=c1
	End If
	If kvs4.ContainsKey("2")Then
		Log("AC_true->3")
		''Panel2.color=c3
		'CSpinner1.SelectedIndex=2
		'act
	Else
		Activity.Color=c1
	End If
	If kvs4.ContainsKey("3")Then
		Log("AC_true->4")
		''Panel2.color=c4
		cs.SelectedIndex=3
		Activity.Color=c4
	Else
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("4")Then
		Log("AC_true->4")
		'act
		cs.SelectedIndex=4
		'''Panel2.color=c5
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("5")Then
		Log("AC_true->4")
		Activity.Color=c6
		'''Panel2.color=c6
		cs.SelectedIndex=5
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("6")Then
		Log("AC_true->4")
		'act
		cs.SelectedIndex=6
		'''Panel2.color=c7
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("7")Then
		Log("AC_true->4")
		'''Panel2.color=c8
		'act
		cs.SelectedIndex=7
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("8")Then
		Log("AC_true->4")
		'act
		Activity.Color=c9
		'''Panel2.color=c9
		cs.SelectedIndex=8
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("9")Then
		Log("AC_true->4")
		Activity.Color=c10
		'''Panel2.color=c10
		cs.SelectedIndex=9
		'act
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("10")Then
		Log("AC_true->4")
		'''Panel2.color=c11
		cs.SelectedIndex=10
		'act
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("11")Then
		Log("AC_true->4")
		'act
		'''Panel2.color=c12
		'Label5.TextColor=Colors.White
		cs.SelectedIndex=11
		Activity.Color=c4
	End If
	If kvs4.ContainsKey("12")Then
		Log("AC_true->4")
		'act
		'''Panel2.color=c13
		cs.SelectedIndex=12
		Activity.Color=c13
	End If
	If kvs4.ContainsKey("13")Then
		Log("AC_true->4")
		cs.SelectedIndex=13
		'''Panel2.color=c14
		'act
		Activity.Color=c14
	End If
	If kvs4.ContainsKey("14")Then
		Log("AC_true->4")
		'act
		'''Panel2.color=c15
		cs.SelectedIndex=14
		Activity.Color=c15
	End If
End Sub

Sub ACSpinner1_ItemClick (Position As Int, Value As Object)
	Value=cs.SelectedIndex
	cs.SelectedIndex=Position
	'ACSpinner1.Prompt="Select Color:"
	'cs.DropdownBackgroundColor= ACSpinner1.THEME_LIGHT
	If Position=0 Then
		kvs4.DeleteAll
		kvs4.PutSimple("0",Value)
		ToastMessageShow(Value,False)
		'Panel2.color=c16
		Log("Is in Store")
	End If
	If Position=1 Then
		kvs4.DeleteAll
		kvs4.PutSimple("1",Value)
		ToastMessageShow(Value,False)
		Log("New Store")
		'Panel2.color=c2
	End If
	If Position=2 Then
		kvs4.DeleteAll
		kvs4.PutSimple("2",Value)
		ToastMessageShow(Value,False)
		Log("Now Stored")
		'Panel2.color=c3
		Activity.Invalidate
	End If
	If Position=3 Then
		kvs4.DeleteAll
		kvs4.PutSimple("3",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c4
		Activity.Invalidate
	End If
	If Position=4 Then
		kvs4.DeleteAll
		kvs4.PutSimple("4",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c5
	End If
	If Position=5 Then
		kvs4.DeleteAll
		kvs4.PutSimple("5",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c6
	End If
	If Position=6 Then
		kvs4.DeleteAll
		kvs4.PutSimple("6",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c7
	End If
	If Position=7 Then
		kvs4.DeleteAll
		kvs4.PutSimple("7",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c8
	End If
	If Position=8 Then
		kvs4.DeleteAll
		kvs4.PutSimple("8",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c9
	End If
	If Position=9 Then
		kvs4.DeleteAll
		kvs4.PutSimple("9",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c10
	End If
	If Position=10 Then
		kvs4.DeleteAll
		kvs4.PutSimple("10",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c11
		Activity.Invalidate
	End If
	If Position=11 Then
		kvs4.DeleteAll
		kvs4.PutSimple("11",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c12
		'Label5.TextColor=Colors.White
	End If
	If Position=12 Then
		kvs4.DeleteAll
		kvs4.PutSimple("12",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c13
	End If
	If Position=13 Then
		kvs4.DeleteAll
		kvs4.PutSimple("13",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c14
	End If
	If Position=14 Then
		kvs4.DeleteAll
		kvs4.PutSimple("14",Value)
		ToastMessageShow(Value,False)
		Log("Stored ---")
		'Panel2.color=c15
	End If
End Sub