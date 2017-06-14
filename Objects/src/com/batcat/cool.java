package com.batcat;

import android.view.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
public com.rootsoft.oslibrary.OSLibrary _op = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
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
public anywheresoftware.b4a.objects.collections.List _list1 = null;
public anywheresoftware.b4a.objects.collections.List _list2 = null;
public anywheresoftware.b4a.objects.collections.List _list3 = null;
public anywheresoftware.b4a.objects.collections.List _clist = null;
public anywheresoftware.b4a.objects.collections.List _piclist = null;
public anywheresoftware.b4a.objects.collections.List _phlis = null;
public anywheresoftware.b4a.objects.collections.List _list4 = null;
public anywheresoftware.b4a.objects.collections.List _list5 = null;
public anywheresoftware.b4a.objects.collections.List _list6 = null;
public anywheresoftware.b4a.objects.collections.List _lis = null;
public anywheresoftware.b4a.objects.collections.List _list7 = null;
public anywheresoftware.b4a.objects.collections.List _list8 = null;
public anywheresoftware.b4a.objects.collections.List _list9 = null;
public anywheresoftware.b4a.objects.collections.List _apklist = null;
public anywheresoftware.b4a.objects.collections.List _reslist = null;
public anywheresoftware.b4a.objects.collections.List _aclist = null;
public anywheresoftware.b4a.objects.collections.List _apclist = null;
public anywheresoftware.b4a.objects.collections.List _filelist = null;
public anywheresoftware.b4a.cachecleaner.CacheCleaner _catdel = null;
public flm.b4a.cache.Cache _cat = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lw2 = null;
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
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public de.donmanfred.NumberProgressBarWrapper _pg = null;
public anywheresoftware.b4a.objects.collections.List _ffil = null;
public anywheresoftware.b4a.objects.collections.List _ffold = null;
public static String _rot = "";
public de.donmanfred.pgWheel _pgwheel1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _andro = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _desk = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _work = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public com.batcat.progress _prog = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.settings _settings = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.charts _charts = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _la = null;
anywheresoftware.b4a.objects.LabelWrapper _la1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lwa1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lwa2 = null;
int _h = 0;
 //BA.debugLineNum = 61;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 62;BA.debugLine="Activity.LoadLayout(\"5\")";
mostCurrent._activity.LoadLayout("5",mostCurrent.activityBA);
 //BA.debugLineNum = 63;BA.debugLine="Activity.Title=pak.GetApplicationLabel(\"com.batca";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(mostCurrent._pak.GetApplicationLabel("com.batcat")+" - "+mostCurrent._pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 64;BA.debugLine="op.Initialize(\"op\")";
mostCurrent._op.Initialize(processBA,"op");
 //BA.debugLineNum = 65;BA.debugLine="list1.Initialize";
mostCurrent._list1.Initialize();
 //BA.debugLineNum = 66;BA.debugLine="list2.Initialize";
mostCurrent._list2.Initialize();
 //BA.debugLineNum = 67;BA.debugLine="list3.Initialize";
mostCurrent._list3.Initialize();
 //BA.debugLineNum = 68;BA.debugLine="list4.Initialize";
mostCurrent._list4.Initialize();
 //BA.debugLineNum = 69;BA.debugLine="list5.Initialize";
mostCurrent._list5.Initialize();
 //BA.debugLineNum = 70;BA.debugLine="list6.Initialize";
mostCurrent._list6.Initialize();
 //BA.debugLineNum = 71;BA.debugLine="list7.Initialize";
mostCurrent._list7.Initialize();
 //BA.debugLineNum = 72;BA.debugLine="list8.Initialize";
mostCurrent._list8.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="list9.Initialize";
mostCurrent._list9.Initialize();
 //BA.debugLineNum = 74;BA.debugLine="reslist.Initialize";
mostCurrent._reslist.Initialize();
 //BA.debugLineNum = 75;BA.debugLine="apklist.Initialize";
mostCurrent._apklist.Initialize();
 //BA.debugLineNum = 76;BA.debugLine="clist.Initialize";
mostCurrent._clist.Initialize();
 //BA.debugLineNum = 77;BA.debugLine="aclist.Initialize";
mostCurrent._aclist.Initialize();
 //BA.debugLineNum = 78;BA.debugLine="filelist.Initialize";
mostCurrent._filelist.Initialize();
 //BA.debugLineNum = 79;BA.debugLine="apclist.Initialize";
mostCurrent._apclist.Initialize();
 //BA.debugLineNum = 80;BA.debugLine="ph.Initialize(\"ph\")";
mostCurrent._ph.Initialize(processBA,"ph");
 //BA.debugLineNum = 81;BA.debugLine="piclist.Initialize";
mostCurrent._piclist.Initialize();
 //BA.debugLineNum = 82;BA.debugLine="lis.Initialize";
mostCurrent._lis.Initialize();
 //BA.debugLineNum = 85;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 86;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 88;BA.debugLine="phlis.Initialize";
mostCurrent._phlis.Initialize();
 //BA.debugLineNum = 89;BA.debugLine="diapan.Initialize(\"diapan\")";
mostCurrent._diapan.Initialize(mostCurrent.activityBA,"diapan");
 //BA.debugLineNum = 90;BA.debugLine="dial.Initialize(\"dial\")";
mostCurrent._dial.Initialize(mostCurrent.activityBA,"dial");
 //BA.debugLineNum = 91;BA.debugLine="dill.Initialize";
mostCurrent._dill.Initialize();
 //BA.debugLineNum = 92;BA.debugLine="lw2.Initialize(\"lw2\")";
mostCurrent._lw2.Initialize(mostCurrent.activityBA,"lw2");
 //BA.debugLineNum = 93;BA.debugLine="ffiles.Initialize";
mostCurrent._ffiles.Initialize();
 //BA.debugLineNum = 94;BA.debugLine="ph.Initialize(\"ph\")";
mostCurrent._ph.Initialize(processBA,"ph");
 //BA.debugLineNum = 96;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 98;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cac";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","ressize.txt")) { 
 //BA.debugLineNum = 99;BA.debugLine="ToastMessageShow(\"core ready...!\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("core ready...!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 100;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 101;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 }else {
 //BA.debugLineNum = 104;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cache\"";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 105;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cache/";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache/store");
 //BA.debugLineNum = 106;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 107;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 //BA.debugLineNum = 108;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","apk1.txt",mostCurrent._list7);
 //BA.debugLineNum = 109;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","apk2.txt",mostCurrent._list8);
 //BA.debugLineNum = 110;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","apk3.txt",mostCurrent._list9);
 //BA.debugLineNum = 111;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","pn1.txt",mostCurrent._lis);
 //BA.debugLineNum = 112;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","apsize.txt",mostCurrent._clist);
 //BA.debugLineNum = 113;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","ressize.txt",mostCurrent._reslist);
 //BA.debugLineNum = 114;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","cclist.txt","");
 //BA.debugLineNum = 115;BA.debugLine="ToastMessageShow(\"Files ready! \"&date&\", \"&time,F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Files ready! "+mostCurrent._date+", "+mostCurrent._time),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 119;BA.debugLine="t1.Initialize(\"t1\",1000)";
_t1.Initialize(processBA,"t1",(long) (1000));
 //BA.debugLineNum = 120;BA.debugLine="t1.Enabled=False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 122;BA.debugLine="l2=ListView1.TwoLinesLayout.Label";
mostCurrent._l2 = mostCurrent._listview1.getTwoLinesLayout().Label;
 //BA.debugLineNum = 123;BA.debugLine="l2.TextSize=11";
mostCurrent._l2.setTextSize((float) (11));
 //BA.debugLineNum = 124;BA.debugLine="l2.TextColor=Colors.Black";
mostCurrent._l2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 125;BA.debugLine="l3=ListView1.TwoLinesLayout.SecondLabel";
mostCurrent._l3 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 126;BA.debugLine="l3.TextColor=Colors.White";
mostCurrent._l3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 127;BA.debugLine="l3.TextSize=10";
mostCurrent._l3.setTextSize((float) (10));
 //BA.debugLineNum = 128;BA.debugLine="ListView1.TwoLinesLayout.ItemHeight=25";
mostCurrent._listview1.getTwoLinesLayout().setItemHeight((int) (25));
 //BA.debugLineNum = 130;BA.debugLine="ffolders.Initialize";
mostCurrent._ffolders.Initialize();
 //BA.debugLineNum = 135;BA.debugLine="dial.TextSize=15";
mostCurrent._dial.setTextSize((float) (15));
 //BA.debugLineNum = 136;BA.debugLine="dial.TextColor=Colors.White";
mostCurrent._dial.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 137;BA.debugLine="dialog.AddView(diapan,350,350)";
mostCurrent._dialog.AddView((android.view.View)(mostCurrent._diapan.getObject()),(int) (350),(int) (350));
 //BA.debugLineNum = 138;BA.debugLine="diapan.AddView(lw2,3,3,-1,-1)";
mostCurrent._diapan.AddView((android.view.View)(mostCurrent._lw2.getObject()),(int) (3),(int) (3),(int) (-1),(int) (-1));
 //BA.debugLineNum = 139;BA.debugLine="Dim la,la1,lwa1,lwa2 As Label";
_la = new anywheresoftware.b4a.objects.LabelWrapper();
_la1 = new anywheresoftware.b4a.objects.LabelWrapper();
_lwa1 = new anywheresoftware.b4a.objects.LabelWrapper();
_lwa2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 140;BA.debugLine="la.Initialize(\"la\")";
_la.Initialize(mostCurrent.activityBA,"la");
 //BA.debugLineNum = 141;BA.debugLine="la1.Initialize(\"la1\")";
_la1.Initialize(mostCurrent.activityBA,"la1");
 //BA.debugLineNum = 142;BA.debugLine="lwa1.Initialize(\"lwa1\")";
_lwa1.Initialize(mostCurrent.activityBA,"lwa1");
 //BA.debugLineNum = 143;BA.debugLine="lwa2.Initialize(\"lwa2\")";
_lwa2.Initialize(mostCurrent.activityBA,"lwa2");
 //BA.debugLineNum = 145;BA.debugLine="lwa1=lw2.SingleLineLayout.Label";
_lwa1 = mostCurrent._lw2.getSingleLineLayout().Label;
 //BA.debugLineNum = 146;BA.debugLine="lwa1.TextColor=Colors.White";
_lwa1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 147;BA.debugLine="lwa1.TextSize=13";
_lwa1.setTextSize((float) (13));
 //BA.debugLineNum = 148;BA.debugLine="lw2.SingleLineLayout.ItemHeight=70";
mostCurrent._lw2.getSingleLineLayout().setItemHeight((int) (70));
 //BA.debugLineNum = 150;BA.debugLine="la=ListView1.TwoLinesAndBitmap.SecondLabel";
_la = mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 151;BA.debugLine="la.TextSize=12";
_la.setTextSize((float) (12));
 //BA.debugLineNum = 152;BA.debugLine="la.TextColor=Colors.ARGB(130,254,254,255)";
