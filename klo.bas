Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Code Module created by trOw
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region
	'#Extends: android.support.v7.app.AppCompatActivity
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
'	Dim tim As Timer
'	Dim counter As Int
	Dim sql As SQL
	Dim t1 As Timer
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	'Dim count As Int=0
	'Private ListView1 As ListView
	Dim l1,l2,l3 As Label

	Private Panel1 As Panel
	Private Panel2 As Panel
	'Dim cc As Int
	'Dim loli As List
	Dim m As Map
	Dim time2 As String
	Dim bat As Batut
	Dim volt,temp,usb,ac As String
	Dim device As PhoneEvents
	Dim pak1 As PackageManager
	Dim kvs2,kvs3,kvs4,kvsvolt,kvstemp As KeyValueStore
	Dim dt As List
	Dim kl As List
	Dim suc As List
	Dim g,g2 As Graph
	Dim ls As LinePoint
	Dim batt,pl,pk As Bitmap
	Dim mcl As MaterialColors
	Dim popa As ACPopupMenu
	Private ACButton1 As ACButton
	Dim level As Int
	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16 As Int
	'Private dev As PhoneEvents

	Private ACButton2 As ACButton
	Dim osstat As OSStats
	Dim metric As MSOS

'	Private MultiBubbleChart1 As MultiBubbleChart
'	Private mbc1 As BarChart
'	Private mlc1 As LineChart
	Private Panel3 As Panel
	Dim fn2 As String
	Dim fg2 As Int
	Dim LD2 As LineData
	Dim fn As String
	Dim fg As Int
	Dim LD As LineData
	Private sm As SlidingMenuStd
	Private cb1 As Circlebutton
	Dim count As Int 
	time2=DateTime.Time(DateTime.Now)
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("2")
	Activity.Title=pak1.GetApplicationLabel("com.batcat")&" - "&pak1.GetVersionName("com.batcat")
	Activity.AddMenuItem("Clear Stats","ccl")
	Activity.AddMenuItem("Exit","cl")
	suc.Initialize
	l1.Initialize("l1")
	l2.Initialize("l2")
	l3.Initialize("l3")
	device.Initialize("device")
	'###############Colors##################
	c1=mcl.md_light_blue_A700
	c2=mcl.md_amber_A700
	c3=mcl.md_lime_A700
	c4=mcl.md_teal_A700
	Activity.Color=mcl.md_white_1000
	'dev.Initialize("dev")
	t1.Initialize("t1",1000) 
	'################## second end#####################
	m.Initialize
	bat.Initialize
	
	kl.Initialize
	dt.Initialize
	ls.Initialize
		kvs2.Initialize(File.DirDefaultExternal, "datastore_2")
		kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
		kvs3.Initialize(File.DirDefaultExternal, "datastore_3")
		kvsvolt.Initialize(File.DirDefaultExternal, "datastore_volt")
		kvstemp.Initialize(File.DirDefaultExternal, "datastore_temp")
	
	Dim bbat As String = bat.BatteryInformation(0)
	volt=bat.BatteryInformation(7)/1000
	temp=bat.BatteryInformation(6)/10
	usb =bat.BatteryInformation(9)
	ac =bat.BatteryInformation(8)
	count=0
	G.Initialize
	g2.Initialize
	If FirstTime=True Then 
		kvs2.PutSimple(bbat,time2)
		kvstemp.PutSimple(temp,time2)
		Msgbox("Wenn die Statistik zum ersten Mal geladen wird, kann es eine Weile dauern, bis die Werte korrekt angezeigt werden, zB: V,C°,% der letzten 10 Einträge. Bitte beachte das die Werte nur ca sind da sie immer leicht Zeit versetzt gespeichert werden und nicht  den 'Live' Zustand anzeigen!","Wichtig!")
	End If
	
	batt=LoadBitmap(File.DirAssets,"Battery Icons - White 64px (40).png")
	pl=LoadBitmap(File.DirAssets,"Battery Icons - White 64px (28).png")
	'#################Menu###############################################
	popa.Initialize("popa",Panel3)
	popa.GetMenu
	Dim bd,bd1 As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets,"multiply-1.png"))
	bd1.Initialize(LoadBitmap(File.DirAssets,"warning.png"))
	popa.AddMenuItem(0,"Reload Stats",bd1)
	popa.AddMenuItem(1,"Schließen",bd)
	'########################################################################
	
	
	cb1.ButtonColor=mcl.md_lime_A200
	cb1.ImageBitmap=LoadBitmap(File.DirAssets,"Bar-chart48.png")
	t1.Initialize("t1",1000)
	t1.Enabled=False
	build
	store_check
	c_start
	't1_Tick
End Sub

