Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'pman Code Module created by trOw
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	Private pak As PackageManager
	Dim catdel As CacheCleaner
End Sub

Sub Globals
	Private applist As ListView
	Private name,apath,l,Types(1),packName As String
	Private icon As BitmapDrawable 
	Private sublist,data,del,clist As List 
	Dim args(1) As Object
	Dim Obj1, Obj2, Obj3 As Reflector
	Dim size,flags As Int
	Private os As OperatingSystem
	Dim mcl As MaterialColors
	Private abf1 As ACFlatButton
	Private abf2 As ACFlatButton
	Private Label1 As Label
	Private subapp As ListView
	Private Panel1 As Panel
	Private ion As Object
	Private kvdata As KeyValueStore
	Private op As OperatingSystem
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("7")
	os.Initialize("os")
	sublist.Initialize
	data.Initialize
	del.Initialize
	clist.Initialize
	catdel.initialize("catdel")
	kvdata.Initialize(File.DirDefaultExternal,"datastore_data")
	op.Initialize("op")
	'##############Initialize the App cach########################
	
	
	'#############################################################
	Dim la,lb,lc,ld As Label 
	la =applist.TwoLinesAndBitmap.Label
	lc =subapp.SingleLineLayout.Label
	lb= applist.TwoLinesAndBitmap.SecondLabel
	ld= subapp.TwoLinesAndBitmap.SecondLabel
	applist.TwoLinesAndBitmap.ItemHeight=60dip
	applist.TwoLinesAndBitmap.ImageView.Height=50dip
	la.TextColor=mcl.md_black_1000
	lc.TextColor=mcl.md_black_1000
	la.TextSize=18
	lc.TextSize=15
	lb.TextSize=13
	ld.TextSize=13
	lb.TextColor=mcl.md_light_blue_300
	ld.TextColor=mcl.md_light_blue_300
	abf1.Color=mcl.md_red_200
	abf2.Color=mcl.md_grey_400
	abf1.Text="deinstall"
	abf2.Text="close"
	app_manage
End Sub

Sub Activity_Resume
	
	
	app_manage
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
		SetAnimation.setanimati("extra_in", "extra_out")
	End If
	Return(True)
End Sub


Sub catdel_OnScanStarted
	
End Sub

Sub catdel_onScanProgress (Current As Int , Total As Int)
	ProgressDialogShow("Suche &"&Current&"Apps:: "&Total&" gefunden")
End Sub

Sub catdel_onScanCompleted (AppsList As Object)
	'time=DateTime.Time(DateTime.Now)
	ProgressDialogHide
	Dim totalsize As Long = 0
	Dim pm As PackageManager
	
	Try
		Dim lu As List = AppsList
		If lu.Size=0 Then
			'Cache is Empty
			clist.Add(op.formatSize("0000"))
			app_manage
			Return
		End If
		For n = 0 To lu.Size-1
			Dim app() As Object = lu.Get(n)
			If app(1) = "com.android.systemui" Then Continue 'This Pakage Have No Icon In Some Android 5
			
			
			totalsize = totalsize+app(2)
			clist.Clear
			clist.Add(app(0)&totalsize)
			clist.Add(app(2)&totalsize)
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","cdata.txt",clist)
			app_manage
		Next
		
	Catch
		Log(LastException.Message)
		'catlist.Add(LastException.Message)
	End Try
End Sub

Sub catdel_onCleanStarted
	
End Sub



Sub abf1_Click
	Dim ndel As String 
	ndel=del.Get(0)
	If kvdata.ContainsKey("data") Then 
		kvdata.DeleteAll
		kvdata.PutSimple("data",ndel)
		CallSubDelayed(datacount,"start")
		panset
	Else
		kvdata.PutSimple("data",ndel)
		panset
		CallSubDelayed(datacount,"start")
	End If 
End Sub

Sub res_bo
	CallSub(Main,"rebound")
End Sub
Sub abf2_Click
	panset
End Sub