_la.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (130),(int) (254),(int) (254),(int) (255)));
 //BA.debugLineNum = 153;BA.debugLine="la1=ListView1.TwoLinesAndBitmap.Label";
_la1 = mostCurrent._listview1.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 154;BA.debugLine="la1.TextSize=14";
_la1.setTextSize((float) (14));
 //BA.debugLineNum = 155;BA.debugLine="la1.TextColor=Colors.ARGB(199,255,255,255)";
_la1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (199),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 156;BA.debugLine="ListView1.TwoLinesAndBitmap.ItemHeight=120";
mostCurrent._listview1.getTwoLinesAndBitmap().setItemHeight((int) (120));
 //BA.debugLineNum = 159;BA.debugLine="paths = storage.Initialize";
mostCurrent._paths = mostCurrent._storage.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="nativeMe.InitializeContext";
mostCurrent._nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 162;BA.debugLine="pgWheel1.Visible=False";
mostCurrent._pgwheel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="prog.Initialize(Activity,12%x,5%y,360,10,mcl.md_i";
mostCurrent._prog._initialize(mostCurrent.activityBA,mostCurrent._activity,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (12),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),(int) (360),(int) (10),mostCurrent._mcl.getmd_indigo_A200(),mostCurrent._mcl.getmd_white_1000(),(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 165;BA.debugLine="prog.BringToFront";
mostCurrent._prog._bringtofront();
 //BA.debugLineNum = 167;BA.debugLine="Panel2.Visible=False";
mostCurrent._panel2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 169;BA.debugLine="ffil.Initialize";
mostCurrent._ffil.Initialize();
 //BA.debugLineNum = 170;BA.debugLine="ffold.Initialize";
mostCurrent._ffold.Initialize();
 //BA.debugLineNum = 171;BA.debugLine="Activity.Color=mcl.md_deep_purple_A400'Colors.ARG";
mostCurrent._activity.setColor(mostCurrent._mcl.getmd_deep_purple_A400());
 //BA.debugLineNum = 173;BA.debugLine="Label1.SetTextColorAnimated(9000,Colors.White)";
mostCurrent._label1.SetTextColorAnimated((int) (9000),anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 175;BA.debugLine="For h = 0 To clist.Size-1";
{
final int step91 = 1;
final int limit91 = (int) (mostCurrent._clist.getSize()-1);
for (_h = (int) (0) ; (step91 > 0 && _h <= limit91) || (step91 < 0 && _h >= limit91); _h = ((int)(0 + _h + step91)) ) {
 //BA.debugLineNum = 176;BA.debugLine="Log(clist.Get(h))";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._clist.Get(_h)));
 }
};
 //BA.debugLineNum = 178;BA.debugLine="andro=LoadBitmap(File.DirAssets,\"ic_autorenew_bla";
mostCurrent._andro = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_autorenew_black_48dp.png");
 //BA.debugLineNum = 179;BA.debugLine="bat=LoadBitmap(File.DirAssets,\"ic_data_usage_blac";
mostCurrent._bat = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_data_usage_black_48dp.png");
 //BA.debugLineNum = 180;BA.debugLine="desk=LoadBitmap(File.DirAssets, \"ic_battery_alert";
mostCurrent._desk = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_battery_alert_black_48dp.png");
 //BA.debugLineNum = 181;BA.debugLine="work=LoadBitmap(File.DirAssets, \"ic_delete_black_";
mostCurrent._work = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_delete_black_48dp.png");
 //BA.debugLineNum = 183;BA.debugLine="GetDeviceId";
_getdeviceid();
 //BA.debugLineNum = 184;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 185;BA.debugLine="loading_norm";
_loading_norm();
 //BA.debugLineNum = 186;BA.debugLine="clean_start";
_clean_start();
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 200;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 201;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 202;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 204;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 196;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 197;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 189;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 190;BA.debugLine="t1.Enabled=True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 191;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 192;BA.debugLine="loading_norm";
_loading_norm();
 //BA.debugLineNum = 193;BA.debugLine="clean_start";
_clean_start();
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static String  _app_info() throws Exception{
int _i = 0;
 //BA.debugLineNum = 589;BA.debugLine="Sub app_info";
 //BA.debugLineNum = 591;BA.debugLine="list1=pak.GetInstalledPackages";
mostCurrent._list1 = mostCurrent._pak.GetInstalledPackages();
 //BA.debugLineNum = 593;BA.debugLine="Obj1.Target = Obj1.GetContext";
mostCurrent._obj1.Target = (Object)(mostCurrent._obj1.GetContext(processBA));
 //BA.debugLineNum = 594;BA.debugLine="Obj1.Target = Obj1.RunMethod(\"getPackageManager\")";
mostCurrent._obj1.Target = mostCurrent._obj1.RunMethod("getPackageManager");
 //BA.debugLineNum = 595;BA.debugLine="Obj2.Target = Obj1.RunMethod2(\"getInstalledPackag";
mostCurrent._obj2.Target = mostCurrent._obj1.RunMethod2("getInstalledPackages",BA.NumberToString(0),"java.lang.int");
 //BA.debugLineNum = 596;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 601;BA.debugLine="For i = 0 To size -1";
{
final int step6 = 1;
final int limit6 = (int) (_size-1);
for (_i = (int) (0) ; (step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6); _i = ((int)(0 + _i + step6)) ) {
 //BA.debugLineNum = 602;BA.debugLine="Obj3.Target = Obj2.RunMethod2(\"get\", i, \"java.la";
mostCurrent._obj3.Target = mostCurrent._obj2.RunMethod2("get",BA.NumberToString(_i),"java.lang.int");
 //BA.debugLineNum = 603;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 605;BA.debugLine="Obj3.Target = Obj3.GetField(\"applicationInfo\") '";
mostCurrent._obj3.Target = mostCurrent._obj3.GetField("applicationInfo");
 //BA.debugLineNum = 606;BA.debugLine="flags = Obj3.GetField(\"flags\")";
_flags = (int)(BA.ObjectToNumber(mostCurrent._obj3.GetField("flags")));
 //BA.debugLineNum = 607;BA.debugLine="packName = Obj3.GetField(\"packageName\")";
mostCurrent._packname = BA.ObjectToString(mostCurrent._obj3.GetField("packageName"));
 //BA.debugLineNum = 609;BA.debugLine="If Bit.And(flags, 1)  = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_flags,(int) (1))==0) { 
 //BA.debugLineNum = 612;BA.debugLine="args(0) = Obj3.Target";
mostCurrent._args[(int) (0)] = mostCurrent._obj3.Target;
 //BA.debugLineNum = 613;BA.debugLine="Types(0) = \"android.content.pm.ApplicationInfo\"";
mostCurrent._types[(int) (0)] = "android.content.pm.ApplicationInfo";
 //BA.debugLineNum = 614;BA.debugLine="name = Obj1.RunMethod4(\"getApplicationLabel\", a";
mostCurrent._name = BA.ObjectToString(mostCurrent._obj1.RunMethod4("getApplicationLabel",mostCurrent._args,mostCurrent._types));
 //BA.debugLineNum = 615;BA.debugLine="icon = Obj1.RunMethod4(\"getApplicationIcon\", ar";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(mostCurrent._obj1.RunMethod4("getApplicationIcon",mostCurrent._args,mostCurrent._types)));
 //BA.debugLineNum = 617;BA.debugLine="phlis.Add(icon.Bitmap)";
mostCurrent._phlis.Add((Object)(mostCurrent._icon.getBitmap()));
 //BA.debugLineNum = 618;BA.debugLine="list3.Add(packName)";
mostCurrent._list3.Add((Object)(mostCurrent._packname));
 };
 }
};
 //BA.debugLineNum = 622;BA.debugLine="End Sub";
return "";
}
public static Object  _byte_to_object(byte[] _data) throws Exception{
anywheresoftware.b4a.randomaccessfile.B4XSerializator _ser = null;
 //BA.debugLineNum = 629;BA.debugLine="Sub byte_to_object(data() As Byte)As Object";
 //BA.debugLineNum = 630;BA.debugLine="Dim ser As B4XSerializator";
_ser = new anywheresoftware.b4a.randomaccessfile.B4XSerializator();
 //BA.debugLineNum = 631;BA.debugLine="Return ser.ConvertBytesToObject(data)";
if (true) return _ser.ConvertBytesToObject(_data);
 //BA.debugLineNum = 632;BA.debugLine="End Sub";
return null;
}
public static String  _c_start() throws Exception{
 //BA.debugLineNum = 207;BA.debugLine="Sub c_start";
 //BA.debugLineNum = 208;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 209;BA.debugLine="pgWheel1.Visible=False";
mostCurrent._pgwheel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 210;BA.debugLine="dill.Clear";
mostCurrent._dill.Clear();
 //BA.debugLineNum = 211;BA.debugLine="reslist.Clear";
mostCurrent._reslist.Clear();
 //BA.debugLineNum = 212;BA.debugLine="piclist.Clear";
mostCurrent._piclist.Clear();
 //BA.debugLineNum = 213;BA.debugLine="filelist.Clear";
mostCurrent._filelist.Clear();
 //BA.debugLineNum = 214;BA.debugLine="apclist.Clear";
mostCurrent._apclist.Clear();
 //BA.debugLineNum = 215;BA.debugLine="list2.Clear";
mostCurrent._list2.Clear();
 //BA.debugLineNum = 216;BA.debugLine="list4.Clear";
mostCurrent._list4.Clear();
 //BA.debugLineNum = 217;BA.debugLine="phlis.Clear";
mostCurrent._phlis.Clear();
 //BA.debugLineNum = 218;BA.debugLine="piclist.Add(andro)";
mostCurrent._piclist.Add((Object)(mostCurrent._andro.getObject()));
 //BA.debugLineNum = 219;BA.debugLine="piclist.Add(bat)";
mostCurrent._piclist.Add((Object)(mostCurrent._bat.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="piclist.Add(desk)";
mostCurrent._piclist.Add((Object)(mostCurrent._desk.getObject()));
 //BA.debugLineNum = 221;BA.debugLine="li4";
_li4();
 //BA.debugLineNum = 222;BA.debugLine="cat_start";
_cat_start();
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static long  _calcsize(String _folder,boolean _recursive) throws Exception{
long _size1 = 0L;
String _f = "";
 //BA.debugLineNum = 685;BA.debugLine="Sub CalcSize(Folder As String, recursive As Boolea";
 //BA.debugLineNum = 686;BA.debugLine="Dim size1 As Long";
_size1 = 0L;
 //BA.debugLineNum = 687;BA.debugLine="For Each f As String In File.ListFiles(Folder)";
final anywheresoftware.b4a.BA.IterableList group2 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
final int groupLen2 = group2.getSize();
for (int index2 = 0;index2 < groupLen2 ;index2++){
_f = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 688;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 689;BA.debugLine="If File.IsDirectory(Folder, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,_f)) { 
 //BA.debugLineNum = 690;BA.debugLine="size1 = size1 + CalcSize(File.Combine(Folder,";
_size1 = (long) (_size1+_calcsize(anywheresoftware.b4a.keywords.Common.File.Combine(_folder,_f),_recursive));
 };
 };
 //BA.debugLineNum = 693;BA.debugLine="size1 = size1 + File.Size(Folder, f)";
_size1 = (long) (_size1+anywheresoftware.b4a.keywords.Common.File.Size(_folder,_f));
 }
;
 //BA.debugLineNum = 695;BA.debugLine="Return size1";
if (true) return _size1;
 //BA.debugLineNum = 696;BA.debugLine="End Sub";
return 0L;
}
public static String  _cat_start() throws Exception{
 //BA.debugLineNum = 282;BA.debugLine="Sub cat_start";
 //BA.debugLineNum = 283;BA.debugLine="count=count+1";
_count = (int) (_count+1);
 //BA.debugLineNum = 286;BA.debugLine="clean_start";
_clean_start();
 //BA.debugLineNum = 287;BA.debugLine="t1.Enabled=True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 288;BA.debugLine="t1_Tick";
_t1_tick();
 //BA.debugLineNum = 289;BA.debugLine="End Sub";
return "";
}
public static String  _clean_start() throws Exception{
 //BA.debugLineNum = 526;BA.debugLine="Sub clean_start";
 //BA.debugLineNum = 527;BA.debugLine="pg.incrementProgressBy(0)";
mostCurrent._pg.incrementProgressBy((int) (0));
 //BA.debugLineNum = 528;BA.debugLine="pg.ProgressTextColor = Colors.Black";
mostCurrent._pg.setProgressTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 529;BA.debugLine="pg.ReachedBarColor = Colors.ARGB(185,255,255,255)";
mostCurrent._pg.setReachedBarColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (185),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 530;BA.debugLine="pg.UnreachedBarColor = Colors.Transparent";
mostCurrent._pg.setUnreachedBarColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 531;BA.debugLine="pg.UnreachedBarHeight = 25dip";
mostCurrent._pg.setUnreachedBarHeight((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25))));
 //BA.debugLineNum = 532;BA.debugLine="pg.ReachedBarHeight = 20dip";
mostCurrent._pg.setReachedBarHeight((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))));
 //BA.debugLineNum = 533;BA.debugLine="pg.ProgressTextSize=20dip";
