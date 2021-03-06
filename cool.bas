﻿Type=Activity
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
#Extends: android.support.v7.app.AppCompatActivity
Sub Process_Globals
	Dim t1 As Timer 
	Dim catdel As CacheCleaner
End Sub

Sub Globals
	Dim op As OperatingSystem
	'Private Label1 As Label
	Dim count As Int=0
	Dim args(1) As Object
	Dim Obj1, Obj2, Obj3 As Reflector
	Dim Types(1), name,packName,date,time As String
    Dim icon As BitmapDrawable
	Dim pak As PackageManager
	Dim list4,list1,apklist,list2,list5,list6,list7,list8,list9,list10,trash As List
	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16 As Int
	Dim cat As Cache
	Dim dir1 As String =File.DirDefaultExternal&"/mnt/cache/store"
	Private lw2,lw3 As ListView
	Dim l1,l2,l3 As Label 
	Dim ph As PhoneEvents
	Dim size,flags,count As Int
	Dim ffiles,ffolders As List
	Private dialog As CustomDialog2
	Dim diapan As Panel
	Dim dial As Label
	Dim dill As List
	Dim ActivityManager1 As ActivityManager
	Dim RunningTaskInfos() As RunningTaskInfo
	Dim paths As Map
	Dim panel1 As Panel 
	Dim storage As env
	Dim de As String = File.DirRootExternal
	Dim mtc As Matcher = Regex.Matcher("(/|\\)[^(/|\\)]*(/|\\)",de)
	Dim extsdcard As String = de
	Dim nativeMe As JavaObject
	Private Panel2 As Panel
	Private ImageView1 As ImageView
	Dim ffil,ffold As List
	Dim andro,bat,desk,work As Bitmap
	Private mcl As MaterialColors
	Dim ffiles,ffolders As List
	Private dpm1 As DonutProgressMaster
	Dim root1 As String 
	Dim anima As AnimationPlus
	Dim ani As Animation
	Dim xMSOS As MSOS
	Dim xOSStats As OSStats
	Private ListView1 As ListView
	Dim l1,l2,l3 As Label
	'Private cav As CoolAnimView
	Private Label1 As Label
	Dim catlist As List
	Private lvg As LVGearsTwo
	'Private lvb As  LVCircularSmile
	Private kvs4sub,kvs4 As KeyValueStore
End Sub 

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("5")
	Activity.Title=pak.GetApplicationLabel("com.batcat")&" - "&pak.GetVersionName("com.batcat")
	op.Initialize("op")
	catdel.initialize("catdel")
	list1.Initialize
	list2.Initialize
	l1.Initialize("l1")
	l2.Initialize("l2")
	l3.Initialize("l3")
	list4.Initialize
	list5.Initialize
	list6.Initialize
	list7.Initialize
	list8.Initialize
	list9.Initialize
	list10.Initialize
	apklist.Initialize
	trash.Initialize
	catlist.Initialize
	ph.Initialize("ph")
	l1.Initialize("l1")
	l2.Initialize("l2")
	diapan.Initialize("diapan")
	dial.Initialize("dial") 
	dill.Initialize
	lw2.Initialize("lw2")
	lw3.Initialize("lw3")

	
	ffiles.Initialize
	ph.Initialize("ph")
	ffiles.Initialize
	ffolders.Initialize
	xOSStats.Initialize(400, 50, Me, "myStats")
	kvs4sub.Initialize(File.DirDefaultExternal, "datastore_sub_4")
	kvs4.Initialize(File.DirDefaultExternal, "datastore_4")
	If FirstTime Then 
		Activity.Color=c10
		If File.Exists(File.DirDefaultExternal&"/mnt/cache","cdata.txt") Then
			'ToastMessageShow("core ready...!",False)
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","sv.txt",list4)
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","fn.txt",list1)
			'ListView1.Clear
			Else
	File.MakeDir(File.DirDefaultExternal, "mnt/cache")
	File.MakeDir(File.DirDefaultExternal, "mnt/cache/store")
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","sv.txt",list4)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","fn.txt",list1)
		File.WriteList(File.DirDefaultExternal&"/mnt/cache","cdata.txt",catlist)
			File.WriteString(File.DirDefaultExternal&"/mnt/cache","cclist.txt","")
	ToastMessageShow("Files ready! "&date&", "&time,False)
	End If 
	Else
	
	End If 
	'#######Timer Settings##############
	t1.Initialize("t1",1000)
	t1.Enabled=False
	
    ffolders.Initialize
	dial.TextSize=15
	dial.TextColor=Colors.White
	dialog.AddView(diapan,350,350)
	diapan.AddView(lw2,3,3,-1,-1)
	'catlist.Add(time)
	'#######################Storage Lolipop########################
	paths = storage.Initialize
	nativeMe.InitializeContext
	'######################################################Activity Colors######################
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
	
	
	ani.InitializeTranslate("ani",0,1,5,9)
	
	'#########################CLS Storage##########################
	Panel2.Visible=False
	'#########################End CLS Storage######################
	'count=0
	ffil.Initialize
	ffold.Initialize
	Activity.Color=mcl.md_white_1000
	ImageView1.BringToFront
	Dim secl As Label = ListView1.TwoLinesAndBitmap.SecondLabel
	l1=ListView1.TwoLinesAndBitmap.Label
	l1.TextSize=15
	secl.TextSize=12
	l1.TextColor=mcl.md_blue_400
	secl.TextColor=mcl.md_black_1000
	GetDeviceId
	store_check
	t1.Enabled=False
	'catdel.ScanCache
	cat_start
	's_scan
