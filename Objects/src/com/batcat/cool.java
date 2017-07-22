package com.batcat;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class cool extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static cool mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.cool");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (cool).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.cool");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.cool", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (cool) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (cool) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return cool.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (cool) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (cool) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public static anywheresoftware.b4a.cachecleaner.CacheCleaner _catdel = null;
public com.rootsoft.oslibrary.OSLibrary _op = null;
public static int _count = 0;
public Object[] _args = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj2 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj3 = null;
public static String[] _types = null;
public static String _name = "";
public static String _packname = "";
public static String _date = "";
public static String _time = "";
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _icon = null;
public anywheresoftware.b4a.phone.PackageManagerWrapper _pak = null;
public anywheresoftware.b4a.objects.collections.List _list4 = null;
public anywheresoftware.b4a.objects.collections.List _list1 = null;
public anywheresoftware.b4a.objects.collections.List _apklist = null;
public anywheresoftware.b4a.objects.collections.List _list2 = null;
public anywheresoftware.b4a.objects.collections.List _list5 = null;
public anywheresoftware.b4a.objects.collections.List _list6 = null;
public anywheresoftware.b4a.objects.collections.List _list7 = null;
public anywheresoftware.b4a.objects.collections.List _list8 = null;
public anywheresoftware.b4a.objects.collections.List _list9 = null;
public anywheresoftware.b4a.objects.collections.List _list10 = null;
public anywheresoftware.b4a.objects.collections.List _trash = null;
public static int _c1 = 0;
public static int _c2 = 0;
public static int _c3 = 0;
public static int _c4 = 0;
public static int _c5 = 0;
public static int _c6 = 0;
public static int _c7 = 0;
public static int _c8 = 0;
public static int _c9 = 0;
public static int _c10 = 0;
public static int _c11 = 0;
public static int _c12 = 0;
public static int _c13 = 0;
public static int _c14 = 0;
public static int _c15 = 0;
public static int _c16 = 0;
public flm.b4a.cache.Cache _cat = null;
public static String _dir1 = "";
public anywheresoftware.b4a.objects.ListViewWrapper _lw2 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lw3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l3 = null;
public anywheresoftware.b4a.phone.PhoneEvents _ph = null;
public static int _size = 0;
public static int _flags = 0;
public anywheresoftware.b4a.objects.collections.List _ffiles = null;
public anywheresoftware.b4a.objects.collections.List _ffolders = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _dialog = null;
public anywheresoftware.b4a.objects.PanelWrapper _diapan = null;
public anywheresoftware.b4a.objects.LabelWrapper _dial = null;
public anywheresoftware.b4a.objects.collections.List _dill = null;
public uk.co.martinpearman.b4a.activitymanager.ActivityManager _activitymanager1 = null;
public uk.co.martinpearman.b4a.activitymanager.RunningTaskInfo[] _runningtaskinfos = null;
public anywheresoftware.b4a.objects.collections.Map _paths = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public de.donmanfred.storage _storage = null;
public static String _de = "";
public anywheresoftware.b4a.keywords.Regex.MatcherWrapper _mtc = null;
public static String _extsdcard = "";
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public anywheresoftware.b4a.objects.collections.List _ffil = null;
public anywheresoftware.b4a.objects.collections.List _ffold = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _andro = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _desk = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _work = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public circleprogressmasterwrapper.donutProgressMasterWrapper _dpm1 = null;
public static String _root1 = "";
public flm.b4a.animationplus.AnimationPlusWrapper _anima = null;
public anywheresoftware.b4a.objects.AnimationWrapper _ani = null;
public com.maximussoft.msos.MSOS _xmsos = null;
public b4a.example.osstats _xosstats = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.collections.List _catlist = null;
public de.donmanfred.LVGearsTwoWrapper _lvg = null;
public com.batcat.keyvaluestore _kvs4sub = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.pman _pman = null;
public com.batcat.wait _wait = null;
public com.batcat.charts _charts = null;
public com.batcat.set2 _set2 = null;
public com.batcat.datacount _datacount = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _secl = null;
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 68;BA.debugLine="Activity.LoadLayout(\"5\")";
mostCurrent._activity.LoadLayout("5",mostCurrent.activityBA);
 //BA.debugLineNum = 69;BA.debugLine="Activity.Title=pak.GetApplicationLabel(\"com.batca";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(mostCurrent._pak.GetApplicationLabel("com.batcat")+" - "+mostCurrent._pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 70;BA.debugLine="op.Initialize(\"op\")";
mostCurrent._op.Initialize(processBA,"op");
 //BA.debugLineNum = 71;BA.debugLine="catdel.initialize(\"catdel\")";
_catdel.initialize("catdel",processBA);
 //BA.debugLineNum = 72;BA.debugLine="list1.Initialize";
mostCurrent._list1.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="list2.Initialize";
mostCurrent._list2.Initialize();
 //BA.debugLineNum = 74;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 75;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 76;BA.debugLine="l3.Initialize(\"l3\")";
mostCurrent._l3.Initialize(mostCurrent.activityBA,"l3");
 //BA.debugLineNum = 77;BA.debugLine="list4.Initialize";
mostCurrent._list4.Initialize();
 //BA.debugLineNum = 78;BA.debugLine="list5.Initialize";
mostCurrent._list5.Initialize();
 //BA.debugLineNum = 79;BA.debugLine="list6.Initialize";
mostCurrent._list6.Initialize();
 //BA.debugLineNum = 80;BA.debugLine="list7.Initialize";
mostCurrent._list7.Initialize();
 //BA.debugLineNum = 81;BA.debugLine="list8.Initialize";
mostCurrent._list8.Initialize();
 //BA.debugLineNum = 82;BA.debugLine="list9.Initialize";
mostCurrent._list9.Initialize();
 //BA.debugLineNum = 83;BA.debugLine="list10.Initialize";
mostCurrent._list10.Initialize();
 //BA.debugLineNum = 84;BA.debugLine="apklist.Initialize";
mostCurrent._apklist.Initialize();
 //BA.debugLineNum = 85;BA.debugLine="trash.Initialize";
mostCurrent._trash.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="catlist.Initialize";
mostCurrent._catlist.Initialize();
 //BA.debugLineNum = 87;BA.debugLine="ph.Initialize(\"ph\")";
mostCurrent._ph.Initialize(processBA,"ph");
 //BA.debugLineNum = 88;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 89;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 90;BA.debugLine="diapan.Initialize(\"diapan\")";
mostCurrent._diapan.Initialize(mostCurrent.activityBA,"diapan");
 //BA.debugLineNum = 91;BA.debugLine="dial.Initialize(\"dial\")";
mostCurrent._dial.Initialize(mostCurrent.activityBA,"dial");
 //BA.debugLineNum = 92;BA.debugLine="dill.Initialize";
mostCurrent._dill.Initialize();
 //BA.debugLineNum = 93;BA.debugLine="lw2.Initialize(\"lw2\")";
mostCurrent._lw2.Initialize(mostCurrent.activityBA,"lw2");
 //BA.debugLineNum = 94;BA.debugLine="lw3.Initialize(\"lw3\")";
mostCurrent._lw3.Initialize(mostCurrent.activityBA,"lw3");
 //BA.debugLineNum = 95;BA.debugLine="ffiles.Initialize";
mostCurrent._ffiles.Initialize();
 //BA.debugLineNum = 96;BA.debugLine="ph.Initialize(\"ph\")";
mostCurrent._ph.Initialize(processBA,"ph");
 //BA.debugLineNum = 97;BA.debugLine="ffiles.Initialize";
mostCurrent._ffiles.Initialize();
 //BA.debugLineNum = 98;BA.debugLine="ffolders.Initialize";
mostCurrent._ffolders.Initialize();
 //BA.debugLineNum = 99;BA.debugLine="xOSStats.Initialize(400, 50, Me, \"myStats\")";