mostCurrent._pg.setProgressTextSize((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))));
 //BA.debugLineNum = 534;BA.debugLine="pg.Max=100";
mostCurrent._pg.setMax((int) (100));
 //BA.debugLineNum = 535;BA.debugLine="pg.Width=100%x";
mostCurrent._pg.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 536;BA.debugLine="pg.Left=1dip";
mostCurrent._pg.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 537;BA.debugLine="pg.Prefix = \"%\"";
mostCurrent._pg.setPrefix("%");
 //BA.debugLineNum = 538;BA.debugLine="pg.Suffix = \"...\"";
mostCurrent._pg.setSuffix("...");
 //BA.debugLineNum = 539;BA.debugLine="End Sub";
return "";
}
public static String  _close() throws Exception{
String _df = "";
 //BA.debugLineNum = 312;BA.debugLine="Sub close";
 //BA.debugLineNum = 313;BA.debugLine="If Not(apklist.size=0) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._apklist.getSize()==0)) { 
 //BA.debugLineNum = 314;BA.debugLine="Dim df As String";
_df = "";
 //BA.debugLineNum = 315;BA.debugLine="df=apklist.size";
_df = BA.NumberToString(mostCurrent._apklist.getSize());
 //BA.debugLineNum = 316;BA.debugLine="Label1.Text=op.formatSize(cat.FreeMemory)&\" RAM";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" RAM free! "+BA.NumberToString(mostCurrent._list4.getSize())+" -Backround Processes closed."+_df+" Files and Trash Data cleared"));
 }else {
 //BA.debugLineNum = 318;BA.debugLine="Label1.Text=op.formatSize(cat.FreeMemory)&\" RAM";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" RAM free! "+BA.NumberToString(mostCurrent._list4.getSize())+" Backround Processes killed...!"));
 };
 //BA.debugLineNum = 323;BA.debugLine="delayed_t2";
_delayed_t2();
 //BA.debugLineNum = 324;BA.debugLine="End Sub";
return "";
}
public static String  _del_quest() throws Exception{
 //BA.debugLineNum = 326;BA.debugLine="Sub del_quest";
 //BA.debugLineNum = 327;BA.debugLine="pgWheel1.Visible=False";
mostCurrent._pgwheel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 328;BA.debugLine="ImageView1.Bitmap=LoadBitmap(File.DirAssets,\"Acce";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Accept128.png").getObject()));
 //BA.debugLineNum = 329;BA.debugLine="Label1.Text= \"clear RAM and close..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("clear RAM and close.."));
 //BA.debugLineNum = 331;BA.debugLine="real_delete";
_real_delete();
 //BA.debugLineNum = 333;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
public static String  _delayed_t2() throws Exception{
 //BA.debugLineNum = 498;BA.debugLine="Sub delayed_t2";
 //BA.debugLineNum = 499;BA.debugLine="pg.Progress=100";
mostCurrent._pg.setProgress((int) (100));
 //BA.debugLineNum = 500;BA.debugLine="prog.ClearProgress";
mostCurrent._prog._clearprogress();
 //BA.debugLineNum = 501;BA.debugLine="prog.SetProgress(360/100*3.6)";
mostCurrent._prog._setprogress((int) (360/(double)100*3.6));
 //BA.debugLineNum = 502;BA.debugLine="If count = 15 Then";
if (_count==15) { 
 //BA.debugLineNum = 503;BA.debugLine="catdel.clearCache";
mostCurrent._catdel.clearCache();
 //BA.debugLineNum = 504;BA.debugLine="t1.Enabled=False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 505;BA.debugLine="ToastMessageShow(op.formatSize(cat.FreeMemory)&\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())+" free"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 506;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 508;BA.debugLine="End Sub";
return "";
}
public static String  _deleting_files() throws Exception{
String _dd = "";
String _ens = "";
int _i = 0;
int _d = 0;
 //BA.debugLineNum = 336;BA.debugLine="Sub deleting_files";
 //BA.debugLineNum = 337;BA.debugLine="dill.Clear";
mostCurrent._dill.Clear();
 //BA.debugLineNum = 339;BA.debugLine="Dim dd As String";
_dd = "";
 //BA.debugLineNum = 340;BA.debugLine="Dim ens As String";
_ens = "";
 //BA.debugLineNum = 341;BA.debugLine="For i = 0 To  list3.Size-1";
{
final int step4 = 1;
final int limit4 = (int) (mostCurrent._list3.getSize()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 342;BA.debugLine="dd=list3.Get(i)";
_dd = BA.ObjectToString(mostCurrent._list3.Get(_i));
 //BA.debugLineNum = 343;BA.debugLine="ens=GetParentPath(GetSourceDir(GetActivitiesInfo";
_ens = _getparentpath(_getsourcedir(_getactivitiesinfo(_dd)));
 //BA.debugLineNum = 344;BA.debugLine="ReadDir(ens,True)";
_readdir(_ens,anywheresoftware.b4a.keywords.Common.True);
 }
};
 //BA.debugLineNum = 347;BA.debugLine="For d = 0 To ffold.Size-1";
{
final int step9 = 1;
final int limit9 = (int) (mostCurrent._ffold.getSize()-1);
for (_d = (int) (0) ; (step9 > 0 && _d <= limit9) || (step9 < 0 && _d >= limit9); _d = ((int)(0 + _d + step9)) ) {
 }
};
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
return "";
}
public static String  _fc_copydone(String _key,boolean _error) throws Exception{
 //BA.debugLineNum = 635;BA.debugLine="Sub fc_CopyDone(Key As String, Error As Boolean)";
 //BA.debugLineNum = 637;BA.debugLine="End Sub";
return "";
}
public static String  _fc_putdone(String _key,boolean _error) throws Exception{
 //BA.debugLineNum = 639;BA.debugLine="Sub fc_PutDone(key As String,Error As Boolean)";
 //BA.debugLineNum = 641;BA.debugLine="End Sub";
return "";
}
public static String  _fc2_putdone(String _key,boolean _error) throws Exception{
 //BA.debugLineNum = 643;BA.debugLine="Sub fc2_PutDone(key As String,Error As Boolean)";
 //BA.debugLineNum = 645;BA.debugLine="End Sub";
return "";
}
public static String  _file_copydone() throws Exception{
 //BA.debugLineNum = 698;BA.debugLine="Sub file_CopyDone";
 //BA.debugLineNum = 699;BA.debugLine="Log(\"copy done!\")";
anywheresoftware.b4a.keywords.Common.Log("copy done!");
 //BA.debugLineNum = 701;BA.debugLine="End Sub";
return "";
}
public static Object  _getactivitiesinfo(String _package) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 667;BA.debugLine="Sub GetActivitiesInfo(package As String) As Object";
 //BA.debugLineNum = 668;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 669;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(processBA));
 //BA.debugLineNum = 670;BA.debugLine="r.Target = r.RunMethod(\"getPackageManager\")";
_r.Target = _r.RunMethod("getPackageManager");
 //BA.debugLineNum = 671;BA.debugLine="r.Target = r.RunMethod3(\"getPackageInfo\", package";
_r.Target = _r.RunMethod3("getPackageInfo",_package,"java.lang.String",BA.NumberToString(0x00000001),"java.lang.int");
 //BA.debugLineNum = 672;BA.debugLine="Return r.GetField(\"applicationInfo\")";
if (true) return _r.GetField("applicationInfo");
 //BA.debugLineNum = 673;BA.debugLine="End Sub";
return null;
}
public static String  _getdeviceid() throws Exception{
int _api = 0;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
int _id = 0;
 //BA.debugLineNum = 225;BA.debugLine="Sub GetDeviceId As String";
 //BA.debugLineNum = 226;BA.debugLine="Dim api As Int";
_api = 0;
 //BA.debugLineNum = 227;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 228;BA.debugLine="api = r.GetStaticField(\"android.os.Build$VERSION\"";
_api = (int)(BA.ObjectToNumber(_r.GetStaticField("android.os.Build$VERSION","SDK_INT")));
 //BA.debugLineNum = 229;BA.debugLine="If api < 18 Then";
if (_api<18) { 
 //BA.debugLineNum = 231;BA.debugLine="If File.Exists(File.DirInternal, \"__id\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id")) { 
 //BA.debugLineNum = 232;BA.debugLine="Return File.ReadString(File.DirInternal, \"__id\"";
if (true) return anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id");
 //BA.debugLineNum = 233;BA.debugLine="c_start";
_c_start();
 }else {
 //BA.debugLineNum = 235;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 236;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 237;BA.debugLine="id = Rnd(0x10000000, 0x7FFFFFFF)";
_id = anywheresoftware.b4a.keywords.Common.Rnd((int) (0x10000000),(int) (0x7fffffff));
 //BA.debugLineNum = 238;BA.debugLine="File.WriteString(File.DirInternal, \"__id\", id)";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"__id",BA.NumberToString(_id));
 //BA.debugLineNum = 239;BA.debugLine="Return id";
if (true) return BA.NumberToString(_id);
 };
 //BA.debugLineNum = 241;BA.debugLine="Log(api)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_api));
 }else {
 //BA.debugLineNum = 244;BA.debugLine="Return r.GetStaticField(\"android.os.Build\", \"SER";
if (true) return BA.ObjectToString(_r.GetStaticField("android.os.Build","SERIAL"));
 //BA.debugLineNum = 248;BA.debugLine="storage_check";
_storage_check();
 };
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
return "";
}
public static long  _getdir(String _dir,boolean _recursive) throws Exception{
String _si = "";
long _siz = 0L;
String _f = "";
 //BA.debugLineNum = 561;BA.debugLine="Sub getdir(dir As String, recursive As Boolean) As";
 //BA.debugLineNum = 562;BA.debugLine="Dim si As String";
_si = "";
 //BA.debugLineNum = 563;BA.debugLine="Dim siz As Long";
_siz = 0L;
 //BA.debugLineNum = 564;BA.debugLine="For Each f As String In File.ListFiles(dir)";
final anywheresoftware.b4a.BA.IterableList group3 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_dir);
final int groupLen3 = group3.getSize();
for (int index3 = 0;index3 < groupLen3 ;index3++){
_f = BA.ObjectToString(group3.Get(index3));
 //BA.debugLineNum = 565;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 566;BA.debugLine="If File.IsDirectory(dir, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_dir,_f)) { 
 //BA.debugLineNum = 567;BA.debugLine="si = dir";
_si = _dir;
 //BA.debugLineNum = 568;BA.debugLine="Log(si)";
anywheresoftware.b4a.keywords.Common.Log(_si);
 //BA.debugLineNum = 569;BA.debugLine="siz = siz + getdir(File.Combine(dir, f),recurs";
_siz = (long) (_siz+_getdir(anywheresoftware.b4a.keywords.Common.File.Combine(_dir,_f),_recursive));
 };
 };
 //BA.debugLineNum = 572;BA.debugLine="siz = siz + File.Size(dir, f)";