End Sub

Sub Activity_Resume
	anima.InitializeRotateCenter("anima",1,3,ImageView1)
	anima.RepeatMode=anima.REPEAT_INFINITE
	anima.SetInterpolator(anima.INTERPOLATOR_BOUNCE)
	store_check
	xOSStats.StartStats
	cat_start
	'catdel.ScanCache
	's_scan
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	xOSStats.EndStats
	anima.Stop(ImageView1)
	Activity.Finish
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
		SetAnimation.setanimati("extra_in", "extra_out")
	End If
	Return True
End Sub
Sub cat_start
	dpm1.TextColor=Colors.White
	dpm1.Color=Colors.Transparent
	dpm1.FinishedStrokeColor= mcl.md_light_blue_700
	dpm1.FinishedStrokeWidth=25
	dpm1.UnfinishedStrokeColor=Colors.Transparent
	dpm1.UnfinishedStrokeWidth=30
	dpm1.SuffixText="%"
	dpm1.InnerBackgroundColor=mcl.md_light_blue_A200
	'dpm1.InnerBottomText="working..."
	dpm1.InnerBottomTextSize=18
	dpm1.InnerBottomTextColor=Colors.Black
	dpm1.PrefixText="Cleaning: "
	'cat.Initialize(25,100*1024*1024,File.DirRootExternal)
	ReadDir(dir1,False)
	'	cav.SetVisibleAnimated(10000,False)
	'	cav.init
	
	ImageView1.Visible=False
	
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
	If kvs4sub.ContainsKey("off") Then
		StopService(Starter)
	End If
	If kvs4.ContainsKey("0")Then
		Log("AC_true->1")
		Activity.Color=c1
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("1")Then
		Log("AC_true->2")
		Activity.Color=c2
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("2")Then
		Log("AC_true->3")
		Activity.Color=c3
		Label1.TextColor=Colors.Black
	End If
	If kvs4.ContainsKey("3")Then
		Log("AC_true->4")
		Activity.Color=c4
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("4")Then
		Log("AC_true->4")
		Activity.Color=c5
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("5")Then
		Log("AC_true->4")
		Activity.Color=c6
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("6")Then
		Log("AC_true->4")
		Activity.Color=c7
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("7")Then
		Log("AC_true->4")
		Activity.Color=c8
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("8")Then
		Log("AC_true->4")
		Activity.Color=c9
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("9")Then
		Log("AC_true->4")
		Activity.Color=c10
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("10")Then
		Log("AC_true->4")
		Activity.Color=c11
		''Button4.Color=mcl.md_lime_A400
	End If
	If kvs4.ContainsKey("11")Then
		Log("AC_true->4")
		Activity.Color=c12
		Label1.TextColor=Colors.ARGB(200,255,255,255)
	End If
	If kvs4.ContainsKey("12")Then
		Log("AC_true->4")
		Activity.Color=c13
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	If kvs4.ContainsKey("13")Then
		Log("AC_true->4")
		Activity.Color=c14
		''Button4.Color=mcl.md_lime_A400
	End If
	If kvs4.ContainsKey("14")Then
		Log("AC_true->4")
		Activity.Color=c15
		''Button4.Color=Colors.ARGB(150,255,255,255)
	End If
	Activity.Invalidate