mostCurrent._xosstats._initialize(mostCurrent.activityBA,(int) (400),(int) (50),cool.getObject(),"myStats");
 //BA.debugLineNum = 100;BA.debugLine="kvs4sub.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvs4sub._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_sub_4");
 //BA.debugLineNum = 101;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 102;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 103;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 //BA.debugLineNum = 104;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cac";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","cdata.txt")) { 
 //BA.debugLineNum = 106;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 107;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 }else {
 //BA.debugLineNum = 110;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cache\"";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 111;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cache/";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache/store");
 //BA.debugLineNum = 112;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 113;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 //BA.debugLineNum = 114;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cac";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","cdata.txt",mostCurrent._catlist);
 //BA.debugLineNum = 115;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","cclist.txt","");
 //BA.debugLineNum = 116;BA.debugLine="ToastMessageShow(\"Files ready! \"&date&\", \"&time,F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Files ready! "+mostCurrent._date+", "+mostCurrent._time),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 };
 //BA.debugLineNum = 122;BA.debugLine="t1.Initialize(\"t1\",1000)";
_t1.Initialize(processBA,"t1",(long) (1000));
 //BA.debugLineNum = 123;BA.debugLine="t1.Enabled=False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 125;BA.debugLine="ffolders.Initialize";
mostCurrent._ffolders.Initialize();
 //BA.debugLineNum = 126;BA.debugLine="dial.TextSize=15";
mostCurrent._dial.setTextSize((float) (15));
 //BA.debugLineNum = 127;BA.debugLine="dial.TextColor=Colors.White";
mostCurrent._dial.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 128;BA.debugLine="dialog.AddView(diapan,350,350)";
mostCurrent._dialog.AddView((android.view.View)(mostCurrent._diapan.getObject()),(int) (350),(int) (350));
 //BA.debugLineNum = 129;BA.debugLine="diapan.AddView(lw2,3,3,-1,-1)";
mostCurrent._diapan.AddView((android.view.View)(mostCurrent._lw2.getObject()),(int) (3),(int) (3),(int) (-1),(int) (-1));
 //BA.debugLineNum = 132;BA.debugLine="paths = storage.Initialize";
mostCurrent._paths = mostCurrent._storage.Initialize();
 //BA.debugLineNum = 133;BA.debugLine="nativeMe.InitializeContext";
mostCurrent._nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 135;BA.debugLine="c1=mcl.md_light_blue_A400";
_c1 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 136;BA.debugLine="c2=mcl.md_amber_A400";
_c2 = mostCurrent._mcl.getmd_amber_A400();
 //BA.debugLineNum = 137;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 138;BA.debugLine="c4=mcl.md_teal_A400";
_c4 = mostCurrent._mcl.getmd_teal_A400();
 //BA.debugLineNum = 139;BA.debugLine="c5=mcl.md_deep_purple_A400";
_c5 = mostCurrent._mcl.getmd_deep_purple_A400();
 //BA.debugLineNum = 140;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 141;BA.debugLine="c7=mcl.md_indigo_A400";
_c7 = mostCurrent._mcl.getmd_indigo_A400();
 //BA.debugLineNum = 142;BA.debugLine="c8=mcl.md_blue_A400";
_c8 = mostCurrent._mcl.getmd_blue_A400();
 //BA.debugLineNum = 143;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 144;BA.debugLine="c10=mcl.md_grey_600";
_c10 = mostCurrent._mcl.getmd_grey_600();
 //BA.debugLineNum = 145;BA.debugLine="c11=mcl.md_green_A400";
_c11 = mostCurrent._mcl.getmd_green_A400();
 //BA.debugLineNum = 146;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 147;BA.debugLine="c13=mcl.md_light_green_A400";
_c13 = mostCurrent._mcl.getmd_light_green_A400();
 //BA.debugLineNum = 148;BA.debugLine="c14=mcl.md_cyan_A400";
_c14 = mostCurrent._mcl.getmd_cyan_A400();
 //BA.debugLineNum = 149;BA.debugLine="c15=mcl.md_blue_grey_400";
_c15 = mostCurrent._mcl.getmd_blue_grey_400();
 //BA.debugLineNum = 150;BA.debugLine="c16=mcl.md_light_blue_A400";
_c16 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 153;BA.debugLine="ani.InitializeTranslate(\"ani\",0,1,5,9)";
mostCurrent._ani.InitializeTranslate(mostCurrent.activityBA,"ani",(float) (0),(float) (1),(float) (5),(float) (9));
 //BA.debugLineNum = 156;BA.debugLine="Panel2.Visible=False";
mostCurrent._panel2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 159;BA.debugLine="ffil.Initialize";
mostCurrent._ffil.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="ffold.Initialize";
mostCurrent._ffold.Initialize();
 //BA.debugLineNum = 161;BA.debugLine="Activity.Color=mcl.md_white_1000";
mostCurrent._activity.setColor(mostCurrent._mcl.getmd_white_1000());
 //BA.debugLineNum = 162;BA.debugLine="ImageView1.BringToFront";
mostCurrent._imageview1.BringToFront();
 //BA.debugLineNum = 163;BA.debugLine="Dim secl As Label = ListView1.TwoLinesAndBitmap.S";
_secl = new anywheresoftware.b4a.objects.LabelWrapper();
_secl = mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 164;BA.debugLine="l1=ListView1.TwoLinesAndBitmap.Label";
mostCurrent._l1 = mostCurrent._listview1.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 165;BA.debugLine="l1.TextSize=15";
mostCurrent._l1.setTextSize((float) (15));
 //BA.debugLineNum = 166;BA.debugLine="secl.TextSize=12";
_secl.setTextSize((float) (12));
 //BA.debugLineNum = 167;BA.debugLine="l1.TextColor=mcl.md_blue_400";
mostCurrent._l1.setTextColor(mostCurrent._mcl.getmd_blue_400());
 //BA.debugLineNum = 168;BA.debugLine="secl.TextColor=mcl.md_black_1000";
_secl.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 169;BA.debugLine="GetDeviceId";
_getdeviceid();
 //BA.debugLineNum = 170;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 171;BA.debugLine="t1.Enabled=False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 172;BA.debugLine="cat_start";
_cat_start();
 //BA.debugLineNum = 173;BA.debugLine="s_scan";
_s_scan();
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 192;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 193;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 194;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 195;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 197;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 186;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 187;BA.debugLine="xOSStats.EndStats";
mostCurrent._xosstats._endstats();
 //BA.debugLineNum = 188;BA.debugLine="anima.Stop(ImageView1)";
mostCurrent._anima.Stop((android.view.View)(mostCurrent._imageview1.getObject()));
 //BA.debugLineNum = 189;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 177;BA.debugLine="anima.InitializeRotateCenter(\"anima\",1,3,ImageVie";
mostCurrent._anima.InitializeRotateCenter(mostCurrent.activityBA,"anima",(float) (1),(float) (3),(android.view.View)(mostCurrent._imageview1.getObject()));
 //BA.debugLineNum = 178;BA.debugLine="anima.RepeatMode=anima.REPEAT_INFINITE";
mostCurrent._anima.setRepeatMode(mostCurrent._anima.REPEAT_INFINITE);
 //BA.debugLineNum = 179;BA.debugLine="anima.SetInterpolator(anima.INTERPOLATOR_BOUNCE)";
mostCurrent._anima.SetInterpolator(mostCurrent._anima.INTERPOLATOR_BOUNCE);
 //BA.debugLineNum = 180;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 181;BA.debugLine="xOSStats.StartStats";
mostCurrent._xosstats._startstats();
 //BA.debugLineNum = 182;BA.debugLine="cat_start";
_cat_start();
 //BA.debugLineNum = 183;BA.debugLine="s_scan";