_siz = (long) (_siz+anywheresoftware.b4a.keywords.Common.File.Size(_dir,_f));
 }
;
 //BA.debugLineNum = 574;BA.debugLine="Return siz";
if (true) return _siz;
 //BA.debugLineNum = 575;BA.debugLine="End Sub";
return 0L;
}
public static String  _getparentpath(String _path) throws Exception{
String _l = "";
String _path1 = "";
 //BA.debugLineNum = 647;BA.debugLine="Sub GetParentPath(Path As String) As String";
 //BA.debugLineNum = 648;BA.debugLine="Dim L As String";
_l = "";
 //BA.debugLineNum = 649;BA.debugLine="Dim Path1 As String";
_path1 = "";
 //BA.debugLineNum = 650;BA.debugLine="If Path = \"/\" Then";
if ((_path).equals("/")) { 
 //BA.debugLineNum = 651;BA.debugLine="Return \"/\"";
if (true) return "/";
 };
 //BA.debugLineNum = 653;BA.debugLine="L = Path.LastIndexOf(\"/\")";
_l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 654;BA.debugLine="If L = Path.Length - 1 Then";
if ((_l).equals(BA.NumberToString(_path.length()-1))) { 
 //BA.debugLineNum = 656;BA.debugLine="Path1 = Path.SubString2(0,L)";
_path1 = _path.substring((int) (0),(int)(Double.parseDouble(_l)));
 }else {
 //BA.debugLineNum = 658;BA.debugLine="Path1 = Path";
_path1 = _path;
 };
 //BA.debugLineNum = 660;BA.debugLine="L = Path.LastIndexOf(\"/\")";
_l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 661;BA.debugLine="If L = 0 Then";
if ((_l).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 662;BA.debugLine="L = 1";
_l = BA.NumberToString(1);
 };
 //BA.debugLineNum = 664;BA.debugLine="Return Path1.SubString2(0,L)";
if (true) return _path1.substring((int) (0),(int)(Double.parseDouble(_l)));
 //BA.debugLineNum = 665;BA.debugLine="End Sub";
return "";
}
public static String  _getsourcedir(Object _appinfo) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 675;BA.debugLine="Sub GetSourceDir(AppInfo As Object) As String";
 //BA.debugLineNum = 676;BA.debugLine="Try";
try { //BA.debugLineNum = 677;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 678;BA.debugLine="r.Target = AppInfo";
_r.Target = _appinfo;
 //BA.debugLineNum = 679;BA.debugLine="Return r.GetField(\"sourceDir\")";
if (true) return BA.ObjectToString(_r.GetField("sourceDir"));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 681;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 683;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim op As OperatingSystem";
mostCurrent._op = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 13;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim count As Int=0";
_count = (int) (0);
 //BA.debugLineNum = 15;BA.debugLine="Dim args(1) As Object";
mostCurrent._args = new Object[(int) (1)];
{
int d0 = mostCurrent._args.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._args[i0] = new Object();
}
}
;
 //BA.debugLineNum = 16;BA.debugLine="Dim Obj1, Obj2, Obj3 As Reflector";
mostCurrent._obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj3 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 17;BA.debugLine="Dim Types(1), name,packName,date,time As String";
mostCurrent._types = new String[(int) (1)];
java.util.Arrays.fill(mostCurrent._types,"");
mostCurrent._name = "";
mostCurrent._packname = "";
mostCurrent._date = "";
mostCurrent._time = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim icon As BitmapDrawable";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 20;BA.debugLine="Dim pak As PackageManager";
mostCurrent._pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim list1,list2,list3,clist,piclist,phlis,list4,l";
mostCurrent._list1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list2 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list3 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._clist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._piclist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._phlis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list4 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list5 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list6 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._lis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list7 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list8 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list9 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._apklist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._reslist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._aclist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._apclist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._filelist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 22;BA.debugLine="Dim catdel As CacheCleaner";
mostCurrent._catdel = new anywheresoftware.b4a.cachecleaner.CacheCleaner();
 //BA.debugLineNum = 23;BA.debugLine="Dim cat As Cache";
mostCurrent._cat = new flm.b4a.cache.Cache();
 //BA.debugLineNum = 25;BA.debugLine="Private lw2 As ListView";
mostCurrent._lw2 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim ph As PhoneEvents";
mostCurrent._ph = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 28;BA.debugLine="Dim size,flags,count As Int";
_size = 0;
_flags = 0;
_count = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim ffiles,ffolders As List";
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
 //BA.debugLineNum = 39;BA.debugLine="Dim paths As Map";
mostCurrent._paths = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 40;BA.debugLine="Dim panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim storage As env";
mostCurrent._storage = new de.donmanfred.storage();
 //BA.debugLineNum = 42;BA.debugLine="Dim de As String = File.DirRootExternal";
mostCurrent._de = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 //BA.debugLineNum = 43;BA.debugLine="Dim mtc As Matcher = Regex.Matcher(\"(/|\\\\)[^(/|\\\\";
mostCurrent._mtc = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
mostCurrent._mtc = anywheresoftware.b4a.keywords.Common.Regex.Matcher("(/|\\\\)[^(/|\\\\)]*(/|\\\\)",mostCurrent._de);
 //BA.debugLineNum = 44;BA.debugLine="Dim extsdcard As String = de";
mostCurrent._extsdcard = mostCurrent._de;
 //BA.debugLineNum = 45;BA.debugLine="Dim nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 46;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private pg As NumberProgressBar";
mostCurrent._pg = new de.donmanfred.NumberProgressBarWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim ffil,ffold As List";
mostCurrent._ffil = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ffold = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 53;BA.debugLine="Dim rot As String";
mostCurrent._rot = "";
 //BA.debugLineNum = 54;BA.debugLine="Private pgWheel1 As pgWheel";
mostCurrent._pgwheel1 = new de.donmanfred.pgWheel();
 //BA.debugLineNum = 55;BA.debugLine="Dim andro,bat,desk,work As Bitmap";
mostCurrent._andro = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._bat = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._desk = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._work = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 58;BA.debugLine="Dim prog As progress";
mostCurrent._prog = new com.batcat.progress();
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _li4() throws Exception{
int _u = 0;
 //BA.debugLineNum = 302;BA.debugLine="Sub li4";
 //BA.debugLineNum = 304;BA.debugLine="list2=op.RunningServiceInfo(999,list4,list5,list6";
mostCurrent._list2.setObject((java.util.List)(mostCurrent._op.RunningServiceInfo((int) (999),(java.util.List)(mostCurrent._list4.getObject()),(java.util.List)(mostCurrent._list5.getObject()),(java.util.List)(mostCurrent._list6.getObject()))));
 //BA.debugLineNum = 305;BA.debugLine="For u = 0 To list2.Size-1";
{
final int step2 = 1;
final int limit2 = (int) (mostCurrent._list2.getSize()-1);
for (_u = (int) (0) ; (step2 > 0 && _u <= limit2) || (step2 < 0 && _u >= limit2); _u = ((int)(0 + _u + step2)) ) {
 //BA.debugLineNum = 306;BA.debugLine="Log(list2.Get(u))";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._list2.Get(_u)));
 }
};
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _loading_norm() throws Exception{
 //BA.debugLineNum = 370;BA.debugLine="Sub loading_norm";
 //BA.debugLineNum = 371;BA.debugLine="pgWheel1.CircleRadius=100";
mostCurrent._pgwheel1.setCircleRadius((int) (100));
 //BA.debugLineNum = 372;BA.debugLine="pgWheel1.FocusableInTouchMode=False";
mostCurrent._pgwheel1.setFocusableInTouchMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 373;BA.debugLine="pgWheel1.CircleColor= Colors.ARGB(90,255,255,255)";
mostCurrent._pgwheel1.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (90),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 374;BA.debugLine="pgWheel1.ContourColor=Colors.ARGB(255,255,255,255";
mostCurrent._pgwheel1.setContourColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 375;BA.debugLine="pgWheel1.BarColor= Colors.ARGB(220,255,255,255)";
mostCurrent._pgwheel1.setBarColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (220),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 376;BA.debugLine="pgWheel1.ContourSize=2dip";
mostCurrent._pgwheel1.setContourSize(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 377;BA.debugLine="pgWheel1.FadingEdgeLength=3dip";
mostCurrent._pgwheel1.setFadingEdgeLength(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3)));
 //BA.debugLineNum = 378;BA.debugLine="pgWheel1.TextSize=45";
mostCurrent._pgwheel1.setTextSize((int) (45));
 //BA.debugLineNum = 379;BA.debugLine="pgWheel1.TextColor=Colors.Black";
mostCurrent._pgwheel1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 380;BA.debugLine="pgWheel1.SpinSpeed=10";
mostCurrent._pgwheel1.setSpinSpeed((int) (10));
 //BA.debugLineNum = 381;BA.debugLine="pgWheel1.RimColor= Colors.ARGB(100,11,170,242)";
mostCurrent._pgwheel1.setRimColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (11),(int) (170),(int) (242)));
 //BA.debugLineNum = 382;BA.debugLine="pgWheel1.RimWidth=25dip";
