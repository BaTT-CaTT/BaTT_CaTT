Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
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
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	'Dim count As Int=0
	Private ListView1 As ListView
	Dim l1,l2,l3 As Label

	Private Panel1 As Panel
	Private Panel2 As Panel
	'Dim cc As Int
	'Dim loli As List
	Dim m As Map
	Dim time2 As String
	Dim bat As Batut
	Dim volt,temp,usb,ac As String
	'Dim device As PhoneEvents
	Dim pak1 As PackageManager
	Dim kvs2,kvs3,kvs4 As KeyValueStore
	Dim dt As List
	Dim kl As List
	Dim suc As List
	Dim g As Graph
	Dim ls As LinePoint
	Dim batt,pl,pk As Bitmap
	Dim mcl As MaterialColors
	Dim popa As ACPopupMenu
	Private ACButton1 As ACButton
	Dim level As Int
	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15 As Int
	Private dev As PhoneEvents
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
	'###############Colors##################
	c1=mcl.md_light_blue_A700
	c2=mcl.md_amber_A700
	c3=mcl.md_lime_A700
	c4=mcl.md_teal_A700
	Activity.Color=c1
	

	dev.Initialize("dev")
	
	'################## second end#####################
	m.Initialize
	bat.Initialize

	kl.Initialize
	dt.Initialize
	ls.Initialize
	l1=ListView1.SingleLineLayout.Label
	l1.TextSize=15
	l1.TextColor=Colors.White
	l2=ListView1.TwoLinesLayout.Label
	l3=ListView1.TwoLinesLayout.SecondLabel
	l2.TextSize=18
	l3.TextSize=12
	l2.TextColor=Colors.Black
	l3.TextColor=Colors.Cyan
	ListView1.SingleLineLayout.ItemHeight=90
	volt=bat.BatteryInformation(7)/1000
	temp=bat.BatteryInformation(6)/10
	usb =bat.BatteryInformation(9)
	ac =bat.BatteryInformation(8)
	G.Initialize
	'#######Listview Settings###########
	Dim la,la1,la2 As Label
	la.Initialize("la")
	la1.Initialize("la1")
	la2.Initialize("la2")
	la2=ListView1.TwoLinesLayout.SecondLabel
	la2.TextSize=11
	la2.TextColor=Colors.ARGB(240,255,255,255)
	la1=ListView1.TwoLinesAndBitmap.SecondLabel
	la1.TextSize=10
	la1.TextColor=Colors.LightGray
	ListView1.TwoLinesAndBitmap.ImageView.Height=32dip
	ListView1.TwoLinesAndBitmap.ImageView.Width=32dip
	ListView1.TwoLinesAndBitmap.ItemHeight=50dip
	If FirstTime=True Then 
		Msgbox("If the statistics is loaded for the first time it can take a while until the values ​​are displayed correctly, for example: current time and battery level -> 100% with the last 10 entries.","Please Note..!")	
	End If
	kvs2.Initialize(File.DirDefaultExternal, "datastore_2")
	kvs3.Initialize(File.DirDefaultExternal, "datastore_3")
	kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
	time2=DateTime.Time(DateTime.Now)
	batt=LoadBitmap(File.DirAssets,"Battery Icons - White 64px (40).png")
	pl=LoadBitmap(File.DirAssets,"Battery Icons - White 64px (28).png")
	'#################Menu###############################################
	popa.Initialize("popa",Panel2)
	Dim bd,bd1 As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets,"ic_clear_black_48dp.png"))
	bd1.Initialize(LoadBitmap(File.DirAssets,"ic_autorenew_black_48dp.png"))
	popa.AddMenuItem(0,"Reload Stats",bd1)
	popa.AddMenuItem(1,"Schließen",bd)
	'########################################################################
	store_check
	c_start

End Sub

Sub Activity_Resume 
c_start
store_check
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	'tim.Enabled=False
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
	level  = bat.BatteryInformation(0)
		kvs2.DeleteAll
	Log("put-> "&level&"%")
	kvs2.PutSimple(level,time2)
		ToastMessageShow("stats bitte neustarten für Aktuelle werte..!",False)
		Log(kvs2.ListKeys&" - clear")
	c_start
End Sub

Sub c_start
	If kvs2.IsInitialized Then
		Log("KVS -> true")
		
	Else
		kvs2.Initialize(File.DirDefaultExternal, "datastore_2")
		kvs3.Initialize(File.DirDefaultExternal, "datastore_3")
		
	End If
	
	get_log
	bat_acusb
	bat_health
	chart_start
End Sub

Sub cl_click
	Activity.Finish
End Sub

Sub get_log
	level=bat.BatteryInformation(0)
	ListView1.Clear
	ListView1.AddTwoLinesAndBitmap("Bat. Level:",level&"%",batt)
	ListView1.AddTwoLinesAndBitmap("Bat. Temperature:",temp&"°C",batt)
	Return