Sub Activity_Resume 
'	t1.Enabled=True
'	build
'	store_check
	c_start
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	t1.Enabled=False
End Sub

Sub t1_Tick
	count=count+1
	If count = 100 Then
		t1.Enabled=False 
		Log("100")
		ToastMessageShow("lade neue werte...",False)
		c_start
	End If		
End Sub

Sub cb1_Click
	If Not (Panel3.Visible=True) Then
		'Panel3.Visible=True	
		Panel2.SendToBack
		Panel1.SendToBack
		Panel2.SetVisibleAnimated(200,False)
		Panel1.SetVisibleAnimated(200,False)
		Panel3.SetVisibleAnimated(200,True)
		Activity.Color=Colors.ARGB(190,0,0,0)
		Else
			Panel2.SetVisibleAnimated(250,True)
			Panel1.SetVisibleAnimated(250,True)
			Panel3.SetVisibleAnimated(250,False)
		store_check
			'Panel3.Visible=False
	End If
End Sub

Sub build
	Dim lv As ListView
	lv.Initialize("lv")
	Dim lva,lvb As Label
	lva.Initialize("lva")
	lvb.Initialize("lvb")
	lva=lv.TwoLinesAndBitmap.Label
	lvb=lv.TwoLinesAndBitmap.SecondLabel
	lva.TextSize=13
	lva.TextColor=mcl.md_black_1000
	lvb.TextSize=11
	lvb.TextColor=mcl.md_grey_700
	lv.TwoLinesAndBitmap.ImageView.Height=40dip
	lv.TwoLinesAndBitmap.ImageView.Width=40dip
	lv.TwoLinesAndBitmap.ItemHeight=60dip
	lv.Enabled=True
	Panel3.AddView(lv,0dip,0dip,60%x,100%y)
	lv.AddTwoLinesAndBitmap2("Reset C°","setzt alle Werte wieder auf 0 und löscht die Temperatur Ansicht",LoadBitmap(File.DirAssets,"warning.png"),0) 
	lv.AddTwoLinesAndBitmap2("Reset Level","setzt alle Werte wieder auf 0 und löscht die Batterie-Level Ansicht",LoadBitmap(File.DirAssets,"warning.png"),1) 
	lv.AddTwoLinesAndBitmap2("OS Power","Android Power Menü",LoadBitmap(File.DirAssets,"Battery.png"),2)
	lv.AddTwoLinesAndBitmap2("Close","zurück zum Hauptmenü",LoadBitmap(File.DirAssets,"multiply-1.png"),3)
End Sub

Sub lv_ItemClick (Position As Int, Value As Object)
	Dim ipo As Intent
	If Value=0 Then 
		ProgressDialogShow("lade...")
		ccl_click
		cb1_Click
		Activity.Invalidate
	End If
	If Value=1 Then 
		ProgressDialogShow("lade...")
		graph_clear
		cb1_Click
		Activity.Invalidate
	End If
	If Value=2 Then 
		ipo.Initialize( "android.intent.action.POWER_USAGE_SUMMARY", "")
		cb1_Click
		Activity.Invalidate
	End If
	If Value= 3 Then 
		Activity_KeyPress(KeyCodes.KEYCODE_BACK)
	End If
End Sub

Sub popa_ItemClicked (Item As ACMenuItem)
	If Item = popa.GetItem(0) Then
		ACButton1_Click
	End If
	If Item = popa.GetItem(1) Then
		Activity.Finish
	End If
End Sub

Sub ccl_click
	kvstemp.DeleteAll
	kvsvolt.DeleteAll
	kvstemp.PutSimple(bat.BatteryInformation(6)/10,time2)
	ToastMessageShow("warte auf Aktuelle werte..!",False)
	Activity.Invalidate
	CallSubDelayed(Main,"reb_start")
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub graph_clear
	'ProgressDialogShow("lade...")
	kvs2.DeleteAll
	kvs2.PutSimple(bat.BatteryInformation(0),time2)
	ToastMessageShow("warte auf Aktuelle werte..!",False)
	Activity.Invalidate
	CallSubDelayed(Main,"reb_start")
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub c_start
	If kvs4.IsInitialized=True Then
		Else
		kvs2.Initialize(File.DirDefaultExternal, "datastore_2")
		kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
		kvs3.Initialize(File.DirDefaultExternal, "datastore_3")
		kvsvolt.Initialize(File.DirDefaultExternal, "datastore_volt")
		kvstemp.Initialize(File.DirDefaultExternal, "datastore_temp")
	End If
	t1.Enabled=True
	chart_2
	chart_start
End Sub