_s_scan();
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _app_info() throws Exception{
int _i = 0;
 //BA.debugLineNum = 590;BA.debugLine="Sub app_info";
 //BA.debugLineNum = 592;BA.debugLine="list1=pak.GetInstalledPackages";
mostCurrent._list1 = mostCurrent._pak.GetInstalledPackages();
 //BA.debugLineNum = 594;BA.debugLine="Obj1.Target = Obj1.GetContext";
mostCurrent._obj1.Target = (Object)(mostCurrent._obj1.GetContext(processBA));
 //BA.debugLineNum = 595;BA.debugLine="Obj1.Target = Obj1.RunMethod(\"getPackageManager\")";
mostCurrent._obj1.Target = mostCurrent._obj1.RunMethod("getPackageManager");
 //BA.debugLineNum = 596;BA.debugLine="Obj2.Target = Obj1.RunMethod2(\"getInstalledPackag";
mostCurrent._obj2.Target = mostCurrent._obj1.RunMethod2("getInstalledPackages",BA.NumberToString(0),"java.lang.int");
 //BA.debugLineNum = 597;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 599;BA.debugLine="For i = 0 To size -1";
{
final int step6 = 1;
final int limit6 = (int) (_size-1);
for (_i = (int) (0) ; (step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6); _i = ((int)(0 + _i + step6)) ) {
 //BA.debugLineNum = 600;BA.debugLine="Obj3.Target = Obj2.RunMethod2(\"get\", i, \"java.la";
mostCurrent._obj3.Target = mostCurrent._obj2.RunMethod2("get",BA.NumberToString(_i),"java.lang.int");
 //BA.debugLineNum = 601;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 603;BA.debugLine="Obj3.Target = Obj3.GetField(\"applicationInfo\") '";
mostCurrent._obj3.Target = mostCurrent._obj3.GetField("applicationInfo");
 //BA.debugLineNum = 604;BA.debugLine="flags = Obj3.GetField(\"flags\")";
_flags = (int)(BA.ObjectToNumber(mostCurrent._obj3.GetField("flags")));
 //BA.debugLineNum = 605;BA.debugLine="packName = Obj3.GetField(\"packageName\")";
mostCurrent._packname = BA.ObjectToString(mostCurrent._obj3.GetField("packageName"));
 //BA.debugLineNum = 607;BA.debugLine="If Bit.And(flags, 1)  = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_flags,(int) (1))==0) { 
 //BA.debugLineNum = 610;BA.debugLine="args(0) = Obj3.Target";
mostCurrent._args[(int) (0)] = mostCurrent._obj3.Target;
 //BA.debugLineNum = 611;BA.debugLine="Types(0) = \"android.content.pm.ApplicationInfo\"";
mostCurrent._types[(int) (0)] = "android.content.pm.ApplicationInfo";
 //BA.debugLineNum = 612;BA.debugLine="name = Obj1.RunMethod4(\"getApplicationLabel\", a";
mostCurrent._name = BA.ObjectToString(mostCurrent._obj1.RunMethod4("getApplicationLabel",mostCurrent._args,mostCurrent._types));
 //BA.debugLineNum = 613;BA.debugLine="icon = Obj1.RunMethod4(\"getApplicationIcon\", ar";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(mostCurrent._obj1.RunMethod4("getApplicationIcon",mostCurrent._args,mostCurrent._types)));
 };
 }
};
 //BA.debugLineNum = 620;BA.debugLine="End Sub";
return "";
}
public static Object  _byte_to_object(byte[] _data) throws Exception{
anywheresoftware.b4a.randomaccessfile.B4XSerializator _ser = null;
 //BA.debugLineNum = 675;BA.debugLine="Sub byte_to_object(data() As Byte)As Object";
 //BA.debugLineNum = 676;BA.debugLine="Dim ser As B4XSerializator";
_ser = new anywheresoftware.b4a.randomaccessfile.B4XSerializator();
 //BA.debugLineNum = 677;BA.debugLine="Return ser.ConvertBytesToObject(data)";
if (true) return _ser.ConvertBytesToObject(_data);
 //BA.debugLineNum = 678;BA.debugLine="End Sub";
return null;
}
public static String  _c_start() throws Exception{
 //BA.debugLineNum = 323;BA.debugLine="Sub c_start";
 //BA.debugLineNum = 324;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 325;BA.debugLine="catlist.Clear";
mostCurrent._catlist.Clear();
 //BA.debugLineNum = 326;BA.debugLine="dill.Clear";
mostCurrent._dill.Clear();
 //BA.debugLineNum = 327;BA.debugLine="list2.Clear";
mostCurrent._list2.Clear();
 //BA.debugLineNum = 328;BA.debugLine="list4.Clear";
mostCurrent._list4.Clear();
 //BA.debugLineNum = 329;BA.debugLine="list7.Clear";
mostCurrent._list7.Clear();
 //BA.debugLineNum = 330;BA.debugLine="apklist.Clear";
mostCurrent._apklist.Clear();
 //BA.debugLineNum = 332;BA.debugLine="list2=op.RunningTaskInfo(99,list8,list9,list10)";
mostCurrent._list2.setObject((java.util.List)(mostCurrent._op.RunningTaskInfo((int) (99),(java.util.List)(mostCurrent._list8.getObject()),(java.util.List)(mostCurrent._list9.getObject()),(java.util.List)(mostCurrent._list10.getObject()))));
 //BA.debugLineNum = 333;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
public static long  _calcsize(String _folder,boolean _recursive) throws Exception{
long _size1 = 0L;
String _f = "";
 //BA.debugLineNum = 680;BA.debugLine="Sub CalcSize(Folder As String, recursive As Boolea";
 //BA.debugLineNum = 681;BA.debugLine="Dim size1 As Long";
_size1 = 0L;
 //BA.debugLineNum = 682;BA.debugLine="For Each f As String In File.ListFiles(Folder)";
final anywheresoftware.b4a.BA.IterableList group2 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
final int groupLen2 = group2.getSize();
for (int index2 = 0;index2 < groupLen2 ;index2++){
_f = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 683;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 684;BA.debugLine="If File.IsDirectory(Folder, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,_f)) { 
 //BA.debugLineNum = 685;BA.debugLine="size1 = size1 + CalcSize(File.Combine(Folder,";
_size1 = (long) (_size1+_calcsize(anywheresoftware.b4a.keywords.Common.File.Combine(_folder,_f),_recursive));
 };
 };
 //BA.debugLineNum = 688;BA.debugLine="size1 = size1 + File.Size(Folder, f)";
_size1 = (long) (_size1+anywheresoftware.b4a.keywords.Common.File.Size(_folder,_f));
 }
;
 //BA.debugLineNum = 690;BA.debugLine="Return size1";
if (true) return _size1;
 //BA.debugLineNum = 691;BA.debugLine="End Sub";
return 0L;
}
public static String  _cat_start() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub cat_start";
 //BA.debugLineNum = 200;BA.debugLine="dpm1.TextColor=Colors.White";
mostCurrent._dpm1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 201;BA.debugLine="dpm1.Color=Colors.Transparent";
mostCurrent._dpm1.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 202;BA.debugLine="dpm1.FinishedStrokeColor= mcl.md_light_blue_700";
mostCurrent._dpm1.setFinishedStrokeColor(mostCurrent._mcl.getmd_light_blue_700());
 //BA.debugLineNum = 203;BA.debugLine="dpm1.FinishedStrokeWidth=25";
mostCurrent._dpm1.setFinishedStrokeWidth((float) (25));
 //BA.debugLineNum = 204;BA.debugLine="dpm1.UnfinishedStrokeColor=Colors.Transparent";
mostCurrent._dpm1.setUnfinishedStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 205;BA.debugLine="dpm1.UnfinishedStrokeWidth=30";
mostCurrent._dpm1.setUnfinishedStrokeWidth((float) (30));
 //BA.debugLineNum = 206;BA.debugLine="dpm1.SuffixText=\"%\"";
mostCurrent._dpm1.setSuffixText("%");
 //BA.debugLineNum = 207;BA.debugLine="dpm1.InnerBackgroundColor=mcl.md_light_blue_A200";
mostCurrent._dpm1.setInnerBackgroundColor(mostCurrent._mcl.getmd_light_blue_A200());
 //BA.debugLineNum = 209;BA.debugLine="dpm1.InnerBottomTextSize=18";
mostCurrent._dpm1.setInnerBottomTextSize((float) (18));
 //BA.debugLineNum = 210;BA.debugLine="dpm1.InnerBottomTextColor=Colors.Black";