End Sub

Sub bat_acusb
	pk=LoadBitmap(File.DirAssets,"Battery Icons - White 64px (35).png")
	If ac=1 Then
		ListView1.AddTwoLinesAndBitmap("Bat. AC-Plugged:","@: "&volt&" V",pk)
	End If
	If ac = 0 Or usb=0 Then
		ListView1.AddTwoLinesAndbitmap("Bat. Discharge:","@: "&volt&" V",pl)
	End If
	
	If usb=1 Then
		ListView1.AddTwoLinesandbitmap("Bat. USB-Plugged:","@: "&volt&" V",pk)

	End If
	Return
End Sub

Sub bat_health
	If bat.BatteryInformation(2)=1 Then
		ListView1.AddTwoLines("Bat. Health:","Super")
	Else
		If 	bat.BatteryInformation(2) = 2 Then
			ListView1.AddTwoLinesandbitmap("Bat. Health:","Good",LoadBitmap(File.DirAssets,"Battery icons - Colorful 64px (32).png"))
		Else
			If bat.BatteryInformation(2) < 2 Then
				ListView1.AddTwoLines("Bat. Health:","Your Battery seems to be in a BAD-Discharge Status please recover a new one for your android Device!")
			End If
		End If
	End If
	Return
End Sub


Sub chart_start
	g.Initialize
	level=bat.BatteryInformation(0)
	Dim fn As String
	Dim fg As String

	Dim BD As BarData
	BD.Initialize
	BD.Target = Panel2
	BD.BarsWidth = 5dip
	BD.Stacked = True 'Makes it a stacked bars chart
	Charts.AddBarColor(BD, MakeTransparent(Colors.white, 180))
	For Each h As String  In kvs2.ListKeys
		
		fg=h
		fn=kvs2.GetSimple(h)
		Log("Map Key-> "&fg)
		Charts.AddBarPoint(BD, fn, Array As Float(fg))
	Next
	G.Title = "Power Chart"
	G.XAxis = ""
	G.YAxis = "Values"
	G.YStart = 0
	G.YEnd = 100
	G.YInterval = 10
	G.AxisColor = Colors.White
	Charts.DrawBarsChart(G, BD, Colors.Transparent)
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
		ToastMessageShow("BCT - Backround  Statistic started..",False)
	End If
	Return(True)
End Sub


Sub store_check
	c1=mcl.md_light_blue_A700
	c2=mcl.md_amber_A700
	c3=mcl.md_lime_A700
	c4=mcl.md_teal_A700
	c5=mcl.md_deep_purple_A700
	c6=mcl.md_red_A700
	c7=mcl.md_indigo_A700
	c8=mcl.md_blue_A700
	c9=mcl.md_orange_A700
	c10=mcl.md_grey_700
	c11=mcl.md_green_A700
	c12=mcl.md_light_green_A700
	c13=mcl.md_yellow_A700
	c14=mcl.md_cyan_A700
	c15=mcl.md_blue_grey_700
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

Sub MakeTransparent(Color As Int, Alpha As Int) As Int
	Return Bit.And(Color, Bit.Or(0x00FFFFFF, Bit.ShiftLeft(Alpha, 24)))
End Sub


Sub ACButton1_Click
	ccl_click
End Sub

 
Sub dev_BatteryChanged (level1 As Int, Scale As Int, Plugged As Boolean, Intent As Intent)
	If kvs3.ListKeys.Size>20 Then 
		kvs3.DeleteAll
		c_start
	End If
	Dim vl As List 
	vl.Initialize
	For v = 0 To Scale Step 2
		vl.Add(v) 
	If level1=v Then 
		store_check
		c_start
	End If
Next 
End Sub

'########sub bardata old#############################

'Dim BD As BarData
'	BD.Initialize
'	BD.Target = Panel2
'	BD.BarsWidth = 10dip
'	BD.Stacked = True 'Makes it a stacked bars chart
'	Charts.AddBarColor(BD, MakeTransparent(Colors.white, 200)) 
'	
'	For Each j As String In kl
'		Log(j)
'		'mop.Put
'		For Each d As Int  In dt
'			Log(d)
'			mop.Put(d,j)
'		Next
'	Next
'	For Each h As String  In mop.Keys
'		fg=mop.GetKeyAt(h)
'		fn=mop.Get(h)
'		Charts.AddBarPoint(BD, d, Array As Float(j))
'	Next
'	G.Title = "Power Chart"
'	G.XAxis = "%"
'	G.YAxis = "Values"
'	G.YStart = 0
'	G.YEnd = 100
'	G.YInterval = 10
'	G.AxisColor = Colors.White
'	Charts.DrawBarsChart(G, BD, Colors.Transparent)


'########sub bardata old#############################