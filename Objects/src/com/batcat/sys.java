package com.batcat;

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

public class sys extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static sys mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.sys");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (sys).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.sys");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.sys", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (sys) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (sys) Resume **");
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
		return sys.class;
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
        BA.LogInfo("** Activity (sys) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (sys) Resume **");
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
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.phone.PhoneEvents _dev = null;
public anywheresoftware.b4a.phone.PackageManagerWrapper _pak = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.rootsoft.oslibrary.OSLibrary _oper = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public static int _c1 = 0;
public static int _c2 = 0;
public static int _c3 = 0;
public static int _c4 = 0;
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l3 = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
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
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="Activity.LoadLayout(\"4\")";
mostCurrent._activity.LoadLayout("4",mostCurrent.activityBA);
 //BA.debugLineNum = 29;BA.debugLine="Activity.Title=pak.GetApplicationLabel(\"com.batca";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(mostCurrent._pak.GetApplicationLabel("com.batcat")+" - "+mostCurrent._pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 30;BA.debugLine="Activity.Color=Colors.ARGB(150,30,124,235)";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (30),(int) (124),(int) (235)));
 //BA.debugLineNum = 31;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 32;BA.debugLine="ToolbarHelper.hide";
mostCurrent._toolbarhelper.Hide();
 //BA.debugLineNum = 35;BA.debugLine="dev.Initialize(\"dev\")";
mostCurrent._dev.Initialize(processBA,"dev");
 //BA.debugLineNum = 36;BA.debugLine="oper.Initialize(\"oper\")";
mostCurrent._oper.Initialize(processBA,"oper");
 //BA.debugLineNum = 37;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 38;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 39;BA.debugLine="l3.Initialize(\"l3\")";
mostCurrent._l3.Initialize(mostCurrent.activityBA,"l3");
 //BA.debugLineNum = 40;BA.debugLine="l1=ListView1.TwoLinesLayout.Label";
mostCurrent._l1 = mostCurrent._listview1.getTwoLinesLayout().Label;
 //BA.debugLineNum = 41;BA.debugLine="l1.TextSize=20";
mostCurrent._l1.setTextSize((float) (20));
 //BA.debugLineNum = 42;BA.debugLine="l1.TextColor=Colors.ARGB(255,0,0,0)";
mostCurrent._l1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 43;BA.debugLine="ListView1.TwoLinesLayout.ItemHeight=100";
mostCurrent._listview1.getTwoLinesLayout().setItemHeight((int) (100));
 //BA.debugLineNum = 44;BA.debugLine="l2=ListView1.TwoLinesLayout.SecondLabel";
mostCurrent._l2 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 45;BA.debugLine="l2.TextSize=16";
mostCurrent._l2.setTextSize((float) (16));
 //BA.debugLineNum = 46;BA.debugLine="l2.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._l2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 48;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal,\"datastor";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 54;BA.debugLine="sys_info";
_sys_info();
 //BA.debugLineNum = 55;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 66;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 67;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 68;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 69;BA.debugLine="ToastMessageShow(\"BCT - Backround  Statistic sta";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BCT - Backround  Statistic started.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 70;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 72;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 59;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim dev As PhoneEvents";
mostCurrent._dev = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 14;BA.debugLine="Dim pak As PackageManager";
mostCurrent._pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim kvs4 As KeyValueStore";
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 16;BA.debugLine="Dim oper As OperatingSystem";
mostCurrent._oper = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 17;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 18;BA.debugLine="Private c1,c2,c3,c4 As Int";
_c1 = 0;
_c2 = 0;
_c3 = 0;
_c4 = 0;
 //BA.debugLineNum = 19;BA.debugLine="Dim nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 20;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 23;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
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
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 233;BA.debugLine="Sub listview1_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 111;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 112;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 113;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 114;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 115;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 116;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 117;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 118;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 119;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 120;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 121;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 122;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 123;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 124;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 125;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 127;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 128;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 130;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 132;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 133;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 135;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 }else {
 };
 //BA.debugLineNum = 139;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 140;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 141;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 }else {
 };
 //BA.debugLineNum = 146;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 147;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 149;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 };
 //BA.debugLineNum = 153;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 154;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 156;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 }else {
 };
 //BA.debugLineNum = 160;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 161;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 162;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 }else {
 };
 //BA.debugLineNum = 167;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 168;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 170;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 }else {
 };
 //BA.debugLineNum = 174;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 175;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 176;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 }else {
 };
 //BA.debugLineNum = 181;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 182;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 184;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 }else {
 };
 //BA.debugLineNum = 188;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 189;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 190;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 }else {
 };
 //BA.debugLineNum = 195;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 196;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 197;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 }else {
 };
 //BA.debugLineNum = 202;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 203;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 205;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 }else {
 };
 //BA.debugLineNum = 209;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 210;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 212;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 }else {
 };
 //BA.debugLineNum = 216;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 217;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 219;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 }else {
 };
 //BA.debugLineNum = 223;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 224;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 226;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 }else {
 };
 //BA.debugLineNum = 230;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _sys_info() throws Exception{
 //BA.debugLineNum = 75;BA.debugLine="Sub sys_info";
 //BA.debugLineNum = 76;BA.debugLine="nativeMe.InitializeContext";