End Sub
Sub s_scan
'	store_check
'	cat_start
	lvg.startAnim
	catdel.ScanCache

	c_start
End Sub
Sub c_start	 
	app_info
	catlist.Clear
	dill.Clear
	list2.Clear
	list4.Clear
	list7.Clear
	apklist.Clear
	'catlist.AddAll(xOSStats.BufferRAM)
	list2=op.RunningTaskInfo(99,list8,list9,list10)
	Return
End Sub

Sub myStats_Update(CPUEfficiency() As Float, RAMUsage As Float)

End Sub

Sub catdel_OnScanStarted
	Log("Started")
	
End Sub

Sub storage_check
	For i = 0 To paths.Size-1
		Log(paths.GetKeyAt(i)&"="&paths.GetValueAt(i))
	Next

	Log ("DirRootExternal = "&de)
	
	If mtc.Find = True Then
		Dim mnt As String = mtc.Group(0)
   
		Log ("mount point = "& mnt)
		Dim dirs As List = File.ListFiles(mnt)
		For Each f As String In dirs
			If storage.isExternalStorageRemovable(mnt&f) Then
				Log ("Device = "& f&":"&mnt&f&" is removable")
				If File.ListFiles(mnt&f).IsInitialized Then
					Log("probably ExtSDCard: "&mnt&f)
					extsdcard = mnt&f
				Else
					'Log("Problem reading "&mnt&f)
				End If
			Else
				Log ("Device = "& f&":"&mnt&f&" is NOT removable")
			End If
		Next
	End If
	'Activity.SetColorAnimated(1000,Colors.ARGB(100,30,124,235),Colors.ARGB(190,35,140,7))
	s_scan
End Sub 


Sub catdel_onScanProgress (Current As Int , Total As Int)
	dpm1.Max=Total
	dpm1.Progress=Current
	Label1.Text="durchsuche -> "&Current&" Apps nach Müll"
	'Log(Current&"/"&Total)
End Sub

Sub catdel_onCleanStarted
	ListView1.AddSingleLine("lösche..." )
End Sub

Sub catdel_onCleanCompleted (CacheSize As Long)
	'catlist.Add(CacheSize&" clean")
	'Label1.Text=xMSOS.formateFileSize(CacheSize)&" cleaned !"
	dpm1.InnerBottomText=xMSOS.formateFileSize(CacheSize)&" clean!"
	ListView1.AddSingleLine(xMSOS.formateFileSize(CacheSize)&" gesäubert")
	t_start
End Sub

Sub catdel_onScanCompleted (AppsList As Object)
	time=DateTime.Time(DateTime.Now)
	lvg.stopAnim
	Dim totalsize As Long = 0
	Dim pm As PackageManager
	ListView1.Clear
	lw2.Clear
	Try
		Dim lu As List = AppsList
		If lu.Size=0 Then
			'Cache is Empty
			ListView1.AddSingleLine("No App cache found. All clear")
			catlist.Clear
			catlist.Add(time&" - No App cache found")
			t_start
			Return
		End If
		For n = 0 To lu.Size-1
			Dim app() As Object = lu.Get(n)
			If app(1) = "com.android.systemui" Then Continue 'This Pakage Have No Icon In Some Android 5
			Dim icon As BitmapDrawable = pm.GetApplicationIcon(app(1))
			ListView1.SetSelection(n)
			ListView1.AddTwoLinesAndBitmap(app(0),NumberFormat2(app(2)/1024/1024,1,2,2,True)&"MB",icon.Bitmap)
			lw3.AddTwoLinesAndBitmap(app(0),NumberFormat2(app(2)/1024/1024,1,2,2,True)&"MB",icon.Bitmap)
			totalsize = totalsize+app(2)
			catlist.Clear
			catlist.Add(app(0)&" - "&NumberFormat2(app(2)/1024/1024,1,2,2,True)&"MB cache"&" | "&op.formatSize(totalsize))
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","cdata.txt",catlist)
			
		Next
		
	Catch
		Log(LastException.Message)
		catlist.Add(LastException.Message)
	End Try
	If lu.Size>0 Then 
		cl_msg
	Else
		close
	End If