mostCurrent._pgwheel1.setRimWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)));
 //BA.debugLineNum = 383;BA.debugLine="pgWheel1.BarWidth=15dip";
mostCurrent._pgwheel1.setBarWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (15)));
 //BA.debugLineNum = 384;BA.debugLine="pgWheel1.DelayMillis=0.6";
mostCurrent._pgwheel1.setDelayMillis((int) (0.6));
 //BA.debugLineNum = 385;BA.debugLine="pgWheel1.Clickable=False";
mostCurrent._pgwheel1.setClickable(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 387;BA.debugLine="End Sub";
return "";
}
public static String  _logcat_logcatdata(byte[] _buffer,int _length) throws Exception{
String _data = "";
 //BA.debugLineNum = 293;BA.debugLine="Sub logcat_LogCatData(Buffer() As Byte, Length As";
 //BA.debugLineNum = 294;BA.debugLine="Dim data As String";
_data = "";
 //BA.debugLineNum = 295;BA.debugLine="data = BytesToString(Buffer,0,Length,\"UTF-8\")";
_data = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_length,"UTF-8");
 //BA.debugLineNum = 297;BA.debugLine="Log(data)";
anywheresoftware.b4a.keywords.Common.Log(_data);
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return "";
}
public static byte[]  _object_to_byte(Object _obj) throws Exception{
anywheresoftware.b4a.randomaccessfile.B4XSerializator _ser = null;
 //BA.debugLineNum = 624;BA.debugLine="Sub object_to_byte(obj As Object)As Byte()";
 //BA.debugLineNum = 625;BA.debugLine="Dim ser As B4XSerializator";
_ser = new anywheresoftware.b4a.randomaccessfile.B4XSerializator();
 //BA.debugLineNum = 626;BA.debugLine="Return ser.ConvertObjectToBytes(obj)";
if (true) return _ser.ConvertObjectToBytes(_obj);
 //BA.debugLineNum = 627;BA.debugLine="End Sub";
return null;
}
public static String  _pg_onprogresschange(int _current,int _maxvalue) throws Exception{
String _tr = "";
 //BA.debugLineNum = 510;BA.debugLine="Sub pg_onProgressChange(current As Int, maxvalue A";
 //BA.debugLineNum = 511;BA.debugLine="andro.Initialize(File.DirAssets,\"Android.png\")";
mostCurrent._andro.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Android.png");
 //BA.debugLineNum = 512;BA.debugLine="bat.Initialize(File.DirAssets,\"Battery.png\")";
mostCurrent._bat.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png");
 //BA.debugLineNum = 513;BA.debugLine="desk.Initialize(File.DirAssets, \"Chart.png\")";
mostCurrent._desk.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Chart.png");
 //BA.debugLineNum = 514;BA.debugLine="maxvalue=100";
_maxvalue = (int) (100);
 //BA.debugLineNum = 515;BA.debugLine="If current=count+1 Then";
if (_current==_count+1) { 
 //BA.debugLineNum = 516;BA.debugLine="For Each tr As String In reslist";
final anywheresoftware.b4a.BA.IterableList group6 = mostCurrent._reslist;
final int groupLen6 = group6.getSize();
for (int index6 = 0;index6 < groupLen6 ;index6++){
_tr = BA.ObjectToString(group6.Get(index6));
 //BA.debugLineNum = 517;BA.debugLine="Log(tr)";
anywheresoftware.b4a.keywords.Common.Log(_tr);
 //BA.debugLineNum = 518;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 //BA.debugLineNum = 519;BA.debugLine="Label1.Text=tr";
mostCurrent._label1.setText(BA.ObjectToCharSequence(_tr));
 }
;
 };
 //BA.debugLineNum = 522;BA.debugLine="If current=100 Then";
if (_current==100) { 
 //BA.debugLineNum = 523;BA.debugLine="ImageView1.Bitmap=LoadBitmap(File.DirAssets,\"Acc";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Accept128.png").getObject()));
 };
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
return "";
}
public static String  _ph_devicestorageok(anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 388;BA.debugLine="Sub ph_DeviceStorageOk (Intent As Intent)";
 //BA.debugLineNum = 389;BA.debugLine="Log(Intent.ExtrasToString)";
anywheresoftware.b4a.keywords.Common.Log(_intent.ExtrasToString());
 //BA.debugLineNum = 390;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim t1 As Timer";
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _readdir(String _folder,boolean _recursive) throws Exception{
anywheresoftware.b4a.objects.collections.List _lst = null;
int _i = 0;
String _v = "";
 //BA.debugLineNum = 542;BA.debugLine="Sub ReadDir(folder As String, recursive As Boolean";
 //BA.debugLineNum = 544;BA.debugLine="Dim lst As List = File.ListFiles(folder)";
_lst = new anywheresoftware.b4a.objects.collections.List();
_lst = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
 //BA.debugLineNum = 545;BA.debugLine="For i = 0 To lst.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_lst.getSize()-1);
for (_i = (int) (0) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 546;BA.debugLine="If File.IsDirectory(folder,lst.Get(i)) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,BA.ObjectToString(_lst.Get(_i)))) { 
 //BA.debugLineNum = 547;BA.debugLine="Dim v As String";
_v = "";
 //BA.debugLineNum = 548;BA.debugLine="v = folder&\"/\"&lst.Get(i)";
_v = _folder+"/"+BA.ObjectToString(_lst.Get(_i));
 //BA.debugLineNum = 550;BA.debugLine="ffold.Add(v.SubString(rot.Length+1))";
mostCurrent._ffold.Add((Object)(_v.substring((int) (mostCurrent._rot.length()+1))));
 //BA.debugLineNum = 551;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 552;BA.debugLine="ReadDir(v,recursive)";
_readdir(_v,_recursive);
 };
 }else {
 //BA.debugLineNum = 555;BA.debugLine="ffil.Add(folder&\"/\"&lst.Get(i))";
mostCurrent._ffil.Add((Object)(_folder+"/"+BA.ObjectToString(_lst.Get(_i))));
 };
 }
};
 //BA.debugLineNum = 559;BA.debugLine="End Sub";
return "";
}
public static String  _real_delete() throws Exception{
uk.co.martinpearman.b4a.activitymanager.RunningTaskInfo _runningtaskinfo1 = null;
 //BA.debugLineNum = 354;BA.debugLine="Sub real_delete";
 //BA.debugLineNum = 355;BA.debugLine="pgWheel1.Progress=360";
mostCurrent._pgwheel1.setProgress((int) (360));
 //BA.debugLineNum = 356;BA.debugLine="RunningTaskInfos=ActivityManager1.GetRunningTasks";
mostCurrent._runningtaskinfos = mostCurrent._activitymanager1.GetRunningTasks(processBA);
 //BA.debugLineNum = 357;BA.debugLine="Log(\"RunningTaskInfos.Length=\"&RunningTaskInfos.L";
anywheresoftware.b4a.keywords.Common.Log("RunningTaskInfos.Length="+BA.NumberToString(mostCurrent._runningtaskinfos.length));
 //BA.debugLineNum = 359;BA.debugLine="For Each RunningTaskInfo1 As RunningTaskInfo In R";
final uk.co.martinpearman.b4a.activitymanager.RunningTaskInfo[] group4 = mostCurrent._runningtaskinfos;
final int groupLen4 = group4.length;
for (int index4 = 0;index4 < groupLen4 ;index4++){
_runningtaskinfo1 = group4[index4];
 //BA.debugLineNum = 362;BA.debugLine="Label1.Text=RunningTaskInfo1.GetApplicationName&";
mostCurrent._label1.setText(BA.ObjectToCharSequence(_runningtaskinfo1.GetApplicationName(processBA)+"  - "+_runningtaskinfo1.GetPackageName()));
 }
;
 //BA.debugLineNum = 368;BA.debugLine="close";
_close();
 //BA.debugLineNum = 369;BA.debugLine="End Sub";
return "";
}
public static String  _storage_check() throws Exception{
int _i = 0;
String _mnt = "";
anywheresoftware.b4a.objects.collections.List _dirs = null;
String _f = "";
 //BA.debugLineNum = 252;BA.debugLine="Sub storage_check";
 //BA.debugLineNum = 253;BA.debugLine="For i = 0 To paths.Size-1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._paths.getSize()-1);
for (_i = (int) (0) ; (step1 > 0 && _i <= limit1) || (step1 < 0 && _i >= limit1); _i = ((int)(0 + _i + step1)) ) {
 //BA.debugLineNum = 254;BA.debugLine="Log(paths.GetKeyAt(i)&\"=\"&paths.GetValueAt(i))";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._paths.GetKeyAt(_i))+"="+BA.ObjectToString(mostCurrent._paths.GetValueAt(_i)));
 }
};
 //BA.debugLineNum = 257;BA.debugLine="Log (\"DirRootExternal = \"&de)";
anywheresoftware.b4a.keywords.Common.Log("DirRootExternal = "+mostCurrent._de);
 //BA.debugLineNum = 259;BA.debugLine="If mtc.Find = True Then";
if (mostCurrent._mtc.Find()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 260;BA.debugLine="Dim mnt As String = mtc.Group(0)";
_mnt = mostCurrent._mtc.Group((int) (0));
 //BA.debugLineNum = 262;BA.debugLine="Log (\"mount point = \"& mnt)";
anywheresoftware.b4a.keywords.Common.Log("mount point = "+_mnt);
 //BA.debugLineNum = 263;BA.debugLine="Dim dirs As List = File.ListFiles(mnt)";
_dirs = new anywheresoftware.b4a.objects.collections.List();
_dirs = anywheresoftware.b4a.keywords.Common.File.ListFiles(_mnt);
 //BA.debugLineNum = 264;BA.debugLine="For Each f As String In dirs";
final anywheresoftware.b4a.BA.IterableList group9 = _dirs;
final int groupLen9 = group9.getSize();
for (int index9 = 0;index9 < groupLen9 ;index9++){
_f = BA.ObjectToString(group9.Get(index9));
 //BA.debugLineNum = 265;BA.debugLine="If storage.isExternalStorageRemovable(mnt&f) Th";
if (mostCurrent._storage.isExternalStorageRemovable(_mnt+_f)) { 
 //BA.debugLineNum = 266;BA.debugLine="Log (\"Device = \"& f&\":\"&mnt&f&\" is removable\")";
anywheresoftware.b4a.keywords.Common.Log("Device = "+_f+":"+_mnt+_f+" is removable");
 //BA.debugLineNum = 267;BA.debugLine="If File.ListFiles(mnt&f).IsInitialized Then";
if (anywheresoftware.b4a.keywords.Common.File.ListFiles(_mnt+_f).IsInitialized()) { 
 //BA.debugLineNum = 268;BA.debugLine="Log(\"probably ExtSDCard: \"&mnt&f)";
anywheresoftware.b4a.keywords.Common.Log("probably ExtSDCard: "+_mnt+_f);
 //BA.debugLineNum = 269;BA.debugLine="extsdcard = mnt&f";
mostCurrent._extsdcard = _mnt+_f;
 }else {
 };
 }else {
 //BA.debugLineNum = 274;BA.debugLine="Log (\"Device = \"& f&\":\"&mnt&f&\" is NOT removab";
anywheresoftware.b4a.keywords.Common.Log("Device = "+_f+":"+_mnt+_f+" is NOT removable");
 };
 }
;
 };
 //BA.debugLineNum = 278;BA.debugLine="Activity.SetColorAnimated(1000,Colors.ARGB(100,30";