Sub chart_start
	If LD.IsInitialized Then
	Else
		LD.Initialize
	End If
	LD.Target =Panel2
	If kvs4.ContainsKey("5")Then
		Charts.AddLineColor(LD, Colors.Blue) 'First line color
	Else
		Charts.AddLineColor(LD, Colors.Red) 'First line color
	End If
	
	
	For Each h Step 3 As String  In kvs2.ListKeys 
		fg=h
		fn=kvs2.GetSimple(h) 
		Log("Map Key-> "&fg)
		'Charts.AddBarPoint(BD, fn, Array As Float(fg))
		Charts.AddLinePoint(LD, fn,fg, True)
		'Charts.AddLineMultiplePoints(LD, fn, Array As Float(fg),True)
	Next
	G.Title = "BC Live Stats"
	G.XAxis = ""
	G.YAxis = "Level in %"
	G.YStart = 0
	G.YEnd = 100
	G.YInterval = 5
	G.AxisColor = Colors.White
	'Charts.DrawBarsChart(G, BD, Colors.Transparent)
	Charts.DrawLineChart(G, LD, Colors.Transparent)
	
End Sub

Sub chart_2
	
	If LD2.IsInitialized Then
	Else
		LD2.Initialize
	End If
	LD2.Target =Panel1
	'LD2.BarsWidth=10
	'Charts.AddLineColor(LD, Colors.Red) 'First line color
	
	Charts.AddlineColor(LD2, mcl.md_light_blue_A700) 'Second line color
	For Each h Step 3  As String  In kvstemp.ListKeys  
		fg2=h  
		fn2=kvstemp.GetSimple(h) 
		Log("Bar Key-> "&h)
		'Charts.AddBarPoint(BD, fn, Array As Float(fg))
		Charts.AddLinePoint(LD2, fn2,fg2,True)
		'Charts.AddLineMultiplePoints(LD, fn, Array As Float(fg),True)
	Next
	g2.Title = ""
	g2.XAxis = ""
	g2.YAxis = "Temp. in C°"
	g2.YStart = 10
	g2.YEnd = 60
	g2.YInterval = 5
	g2.AxisColor = Colors.White
	'Charts.DrawBarsChart(G, BD, Colors.Transparent)
	Charts.DrawLineChart(g2, LD2, Colors.Transparent)

End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
		ToastMessageShow("BCT - Backround  Statistic started..",False)
			SetAnimation.setanimati("extra_in", "extra_out")
	End If
	Return(True)
End Sub

Sub ACButton1_Click
	graph_clear
End Sub


Sub store_check
	c1=mcl.md_light_blue_A400
	c2=mcl.md_amber_A400
	c3=mcl.md_white_1000
	c4=mcl.md_teal_A400
	c5=mcl.md_deep_purple_A400
	c6=mcl.md_red_A700
	c7=mcl.md_indigo_A400
	c8=mcl.md_blue_A400
	c9=mcl.md_orange_A700
	c10=mcl.md_grey_600
	c11=mcl.md_green_A400
	c12=mcl.md_black_1000
	c13=mcl.md_light_green_A400
	c14=mcl.md_cyan_A400
	c15=mcl.md_blue_grey_400
	c16=mcl.md_light_blue_A400
	If kvs4.ContainsKey("0")Then
		Log("AC_true->1")
		Activity.Color=c1
	End If
	If kvs4.ContainsKey("1")Then
		Log("AC_true->2")
		Activity.Color=c2
	Else
		'Activity.Color=c1
	End If
	If kvs4.ContainsKey("2")Then
		Log("AC_true->3")
		Activity.Color=c3
		g.AxisColor = Colors.Black
		g2.AxisColor = Colors.Black
	Else
		'Activity.Color=c1
	End If
	If kvs4.ContainsKey("3")Then
		Log("AC_true->4")
		Activity.Color=c4
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("4")Then
		Log("AC_true->4")
		Activity.Color=c5
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("5")Then
		Log("AC_true->4")
		Activity.Color=c6
		 'Second line color
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("6")Then
		Log("AC_true->4")
		Activity.Color=c7
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("7")Then
		Log("AC_true->4")
		Activity.Color=c8
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("8")Then
		Log("AC_true->4")
		Activity.Color=c9
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("9")Then
		Log("AC_true->4")
		Activity.Color=c10
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("10")Then
		Log("AC_true->4")
		Activity.Color=c11
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("11")Then
		Log("AC_true->4")
		Activity.Color=c12
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("12")Then
		Log("AC_true->4")
		Activity.Color=c13
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("13")Then
		Log("AC_true->4")
		Activity.Color=c14
	Else
		'Activity.Color=c4
	End If
	If kvs4.ContainsKey("14")Then
		Log("AC_true->4")
		Activity.Color=c15
	Else
		'Activity.Color=c4
	End If
	Activity.Invalidate
End Sub



Sub mlc1_value_selected(index As Int, value As Float)
	
End Sub