mostCurrent._dpm1.setInnerBottomTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 211;BA.debugLine="dpm1.PrefixText=\"Cleaning: \"";
mostCurrent._dpm1.setPrefixText("Cleaning: ");
 //BA.debugLineNum = 213;BA.debugLine="ReadDir(dir1,False)";
_readdir(mostCurrent._dir1,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="ImageView1.Visible=False";
mostCurrent._imageview1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static String  _catdel_oncleancompleted(long _cachesize) throws Exception{
 //BA.debugLineNum = 387;BA.debugLine="Sub catdel_onCleanCompleted (CacheSize As Long)";
 //BA.debugLineNum = 390;BA.debugLine="dpm1.InnerBottomText=xMSOS.formateFileSize(CacheS";
mostCurrent._dpm1.setInnerBottomText(mostCurrent._xmsos.formateFileSize(mostCurrent.activityBA,_cachesize)+" clean!");
 //BA.debugLineNum = 391;BA.debugLine="ListView1.AddSingleLine(xMSOS.formateFileSize(Cac";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence(mostCurrent._xmsos.formateFileSize(mostCurrent.activityBA,_cachesize)+" gesäubert"));
 //BA.debugLineNum = 392;BA.debugLine="t_start";
_t_start();
 //BA.debugLineNum = 393;BA.debugLine="End Sub";
return "";
}
public static String  _catdel_oncleanstarted() throws Exception{
 //BA.debugLineNum = 383;BA.debugLine="Sub catdel_onCleanStarted";
 //BA.debugLineNum = 384;BA.debugLine="ListView1.AddSingleLine(\"lösche...\" )";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence("lösche..."));
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
return "";
}
public static String  _catdel_onscancompleted(Object _appslist) throws Exception{
long _totalsize = 0L;
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
anywheresoftware.b4a.objects.collections.List _lu = null;
int _n = 0;
Object[] _app = null;
 //BA.debugLineNum = 395;BA.debugLine="Sub catdel_onScanCompleted (AppsList As Object)";
 //BA.debugLineNum = 396;BA.debugLine="time=DateTime.Time(DateTime.Now)";
mostCurrent._time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 397;BA.debugLine="lvg.stopAnim";
mostCurrent._lvg.stopAnim();
 //BA.debugLineNum = 398;BA.debugLine="Dim totalsize As Long = 0";
_totalsize = (long) (0);
 //BA.debugLineNum = 399;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 400;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 401;BA.debugLine="lw2.Clear";
mostCurrent._lw2.Clear();
 //BA.debugLineNum = 402;BA.debugLine="Try";
try { //BA.debugLineNum = 403;BA.debugLine="Dim lu As List = AppsList";
_lu = new anywheresoftware.b4a.objects.collections.List();
_lu.setObject((java.util.List)(_appslist));
 //BA.debugLineNum = 404;BA.debugLine="If lu.Size=0 Then";
if (_lu.getSize()==0) { 
 //BA.debugLineNum = 406;BA.debugLine="ListView1.AddSingleLine(\"No App cache found. Al";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence("No App cache found. All clear"));
 //BA.debugLineNum = 407;BA.debugLine="catlist.Clear";
mostCurrent._catlist.Clear();
 //BA.debugLineNum = 408;BA.debugLine="catlist.Add(time&\" - No App cache found\")";
mostCurrent._catlist.Add((Object)(mostCurrent._time+" - No App cache found"));
 //BA.debugLineNum = 409;BA.debugLine="t_start";
_t_start();
 //BA.debugLineNum = 410;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 412;BA.debugLine="For n = 0 To lu.Size-1";
{
final int step16 = 1;
final int limit16 = (int) (_lu.getSize()-1);
for (_n = (int) (0) ; (step16 > 0 && _n <= limit16) || (step16 < 0 && _n >= limit16); _n = ((int)(0 + _n + step16)) ) {
 //BA.debugLineNum = 413;BA.debugLine="Dim app() As Object = lu.Get(n)";
_app = (Object[])(_lu.Get(_n));
 //BA.debugLineNum = 414;BA.debugLine="If app(1) = \"com.android.systemui\" Then Continu";
if ((_app[(int) (1)]).equals((Object)("com.android.systemui"))) { 
if (true) continue;};
 //BA.debugLineNum = 415;BA.debugLine="Dim icon As BitmapDrawable = pm.GetApplicationI";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(_pm.GetApplicationIcon(BA.ObjectToString(_app[(int) (1)]))));
 //BA.debugLineNum = 416;BA.debugLine="ListView1.SetSelection(n)";
mostCurrent._listview1.SetSelection(_n);
 //BA.debugLineNum = 417;BA.debugLine="ListView1.AddTwoLinesAndBitmap(app(0),NumberFor";
mostCurrent._listview1.AddTwoLinesAndBitmap(BA.ObjectToCharSequence(_app[(int) (0)]),BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.NumberFormat2((double)(BA.ObjectToNumber(_app[(int) (2)]))/(double)1024/(double)1024,(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.True)+"MB"),mostCurrent._icon.getBitmap());
 //BA.debugLineNum = 418;BA.debugLine="lw3.AddTwoLinesAndBitmap(app(0),NumberFormat2(a";
mostCurrent._lw3.AddTwoLinesAndBitmap(BA.ObjectToCharSequence(_app[(int) (0)]),BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.NumberFormat2((double)(BA.ObjectToNumber(_app[(int) (2)]))/(double)1024/(double)1024,(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.True)+"MB"),mostCurrent._icon.getBitmap());
 //BA.debugLineNum = 419;BA.debugLine="totalsize = totalsize+app(2)";
_totalsize = (long) (_totalsize+(double)(BA.ObjectToNumber(_app[(int) (2)])));
 //BA.debugLineNum = 420;BA.debugLine="catlist.Clear";
mostCurrent._catlist.Clear();
 //BA.debugLineNum = 421;BA.debugLine="catlist.Add(app(0)&\" - \"&NumberFormat2(app(2)/1";
mostCurrent._catlist.Add((Object)(BA.ObjectToString(_app[(int) (0)])+" - "+anywheresoftware.b4a.keywords.Common.NumberFormat2((double)(BA.ObjectToNumber(_app[(int) (2)]))/(double)1024/(double)1024,(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.True)+"MB cache"+" | "+mostCurrent._op.formatSize(_totalsize)));
 //BA.debugLineNum = 422;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","cdata.txt",mostCurrent._catlist);
 }
};
 } 
       catch (Exception e29) {
			processBA.setLastException(e29); //BA.debugLineNum = 427;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 //BA.debugLineNum = 428;BA.debugLine="catlist.Add(LastException.Message)";
mostCurrent._catlist.Add((Object)(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage()));
 };
 //BA.debugLineNum = 430;BA.debugLine="If lu.Size>0 Then";
if (_lu.getSize()>0) { 
 //BA.debugLineNum = 431;BA.debugLine="cl_msg";
_cl_msg();
 }else {
 //BA.debugLineNum = 433;BA.debugLine="close";
_close();
 };
 //BA.debugLineNum = 435;BA.debugLine="End Sub";
return "";
}
public static String  _catdel_onscanprogress(int _current,int _total) throws Exception{
 //BA.debugLineNum = 376;BA.debugLine="Sub catdel_onScanProgress (Current As Int , Total";
 //BA.debugLineNum = 377;BA.debugLine="dpm1.Max=Total";
mostCurrent._dpm1.setMax(_total);
 //BA.debugLineNum = 378;BA.debugLine="dpm1.Progress=Current";
mostCurrent._dpm1.setProgress(_current);
 //BA.debugLineNum = 379;BA.debugLine="Label1.Text=\"durchsuche -> \"&Current&\" Apps nach";
mostCurrent._label1.setText(BA.ObjectToCharSequence("durchsuche -> "+BA.NumberToString(_current)+" Apps nach Müll"));
 //BA.debugLineNum = 381;BA.debugLine="End Sub";
return "";
}
public static String  _catdel_onscanstarted() throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Sub catdel_OnScanStarted";
 //BA.debugLineNum = 341;BA.debugLine="Log(\"Started\")";
anywheresoftware.b4a.keywords.Common.Log("Started");
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _cl_msg() throws Exception{
flm.b4a.betterdialogs.BetterDialogs _diap = null;
int _res = 0;
 //BA.debugLineNum = 437;BA.debugLine="Sub cl_msg";
 //BA.debugLineNum = 438;BA.debugLine="Dim diap As BetterDialogs";
_diap = new flm.b4a.betterdialogs.BetterDialogs();
 //BA.debugLineNum = 439;BA.debugLine="Dim res As Int";
_res = 0;
 //BA.debugLineNum = 440;BA.debugLine="res=diap.CustomDialog(\"Cache found:\",130dip,80dip";
_res = _diap.CustomDialog((Object)("Cache found:"),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (130)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._lw3.getObject())),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (350)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(Object)(anywheresoftware.b4a.keywords.Common.Colors.White),(Object)("Ja löschen"),(Object)(""),(Object)("Nein Ende"),anywheresoftware.b4a.keywords.Common.False,"diap",mostCurrent.activityBA);
 //BA.debugLineNum = 441;BA.debugLine="If res=DialogResponse.POSITIVE Then";
if (_res==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 442;BA.debugLine="catdel.CleanCache";
_catdel.CleanCache();
 //BA.debugLineNum = 443;BA.debugLine="t_start";
_t_start();
 }else {
 //BA.debugLineNum = 445;BA.debugLine="ImageView1.Visible=True";
mostCurrent._imageview1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 446;BA.debugLine="dpm1.Visible=False";
mostCurrent._dpm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 448;BA.debugLine="anima.Start(ImageView1)";
mostCurrent._anima.Start((android.view.View)(mostCurrent._imageview1.getObject()));
 //BA.debugLineNum = 449;BA.debugLine="full_close";
_full_close();
 };
 //BA.debugLineNum = 451;BA.debugLine="End Sub";
return "";
}
public static String  _close() throws Exception{
String _df = "";
 //BA.debugLineNum = 460;BA.debugLine="Sub close";
 //BA.debugLineNum = 461;BA.debugLine="If Not(catlist.size=0) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._catlist.getSize()==0)) { 
 //BA.debugLineNum = 462;BA.debugLine="Dim df As String";
_df = "";
 //BA.debugLineNum = 463;BA.debugLine="df=apklist.size";
_df = BA.NumberToString(mostCurrent._apklist.getSize());
 //BA.debugLineNum = 464;BA.debugLine="Label1.Text=op.formatSize(cat.FreeMemory)&\" RAM";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" RAM free! "+BA.NumberToString(mostCurrent._list2.getSize())+" -Backround Processes closed."+mostCurrent._xmsos.formateFileSize(mostCurrent.activityBA,(long)(Double.parseDouble(_df)))+" Files and Trash Data cleared"));
 //BA.debugLineNum = 466;BA.debugLine="catdel.CleanCache";
_catdel.CleanCache();
 //BA.debugLineNum = 467;BA.debugLine="Log(\"----------------CDATA -> \"&catlist.Size)";
anywheresoftware.b4a.keywords.Common.Log("----------------CDATA -> "+BA.NumberToString(mostCurrent._catlist.getSize()));
 }else {
 //BA.debugLineNum = 469;BA.debugLine="catlist.Clear";
mostCurrent._catlist.Clear();
 //BA.debugLineNum = 470;BA.debugLine="Label1.Text=op.formatSize(cat.FreeMemory)&\" RAM";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" RAM free! "+BA.NumberToString(mostCurrent._list4.getSize())+" Backround Processes killed...!"));
 //BA.debugLineNum = 471;BA.debugLine="catlist.Add(time&\" - No App cache found\")";
mostCurrent._catlist.Add((Object)(mostCurrent._time+" - No App cache found"));
 //BA.debugLineNum = 472;BA.debugLine="Log(\"--------------- NO CDATA--------------\")";
anywheresoftware.b4a.keywords.Common.Log("--------------- NO CDATA--------------");
 };
 //BA.debugLineNum = 474;BA.debugLine="delayed_t2";
_delayed_t2();
 //BA.debugLineNum = 475;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 476;BA.debugLine="End Sub";
return "";
}
public static String  _del_quest() throws Exception{
 //BA.debugLineNum = 478;BA.debugLine="Sub del_quest";
 //BA.debugLineNum = 479;BA.debugLine="ImageView1.Visible=True";
mostCurrent._imageview1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 480;BA.debugLine="dpm1.Visible=False";
mostCurrent._dpm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 482;BA.debugLine="ImageView1.Bitmap=LoadBitmap(File.DirAssets,\"Acce";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Accept128.png").getObject()));
 //BA.debugLineNum = 483;BA.debugLine="Label1.Text= \"clear RAM and close..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("clear RAM and close.."));
 //BA.debugLineNum = 485;BA.debugLine="real_delete";
_real_delete();
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _delayed_t2() throws Exception{
 //BA.debugLineNum = 553;BA.debugLine="Sub delayed_t2";
 //BA.debugLineNum = 554;BA.debugLine="dpm1.InnerBottomText=Label1.text";
mostCurrent._dpm1.setInnerBottomText(mostCurrent._label1.getText());
 //BA.debugLineNum = 555;BA.debugLine="dpm1.Progress=100";
mostCurrent._dpm1.setProgress((int) (100));
 //BA.debugLineNum = 556;BA.debugLine="lvg.SetVisibleAnimated(100,False)";
mostCurrent._lvg.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 557;BA.debugLine="lvg.stopAnim";
mostCurrent._lvg.stopAnim();
 //BA.debugLineNum = 559;BA.debugLine="If count > 7 Then";
if (_count>7) { 
 //BA.debugLineNum = 560;BA.debugLine="Label1.text=op.formatSize(cat.FreeMemory)&\" free";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" free.."));
 //BA.debugLineNum = 561;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 };
 //BA.debugLineNum = 563;BA.debugLine="If count> 8 Then";
if (_count>8) { 
 //BA.debugLineNum = 564;BA.debugLine="Label1.text=op.formatSize(cat.FreeMemory)";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())));
 //BA.debugLineNum = 566;BA.debugLine="ListView1.AddSingleLine(xMSOS.formateFileSize(ca";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence(mostCurrent._xmsos.formateFileSize(mostCurrent.activityBA,mostCurrent._cat.getFreeMemory())));
 };
 //BA.debugLineNum = 568;BA.debugLine="If count> 9 Then";
if (_count>9) { 
 //BA.debugLineNum = 570;BA.debugLine="anima.Start(ImageView1)";
mostCurrent._anima.Start((android.view.View)(mostCurrent._imageview1.getObject()));
 //BA.debugLineNum = 571;BA.debugLine="dpm1.Visible=False";
mostCurrent._dpm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 574;BA.debugLine="If count> 10 Then";
if (_count>10) { 
 };
 //BA.debugLineNum = 577;BA.debugLine="If count = 11 Then";
if (_count==11) { 
 //BA.debugLineNum = 579;BA.debugLine="t1.Enabled=False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 581;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 582;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 584;BA.debugLine="End Sub";
return "";
}
public static String  _file_copydone() throws Exception{
 //BA.debugLineNum = 693;BA.debugLine="Sub file_CopyDone";
 //BA.debugLineNum = 694;BA.debugLine="Log(\"copy done!\")";
anywheresoftware.b4a.keywords.Common.Log("copy done!");
 //BA.debugLineNum = 696;BA.debugLine="End Sub";
return "";
}
public static String  _full_close() throws Exception{
 //BA.debugLineNum = 585;BA.debugLine="Sub full_close";
 //BA.debugLineNum = 586;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 587;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 588;BA.debugLine="End Sub";
return "";
}
public static String  _getdeviceid() throws Exception{
int _api = 0;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
int _id = 0;
 //BA.debugLineNum = 646;BA.debugLine="Sub GetDeviceId As String";
 //BA.debugLineNum = 647;BA.debugLine="Dim api As Int";
_api = 0;
 //BA.debugLineNum = 648;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 649;BA.debugLine="api = r.GetStaticField(\"android.os.Build$VERSION\"";
_api = (int)(BA.ObjectToNumber(_r.GetStaticField("android.os.Build$VERSION","SDK_INT")));
 //BA.debugLineNum = 650;BA.debugLine="If api < 18 Then";
if (_api<18) { 
 //BA.debugLineNum = 652;BA.debugLine="If File.Exists(File.DirInternal, \"__id\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id")) { 
 //BA.debugLineNum = 653;BA.debugLine="Return File.ReadString(File.DirInternal, \"__id\"";
if (true) return anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id");
 //BA.debugLineNum = 654;BA.debugLine="c_start";
_c_start();
 }else {
 //BA.debugLineNum = 656;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 657;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 658;BA.debugLine="id = Rnd(0x10000000, 0x7FFFFFFF)";
_id = anywheresoftware.b4a.keywords.Common.Rnd((int) (0x10000000),(int) (0x7fffffff));
 //BA.debugLineNum = 659;BA.debugLine="File.WriteString(File.DirInternal, \"__id\", id)";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id",BA.NumberToString(_id));
 //BA.debugLineNum = 660;BA.debugLine="Return id";
if (true) return BA.NumberToString(_id);
 };
 //BA.debugLineNum = 662;BA.debugLine="Log(api)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_api));
 }else {
 //BA.debugLineNum = 665;BA.debugLine="Return r.GetStaticField(\"android.os.Build\", \"SER";
if (true) return BA.ObjectToString(_r.GetStaticField("android.os.Build","SERIAL"));
 //BA.debugLineNum = 666;BA.debugLine="storage_check";
_storage_check();
 };
 //BA.debugLineNum = 668;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim op As OperatingSystem";
mostCurrent._op = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 17;BA.debugLine="Dim count As Int=0";
_count = (int) (0);
 //BA.debugLineNum = 18;BA.debugLine="Dim args(1) As Object";
mostCurrent._args = new Object[(int) (1)];
{
int d0 = mostCurrent._args.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._args[i0] = new Object();
}
}
;
 //BA.debugLineNum = 19;BA.debugLine="Dim Obj1, Obj2, Obj3 As Reflector";
mostCurrent._obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj3 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 20;BA.debugLine="Dim Types(1), name,packName,date,time As String";
mostCurrent._types = new String[(int) (1)];
java.util.Arrays.fill(mostCurrent._types,"");
mostCurrent._name = "";
mostCurrent._packname = "";
mostCurrent._date = "";
mostCurrent._time = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim icon As BitmapDrawable";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 22;BA.debugLine="Dim pak As PackageManager";
mostCurrent._pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim list4,list1,apklist,list2,list5,list6,list7,l";
mostCurrent._list4 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._apklist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list2 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list5 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list6 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list7 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list8 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list9 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list10 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._trash = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 24;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
_c1 = 0;
_c2 = 0;
_c3 = 0;
_c4 = 0;
_c5 = 0;
_c6 = 0;
_c7 = 0;
_c8 = 0;
_c9 = 0;
_c10 = 0;
_c11 = 0;
_c12 = 0;
_c13 = 0;
_c14 = 0;
_c15 = 0;
_c16 = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim cat As Cache";
mostCurrent._cat = new flm.b4a.cache.Cache();
 //BA.debugLineNum = 26;BA.debugLine="Dim dir1 As String =File.DirDefaultExternal&\"/mnt";
mostCurrent._dir1 = anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache/store";
 //BA.debugLineNum = 27;BA.debugLine="Private lw2,lw3 As ListView";
mostCurrent._lw2 = new anywheresoftware.b4a.objects.ListViewWrapper();
mostCurrent._lw3 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim ph As PhoneEvents";
mostCurrent._ph = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 30;BA.debugLine="Dim size,flags,count As Int";
_size = 0;
_flags = 0;
_count = 0;
 //BA.debugLineNum = 31;BA.debugLine="Dim ffiles,ffolders As List";
mostCurrent._ffiles = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ffolders = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 32;BA.debugLine="Private dialog As CustomDialog2";
mostCurrent._dialog = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 33;BA.debugLine="Dim diapan As Panel";
mostCurrent._diapan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim dial As Label";
mostCurrent._dial = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim dill As List";
mostCurrent._dill = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 36;BA.debugLine="Dim ActivityManager1 As ActivityManager";
mostCurrent._activitymanager1 = new uk.co.martinpearman.b4a.activitymanager.ActivityManager();
 //BA.debugLineNum = 37;BA.debugLine="Dim RunningTaskInfos() As RunningTaskInfo";
mostCurrent._runningtaskinfos = new uk.co.martinpearman.b4a.activitymanager.RunningTaskInfo[(int) (0)];
{
int d0 = mostCurrent._runningtaskinfos.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._runningtaskinfos[i0] = new uk.co.martinpearman.b4a.activitymanager.RunningTaskInfo();
}
}
;
 //BA.debugLineNum = 38;BA.debugLine="Dim paths As Map";
mostCurrent._paths = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 39;BA.debugLine="Dim panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim storage As env";
mostCurrent._storage = new de.donmanfred.storage();
 //BA.debugLineNum = 41;BA.debugLine="Dim de As String = File.DirRootExternal";
mostCurrent._de = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 //BA.debugLineNum = 42;BA.debugLine="Dim mtc As Matcher = Regex.Matcher(\"(/|\\\\)[^(/|\\\\";
mostCurrent._mtc = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
mostCurrent._mtc = anywheresoftware.b4a.keywords.Common.Regex.Matcher("(/|\\\\)[^(/|\\\\)]*(/|\\\\)",mostCurrent._de);
 //BA.debugLineNum = 43;BA.debugLine="Dim extsdcard As String = de";
mostCurrent._extsdcard = mostCurrent._de;
 //BA.debugLineNum = 44;BA.debugLine="Dim nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 45;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim ffil,ffold As List";
mostCurrent._ffil = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ffold = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 48;BA.debugLine="Dim andro,bat,desk,work As Bitmap";
mostCurrent._andro = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._bat = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._desk = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._work = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 50;BA.debugLine="Dim ffiles,ffolders As List";
mostCurrent._ffiles = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ffolders = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 51;BA.debugLine="Private dpm1 As DonutProgressMaster";
mostCurrent._dpm1 = new circleprogressmasterwrapper.donutProgressMasterWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Dim root1 As String";
mostCurrent._root1 = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim anima As AnimationPlus";
mostCurrent._anima = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 54;BA.debugLine="dim ani as Animation";
mostCurrent._ani = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Dim xMSOS As MSOS";
mostCurrent._xmsos = new com.maximussoft.msos.MSOS();
 //BA.debugLineNum = 56;BA.debugLine="Dim xOSStats As OSStats";
mostCurrent._xosstats = new b4a.example.osstats();
 //BA.debugLineNum = 57;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Dim catlist As List";
mostCurrent._catlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 62;BA.debugLine="Private lvg As LVGearsTwo";
mostCurrent._lvg = new de.donmanfred.LVGearsTwoWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private kvs4sub,kvs4 As KeyValueStore";
mostCurrent._kvs4sub = new com.batcat.keyvaluestore();
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _mystats_update(float[] _cpuefficiency,float _ramusage) throws Exception{
 //BA.debugLineNum = 336;BA.debugLine="Sub myStats_Update(CPUEfficiency() As Float, RAMUs";
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static byte[]  _object_to_byte(Object _obj) throws Exception{
anywheresoftware.b4a.randomaccessfile.B4XSerializator _ser = null;
 //BA.debugLineNum = 670;BA.debugLine="Sub object_to_byte(obj As Object)As Byte()";
 //BA.debugLineNum = 671;BA.debugLine="Dim ser As B4XSerializator";
_ser = new anywheresoftware.b4a.randomaccessfile.B4XSerializator();
 //BA.debugLineNum = 672;BA.debugLine="Return ser.ConvertObjectToBytes(obj)";
if (true) return _ser.ConvertObjectToBytes(_obj);
 //BA.debugLineNum = 673;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim t1 As Timer";
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="Dim catdel As CacheCleaner";
_catdel = new anywheresoftware.b4a.cachecleaner.CacheCleaner();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _readdir(String _folder,boolean _recursive) throws Exception{
anywheresoftware.b4a.objects.collections.List _lst = null;
int _i = 0;
String _v = "";
 //BA.debugLineNum = 622;BA.debugLine="Sub ReadDir(folder As String, recursive As Boolean";
 //BA.debugLineNum = 623;BA.debugLine="ffolders.Clear";
mostCurrent._ffolders.Clear();
 //BA.debugLineNum = 624;BA.debugLine="ffiles.Clear";
mostCurrent._ffiles.Clear();
 //BA.debugLineNum = 626;BA.debugLine="Dim lst As List = File.ListFiles(folder)";
_lst = new anywheresoftware.b4a.objects.collections.List();
_lst = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
 //BA.debugLineNum = 627;BA.debugLine="For i = 0 To lst.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (_lst.getSize()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 628;BA.debugLine="If File.IsDirectory(folder,lst.Get(i)) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,BA.ObjectToString(_lst.Get(_i)))) { 
 //BA.debugLineNum = 629;BA.debugLine="Dim v As String";
_v = "";
 //BA.debugLineNum = 630;BA.debugLine="v = folder&\"/\"&lst.Get(i)";
_v = _folder+"/"+BA.ObjectToString(_lst.Get(_i));
 //BA.debugLineNum = 632;BA.debugLine="ffolders.Add(v.SubString(root1.Length+1))";
mostCurrent._ffolders.Add((Object)(_v.substring((int) (mostCurrent._root1.length()+1))));
 //BA.debugLineNum = 634;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 635;BA.debugLine="ReadDir(v,recursive)";
_readdir(_v,_recursive);
 };
 }else {
 //BA.debugLineNum = 639;BA.debugLine="ffiles.Add(folder&\"/\"&lst.Get(i))";
mostCurrent._ffiles.Add((Object)(_folder+"/"+BA.ObjectToString(_lst.Get(_i))));
 };
 }
};
 //BA.debugLineNum = 643;BA.debugLine="Log(ffolders.Size&\" Ordner / \"&ffiles.Size&\" Date";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(mostCurrent._ffolders.getSize())+" Ordner / "+BA.NumberToString(mostCurrent._ffiles.getSize())+" Dateien");
 //BA.debugLineNum = 644;BA.debugLine="End Sub";
return "";
}
public static String  _real_delete() throws Exception{
int _j = 0;
 //BA.debugLineNum = 488;BA.debugLine="Sub real_delete";
 //BA.debugLineNum = 489;BA.debugLine="RunningTaskInfos=ActivityManager1.GetRunningTasks";
mostCurrent._runningtaskinfos = mostCurrent._activitymanager1.GetRunningTasks(processBA);
 //BA.debugLineNum = 490;BA.debugLine="Log(\"RunningTaskInfos.Length=\"&RunningTaskInfos.L";
anywheresoftware.b4a.keywords.Common.Log("RunningTaskInfos.Length="+BA.NumberToString(mostCurrent._runningtaskinfos.length));
 //BA.debugLineNum = 492;BA.debugLine="For j = 0 To list7.Size-1";
{
final int step3 = 1;
final int limit3 = (int) (mostCurrent._list7.getSize()-1);
for (_j = (int) (0) ; (step3 > 0 && _j <= limit3) || (step3 < 0 && _j >= limit3); _j = ((int)(0 + _j + step3)) ) {
 //BA.debugLineNum = 493;BA.debugLine="Log(\"Recent Tasks: \"&list7.get(j))";
anywheresoftware.b4a.keywords.Common.Log("Recent Tasks: "+BA.ObjectToString(mostCurrent._list7.Get(_j)));
 //BA.debugLineNum = 494;BA.debugLine="op.killBackgroundProcesses(\"com.batcat\")";
mostCurrent._op.killBackgroundProcesses("com.batcat");
 //BA.debugLineNum = 495;BA.debugLine="op.killProcess(list7.Get(j))";
mostCurrent._op.killProcess((int)(BA.ObjectToNumber(mostCurrent._list7.Get(_j))));
 }
};
 //BA.debugLineNum = 497;BA.debugLine="close";
_close();
 //BA.debugLineNum = 498;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _s_scan() throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Sub s_scan";
 //BA.debugLineNum = 318;BA.debugLine="lvg.startAnim";
mostCurrent._lvg.startAnim();
 //BA.debugLineNum = 319;BA.debugLine="catdel.ScanCache";
_catdel.ScanCache();
 //BA.debugLineNum = 320;BA.debugLine="catdel.initialize(\"catdel\")";
_catdel.initialize("catdel",processBA);
 //BA.debugLineNum = 321;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _storage_check() throws Exception{
int _i = 0;
String _mnt = "";
anywheresoftware.b4a.objects.collections.List _dirs = null;
String _f = "";
 //BA.debugLineNum = 345;BA.debugLine="Sub storage_check";
 //BA.debugLineNum = 346;BA.debugLine="For i = 0 To paths.Size-1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._paths.getSize()-1);
for (_i = (int) (0) ; (step1 > 0 && _i <= limit1) || (step1 < 0 && _i >= limit1); _i = ((int)(0 + _i + step1)) ) {
 //BA.debugLineNum = 347;BA.debugLine="Log(paths.GetKeyAt(i)&\"=\"&paths.GetValueAt(i))";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._paths.GetKeyAt(_i))+"="+BA.ObjectToString(mostCurrent._paths.GetValueAt(_i)));
 }
};
 //BA.debugLineNum = 350;BA.debugLine="Log (\"DirRootExternal = \"&de)";