mostCurrent._activity.SetColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (30),(int) (124),(int) (235)),anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (190),(int) (35),(int) (140),(int) (7)));
 //BA.debugLineNum = 279;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 280;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 392;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 393;BA.debugLine="andro=LoadBitmap(File.DirAssets,\"ic_autorenew_bla";
mostCurrent._andro = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_autorenew_black_48dp.png");
 //BA.debugLineNum = 394;BA.debugLine="bat=LoadBitmap(File.DirAssets,\"ic_data_usage_blac";
mostCurrent._bat = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_data_usage_black_48dp.png");
 //BA.debugLineNum = 395;BA.debugLine="desk=LoadBitmap(File.DirAssets, \"ic_battery_alert";
mostCurrent._desk = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_battery_alert_black_48dp.png");
 //BA.debugLineNum = 396;BA.debugLine="work=LoadBitmap(File.DirAssets, \"ic_delete_black_";
mostCurrent._work = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_delete_black_48dp.png");
 //BA.debugLineNum = 398;BA.debugLine="pgWheel1.SpinSpeed=100";
mostCurrent._pgwheel1.setSpinSpeed((int) (100));
 //BA.debugLineNum = 399;BA.debugLine="pgWheel1.spin";
mostCurrent._pgwheel1.spin();
 //BA.debugLineNum = 400;BA.debugLine="ListView1.Visible=True";
mostCurrent._listview1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 401;BA.debugLine="pg.SetColorAnimated(12000,Colors.Transparent,mcl.";
mostCurrent._pg.SetColorAnimated((int) (12000),anywheresoftware.b4a.keywords.Common.Colors.Transparent,mostCurrent._mcl.getmd_lime_A700());
 //BA.debugLineNum = 402;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 //BA.debugLineNum = 403;BA.debugLine="count=count+1";
_count = (int) (_count+1);
 //BA.debugLineNum = 404;BA.debugLine="prog.ClearProgress";
mostCurrent._prog._clearprogress();
 //BA.debugLineNum = 405;BA.debugLine="prog.SetProgress(360/15*count+1)";
mostCurrent._prog._setprogress((int) (360/(double)15*_count+1));
 //BA.debugLineNum = 406;BA.debugLine="pg.incrementProgressBy(count)";
mostCurrent._pg.incrementProgressBy(_count);
 //BA.debugLineNum = 407;BA.debugLine="If count>0 Then";
if (_count>0) { 
 //BA.debugLineNum = 408;BA.debugLine="Label1.Text=\"check Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check Battery.."));
 };
 //BA.debugLineNum = 410;BA.debugLine="If count > 1 Then";
if (_count>1) { 
 //BA.debugLineNum = 411;BA.debugLine="Label1.Text=\"check Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check Battery.."));
 };
 //BA.debugLineNum = 413;BA.debugLine="If count > 2 Then";
if (_count>2) { 
 //BA.debugLineNum = 414;BA.debugLine="Label1.Text=\"check Battery..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check Battery.."));
 };
 //BA.debugLineNum = 417;BA.debugLine="If count > 3 Then";