End Sub

Sub cl_msg
	Dim diap As BetterDialogs
	Dim res As Int
	res=diap.CustomDialog("Cache found:",130dip,80dip,lw3,300dip,350dip,10dip,Colors.white,"Ja löschen","","Nein Ende",False,"diap")
	If res=DialogResponse.POSITIVE Then
		catdel.CleanCache
	t_start
	Else
		ImageView1.Visible=True
		dpm1.Visible=False
		'ImageView1.Bitmap=LoadBitmap(File.DirAssets,"Accept128.png")
		anima.Start(ImageView1)
		full_close
	End If
End Sub

Sub t_start
	lvg.startAnim
	dpm1.max=100
	t1.Enabled=True
	t1_Tick
End Sub

Sub close
		If Not(catlist.size=0) Then
				Dim df As String  
		df=apklist.size
		Label1.Text=op.formatSize(cat.FreeMemory)&" RAM free! "&list2.Size&" -Backround Processes closed."&xMSOS.formateFileSize(df)&" Files and Trash Data cleared"
		'catlist.Add("RAM boosted: "&op.formatSize(cat.FreeMemory))
		catdel.CleanCache
		Log("----------------CDATA -> "&catlist.Size)
		Else
			catlist.Clear
		Label1.Text=op.formatSize(cat.FreeMemory)&" RAM free! "&list4.Size&" Backround Processes killed...!"
		catlist.Add(time&" - No App cache found")
		Log("--------------- NO CDATA--------------")
		End If 	
	delayed_t2
	Return
End Sub

Sub del_quest
	ImageView1.Visible=True
	dpm1.Visible=False
'	pgWheel1.Visible=False
	ImageView1.Bitmap=LoadBitmap(File.DirAssets,"Accept128.png")
		Label1.Text= "clear RAM and close.."
	'File.WriteList(File.DirDefaultExternal&"/mnt/cache","cdata.txt",catlist)
		real_delete
End Sub

Sub real_delete
	RunningTaskInfos=ActivityManager1.GetRunningTasks
	Log("RunningTaskInfos.Length="&RunningTaskInfos.Length)
	
	For j = 0 To list7.Size-1
		Log("Recent Tasks: "&list7.get(j))
		ActivityManager1.killBackgroundProcesses("com.batcat")
		op.killProcess(list7.Get(j))
	Next
	close
	Return
End Sub

Sub t1_Tick
	count=count+1
	ListView1.SetSelection(-1)
	dpm1.Max=100
	dpm1.UnfinishedStrokeWidth=20
	dpm1.UnfinishedStrokeColor=Colors.ARGB(180,255,255,255)'mcl.md_light_green_A100
	If count>0 Then 
		Label1.text="search Battery.."
	End If
	If count > 1 Then
		'ListView1.Clear
		Label1.text="check Battery.."
		dpm1.Progress=20
	End If
	If count > 2 Then
		Label1.text="optimize Battery.."
		dpm1.Progress=36
	End If
	If count > 3 Then

		Label1.text="search Application cache"
		
		dpm1.Progress=42
	End If

	If count > 4 Then
		'#ListView1.Clear
		dpm1.Progress=52
		'ImageView1.Bitmap=bat
	
		Label1.text="cleaning Cache System.."
		
		dpm1.Progress=67
	End If
	If count > 5 Then
		dpm1.Progress=89
		Label1.text="deleting Garbage Cache.."
		'ImageView1.Bitmap=desk
		
				Label1.text="check: "&op.formatSize(op.AvailableMemory)
		
		dpm1.Progress=100
	End If
	If count > 6 Then
		'#ListView1.Clear
		
		Label1.text=op.formatSize(cat.FreeMemory)
		'ToastMessageShow("closing in 1 sec...",False)
		CallSub(Me,"del_quest")
	End If