anywheresoftware.b4a.keywords.Common.Log("DirRootExternal = "+mostCurrent._de);
 //BA.debugLineNum = 352;BA.debugLine="If mtc.Find = True Then";
if (mostCurrent._mtc.Find()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 353;BA.debugLine="Dim mnt As String = mtc.Group(0)";
_mnt = mostCurrent._mtc.Group((int) (0));
 //BA.debugLineNum = 355;BA.debugLine="Log (\"mount point = \"& mnt)";
anywheresoftware.b4a.keywords.Common.Log("mount point = "+_mnt);
 //BA.debugLineNum = 356;BA.debugLine="Dim dirs As List = File.ListFiles(mnt)";
_dirs = new anywheresoftware.b4a.objects.collections.List();
_dirs = anywheresoftware.b4a.keywords.Common.File.ListFiles(_mnt);
 //BA.debugLineNum = 357;BA.debugLine="For Each f As String In dirs";
final anywheresoftware.b4a.BA.IterableList group9 = _dirs;
final int groupLen9 = group9.getSize();
for (int index9 = 0;index9 < groupLen9 ;index9++){
_f = BA.ObjectToString(group9.Get(index9));
 //BA.debugLineNum = 358;BA.debugLine="If storage.isExternalStorageRemovable(mnt&f) Th";
if (mostCurrent._storage.isExternalStorageRemovable(_mnt+_f)) { 
 //BA.debugLineNum = 359;BA.debugLine="Log (\"Device = \"& f&\":\"&mnt&f&\" is removable\")";
anywheresoftware.b4a.keywords.Common.Log("Device = "+_f+":"+_mnt+_f+" is removable");
 //BA.debugLineNum = 360;BA.debugLine="If File.ListFiles(mnt&f).IsInitialized Then";
if (anywheresoftware.b4a.keywords.Common.File.ListFiles(_mnt+_f).IsInitialized()) { 
 //BA.debugLineNum = 361;BA.debugLine="Log(\"probably ExtSDCard: \"&mnt&f)";
anywheresoftware.b4a.keywords.Common.Log("probably ExtSDCard: "+_mnt+_f);
 //BA.debugLineNum = 362;BA.debugLine="extsdcard = mnt&f";
mostCurrent._extsdcard = _mnt+_f;
 }else {
 };
 }else {
 //BA.debugLineNum = 367;BA.debugLine="Log (\"Device = \"& f&\":\"&mnt&f&\" is NOT removab";
anywheresoftware.b4a.keywords.Common.Log("Device = "+_f+":"+_mnt+_f+" is NOT removable");
 };
 }
;
 };
 //BA.debugLineNum = 372;BA.debugLine="s_scan";