if (_count>3) { 
 //BA.debugLineNum = 419;BA.debugLine="Label1.Text=\"check System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check System.."));
 //BA.debugLineNum = 420;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"System Prozesse:";
mostCurrent._listview1.AddTwoLinesAndBitmap(BA.ObjectToCharSequence("System Prozesse:"),BA.ObjectToCharSequence(BA.NumberToString(mostCurrent._list2.getSize())+" gefunden."),(android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 };
 //BA.debugLineNum = 422;BA.debugLine="If count > 4 Then";
if (_count>4) { 
 //BA.debugLineNum = 424;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 425;BA.debugLine="Label1.Text=\"check System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check System.."));
 };
 //BA.debugLineNum = 428;BA.debugLine="If count > 5 Then";
if (_count>5) { 
 //BA.debugLineNum = 430;BA.debugLine="pgWheel1.SpinSpeed=200";
mostCurrent._pgwheel1.setSpinSpeed((int) (200));
 //BA.debugLineNum = 431;BA.debugLine="ImageView1.Bitmap=bat";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._bat.getObject()));
 //BA.debugLineNum = 432;BA.debugLine="Label1.Text=\"clear Cache System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("clear Cache System.."));
 };
 //BA.debugLineNum = 435;BA.debugLine="If count > 6 Then";
if (_count>6) { 
 //BA.debugLineNum = 436;BA.debugLine="Label1.Text=\"defrag File System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("defrag File System.."));
 };
 //BA.debugLineNum = 438;BA.debugLine="If count > 7 Then";
if (_count>7) { 
 //BA.debugLineNum = 439;BA.debugLine="Label1.Text=\"search SDCard System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("search SDCard System.."));
 //BA.debugLineNum = 440;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 };
 //BA.debugLineNum = 446;BA.debugLine="If count > 8 Then";
if (_count>8) { 
 //BA.debugLineNum = 447;BA.debugLine="Label1.Text=\"clear Cache System..\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("clear Cache System.."));
 //BA.debugLineNum = 448;BA.debugLine="ImageView1.Bitmap=desk";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._desk.getObject()));
 //BA.debugLineNum = 449;BA.debugLine="Label1.Text=\"check \"&op.formatSize(op.Availabl";
mostCurrent._label1.setText(BA.ObjectToCharSequence("check "+mostCurrent._op.formatSize(mostCurrent._op.getAvailableMemory())));
 };
 //BA.debugLineNum = 451;BA.debugLine="If count > 9 Then";
if (_count>9) { 
 //BA.debugLineNum = 452;BA.debugLine="pgWheel1.SpinSpeed=100";
mostCurrent._pgwheel1.setSpinSpeed((int) (100));
 //BA.debugLineNum = 453;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 };
 //BA.debugLineNum = 456;BA.debugLine="If count > 10 Then";
if (_count>10) { 
 //BA.debugLineNum = 457;BA.debugLine="ImageView1.Bitmap=desk";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._desk.getObject()));
 //BA.debugLineNum = 458;BA.debugLine="Label1.Text=\"Running Process: \"&  list3.Size";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Running Process: "+BA.NumberToString(mostCurrent._list3.getSize())));
 };
 //BA.debugLineNum = 461;BA.debugLine="If count > 11 Then";
if (_count>11) { 
 //BA.debugLineNum = 462;BA.debugLine="ImageView1.Bitmap=bat";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._bat.getObject()));
 };
 //BA.debugLineNum = 464;BA.debugLine="If count > 12 Then";
if (_count>12) { 
 //BA.debugLineNum = 465;BA.debugLine="If Not (filelist.Size=0) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._filelist.getSize()==0)) { 
 //BA.debugLineNum = 466;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 }else {
 //BA.debugLineNum = 469;BA.debugLine="ImageView1.Bitmap=desk";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._desk.getObject()));
 //BA.debugLineNum = 470;BA.debugLine="Label1.Text=\"Clear!\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Clear!"));
 };
 //BA.debugLineNum = 473;BA.debugLine="pgWheel1.stopSpinning";
mostCurrent._pgwheel1.stopSpinning();
 };
 //BA.debugLineNum = 477;BA.debugLine="If count > 13 Then";
if (_count>13) { 
 //BA.debugLineNum = 478;BA.debugLine="Label1.Text=op.formatSize(cat.FreeMemory)";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._op.formatSize(mostCurrent._cat.getFreeMemory())));
 //BA.debugLineNum = 479;BA.debugLine="If Not (filelist.Size=1) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._filelist.getSize()==1)) { 
 //BA.debugLineNum = 480;BA.debugLine="pgWheel1.Progress=180";
mostCurrent._pgwheel1.setProgress((int) (180));
 //BA.debugLineNum = 481;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 //BA.debugLineNum = 482;BA.debugLine="Label1.Text=nativeMe.RunMethod(\"getOSCodename\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getOSCodename",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 483;BA.debugLine="ImageView1.Bitmap=bat";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._bat.getObject()));
 }else {
 //BA.debugLineNum = 485;BA.debugLine="ImageView1.Bitmap=andro";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._andro.getObject()));
 //BA.debugLineNum = 487;BA.debugLine="ImageView1.Bitmap=desk";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(mostCurrent._desk.getObject()));
 };
 };
 //BA.debugLineNum = 492;BA.debugLine="If count > 14 Then";
if (_count>14) { 
 //BA.debugLineNum = 493;BA.debugLine="pgWheel1.Progress=360";
mostCurrent._pgwheel1.setProgress((int) (360));
 //BA.debugLineNum = 494;BA.debugLine="CallSub(Me,\"del_quest\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(mostCurrent.activityBA,cool.getObject(),"del_quest");
 };
 //BA.debugLineNum = 496;BA.debugLine="End Sub";
return "";
}

//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;


  //private final Context context;
  private TelephonyManager tm; // = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
  private String initialVal = "";

  public int RINGER_MODE_SILENT = 0;
  public int RINGER_MODE_NORMAL = 1;
  public int RINGER_MODE_VIBRATE = 2;

  /**
   * The constant LOGTAG.
   */
  //private static final String LOGTAG = "EasyDeviceInfo";





  /**
   * Instantiates a new Easy device info.
   *
   * @param context the context
   */
//  public EasyDeviceInfo(Context context) {
//    this.context = context;
//    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//    initialVal = "na";
//  }

  /**
   * Gets library version.
   *
   * @return the library version
   */
//  public String getLibraryVersion() {
//    String version = "1.1.9";
//    int versionCode = 11;
//    return version + "-" + versionCode;
//  }

  /**
   * Gets android id.
   *
   * @return the android id
   */
  public String getAndroidID() {
    String result = initialVal;
    try {
      result = Settings.Secure.getString(BA.applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
   /**
   * Gets model.
   *
   * @return the model
   */
  public String getModel() {
    String result = initialVal;
    try {
      result = Build.MODEL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 


  /**
   * Gets build brand.
   *
   * @return the build brand
   */
  public String getBuildBrand() {
    String result = initialVal;
    try {
      result = Build.BRAND;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }

  /**
   * Gets build host.
   *
   * @return the build host
   */
  public String getBuildHost() {
    String result = initialVal;
    try {
      result = Build.HOST;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets build tags.
   *
   * @return the build tags
   */
  public String getBuildTags() {
    String result = initialVal;
    try {
      result = Build.TAGS;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
  /**
   * Gets build time.
   *
   * @return the build time
   */
  public long getBuildTime() {
    long result = 0;
    try {
      result = Build.TIME;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }  
  
  /**
   * Gets build user.
   *
   * @return the build user
   */
  public String getBuildUser() {
    String result = initialVal;
    try {
      result = Build.USER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets build version release.
   *
   * @return the build version release
   */
  public String getBuildVersionRelease() {
    String result = initialVal;
    try {
      result = Build.VERSION.RELEASE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
    /**
   * Gets screen display id.
   *
   * @return the screen display id
   */
  public String getScreenDisplayID() {
    String result = initialVal;
    try {
      WindowManager wm = (WindowManager) BA.applicationContext.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      result = String.valueOf(display.getDisplayId());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets build version codename.
   *
   * @return the build version codename
   */
  public String getBuildVersionCodename() {
    String result = initialVal;
    try {
      result = Build.VERSION.CODENAME;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  /**
   * Gets build version incremental.
   *
   * @return the build version incremental
   */
  public String getBuildVersionIncremental() {
    String result = initialVal;
    try {
      result = Build.VERSION.INCREMENTAL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  

  /**
   * Gets build version sdk.
   *
   * @return the build version sdk
   */
  public int getBuildVersionSDK() {
    int result = 0;
    try {
      result = Build.VERSION.SDK_INT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  

  /**
   * Gets build id.
   *
   * @return the build id
   */
  public String getBuildID() {
    String result = initialVal;
    try {
      result = Build.ID;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
        
  /**
   * Is Device rooted boolean
   *
   * @return the boolean
   */
  public boolean isDeviceRooted() {
    String su = "su";
    String[] locations = {
        "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
        "/data/local/xbin/", "/data/local/bin/", "/data/local/"
    };
    for (String location : locations) {
      if (new File(location + su).exists()) {
        return true;
      }
    }
    return false;
  }
  

  /**
   * Get supported abis string [ ].
   *
   * @return the string [ ]
   */
  public String[] getSupportedABIS() {
    String[] result = new String[] { "-" };
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        result = Build.SUPPORTED_ABIS;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length == 0) {
      result = new String[] { "-" };
    }
    return result;
  }
  


  /**
   * Gets string supported abis.
   *
   * @return the string supported abis
   */
  public String getStringSupportedABIS() {
    String result = initialVal;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        String[] supportedABIS = Build.SUPPORTED_ABIS;

        StringBuilder supportedABIString = new StringBuilder();
        if (supportedABIS.length > 0) {
          for (String abis : supportedABIS) {
            supportedABIString.append(abis).append("_");
          }
          supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
        } else {
          supportedABIString.append(initialVal);
        }
        result = supportedABIString.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }  
  
  
   /**
   * Gets string supported 32 bit abis.
   *
   * @return the string supported 32 bit abis
   */
  public String getStringSupported32bitABIS() {
    String result = initialVal;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        String[] supportedABIS = Build.SUPPORTED_32_BIT_ABIS;

        StringBuilder supportedABIString = new StringBuilder();
        if (supportedABIS.length > 0) {
          for (String abis : supportedABIS) {
            supportedABIString.append(abis).append("_");
          }
          supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
        } else {
          supportedABIString.append(initialVal);
        }

        result = supportedABIString.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }

    return handleIllegalCharacterInResult(result);
  } 
  
  
  /**
   * Gets manufacturer.
   *
   * @return the manufacturer
   */
  public String getManufacturer() {
    String result = initialVal;
    try {
      result = Build.MANUFACTURER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }  
  
  
  
   /**
   * Gets resolution.
   *
   * @return the resolution
   */
  public String getResolution() {
    String result = initialVal;
    try {
      WindowManager wm = (WindowManager) BA.applicationContext.getSystemService(Context.WINDOW_SERVICE);

      Display display = wm.getDefaultDisplay();

      DisplayMetrics metrics = new DisplayMetrics();
      display.getMetrics(metrics);
      result = metrics.heightPixels + "x" + metrics.widthPixels;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  } 
  

  /**
   * Gets carrier.
   *
   * @return the carrier
   */
  public String getCarrier() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
        result = tm.getNetworkOperatorName().toLowerCase(Locale.getDefault());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }
  
  
  /**
   * Gets device.
   *
   * @return the device
   */
  public String getDevice() {
    String result = initialVal;
    try {
      result = Build.DEVICE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets bootloader.
   *
   * @return the bootloader
   */
  public String getBootloader() {
    String result = initialVal;
    try {
      result = Build.BOOTLOADER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets board.
   *
   * @return the board
   */
  public String getBoard() {
    String result = initialVal;
    try {
      result = Build.BOARD;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets display version.
   *
   * @return the display version
   */
  public String getDisplayVersion() {
    String result = initialVal;
    try {
      result = Build.DISPLAY;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
   /**
   * Gets language.
   *
   * @return the language
   */
  public String getLanguage() {
    String result = initialVal;
    try {
      result = Locale.getDefault().getLanguage();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
        result = tm.getSimCountryIso().toLowerCase(Locale.getDefault());
      } else {
        Locale locale = Locale.getDefault();
        result = locale.getCountry().toLowerCase(locale);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 
  
  
  
  /**
   * Gets battery percentage
   *
   * @return the battery percentage
   */
  public int getBatteryPercentage() {
    int percentage = 0;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
      int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
      percentage = (int) ((level / (float) scale) * 100);
    }

    return percentage;
  }

  /**
   * Is device charging boolean.
   *
   * @return is battery charging boolean
   */
  public boolean isDeviceCharging() {
    Intent batteryStatus = getBatteryStatusIntent();
    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    return (status == BatteryManager.BATTERY_STATUS_CHARGING
        || status == BatteryManager.BATTERY_STATUS_FULL);
  }

  /**
   * Is device charging usb boolean.
   *
   * @return is battery charging via USB boolean
   */
  public boolean isDeviceChargingUSB() {
    Intent batteryStatus = getBatteryStatusIntent();
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    return (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
  }

  /**
   * Is device charging ac boolean.
   *
   * @return is battery charging via AC boolean
   */
  public boolean isDeviceChargingAC() {
    Intent batteryStatus = getBatteryStatusIntent();
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    return (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
  }  
  
  
   /**
   * Gets network type.
   *
   * @return the network type
   */
  public String getNetworkType() {
    int networkStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

    String result = initialVal;

    if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
      try {
        ConnectivityManager cm =
            (ConnectivityManager) BA.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
          result = "Unknown";
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
            || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
          result = "Wifi/WifiMax";
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
          TelephonyManager manager =
              (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
          if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            switch (manager.getNetworkType()) {

              // Unknown
              case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                result = "Cellular - Unknown";
                break;
              // Cellular Data–2G
              case TelephonyManager.NETWORK_TYPE_EDGE:
              case TelephonyManager.NETWORK_TYPE_GPRS:
              case TelephonyManager.NETWORK_TYPE_CDMA:
              case TelephonyManager.NETWORK_TYPE_IDEN:
              case TelephonyManager.NETWORK_TYPE_1xRTT:
                result = "Cellular - 2G";
                break;
              // Cellular Data–3G
              case TelephonyManager.NETWORK_TYPE_UMTS:
              case TelephonyManager.NETWORK_TYPE_HSDPA:
              case TelephonyManager.NETWORK_TYPE_HSPA:
              case TelephonyManager.NETWORK_TYPE_HSPAP:
              case TelephonyManager.NETWORK_TYPE_HSUPA:
              case TelephonyManager.NETWORK_TYPE_EVDO_0:
              case TelephonyManager.NETWORK_TYPE_EVDO_A:
              case TelephonyManager.NETWORK_TYPE_EVDO_B:
                result = "Cellular - 3G";
                break;
              // Cellular Data–4G
              case TelephonyManager.NETWORK_TYPE_LTE:
                result = "Cellular - 4G";
                break;
              // Cellular Data–Unknown Generation
              default:
                result = "Cellular - Unknown Generation";
                break;
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 
  
  
  
  /**
   * Gets os codename.
   *
   * @return the os codename
   */
  public String getOSCodename() {
    String codename = initialVal;
    switch (Build.VERSION.SDK_INT) {
      case Build.VERSION_CODES.BASE:
        codename = "First Android Version. Yay !";
        break;
      case Build.VERSION_CODES.BASE_1_1:
        codename = "Base Android 1.1";
        break;
      case Build.VERSION_CODES.CUPCAKE:
        codename = "Cupcake";
        break;
      case Build.VERSION_CODES.DONUT:
        codename = "Donut";
        break;
      case Build.VERSION_CODES.ECLAIR:
      case Build.VERSION_CODES.ECLAIR_0_1:
      case Build.VERSION_CODES.ECLAIR_MR1:

        codename = "Eclair";
        break;
      case Build.VERSION_CODES.FROYO:
        codename = "Froyo";
        break;
      case Build.VERSION_CODES.GINGERBREAD:
      case Build.VERSION_CODES.GINGERBREAD_MR1:
        codename = "Gingerbread";
        break;
      case Build.VERSION_CODES.HONEYCOMB:
      case Build.VERSION_CODES.HONEYCOMB_MR1:
      case Build.VERSION_CODES.HONEYCOMB_MR2:
        codename = "Honeycomb";
        break;
      case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
      case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
        codename = "Ice Cream Sandwich";
        break;
      case Build.VERSION_CODES.JELLY_BEAN:
      case Build.VERSION_CODES.JELLY_BEAN_MR1:
      case Build.VERSION_CODES.JELLY_BEAN_MR2:
        codename = "Jelly Bean";
        break;
      case Build.VERSION_CODES.KITKAT:
        codename = "Kitkat";
        break;
      case Build.VERSION_CODES.KITKAT_WATCH:
        codename = "Kitkat Watch";
        break;
      case Build.VERSION_CODES.LOLLIPOP:
      case Build.VERSION_CODES.LOLLIPOP_MR1:
        codename = "Lollipop";
        break;
      case Build.VERSION_CODES.M:
        codename = "Marshmallow";
        break;
    }
    return codename;
  }  
  
  
  /**
   * Gets os version.
   *
   * @return the os version
   */
  public String getOSVersion() {
    String result = initialVal;
    try {
      result = Build.VERSION.RELEASE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  


  /**
   * Gets wifi mac.
   *
   * @return the wifi mac
   */
  @SuppressWarnings("MissingPermission") public String getWifiMAC() {
    String result = initialVal;
    try {

      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
          == PackageManager.PERMISSION_GRANTED) {

        WifiManager wm = (WifiManager) BA.applicationContext.getSystemService(Context.WIFI_SERVICE);
        result = wm.getConnectionInfo().getMacAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
  /**
   * Gets imei.
   *
   * @return the imei
   */
  public String getIMEI() {
    tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    String result = initialVal;
    boolean hasReadPhoneStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED;
    try {
      if (hasReadPhoneStatePermission) result = tm.getDeviceId();
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets imsi.
   *
   * @return the imsi
   */
  public String getIMSI() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    boolean hasReadPhoneStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED;
    try {
      if (hasReadPhoneStatePermission) result = tm.getSubscriberId();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets serial.
   *
   * @return the serial
   */
  public String getSerial() {
    String result = initialVal;
    try {
      result = Build.SERIAL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets sim serial.
   *
   * @return the sim serial
   */
  public String getSIMSerial() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      result = tm.getSimSerialNumber();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets gsfid.
   *
   * @return the gsfid
   */
  public String getGSFID() {
    final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
    final String ID_KEY = "android_id";

    String[] params = { ID_KEY };
    Cursor c = BA.applicationContext.getContentResolver().query(URI, null, null, params, null);

    if (c == null) {
      return initialVal;
    } else if (!c.moveToFirst() || c.getColumnCount() < 2) {
      c.close();
      return initialVal;
    }

    try {
      String gsfID = Long.toHexString(Long.parseLong(c.getString(1)));
      c.close();
      return gsfID;
    } catch (NumberFormatException e) {
      c.close();
      return initialVal;
    }
  }

  /**
   * Gets bluetooth mac.
   *
   * @return the bluetooth mac
   */
  @SuppressWarnings("MissingPermission") public String getBluetoothMAC() {
    String result = initialVal;
    try {
      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
          == PackageManager.PERMISSION_GRANTED) {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        result = bta.getAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets psuedo unique id.
   *
   * @return the psuedo unique id
   */
  public String getPsuedoUniqueID() {
    // If all else fails, if the user does have lower than API 9 (lower
    // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
    // returns 'null', then simply the ID returned will be solely based
    // off their Android device information. This is where the collisions
    // can happen.
    // Try not to use DISPLAY, HOST or ID - these items could change.
    // If there are collisions, there will be overlapping data
    String devIDShort = "35" +
        (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
    } else {
      devIDShort += (Build.CPU_ABI.length() % 10);
    }

    devIDShort +=
        (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length()
            % 10) + (Build.PRODUCT.length() % 10);

    // Only devices with API >= 9 have android.os.Build.SERIAL
    // http://developer.android.com/reference/android/os/Build.html#SERIAL
    // If a user upgrades software or roots their phone, there will be a duplicate entry
    String serial;
    try {
      serial = Build.class.getField("SERIAL").get(null).toString();

      // Go ahead and return the serial for api => 9
      return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
    } catch (Exception e) {
      // String needs to be initialized
      serial = "ESYDV000"; // some value
    }

    // Finally, combine the values we have found by using the UUID class to create a unique identifier
    return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
  }  
  
  
  /**
   * Gets phone no.
   *
   * @return the phone no
   */
  public String getPhoneNo() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm.getLine1Number() != null) {
        result = tm.getLine1Number();
        if (result.equals("")) {
          result = initialVal;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets product.
   *
   * @return the product
   */
  public String getProduct() {
    String result = initialVal;
    try {
      result = Build.PRODUCT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets fingerprint.
   *
   * @return the fingerprint
   */
  public String getFingerprint() {
    String result = initialVal;
    try {
      result = Build.FINGERPRINT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets hardware.
   *
   * @return the hardware
   */
  public String getHardware() {
    String result = initialVal;
    try {
      result = Build.HARDWARE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets radio ver.
   *
   * @return the radio ver
   */
  public String getRadioVer() {
    String result = initialVal;
    try {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        result = Build.getRadioVersion();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets ip address.
   *
   * @param useIPv4 the use i pv 4
   * @return the ip address
   */
  public String getIPAddress(boolean useIPv4) {
    String result = initialVal;
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress().toUpperCase();
            boolean isIPv4 = addr instanceof Inet4Address;
            if (useIPv4) {
              if (isIPv4) result = sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                result = delim < 0 ? sAddr : sAddr.substring(0, delim);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets ua.
   *
   * @return the ua
   */
  public String getUA() {
    final String system_ua = System.getProperty("http.agent");
    String result = system_ua;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        result = new WebView(BA.applicationContext).getSettings().getDefaultUserAgent(BA.applicationContext) +
            "__" + system_ua;
      } else {
        result = new WebView(BA.applicationContext).getSettings().getUserAgentString() +
            "__" + system_ua;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() < 0 || result == null) {
      result = system_ua;
    }
    return result;
  }

  /**
   * Get lat long double [ ].
   *
   * @return the double [ ]
   */
  @SuppressWarnings("MissingPermission") @TargetApi(Build.VERSION_CODES.M)
  public double[] getLatLong() {
    boolean hasFineLocationPermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED ? true : false;
    boolean isGPSEnabled, isNetworkEnabled;

    double[] gps = new double[2];
    gps[0] = 0;
    gps[1] = 0;
    if (hasFineLocationPermission) {
      try {
        LocationManager lm = (LocationManager) BA.applicationContext.getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, final_loc = null;

        if (isGPSEnabled) {
          gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (isNetworkEnabled) {
          net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (gps_loc != null && net_loc != null) {
          if (gps_loc.getAccuracy() >= net_loc.getAccuracy()) {
            final_loc = gps_loc;
          } else {
            final_loc = net_loc;
          }
        } else {
          if (gps_loc != null) {
            final_loc = gps_loc;
          } else if (net_loc != null) {
            final_loc = net_loc;
          } else {
            // GPS and Network both are null so try passive
            final_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
          }
        }

        if (final_loc != null) {
          gps[0] = final_loc.getLatitude();
          gps[1] = final_loc.getLongitude();
        }

        return gps;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return gps;
  }  
  
  
  /**
   * Gets time.
   *
   * @return the time
   */
  public long getTime() {
    return System.currentTimeMillis();
  }

  /**
   * Gets formatted time.
   *
   * @return the formatted time
   */
  public String getFormatedTime() {

    long millis = System.currentTimeMillis();
    int sec = (int) (millis / 1000) % 60;
    int min = (int) ((millis / (1000 * 60)) % 60);
    int hr = (int) ((millis / (1000 * 60 * 60)) % 24);

    return String.format("%02d:%02d:%02d", hr, min, sec);
  }  
  
  
  /**
   * Gets app name.
   *
   * @return the app name
   */
  public String getAppName() {
    String result;
    final PackageManager pm = BA.applicationContext.getPackageManager();
    ApplicationInfo ai;
    try {
      ai = pm.getApplicationInfo(BA.applicationContext.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      ai = null;
      e.printStackTrace();
    }
    result = (String) (ai != null ? pm.getApplicationLabel(ai) : initialVal);
    return result;
  }

  /**
   * Gets app version.
   *
   * @return the app version
   */
  public String getAppVersion() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getPackageManager().getPackageInfo(BA.applicationContext.getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets app version code.
   *
   * @return the app version code
   */
  public String getAppVersionCode() {
    String result = initialVal;
    try {
      result = String.valueOf(
          BA.applicationContext.getPackageManager().getPackageInfo(BA.applicationContext.getPackageName(), 0).versionCode);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets activity name.
   *
   * @return the activity name
   */
  public String getActivityName() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getClass().getSimpleName();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
   /**
   * Gets package name.
   *
   * @return the package name
   */
  public String getPackageName() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getPackageName();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets store.
   *
   * @return the store
   */
  public String getStore() {
    String result = initialVal;
    if (Build.VERSION.SDK_INT >= 3) {
      try {
        result = BA.applicationContext.getPackageManager().getInstallerPackageName(BA.applicationContext.getPackageName());
      } catch (Exception e) {
        //Log.i(LOGTAG, "Can't get Installer package");
      }
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  } 
  
  /**
   * Gets density.
   *
   * @return the density
   */
  public String getDensity() {
    String densityStr = initialVal;
    final int density = BA.applicationContext.getResources().getDisplayMetrics().densityDpi;
    switch (density) {
      case DisplayMetrics.DENSITY_LOW:
        densityStr = "LDPI";
        break;
      case DisplayMetrics.DENSITY_MEDIUM:
        densityStr = "MDPI";
        break;
      case DisplayMetrics.DENSITY_TV:
        densityStr = "TVDPI";
        break;
      case DisplayMetrics.DENSITY_HIGH:
        densityStr = "HDPI";
        break;
      case DisplayMetrics.DENSITY_XHIGH:
        densityStr = "XHDPI";
        break;
      case DisplayMetrics.DENSITY_400:
        densityStr = "XMHDPI";
        break;
      case DisplayMetrics.DENSITY_XXHIGH:
        densityStr = "XXHDPI";
        break;
      case DisplayMetrics.DENSITY_XXXHIGH:
        densityStr = "XXXHDPI";
        break;
    }
    return densityStr;
  }  
  
  /**
   * Get accounts string [ ].
   *
   * @return the string [ ]
   */
  @SuppressWarnings("MissingPermission") public String[] getAccounts() {
    try {

      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.GET_ACCOUNTS)
          == PackageManager.PERMISSION_GRANTED) {
        Account[] accounts = AccountManager.get(BA.applicationContext).getAccountsByType("com.google");
        String[] result = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
          result[i] = accounts[i].name;
        }
        return result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }  
  

  /**
   * Is network available boolean.
   *
   * @return the boolean
   */
  public boolean isNetworkAvailable() {
    if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        == PackageManager.PERMISSION_GRANTED
        && BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        == PackageManager.PERMISSION_GRANTED) {
      ConnectivityManager cm = (ConnectivityManager) BA.applicationContext.getApplicationContext()
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getActiveNetworkInfo();
      return netInfo != null && netInfo.isConnected();
    }
    return false;
  }

  /**
   * Is running on emulator boolean.
   *
   * @return the boolean
   */
  public static boolean isRunningOnEmulator() {
    return Build.BRAND.contains("generic")
        || Build.DEVICE.contains("generic")
        || Build.PRODUCT.contains("sdk")
        || Build.HARDWARE.contains("goldfish")
        || Build.MANUFACTURER.contains("Genymotion")
        || Build.PRODUCT.contains("vbox86p")
        || Build.DEVICE.contains("vbox86p")
        || Build.HARDWARE.contains("vbox86");
  }  
  


  /**
   * Is wifi enabled
   *
   * @return the boolean
   */
  public boolean isWifiEnabled() {
    boolean wifiState = false;

    WifiManager wifiManager = (WifiManager) BA.applicationContext.getSystemService(Context.WIFI_SERVICE);
    if (wifiManager != null) {
      wifiState = wifiManager.isWifiEnabled() ? true : false;
    }
    return wifiState;
  }

  /**
   * Gets Device Ringer Mode
   *
   * @return Device Ringer Mode
   */
  public int getDeviceRingerMode() {
    int ringerMode = RINGER_MODE_NORMAL;
    AudioManager audioManager = (AudioManager) BA.applicationContext.getSystemService(Context.AUDIO_SERVICE);
    switch (audioManager.getRingerMode()) {
      case AudioManager.RINGER_MODE_NORMAL:
        ringerMode = RINGER_MODE_NORMAL;
        break;
      case AudioManager.RINGER_MODE_SILENT:
        ringerMode = RINGER_MODE_SILENT;
        break;
      case AudioManager.RINGER_MODE_VIBRATE:
        ringerMode = RINGER_MODE_VIBRATE;
    }
    return ringerMode;
  }


  private Intent getBatteryStatusIntent() {
    IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    return BA.applicationContext.registerReceiver(null, batFilter);
  }


  private String handleIllegalCharacterInResult(String result) {
    if (result.indexOf(" ") > 0) {
      result = result.replaceAll(" ", "_");
    }
    return result;
  }

  /**
   * Gets app cache
   *
   * @return Installed app cache
   */
 





}