mostCurrent._nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 78;BA.debugLine="Log(\"Battery Percentage = \" & nativeMe.RunMethod(";
anywheresoftware.b4a.keywords.Common.Log("Battery Percentage = "+BA.ObjectToString(mostCurrent._nativeme.RunMethod("getBatteryPercentage",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 79;BA.debugLine="Log(\"Is Device Charging = \" & nativeMe.RunMethod(";
anywheresoftware.b4a.keywords.Common.Log("Is Device Charging = "+BA.ObjectToString(mostCurrent._nativeme.RunMethod("isDeviceCharging",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 80;BA.debugLine="Log(\"Is Device Charging USB = \" & nativeMe.RunMet";
anywheresoftware.b4a.keywords.Common.Log("Is Device Charging USB = "+BA.ObjectToString(mostCurrent._nativeme.RunMethod("isDeviceChargingUSB",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 81;BA.debugLine="Log(\"Is Device Charging AC = \" & nativeMe.RunMeth";
anywheresoftware.b4a.keywords.Common.Log("Is Device Charging AC = "+BA.ObjectToString(mostCurrent._nativeme.RunMethod("isDeviceChargingAC",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 83;BA.debugLine="ListView1.AddTwoLines(\"OS Code Name:\" ,nativeMe.R";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("OS Code Name:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getOSCodename",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 84;BA.debugLine="ListView1.AddTwoLines(\"OS Version:\" ,nativeMe.Run";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("OS Version:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getOSVersion",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 85;BA.debugLine="ListView1.AddTwoLines(\"Model:\",nativeMe.RunMethod";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Model:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getModel",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 86;BA.debugLine="ListView1.AddTwoLines(\"Build Brand:\" ,nativeMe.Ru";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Build Brand:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getBuildBrand",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 87;BA.debugLine="ListView1.AddTwoLines(\"Device Rooted:\" ,nativeMe.";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Device Rooted:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("isDeviceRooted",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 88;BA.debugLine="ListView1.AddTwoLines(\"Manufacturer:\" ,nativeMe.R";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Manufacturer:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getManufacturer",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 89;BA.debugLine="ListView1.AddTwoLines(\"Resolution:\" ,nativeMe.Run";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Resolution:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getResolution",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 90;BA.debugLine="ListView1.AddTwoLines(\"Carrier:\" ,nativeMe.RunMet";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Carrier:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getCarrier",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 91;BA.debugLine="ListView1.AddTwoLines(\"Device:\" ,nativeMe.RunMeth";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Device:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getDevice",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 92;BA.debugLine="ListView1.AddTwoLines(\"Network Type:\",nativeMe.Ru";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Network Type:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getNetworkType",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 93;BA.debugLine="ListView1.AddTwoLines(\"Display Version.\" , native";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Display Version."),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getDisplayVersion",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 94;BA.debugLine="ListView1.AddTwoLines(\"Language:\" ,nativeMe.RunMe";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Language:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getLanguage",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 95;BA.debugLine="ListView1.AddTwoLines(\"Country:\" ,nativeMe.RunMet";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Country:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getCountry",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 102;BA.debugLine="ListView1.AddTwoLines(\"Product:\" ,nativeMe.RunMet";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Product:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getProduct",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 103;BA.debugLine="ListView1.AddTwoLines(\"Hardware;\" ,nativeMe.RunMe";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("Hardware;"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getHardware",(Object[])(anywheresoftware.b4a.keywords.Common.Null))));
 //BA.debugLineNum = 104;BA.debugLine="ListView1.AddTwoLines(\"IP Address:\" ,nativeMe.Run";
mostCurrent._listview1.AddTwoLines(BA.ObjectToCharSequence("IP Address:"),BA.ObjectToCharSequence(mostCurrent._nativeme.RunMethod("getIPAddress",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.True)})));
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
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


}