End Sub

Sub delayed_t2
	dpm1.InnerBottomText=Label1.text
	dpm1.Progress=100
	lvg.SetVisibleAnimated(100,False)
	lvg.stopAnim
	'dpm1.SetLayoutAnimated(2dip,46dip,50,10,25)
		If count > 7 Then 
		Label1.text=op.formatSize(cat.FreeMemory)&" free.."
		ListView1.Clear
	End If
	If count> 8 Then 
		Label1.text=op.formatSize(cat.FreeMemory)
		'ToastMessageShow("closing in 1 sec...",False)
		ListView1.AddSingleLine(xMSOS.formateFileSize(cat.FreeMemory))
	End If
	If count> 9 Then
		
		anima.Start(ImageView1)
		dpm1.Visible=False
		'ToastMessageShow("closing in 1 sec...",False)
	End If
	If count> 10 Then
		'ToastMessageShow("closing in 1 sec...",False)
	End If
	If count = 11 Then 
		
		t1.Enabled=False
		'ToastMessageShow(op.formatSize(cat.FreeMemory)&" free",False)
		Activity.Finish
		SetAnimation.setanimati("extra_in", "extra_out")
	End If
End Sub
Sub full_close
	Activity.Finish
	SetAnimation.setanimati("extra_in", "extra_out")
End Sub

Sub app_info

	list1=pak.GetInstalledPackages

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

			
			
		End If
	Next
	
End Sub

Sub ReadDir(folder As String, recursive As Boolean)
	ffolders.Clear
	ffiles.Clear
	'Log("ReadDir("&folder&")")
	Dim lst As List = File.ListFiles(folder)
	For i = 0 To lst.Size - 1
		If File.IsDirectory(folder,lst.Get(i)) Then
			Dim v As String
			v = folder&"/"&lst.Get(i)
			'Log("v="&v)
			ffolders.Add(v.SubString(root1.Length+1))
			
			If recursive Then
				ReadDir(v,recursive)
			End If
			'ListView1.AddSingleLine(lst.Get(i))
		Else
			ffiles.Add(folder&"/"&lst.Get(i))
			'ListView1.AddSingleLine(lst.Get(i))
		End If
	Next
	Log(ffolders.Size&" Ordner / "&ffiles.Size&" Dateien")
End Sub

Sub GetDeviceId As String
	Dim api As Int
	Dim r As Reflector
	api = r.GetStaticField("android.os.Build$VERSION", "SDK_INT")
	If api < 18 Then
		'Old device
		If File.Exists(File.DirInternal, "__id") Then
			Return File.ReadString(File.DirInternal, "__id")
			c_start
		Else
			c_start
			Dim id As Int
			id = Rnd(0x10000000, 0x7FFFFFFF)
			File.WriteString(File.DirInternal, "__id", id)
			Return id
		End If
		Log(api)
	Else
		'New device
		Return r.GetStaticField("android.os.Build", "SERIAL")
		storage_check
	End If
End Sub

Sub object_to_byte(obj As Object)As Byte()
	Dim ser As B4XSerializator
	Return ser.ConvertObjectToBytes(obj)
End Sub

Sub byte_to_object(data() As Byte)As Object
	Dim ser As B4XSerializator
	Return ser.ConvertBytesToObject(data)
End Sub

Sub CalcSize(Folder As String, recursive As Boolean) As Long
	Dim size1 As Long
	For Each f As String In File.ListFiles(Folder)
		If recursive Then
			If File.IsDirectory(Folder, f) Then
				size1 = size1 + CalcSize(File.Combine(Folder, f),recursive)
			End If
		End If
		size1 = size1 + File.Size(Folder, f)
	Next
	Return size1
End Sub

Sub file_CopyDone
	Log("copy done!")
	
End Sub