_s_scan();
 //BA.debugLineNum = 373;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 221;BA.debugLine="c1=mcl.md_light_blue_A400";
_c1 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 222;BA.debugLine="c2=mcl.md_amber_A400";
_c2 = mostCurrent._mcl.getmd_amber_A400();
 //BA.debugLineNum = 223;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 224;BA.debugLine="c4=mcl.md_teal_A400";
_c4 = mostCurrent._mcl.getmd_teal_A400();
 //BA.debugLineNum = 225;BA.debugLine="c5=mcl.md_deep_purple_A400";
_c5 = mostCurrent._mcl.getmd_deep_purple_A400();
 //BA.debugLineNum = 226;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 227;BA.debugLine="c7=mcl.md_indigo_A400";
_c7 = mostCurrent._mcl.getmd_indigo_A400();
 //BA.debugLineNum = 228;BA.debugLine="c8=mcl.md_blue_A400";
_c8 = mostCurrent._mcl.getmd_blue_A400();
 //BA.debugLineNum = 229;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 230;BA.debugLine="c10=mcl.md_grey_600";
_c10 = mostCurrent._mcl.getmd_grey_600();
 //BA.debugLineNum = 231;BA.debugLine="c11=mcl.md_green_A400";
_c11 = mostCurrent._mcl.getmd_green_A400();
 //BA.debugLineNum = 232;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 233;BA.debugLine="c13=mcl.md_light_green_A400";