Sub applist_ItemClick (Position As Int, Value As Object)
	subapp.Clear
	del.Clear 
		For Each  f As String In data
			packName = f
		If Value=packName Then
			name= pak.GetApplicationLabel(f)
	size=File.Size(GetParentPath(GetSourceDir(GetActivitiesInfo(packName))),GetFileName(GetSourceDir(GetActivitiesInfo(packName))))
	icon=pak.GetApplicationIcon(packName)
	apath=GetParentPath(GetSourceDir(GetActivitiesInfo(packName)))
	subapp.AddTwoLinesAndBitmap("","",icon.Bitmap)
	subapp.AddSingleLine("Größe: "&FormatFileSize(size))
	subapp.AddSingleLine2(packName,0)
	del.Add(packName)
	subapp.AddSingleLine(apath)
	Label1.Text=name
		panset
	End If 
	Next
End Sub

Sub applist_ItemLongClick (Position As Int, Value As Object)
	
End Sub
Sub panset
	If Not (Panel1.Visible=True) Then 
		Panel1.Visible=True
		Else
			Panel1.Visible=False
	End If
End Sub


Sub panel1_Touch (Action As Int, X As Float, Y As Float)

End Sub


Sub app_manage
	applist.Clear
	data.Clear
	sublist=pak.GetInstalledPackages
	Obj1.Target = Obj1.GetContext
	Obj1.Target = Obj1.RunMethod("getPackageManager") ' PackageManager
	Obj2.Target = Obj1.RunMethod2("getInstalledPackages", 0, "java.lang.int") ' List<PackageInfo>
	size = Obj2.RunMethod("size")
	For i = 0 To size -1
		Obj3.Target = Obj2.RunMethod2("get", i, "java.lang.int") ' PackageInfo
		size = Obj2.RunMethod("size")
		Obj3.Target = Obj3.GetField("applicationInfo") ' ApplicationInfo
		flags = Obj3.GetField("flags")
		packName = Obj3.GetField("packageName")
		If Bit.And(flags, 1)  = 0 Then
			'app is not in the system image
			args(0) = Obj3.Target
			Types(0) = "android.content.pm.ApplicationInfo"
			name = Obj1.RunMethod4("getApplicationLabel", args, Types)
			icon = Obj1.RunMethod4("getApplicationIcon", args, Types)
		Dim total As String 
			total = File.Size(GetParentPath(GetSourceDir(GetActivitiesInfo(packName))),GetFileName(GetSourceDir(GetActivitiesInfo(packName))))
					applist.AddTwoLinesAndBitmap2(name,packName&" | "&FormatFileSize(total),icon.Bitmap,packName)
	
		data.Add(packName)
	End If 
	Next
End Sub
	
	
Sub FormatFileSize(Bytes As Float) As String
   
	Private Unit() As String = Array As String(" Byte", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB")
   
	If Bytes = 0 Then      
		Return "0 Bytes"
	Else
		Private Po, Si As Double
		Private I As Int
		Bytes = Abs(Bytes)                   
		I = Floor(Logarithm(Bytes, 1024))
		Po = Power(1024, I)
		Si = Bytes / Po
		Return NumberFormat(Si, 1, 2) & Unit(I)
	End If
End Sub

Sub GetFileName(FullPath As String) As String
	Return FullPath.SubString(FullPath.LastIndexOf("/")+1)
End Sub

Sub cli_click
	'lis.Add("Optimierung "&date&", "&time)
	Activity.Finish
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub GetParentPath(path As String) As String
	Dim Path1 As String
	If path = "/" Then
		Return "/"
	End If
	L = path.LastIndexOf("/")
	If L = path.Length - 1 Then
		'Strip the last slash
		Path1 = path.SubString2(0,L)
	Else
		Path1 = path
	End If
	L = path.LastIndexOf("/")
	If L = 0 Then
		L = 1
	End If
	Return Path1.SubString2(0,L)
End Sub

Sub GetActivitiesInfo(package As String) As Object
	Dim r As Reflector
	r.Target = r.GetContext
	r.Target = r.RunMethod("getPackageManager")
	r.Target = r.RunMethod3("getPackageInfo", package, "java.lang.String", 0x00000001, "java.lang.int")
	Return r.GetField("applicationInfo")
End Sub

Sub GetSourceDir(AppInfo As Object) As String
	Try
		Dim r As Reflector
		r.Target = AppInfo
		Return r.GetField("sourceDir")
	Catch
		Return ""
	End Try
End Sub