_c13 = mostCurrent._mcl.getmd_light_green_A400();
 //BA.debugLineNum = 234;BA.debugLine="c14=mcl.md_cyan_A400";
_c14 = mostCurrent._mcl.getmd_cyan_A400();
 //BA.debugLineNum = 235;BA.debugLine="c15=mcl.md_blue_grey_400";
_c15 = mostCurrent._mcl.getmd_blue_grey_400();
 //BA.debugLineNum = 236;BA.debugLine="c16=mcl.md_light_blue_A400";
_c16 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 237;BA.debugLine="If kvs4sub.ContainsKey(\"off\") Then";
if (mostCurrent._kvs4sub._containskey("off")) { 
 //BA.debugLineNum = 238;BA.debugLine="StopService(Starter)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 };
 //BA.debugLineNum = 240;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 241;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 242;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 245;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 246;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 247;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 };
 //BA.debugLineNum = 250;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 251;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 252;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 //BA.debugLineNum = 253;BA.debugLine="Label1.TextColor=Colors.Black";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 255;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 256;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 257;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 260;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 261;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 262;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 };
 //BA.debugLineNum = 265;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 266;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 267;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 };
 //BA.debugLineNum = 270;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 271;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 272;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 };
 //BA.debugLineNum = 275;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 276;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 277;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 };
 //BA.debugLineNum = 280;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 281;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 282;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 };
 //BA.debugLineNum = 285;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 286;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 287;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 };
 //BA.debugLineNum = 290;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 291;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 292;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 };
 //BA.debugLineNum = 295;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 296;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 297;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 //BA.debugLineNum = 298;BA.debugLine="Label1.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 300;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 301;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 302;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 };
 //BA.debugLineNum = 305;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 306;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 307;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 };
 //BA.debugLineNum = 310;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 311;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 312;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 };
 //BA.debugLineNum = 315;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return "";
}
public static String  _t_start() throws Exception{
 //BA.debugLineNum = 453;BA.debugLine="Sub t_start";
 //BA.debugLineNum = 454;BA.debugLine="lvg.startAnim";
mostCurrent._lvg.startAnim();
 //BA.debugLineNum = 455;BA.debugLine="dpm1.max=100";
mostCurrent._dpm1.setMax((int) (100));
 //BA.debugLineNum = 456;BA.debugLine="t1.Enabled=True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 457;BA.debugLine="t1_Tick";
_t1_tick();
 //BA.debugLineNum = 458;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 501;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 502;BA.debugLine="count=count+1";
_count = (int) (_count+1);
 //BA.debugLineNum = 503;BA.debugLine="ListView1.SetSelection(-1)";
mostCurrent._listview1.SetSelection((int) (-1));
 //BA.debugLineNum = 504;BA.debugLine="dpm1.Max=100";
mostCurrent._dpm1.setMax((int) (100));
 //BA.debugLineNum = 505;BA.debugLine="dpm1.UnfinishedStrokeWidth=20";
mostCurrent._dpm1.setUnfinishedStrokeWidth((float) (20));
 //BA.debugLineNum = 506;BA.debugLine="dpm1.UnfinishedStrokeColor=Colors.ARGB(180,255,25";
mostCurrent._dpm1.setUnfinishedStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (180),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 507;BA.debugLine="If count>0 Then";
if (_count>0) { 
 //BA.debugLineNum = 508;BA.debugLine="Label1.text=\"search Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("search Battery.."));
 };
 //BA.debugLineNum = 510;BA.debugLine="If count > 1 Then";
if (_count>1) { 
 //BA.debugLineNum = 512;BA.debugLine="Label1.text=\"check Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check Battery.."));
 //BA.debugLineNum = 513;BA.debugLine="dpm1.Progress=20";
mostCurrent._dpm1.setProgress((int) (20));
 };
 //BA.debugLineNum = 515;BA.debugLine="If count > 2 Then";
if (_count>2) { 
 //BA.debugLineNum = 516;BA.debugLine="Label1.text=\"optimize Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("optimize Battery.."));
 //BA.debugLineNum = 517;BA.debugLine="dpm1.Progress=36";
mostCurrent._dpm1.setProgress((int) (36));
 };
 //BA.debugLineNum = 519;BA.debugLine="If count > 3 Then";
if (_count>3) { 
 //BA.debugLineNum = 521;BA.debugLine="Label1.text=\"search Application cache\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("search Application cache"));
 //BA.debugLineNum = 523;BA.debugLine="dpm1.Progress=42";
mostCurrent._dpm1.setProgress((int) (42));
 };
 //BA.debugLineNum = 526;BA.debugLine="If count > 4 Then";
if (_count>4) { 
 //BA.debugLineNum = 528;BA.debugLine="dpm1.Progress=52";
mostCurrent._dpm1.setProgress((int) (52));
 //BA.debugLineNum = 531;BA.debugLine="Label1.text=\"cleaning Cache System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("cleaning Cache System.."));
 //BA.debugLineNum = 533;BA.debugLine="dpm1.Progress=67";
mostCurrent._dpm1.setProgress((int) (67));
 };
 //BA.debugLineNum = 535;BA.debugLine="If count > 5 Then";
if (_count>5) { 
 //BA.debugLineNum = 536;BA.debugLine="dpm1.Progress=89";
mostCurrent._dpm1.setProgress((int) (89));
 //BA.debugLineNum = 537;BA.debugLine="Label1.text=\"deleting Garbage Cache..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("deleting Garbage Cache.."));
 //BA.debugLineNum = 540;BA.debugLine="Label1.text=\"check: \"&op.formatSize(op.Availab";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check: "+mostCurrent._op.formatSize(mostCurrent._op.getAvailableMemory())));
 //BA.debugLineNum = 542;BA.debugLine="dpm1.Progress=100";
mostCurrent._dpm1.setProgress((int) (100));
 };
 //BA.debugLineNum = 544;BA.debugLine="If count > 6 Then";
if (_count>6) { 
 //BA.debugLineNum = 547;BA.debugLine="Label1.text=op.formatSize(cat.FreeMemory)";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())));
 //BA.debugLineNum = 549;BA.debugLine="CallSub(Me,\"del_quest\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(mostCurrent.activityBA,cool.getObject(),"del_quest");
 };
 //BA.debugLineNum = 551;BA.debugLine="End Sub";
return "";
}
}
