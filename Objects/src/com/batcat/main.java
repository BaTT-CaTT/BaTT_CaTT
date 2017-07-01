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

public class main extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.phone.PackageManagerWrapper _pak = null;
public static anywheresoftware.b4a.objects.CSBuilder _cs = null;
public static anywheresoftware.b4a.sql.SQL _sql = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public com.rootsoft.oslibrary.OSLibrary _ra = null;
public anywheresoftware.b4a.phone.PhoneEvents _device = null;
public com.rootsoft.customtoast.CustomToast _ct = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lk = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lv2 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvmenu = null;
public Object[] _args = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj2 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _la = null;
public anywheresoftware.b4a.objects.LabelWrapper _la1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _la2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ima = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _icon = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _logo = null;
public com.batcat.batut _bat = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox1 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox2 = null;
public static String _temp = "";
public static String _level1 = "";
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _lip = null;
public anywheresoftware.b4a.objects.PanelWrapper _smlp = null;
public com.batcat.keyvaluestore _kvs = null;
public com.batcat.keyvaluestore _kvs2 = null;
public com.batcat.keyvaluestore _kvs3 = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.keyvaluestore _kvs4sub = null;
public anywheresoftware.b4a.objects.collections.Map _optmap = null;
public anywheresoftware.b4a.objects.LabelWrapper _label5 = null;
public anywheresoftware.b4a.objects.collections.List _proc = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public com.maximussoft.showtips.BuilderWrapper _showtip = null;
public com.batcat.clsslidingsidebar _slm = null;
public static int _cas = 0;
public static int _size = 0;
public static int _flags = 0;
public static String[] _types = null;
public static String _name = "";
public static String _packname = "";
public static String _date = "";
public static String _time = "";
public static String _l = "";
public static String _ramsize = "";
public anywheresoftware.b4a.objects.collections.List _list = null;
public anywheresoftware.b4a.objects.collections.List _list1 = null;
public anywheresoftware.b4a.objects.collections.List _list2 = null;
public anywheresoftware.b4a.objects.collections.List _list3 = null;
public anywheresoftware.b4a.objects.collections.List _list4 = null;
public anywheresoftware.b4a.objects.collections.List _list5 = null;
public anywheresoftware.b4a.objects.collections.List _list6 = null;
public anywheresoftware.b4a.objects.collections.List _list7 = null;
public anywheresoftware.b4a.objects.collections.List _list8 = null;
public anywheresoftware.b4a.objects.collections.List _list9 = null;
public anywheresoftware.b4a.objects.collections.List _phlis = null;
public anywheresoftware.b4a.objects.collections.List _lis = null;
public anywheresoftware.b4a.objects.collections.List _setlist = null;
public anywheresoftware.b4a.objects.collections.List _datalist = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _cd2 = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _cd = null;
public anywheresoftware.b4a.objects.PanelWrapper _fakeactionbar = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _sd = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _sdi = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
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
public com.tchart.materialcolors.MaterialColors _mcl = null;
public anywheresoftware.b4a.objects.collections.List _mclist = null;
public anywheresoftware.b4a.objects.collections.List _ramlist = null;
public flm.b4a.cache.Cache _cat = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _fbc = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _xml = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public circleprogressmasterwrapper.arcProgressMasterWrapper _apm = null;
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ims1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ims2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ims3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ims4 = null;
public progressroundbuttonwrapper.progressRoundButtonWrapper _prb1 = null;
public static String _volt = "";
public static String _root1 = "";
public anywheresoftware.b4a.objects.collections.List _ffiles = null;
public anywheresoftware.b4a.objects.collections.List _ffolders = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;
public com.batcat.webhost _webhost = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (klo.mostCurrent != null);
vis = vis | (sys.mostCurrent != null);
vis = vis | (cool.mostCurrent != null);
vis = vis | (settings.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _barsize = 0;
anywheresoftware.b4a.objects.LabelWrapper _lvm1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lvm2 = null;
 //BA.debugLineNum = 94;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 95;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 97;BA.debugLine="Activity.Title=pak.GetApplicationLabel(\"com.batca";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(_pak.GetApplicationLabel("com.batcat")+" - "+_pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 99;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 101;BA.debugLine="ToolbarHelper.Hide";
mostCurrent._toolbarhelper.Hide();
 //BA.debugLineNum = 103;BA.debugLine="device.Initialize(\"device\")";
mostCurrent._device.Initialize(processBA,"device");
 //BA.debugLineNum = 104;BA.debugLine="t1.Initialize(\"t1\",1000)";
_t1.Initialize(processBA,"t1",(long) (1000));
 //BA.debugLineNum = 105;BA.debugLine="ra.Initialize(\"ra\")";
mostCurrent._ra.Initialize(processBA,"ra");
 //BA.debugLineNum = 106;BA.debugLine="bat.Initialize";
mostCurrent._bat._initialize(processBA);
 //BA.debugLineNum = 107;BA.debugLine="ct.Initialize";
mostCurrent._ct.Initialize(processBA);
 //BA.debugLineNum = 108;BA.debugLine="List.Initialize";
mostCurrent._list.Initialize();
 //BA.debugLineNum = 109;BA.debugLine="list1.Initialize";
mostCurrent._list1.Initialize();
 //BA.debugLineNum = 110;BA.debugLine="list2.Initialize";
mostCurrent._list2.Initialize();
 //BA.debugLineNum = 111;BA.debugLine="list3.Initialize";
mostCurrent._list3.Initialize();
 //BA.debugLineNum = 112;BA.debugLine="list4.Initialize";
mostCurrent._list4.Initialize();
 //BA.debugLineNum = 113;BA.debugLine="list5.Initialize";
mostCurrent._list5.Initialize();
 //BA.debugLineNum = 114;BA.debugLine="list6.Initialize";
mostCurrent._list6.Initialize();
 //BA.debugLineNum = 115;BA.debugLine="list7.Initialize";
mostCurrent._list7.Initialize();
 //BA.debugLineNum = 116;BA.debugLine="list8.Initialize";
mostCurrent._list8.Initialize();
 //BA.debugLineNum = 117;BA.debugLine="list9.Initialize";
mostCurrent._list9.Initialize();
 //BA.debugLineNum = 118;BA.debugLine="phlis.Initialize";
mostCurrent._phlis.Initialize();
 //BA.debugLineNum = 119;BA.debugLine="datalist.Initialize";
mostCurrent._datalist.Initialize();
 //BA.debugLineNum = 120;BA.debugLine="setlist.Initialize";
mostCurrent._setlist.Initialize();
 //BA.debugLineNum = 121;BA.debugLine="lv2.Initialize(\"lv2\")";
mostCurrent._lv2.Initialize(mostCurrent.activityBA,"lv2");
 //BA.debugLineNum = 122;BA.debugLine="panel3.Initialize(\"panel3\")";
mostCurrent._panel3.Initialize(mostCurrent.activityBA,"panel3");
 //BA.debugLineNum = 123;BA.debugLine="smlp.Initialize(\"smlp\")";
mostCurrent._smlp.Initialize(mostCurrent.activityBA,"smlp");
 //BA.debugLineNum = 124;BA.debugLine="lip.Initialize(\"lip\")";
mostCurrent._lip.Initialize(mostCurrent.activityBA,"lip");
 //BA.debugLineNum = 126;BA.debugLine="Lis.Initialize";
mostCurrent._lis.Initialize();
 //BA.debugLineNum = 127;BA.debugLine="proc.Initialize";
mostCurrent._proc.Initialize();
 //BA.debugLineNum = 128;BA.debugLine="lk.Initialize(\"lk\")";
mostCurrent._lk.Initialize(mostCurrent.activityBA,"lk");
 //BA.debugLineNum = 129;BA.debugLine="optmap.Initialize";
mostCurrent._optmap.Initialize();
 //BA.debugLineNum = 130;BA.debugLine="la.Initialize(\"la\")";
mostCurrent._la.Initialize(mostCurrent.activityBA,"la");
 //BA.debugLineNum = 131;BA.debugLine="la1.Initialize(\"la1\")";
mostCurrent._la1.Initialize(mostCurrent.activityBA,"la1");
 //BA.debugLineNum = 132;BA.debugLine="la2.Initialize(\"la2\")";
mostCurrent._la2.Initialize(mostCurrent.activityBA,"la2");
 //BA.debugLineNum = 133;BA.debugLine="ramlist.Initialize";
mostCurrent._ramlist.Initialize();
 //BA.debugLineNum = 134;BA.debugLine="ffiles.Initialize";
mostCurrent._ffiles.Initialize();
 //BA.debugLineNum = 135;BA.debugLine="ffolders.Initialize";
mostCurrent._ffolders.Initialize();
 //BA.debugLineNum = 137;BA.debugLine="panel3.AddView(lk,10,5,80%x,50%y)";
mostCurrent._panel3.AddView((android.view.View)(mostCurrent._lk.getObject()),(int) (10),(int) (5),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 138;BA.debugLine="lv2.FastScrollEnabled=True";
mostCurrent._lv2.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 139;BA.debugLine="logo.InitializeSample(File.DirAssets, \"sulo_log99";
mostCurrent._logo.InitializeSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"sulo_log99.png",(int) (48),(int) (48));
 //BA.debugLineNum = 140;BA.debugLine="panel3.Color=Colors.Transparent";
mostCurrent._panel3.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 141;BA.debugLine="lip.Color=Colors.ARGB(255,77,79,79)";
mostCurrent._lip.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (77),(int) (79),(int) (79)));
 //BA.debugLineNum = 142;BA.debugLine="lip.Elevation=50dip";
mostCurrent._lip.setElevation((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 143;BA.debugLine="lip.AddView(lv2,1dip,1dip,-1,-1)";
mostCurrent._lip.AddView((android.view.View)(mostCurrent._lv2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),(int) (-1),(int) (-1));
 //BA.debugLineNum = 144;BA.debugLine="cd2.AddView(lip,-1,-1)";
mostCurrent._cd2.AddView((android.view.View)(mostCurrent._lip.getObject()),(int) (-1),(int) (-1));
 //BA.debugLineNum = 145;BA.debugLine="smlp.Enabled=True";
mostCurrent._smlp.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="showtip.Initialize(\"showtip\")";
mostCurrent._showtip.Initialize(mostCurrent.activityBA,"showtip");
 //BA.debugLineNum = 147;BA.debugLine="ima.Initialize(\"ima\")";
mostCurrent._ima.Initialize(mostCurrent.activityBA,"ima");
 //BA.debugLineNum = 148;BA.debugLine="ima.Bitmap=LoadBitmap(File.DirAssets,\"icon_batcat";
mostCurrent._ima.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon_batcat.png").getObject()));
 //BA.debugLineNum = 149;BA.debugLine="panel3.AddView(ima,50,150,450,250)";
mostCurrent._panel3.AddView((android.view.View)(mostCurrent._ima.getObject()),(int) (50),(int) (150),(int) (450),(int) (250));
 //BA.debugLineNum = 152;BA.debugLine="date=DateTime.Date(DateTime.Now)";
mostCurrent._date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 153;BA.debugLine="time=DateTime.Time(DateTime.Now)";
mostCurrent._time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 155;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 156;BA.debugLine="temp=bat.BatteryInformation(6)/10";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 157;BA.debugLine="level1=bat.BatteryInformation(0)";
mostCurrent._level1 = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (0)]);
 //BA.debugLineNum = 159;BA.debugLine="Label3.Text=\"Ver. \"&pak.GetVersionName(\"com.batc";
mostCurrent._label3.setText(BA.ObjectToCharSequence("Ver. "+_pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 161;BA.debugLine="Dim BarSize As Int: BarSize = 60dip";
_barsize = 0;
 //BA.debugLineNum = 161;BA.debugLine="Dim BarSize As Int: BarSize = 60dip";
_barsize = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60));
 //BA.debugLineNum = 162;BA.debugLine="FakeActionBar.Initialize(\"\")";
mostCurrent._fakeactionbar.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 163;BA.debugLine="FakeActionBar.Color =  Colors.Transparent";
mostCurrent._fakeactionbar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 164;BA.debugLine="Activity.AddView(FakeActionBar, 0, 0, 100%x, BarS";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._fakeactionbar.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_barsize);
 //BA.debugLineNum = 165;BA.debugLine="Activity.AddView(smlp,0,5dip,100%x,65%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._smlp.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (65),mostCurrent.activityBA));
 //BA.debugLineNum = 167;BA.debugLine="slm.Initialize(smlp, 250dip, 1, 1, 100, 200)";
mostCurrent._slm._initialize(mostCurrent.activityBA,mostCurrent._smlp,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (250)),(byte) (1),(byte) (1),(int) (100),(int) (200));
 //BA.debugLineNum = 168;BA.debugLine="slm.ContentPanel.Color = Colors.Transparent";
mostCurrent._slm._contentpanel().setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 169;BA.debugLine="slm.Sidebar.Background = slm.LoadDrawable(\"popup_";
mostCurrent._slm._sidebar().setBackground((android.graphics.drawable.Drawable)(mostCurrent._slm._loaddrawable("popup_top_dark")));
 //BA.debugLineNum = 170;BA.debugLine="slm.SetOnChangeListeners(Me, \"Menu_onFullyOpen\",";
mostCurrent._slm._setonchangelisteners(main.getObject(),"Menu_onFullyOpen","Menu_onFullyClosed","Menu_onMove");
 //BA.debugLineNum = 171;BA.debugLine="lvMenu.Initialize(\"lvMenu\")";
mostCurrent._lvmenu.Initialize(mostCurrent.activityBA,"lvMenu");
 //BA.debugLineNum = 172;BA.debugLine="Dim lvm1,lvm2 As Label";
_lvm1 = new anywheresoftware.b4a.objects.LabelWrapper();
_lvm2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 173;BA.debugLine="lvm1=lvMenu.TwoLinesAndBitmap.Label";
_lvm1 = mostCurrent._lvmenu.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 174;BA.debugLine="lvm2=lvMenu.TwoLinesAndBitmap.SecondLabel";
_lvm2 = mostCurrent._lvmenu.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 175;BA.debugLine="lvm1.TextColor = Colors.White";
_lvm1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 176;BA.debugLine="lvm1.textsize=14";
_lvm1.setTextSize((float) (14));
 //BA.debugLineNum = 177;BA.debugLine="lvm2.textsize=12";
_lvm2.setTextSize((float) (12));
 //BA.debugLineNum = 178;BA.debugLine="lvm2.textcolor=Colors.LightGray";
_lvm2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 179;BA.debugLine="lvMenu.TwoLinesAndBitmap.ImageView.Height=42dip";
mostCurrent._lvmenu.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (42)));
 //BA.debugLineNum = 180;BA.debugLine="lvMenu.TwoLinesAndBitmap.ImageView.Width=42dip";
mostCurrent._lvmenu.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (42)));
 //BA.debugLineNum = 181;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"System Info\",\"Detai";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("System Info"),BA.ObjectToCharSequence("Detail Phone Info"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Tag.png").getObject()),(Object)(4));
 //BA.debugLineNum = 182;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Statistic\",\"Battery";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Statistic"),BA.ObjectToCharSequence("Battery Cyclus"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chart.png").getObject()),(Object)(3));
 //BA.debugLineNum = 183;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Power\",\"OS Power Me";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Power"),BA.ObjectToCharSequence("OS Power Menu"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()),(Object)(2));
 //BA.debugLineNum = 184;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Settings\",\"Options";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Settings"),BA.ObjectToCharSequence("Options Menu"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Control.png").getObject()),(Object)(0));
 //BA.debugLineNum = 185;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Info\",\"Version Info";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Info"),BA.ObjectToCharSequence("Version Info, über BC.."),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Rss.png").getObject()),(Object)(5));
 //BA.debugLineNum = 186;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Exit\",\"Close the Ap";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Exit"),BA.ObjectToCharSequence("Close the Application/Service"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Power.png").getObject()),(Object)(1));
 //BA.debugLineNum = 187;BA.debugLine="lvMenu.TwoLinesAndBitmap.ItemHeight=95";
mostCurrent._lvmenu.getTwoLinesAndBitmap().setItemHeight((int) (95));
 //BA.debugLineNum = 188;BA.debugLine="lvMenu.Color = Colors.Transparent";
mostCurrent._lvmenu.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 189;BA.debugLine="lvMenu.ScrollingBackgroundColor = Colors.Transpar";
mostCurrent._lvmenu.setScrollingBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 190;BA.debugLine="slm.Sidebar.AddView(lvMenu, 10dip,9dip, -1, -1)";
mostCurrent._slm._sidebar().AddView((android.view.View)(mostCurrent._lvmenu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (9)),(int) (-1),(int) (-1));
 //BA.debugLineNum = 192;BA.debugLine="slm.EnableSwipeGesture(True,400,1)";
mostCurrent._slm._enableswipegesture(anywheresoftware.b4a.keywords.Common.True,(int) (400),(byte) (1));
 //BA.debugLineNum = 195;BA.debugLine="smlp.BringToFront";
mostCurrent._smlp.BringToFront();
 //BA.debugLineNum = 196;BA.debugLine="slm.Sidebar.BringToFront";
mostCurrent._slm._sidebar().BringToFront();
 //BA.debugLineNum = 197;BA.debugLine="lvMenu.BringToFront";
mostCurrent._lvmenu.BringToFront();
 //BA.debugLineNum = 198;BA.debugLine="lv2.Enabled=True";
mostCurrent._lv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 199;BA.debugLine="Button4.SendToBack";
mostCurrent._button4.SendToBack();
 //BA.debugLineNum = 201;BA.debugLine="kvs.Initialize(File.DirDefaultExternal, \"datastor";
mostCurrent._kvs._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore");
 //BA.debugLineNum = 202;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 203;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 204;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 205;BA.debugLine="kvs4sub.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvs4sub._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_sub_4");
 //BA.debugLineNum = 208;BA.debugLine="logo.Initialize(File.DirAssets,\"icon_batcat.png\")";
mostCurrent._logo.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon_batcat.png");
 //BA.debugLineNum = 210;BA.debugLine="Label2.Text= temp &\"°C\"";
mostCurrent._label2.setText(BA.ObjectToCharSequence(mostCurrent._temp+"°C"));
 //BA.debugLineNum = 211;BA.debugLine="Label4.Text= Round2(volt,1) &\" V\"";
mostCurrent._label4.setText(BA.ObjectToCharSequence(BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round2((double)(Double.parseDouble(mostCurrent._volt)),(int) (1)))+" V"));
 //BA.debugLineNum = 213;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 214;BA.debugLine="show_tip";
_show_tip();
 //BA.debugLineNum = 216;BA.debugLine="slm.OpenSidebar";
mostCurrent._slm._opensidebar();
 //BA.debugLineNum = 217;BA.debugLine="Button4.SetVisibleAnimated(300,False)";
mostCurrent._button4.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cac";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","1.db")) { 
 }else {
 //BA.debugLineNum = 224;BA.debugLine="sql.Initialize(File.DirRootExternal, \"1.db\", Tr";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"1.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 225;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cach";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 226;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/data";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/data");
 //BA.debugLineNum = 227;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 228;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 //BA.debugLineNum = 229;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","APnames.txt",mostCurrent._list3);
 //BA.debugLineNum = 230;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl.txt",mostCurrent._level1);
 //BA.debugLineNum = 231;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","volt.txt","5");
 //BA.debugLineNum = 232;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data","vid.txt",BA.NumberToString(_pak.GetVersionCode("com.batcat")));
 //BA.debugLineNum = 233;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","pn1.txt",mostCurrent._lis);
 //BA.debugLineNum = 234;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","settings.txt",mostCurrent._setlist);
 //BA.debugLineNum = 235;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","proc.txt",mostCurrent._proc);
 //BA.debugLineNum = 236;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl2.txt","0");
 //BA.debugLineNum = 237;BA.debugLine="File.WriteMap(File.DirDefaultExternal&\"/mnt/cac";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","smap1.txt",mostCurrent._optmap);
 //BA.debugLineNum = 238;BA.debugLine="ToastMessageShow(\"BC Log loaded! \"&date&\", \"&ti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BC Log loaded! "+mostCurrent._date+", "+mostCurrent._time),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 241;BA.debugLine="ToastMessageShow(\"Willkommen\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Willkommen"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 244;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 245;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 246;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 247;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 248;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 249;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 250;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 251;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 252;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 253;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 254;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 255;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 256;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 257;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 258;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 259;BA.debugLine="c16=mcl.md_light_blue_A700";
_c16 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 260;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 //BA.debugLineNum = 262;BA.debugLine="ra.setLowMemory";
mostCurrent._ra.setLowMemory();
 //BA.debugLineNum = 263;BA.debugLine="apm.FinishedStrokeColor=mcl.md_light_green_A700";
mostCurrent._apm.setFinishedStrokeColor(mostCurrent._mcl.getmd_light_green_A700());
 //BA.debugLineNum = 264;BA.debugLine="apm.Color=Colors.Transparent";
mostCurrent._apm.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 265;BA.debugLine="apm.UnfinishedStrokeColor=Colors.ARGB(190,255,255";
mostCurrent._apm.setUnfinishedStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (190),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 266;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 267;BA.debugLine="apm.TextSize=55";
mostCurrent._apm.setTextSize((float) (55));
 //BA.debugLineNum = 268;BA.debugLine="apm.StrokeWidth=12dip";
mostCurrent._apm.setStrokeWidth((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12))));
 //BA.debugLineNum = 269;BA.debugLine="apm.ArcAngle=270";
mostCurrent._apm.setArcAngle((float) (270));
 //BA.debugLineNum = 270;BA.debugLine="ims3.Bitmap=LoadBitmap(File.DirAssets,\"ic_brightn";
mostCurrent._ims3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_brightness_auto_black_24dp.png").getObject()));
 //BA.debugLineNum = 271;BA.debugLine="ims4.Bitmap=LoadBitmap(File.DirAssets,\"ic_sim_car";
mostCurrent._ims4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_sim_card_white_24dp.png").getObject()));
 //BA.debugLineNum = 274;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 275;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 276;BA.debugLine="label_text";
_label_text();
 //BA.debugLineNum = 277;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 279;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 755;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 756;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 757;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 758;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 759;BA.debugLine="ToastMessageShow(\"BCT - Backround Service\",False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BCT - Backround Service"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 761;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 762;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 286;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 287;BA.debugLine="cat.CloseAndClearDiskCache";
mostCurrent._cat.CloseAndClearDiskCache();
 //BA.debugLineNum = 288;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 281;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 282;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 283;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
return "";
}
public static String  _app_info() throws Exception{
int _i = 0;
int _form = 0;
 //BA.debugLineNum = 863;BA.debugLine="Sub app_info";
 //BA.debugLineNum = 864;BA.debugLine="lv2.Clear";
mostCurrent._lv2.Clear();
 //BA.debugLineNum = 865;BA.debugLine="list3.Clear";
mostCurrent._list3.Clear();
 //BA.debugLineNum = 866;BA.debugLine="datalist.Clear";
mostCurrent._datalist.Clear();
 //BA.debugLineNum = 868;BA.debugLine="list1=pak.GetInstalledPackages";
mostCurrent._list1 = _pak.GetInstalledPackages();
 //BA.debugLineNum = 870;BA.debugLine="Obj1.Target = Obj1.GetContext";
mostCurrent._obj1.Target = (Object)(mostCurrent._obj1.GetContext(processBA));
 //BA.debugLineNum = 871;BA.debugLine="Obj1.Target = Obj1.RunMethod(\"getPackageManager\")";
mostCurrent._obj1.Target = mostCurrent._obj1.RunMethod("getPackageManager");
 //BA.debugLineNum = 872;BA.debugLine="Obj2.Target = Obj1.RunMethod2(\"getInstalledPackag";
mostCurrent._obj2.Target = mostCurrent._obj1.RunMethod2("getInstalledPackages",BA.NumberToString(0),"java.lang.int");
 //BA.debugLineNum = 873;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 878;BA.debugLine="For i = 0 To size -1";
{
final int step9 = 1;
final int limit9 = (int) (_size-1);
for (_i = (int) (0) ; (step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9); _i = ((int)(0 + _i + step9)) ) {
 //BA.debugLineNum = 879;BA.debugLine="Obj3.Target = Obj2.RunMethod2(\"get\", i, \"java.la";
mostCurrent._obj3.Target = mostCurrent._obj2.RunMethod2("get",BA.NumberToString(_i),"java.lang.int");
 //BA.debugLineNum = 880;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 882;BA.debugLine="Obj3.Target = Obj3.GetField(\"applicationInfo\") '";
mostCurrent._obj3.Target = mostCurrent._obj3.GetField("applicationInfo");
 //BA.debugLineNum = 883;BA.debugLine="flags = Obj3.GetField(\"flags\")";
_flags = (int)(BA.ObjectToNumber(mostCurrent._obj3.GetField("flags")));
 //BA.debugLineNum = 884;BA.debugLine="packName = Obj3.GetField(\"packageName\")";
mostCurrent._packname = BA.ObjectToString(mostCurrent._obj3.GetField("packageName"));
 //BA.debugLineNum = 886;BA.debugLine="If Bit.And(flags, 1)  = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_flags,(int) (1))==0) { 
 //BA.debugLineNum = 889;BA.debugLine="args(0) = Obj3.Target";
mostCurrent._args[(int) (0)] = mostCurrent._obj3.Target;
 //BA.debugLineNum = 890;BA.debugLine="Types(0) = \"android.content.pm.ApplicationInfo\"";
mostCurrent._types[(int) (0)] = "android.content.pm.ApplicationInfo";
 //BA.debugLineNum = 891;BA.debugLine="name = Obj1.RunMethod4(\"getApplicationLabel\", a";
mostCurrent._name = BA.ObjectToString(mostCurrent._obj1.RunMethod4("getApplicationLabel",mostCurrent._args,mostCurrent._types));
 //BA.debugLineNum = 892;BA.debugLine="icon = Obj1.RunMethod4(\"getApplicationIcon\", ar";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(mostCurrent._obj1.RunMethod4("getApplicationIcon",mostCurrent._args,mostCurrent._types)));
 //BA.debugLineNum = 894;BA.debugLine="list3.Add(packName)";
mostCurrent._list3.Add((Object)(mostCurrent._packname));
 //BA.debugLineNum = 897;BA.debugLine="phlis.Add(icon.Bitmap)";
mostCurrent._phlis.Add((Object)(mostCurrent._icon.getBitmap()));
 //BA.debugLineNum = 900;BA.debugLine="Dim form As Int";
_form = 0;
 //BA.debugLineNum = 901;BA.debugLine="form=File.Size(GetParentPath(GetSourceDir(GetAc";
_form = (int) (anywheresoftware.b4a.keywords.Common.File.Size(_getparentpath(_getsourcedir(_getactivitiesinfo(mostCurrent._packname))),_getfilename(_getsourcedir(_getactivitiesinfo(mostCurrent._packname)))));
 //BA.debugLineNum = 902;BA.debugLine="datalist.Add(form)";
mostCurrent._datalist.Add((Object)(_form));
 //BA.debugLineNum = 903;BA.debugLine="lv2.AddTwoLinesAndBitmap2(name,packName&\" - \"&F";
mostCurrent._lv2.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence(mostCurrent._name),BA.ObjectToCharSequence(mostCurrent._packname+" - "+_formatfilesize((float) (_form))),mostCurrent._icon.getBitmap(),(Object)(mostCurrent._packname));
 //BA.debugLineNum = 904;BA.debugLine="Log(ffolders.Size&\" folder / \"&ffiles.Size&\" fi";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(mostCurrent._ffolders.getSize())+" folder / "+BA.NumberToString(mostCurrent._ffiles.getSize())+" files");
 };
 }
};
 //BA.debugLineNum = 912;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 735;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 736;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 737;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 738;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 765;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 766;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 767;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 768;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 584;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 585;BA.debugLine="StartService(webhost)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._webhost.getObject()));
 //BA.debugLineNum = 586;BA.debugLine="StartActivity(cool)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._cool.getObject()));
 //BA.debugLineNum = 587;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 589;BA.debugLine="End Sub";
return "";
}
public static String  _bytestofile(String _dir,String _filename,byte[] _data) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
 //BA.debugLineNum = 914;BA.debugLine="Sub BytesToFile (Dir As String, FileName As String";
 //BA.debugLineNum = 915;BA.debugLine="Dim out As OutputStream = File.OpenOutput(Dir, Fi";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(_dir,_filename,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 916;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 917;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 918;BA.debugLine="End Sub";
return "";
}
public static long  _calcsize(String _folder,boolean _recursive) throws Exception{
long _size1 = 0L;
String _f = "";
 //BA.debugLineNum = 662;BA.debugLine="Sub CalcSize(Folder As String, recursive As Boolea";
 //BA.debugLineNum = 663;BA.debugLine="Dim size1 As Long";
_size1 = 0L;
 //BA.debugLineNum = 664;BA.debugLine="For Each f As String In File.ListFiles(Folder)";
final anywheresoftware.b4a.BA.IterableList group2 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
final int groupLen2 = group2.getSize();
for (int index2 = 0;index2 < groupLen2 ;index2++){
_f = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 665;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 666;BA.debugLine="If File.IsDirectory(Folder, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,_f)) { 
 //BA.debugLineNum = 667;BA.debugLine="size1 = size1 + CalcSize(File.Combine(Folder,";
_size1 = (long) (_size1+_calcsize(anywheresoftware.b4a.keywords.Common.File.Combine(_folder,_f),_recursive));
 };
 };
 //BA.debugLineNum = 670;BA.debugLine="size1 = size1 + File.Size(Folder, f)";
_size1 = (long) (_size1+anywheresoftware.b4a.keywords.Common.File.Size(_folder,_f));
 }
;
 //BA.debugLineNum = 672;BA.debugLine="Return size1";
if (true) return _size1;
 //BA.debugLineNum = 673;BA.debugLine="End Sub";
return 0L;
}
public static boolean  _cl_click() throws Exception{
int _res = 0;
 //BA.debugLineNum = 740;BA.debugLine="Sub cl_click As Boolean";
 //BA.debugLineNum = 741;BA.debugLine="Dim res As Int";
_res = 0;
 //BA.debugLineNum = 742;BA.debugLine="res=Msgbox2(cs.Initialize.Alignment(\"ALIGN_CENTER";
_res = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence(_cs.Initialize().Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence(("App wird geschlossen Service Icon läuft im Hintergrund Prozess weiter und wird als Info weiterhin in der statusbar ausgeführt., zum deaktivieren bitte 'Settings->Start/stop Notify Service'.!\",\"Bat-CaT beenden:"))).PopAll().getObject()),BA.ObjectToCharSequence(_cs.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT).Color((int) (0xff01ff20)).Size((int) (40)).PopAll().getObject()),"ja","Abbruch","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Power.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 744;BA.debugLine="If res=DialogResponse.POSITIVE Then";
if (_res==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 745;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 746;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 }else {
 //BA.debugLineNum = 748;BA.debugLine="If res=DialogResponse.CANCEL Then";
if (_res==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 749;BA.debugLine="ToastMessageShow(\"zurück..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("zurück.."),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 752;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 753;BA.debugLine="End Sub";
return false;
}
public static String  _cli_click() throws Exception{
 //BA.debugLineNum = 932;BA.debugLine="Sub cli_click";
 //BA.debugLineNum = 934;BA.debugLine="StartActivity(cool)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._cool.getObject()));
 //BA.debugLineNum = 935;BA.debugLine="End Sub";
return "";
}
public static String  _copyfolder(String _source,String _targetfolder) throws Exception{
String _f = "";
 //BA.debugLineNum = 675;BA.debugLine="Private Sub CopyFolder(Source As String, targetFol";
 //BA.debugLineNum = 676;BA.debugLine="If File.Exists(targetFolder, \"\") = False Then Fil";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_targetfolder,"")==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(_targetfolder,"");};
 //BA.debugLineNum = 677;BA.debugLine="For Each f As String In File.ListFiles(Source)";
final anywheresoftware.b4a.BA.IterableList group2 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_source);
final int groupLen2 = group2.getSize();
for (int index2 = 0;index2 < groupLen2 ;index2++){
_f = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 678;BA.debugLine="If File.IsDirectory(Source, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_source,_f)) { 
 //BA.debugLineNum = 679;BA.debugLine="CopyFolder(File.Combine(Source, f), File.Combin";
_copyfolder(anywheresoftware.b4a.keywords.Common.File.Combine(_source,_f),anywheresoftware.b4a.keywords.Common.File.Combine(_targetfolder,_f));
 //BA.debugLineNum = 680;BA.debugLine="Continue";
if (true) continue;
 };
 //BA.debugLineNum = 682;BA.debugLine="File.Copy(Source, f, targetFolder, f)";
anywheresoftware.b4a.keywords.Common.File.Copy(_source,_f,_targetfolder,_f);
 }
;
 //BA.debugLineNum = 684;BA.debugLine="End Sub";
return "";
}
public static String  _device_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
int _rst = 0;
int _ts = 0;
int _volta = 0;
int _vs = 0;
int _vol = 0;
int _days = 0;
 //BA.debugLineNum = 771;BA.debugLine="Sub device_BatteryChanged (level As Int, Scale As";
 //BA.debugLineNum = 772;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 773;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 774;BA.debugLine="Dim val,hours,minutes,rst,ts,volta,vs As Int";
_val = 0;
_hours = 0;
_minutes = 0;
_rst = 0;
_ts = 0;
_volta = 0;
_vs = 0;
 //BA.debugLineNum = 775;BA.debugLine="volta=Intent.GetExtra(\"voltage\")/1000";
_volta = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 776;BA.debugLine="temp=Intent.GetExtra(\"temperature\")/10";
mostCurrent._temp = BA.NumberToString((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))/(double)10);
 //BA.debugLineNum = 778;BA.debugLine="Label1.Visible=True";
mostCurrent._label1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 779;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl.txt",BA.NumberToString(_level));
 //BA.debugLineNum = 780;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","volt.txt",BA.NumberToString(_volta));
 //BA.debugLineNum = 782;BA.debugLine="prb1.ButtonRadius=Scale";
mostCurrent._prb1.setButtonRadius((float) (_scale));
 //BA.debugLineNum = 783;BA.debugLine="prb1.Color=Colors.ARGB(190,255,255,255)";
mostCurrent._prb1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (190),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 784;BA.debugLine="prb1.BackgroundSecondColor=Colors.Transparent";
mostCurrent._prb1.setBackgroundSecondColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 785;BA.debugLine="prb1.CurrentText=temp&\"°C\"";
mostCurrent._prb1.setCurrentText(BA.ObjectToCharSequence(mostCurrent._temp+"°C"));
 //BA.debugLineNum = 786;BA.debugLine="prb1.MaxProgress=410";
mostCurrent._prb1.setMaxProgress((int) (410));
 //BA.debugLineNum = 787;BA.debugLine="prb1.MinProgress=Intent.GetExtra(\"temperature\")-1";
mostCurrent._prb1.setMinProgress((int) ((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))-10));
 //BA.debugLineNum = 788;BA.debugLine="prb1.Progress=Intent.GetExtra(\"temperature\")";
mostCurrent._prb1.setProgress((float)(BA.ObjectToNumber(_intent.GetExtra("temperature"))));
 //BA.debugLineNum = 789;BA.debugLine="prb1.TextColor=mcl.md_black_1000";
mostCurrent._prb1.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 790;BA.debugLine="prb1.TextCoverColor=Colors.ARGB(199,0,0,0)";
mostCurrent._prb1.setTextCoverColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (199),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 791;BA.debugLine="prb1.TextSize=18";
mostCurrent._prb1.setTextSize((float) (18));
 //BA.debugLineNum = 792;BA.debugLine="prb1.TheTextSize=18";
mostCurrent._prb1.setTheTextSize((float) (18));
 //BA.debugLineNum = 794;BA.debugLine="apm.Progress=level";
mostCurrent._apm.setProgress(_level);
 //BA.debugLineNum = 795;BA.debugLine="apm.BottomText=Intent.GetExtra(\"technology\")";
mostCurrent._apm.setBottomText(BA.ObjectToString(_intent.GetExtra("technology")));
 //BA.debugLineNum = 796;BA.debugLine="apm.Max=Scale";
mostCurrent._apm.setMax(_scale);
 //BA.debugLineNum = 797;BA.debugLine="rst=Scale-level";
_rst = (int) (_scale-_level);
 //BA.debugLineNum = 798;BA.debugLine="If Plugged=True Then";
if (_plugged==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 799;BA.debugLine="ims2.Bitmap=LoadBitmap(File.DirAssets,\"ic_usb_bl";
mostCurrent._ims2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_usb_black_24dp.png").getObject()));
 //BA.debugLineNum = 800;BA.debugLine="apm.BottomText=\"Aufladen via USB: \"&volta&\" V\"";
mostCurrent._apm.setBottomText("Aufladen via USB: "+BA.NumberToString(_volta)+" V");
 //BA.debugLineNum = 801;BA.debugLine="Log(\"VOLT: \"&Intent.GetExtra(\"voltage\"))";
anywheresoftware.b4a.keywords.Common.Log("VOLT: "+BA.ObjectToString(_intent.GetExtra("voltage")));
 //BA.debugLineNum = 802;BA.debugLine="val =rst*Intent.GetExtra(\"voltage\") /1000";
_val = (int) (_rst*(double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 803;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 804;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 805;BA.debugLine="If level=100 Then";
if (_level==100) { 
 //BA.debugLineNum = 806;BA.debugLine="apm.BottomText=\"Kabel entfernen(USB)!\"";
mostCurrent._apm.setBottomText("Kabel entfernen(USB)!");
 //BA.debugLineNum = 807;BA.debugLine="Label4.Text=\"Batterie voll! Bitte Kabel entfern";
mostCurrent._label4.setText(BA.ObjectToCharSequence("Batterie voll! Bitte Kabel entfernen!"));
 }else {
 //BA.debugLineNum = 810;BA.debugLine="Label4.Text=\"voll in: \"&hours&\"h - \"&minutes&\"mi";
mostCurrent._label4.setText(BA.ObjectToCharSequence("voll in: "+BA.NumberToString(_hours)+"h - "+BA.NumberToString(_minutes)+"min"));
 };
 }else {
 //BA.debugLineNum = 814;BA.debugLine="ims2.Bitmap=LoadBitmap(File.DirAssets,\"ic_data_u";
mostCurrent._ims2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_data_usage_black_48dp.png").getObject()));
 //BA.debugLineNum = 815;BA.debugLine="If level<=5 Then";
if (_level<=5) { 
 //BA.debugLineNum = 816;BA.debugLine="Label1.TextColor=Colors.Red";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 817;BA.debugLine="Label1.TextSize=15";
mostCurrent._label1.setTextSize((float) (15));
 //BA.debugLineNum = 818;BA.debugLine="Label1.Text=\"Akku laden!\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Akku laden!"));
 };
 //BA.debugLineNum = 821;BA.debugLine="Dim vol As Int";
_vol = 0;
 //BA.debugLineNum = 822;BA.debugLine="vol=Intent.GetExtra(\"voltage\")";
_vol = (int)(BA.ObjectToNumber(_intent.GetExtra("voltage")));
 //BA.debugLineNum = 823;BA.debugLine="Dim days As Int";
_days = 0;
 //BA.debugLineNum = 824;BA.debugLine="val = level*1000 /60";
_val = (int) (_level*1000/(double)60);
 //BA.debugLineNum = 825;BA.debugLine="days=Floor(val/60/24)";
_days = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60/(double)24));
 //BA.debugLineNum = 826;BA.debugLine="hours = Floor(val/60 Mod 24)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60%24));
 //BA.debugLineNum = 827;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 828;BA.debugLine="Label4.Text= \"noch: \"&days&\"d - \"&hours&\"h - \"&m";
mostCurrent._label4.setText(BA.ObjectToCharSequence("noch: "+BA.NumberToString(_days)+"d - "+BA.NumberToString(_hours)+"h - "+BA.NumberToString(_minutes)+"m"));
 };
 //BA.debugLineNum = 830;BA.debugLine="If temp<=31 Then";
if ((double)(Double.parseDouble(mostCurrent._temp))<=31) { 
 //BA.debugLineNum = 831;BA.debugLine="prb1.Color=mcl.md_lime_A400";
mostCurrent._prb1.setColor(mostCurrent._mcl.getmd_lime_A400());
 //BA.debugLineNum = 832;BA.debugLine="prb1.TextColor=mcl.md_black_1000";
mostCurrent._prb1.setTextColor(mostCurrent._mcl.getmd_black_1000());
 };
 //BA.debugLineNum = 834;BA.debugLine="If temp>=31 Then";
if ((double)(Double.parseDouble(mostCurrent._temp))>=31) { 
 //BA.debugLineNum = 835;BA.debugLine="prb1.Color=mcl.md_amber_A400";
mostCurrent._prb1.setColor(mostCurrent._mcl.getmd_amber_A400());
 //BA.debugLineNum = 836;BA.debugLine="prb1.TextColor=mcl.md_black_1000";
mostCurrent._prb1.setTextColor(mostCurrent._mcl.getmd_black_1000());
 };
 //BA.debugLineNum = 838;BA.debugLine="If temp >=45 Then";
if ((double)(Double.parseDouble(mostCurrent._temp))>=45) { 
 //BA.debugLineNum = 839;BA.debugLine="prb1.Color=mcl.md_red_A400";
mostCurrent._prb1.setColor(mostCurrent._mcl.getmd_red_A400());
 //BA.debugLineNum = 840;BA.debugLine="prb1.TextColor=mcl.md_black_1000";
mostCurrent._prb1.setTextColor(mostCurrent._mcl.getmd_black_1000());
 };
 //BA.debugLineNum = 842;BA.debugLine="End Sub";
return "";
}
public static String  _device_devicestorageok(anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 291;BA.debugLine="Sub  device_DeviceStorageOk (Intent As Intent)";
 //BA.debugLineNum = 292;BA.debugLine="Log(Intent.ExtrasToString)";
anywheresoftware.b4a.keywords.Common.Log(_intent.ExtrasToString());
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _fc_copydone(String _key,boolean _error) throws Exception{
 //BA.debugLineNum = 924;BA.debugLine="Sub fc_CopyDone(Key As String, Error As Boolean)";
 //BA.debugLineNum = 926;BA.debugLine="End Sub";
return "";
}
public static byte[]  _filetobytes(String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 920;BA.debugLine="Sub FileToBytes (Dir As String, FileName As String";
 //BA.debugLineNum = 921;BA.debugLine="Return Bit.InputStreamToBytes(File.OpenInput(Dir,";
if (true) return anywheresoftware.b4a.keywords.Common.Bit.InputStreamToBytes((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_dir,_filename).getObject()));
 //BA.debugLineNum = 922;BA.debugLine="End Sub";
return null;
}
public static String  _formatfilesize(float _bytes) throws Exception{
String[] _unit = null;
double _po = 0;
double _si = 0;
int _i = 0;
 //BA.debugLineNum = 705;BA.debugLine="Sub FormatFileSize(Bytes As Float) As String";
 //BA.debugLineNum = 707;BA.debugLine="Private Unit() As String = Array As String(\" Byte";
_unit = new String[]{" Byte"," KB"," MB"," GB"," TB"," PB"," EB"," ZB"," YB"};
 //BA.debugLineNum = 709;BA.debugLine="If Bytes = 0 Then";
if (_bytes==0) { 
 //BA.debugLineNum = 711;BA.debugLine="Return \"0 Bytes\"";
if (true) return "0 Bytes";
 }else {
 //BA.debugLineNum = 715;BA.debugLine="Private Po, Si As Double";
_po = 0;
_si = 0;
 //BA.debugLineNum = 716;BA.debugLine="Private I As Int";
_i = 0;
 //BA.debugLineNum = 718;BA.debugLine="Bytes = Abs(Bytes)";
_bytes = (float) (anywheresoftware.b4a.keywords.Common.Abs(_bytes));
 //BA.debugLineNum = 720;BA.debugLine="I = Floor(Logarithm(Bytes, 1024))";
_i = (int) (anywheresoftware.b4a.keywords.Common.Floor(anywheresoftware.b4a.keywords.Common.Logarithm(_bytes,1024)));
 //BA.debugLineNum = 721;BA.debugLine="Po = Power(1024, I)";
_po = anywheresoftware.b4a.keywords.Common.Power(1024,_i);
 //BA.debugLineNum = 722;BA.debugLine="Si = Bytes / Po";
_si = _bytes/(double)_po;
 //BA.debugLineNum = 724;BA.debugLine="Return NumberFormat(Si, 1, 2) & Unit(I)";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_si,(int) (1),(int) (2))+_unit[_i];
 };
 //BA.debugLineNum = 728;BA.debugLine="End Sub";
return "";
}
public static Object  _getactivitiesinfo(String _package) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 959;BA.debugLine="Sub GetActivitiesInfo(package As String) As Object";
 //BA.debugLineNum = 960;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 961;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(processBA));
 //BA.debugLineNum = 962;BA.debugLine="r.Target = r.RunMethod(\"getPackageManager\")";
_r.Target = _r.RunMethod("getPackageManager");
 //BA.debugLineNum = 963;BA.debugLine="r.Target = r.RunMethod3(\"getPackageInfo\", package";
_r.Target = _r.RunMethod3("getPackageInfo",_package,"java.lang.String",BA.NumberToString(0x00000001),"java.lang.int");
 //BA.debugLineNum = 964;BA.debugLine="Return r.GetField(\"applicationInfo\")";
if (true) return _r.GetField("applicationInfo");
 //BA.debugLineNum = 965;BA.debugLine="End Sub";
return null;
}
public static String  _getfilename(String _fullpath) throws Exception{
 //BA.debugLineNum = 928;BA.debugLine="Sub GetFileName(FullPath As String) As String";
 //BA.debugLineNum = 929;BA.debugLine="Return FullPath.SubString(FullPath.LastIndexOf(\"/";
if (true) return _fullpath.substring((int) (_fullpath.lastIndexOf("/")+1));
 //BA.debugLineNum = 930;BA.debugLine="End Sub";
return "";
}
public static String  _getparentpath(String _path) throws Exception{
String _path1 = "";
 //BA.debugLineNum = 940;BA.debugLine="Sub GetParentPath(Path As String) As String";
 //BA.debugLineNum = 941;BA.debugLine="Dim Path1 As String";
_path1 = "";
 //BA.debugLineNum = 942;BA.debugLine="If Path = \"/\" Then";
if ((_path).equals("/")) { 
 //BA.debugLineNum = 943;BA.debugLine="Return \"/\"";
if (true) return "/";
 };
 //BA.debugLineNum = 945;BA.debugLine="L = Path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 946;BA.debugLine="If L = Path.Length - 1 Then";
if ((mostCurrent._l).equals(BA.NumberToString(_path.length()-1))) { 
 //BA.debugLineNum = 948;BA.debugLine="Path1 = Path.SubString2(0,L)";
_path1 = _path.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 }else {
 //BA.debugLineNum = 950;BA.debugLine="Path1 = Path";
_path1 = _path;
 };
 //BA.debugLineNum = 952;BA.debugLine="L = Path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 953;BA.debugLine="If L = 0 Then";
if ((mostCurrent._l).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 954;BA.debugLine="L = 1";
mostCurrent._l = BA.NumberToString(1);
 };
 //BA.debugLineNum = 956;BA.debugLine="Return Path1.SubString2(0,L)";
if (true) return _path1.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 //BA.debugLineNum = 957;BA.debugLine="End Sub";
return "";
}
public static String  _getsourcedir(Object _appinfo) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 967;BA.debugLine="Sub GetSourceDir(AppInfo As Object) As String";
 //BA.debugLineNum = 968;BA.debugLine="Try";
try { //BA.debugLineNum = 969;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 970;BA.debugLine="r.Target = AppInfo";
_r.Target = _appinfo;
 //BA.debugLineNum = 971;BA.debugLine="Return r.GetField(\"sourceDir\")";
if (true) return BA.ObjectToString(_r.GetField("sourceDir"));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 973;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 975;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Private ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private ra As OperatingSystem";
mostCurrent._ra = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 30;BA.debugLine="Dim device As PhoneEvents";
mostCurrent._device = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 31;BA.debugLine="Dim ct As CustomToast";
mostCurrent._ct = new com.rootsoft.customtoast.CustomToast();
 //BA.debugLineNum = 33;BA.debugLine="Private Label1,Label2,Label3,Label4,lk As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label4 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lk = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private lv2,lvMenu As ListView";
mostCurrent._lv2 = new anywheresoftware.b4a.objects.ListViewWrapper();
mostCurrent._lvmenu = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim args(1) As Object";
mostCurrent._args = new Object[(int) (1)];
{
int d0 = mostCurrent._args.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._args[i0] = new Object();
}
}
;
 //BA.debugLineNum = 36;BA.debugLine="Dim Obj1, Obj2, Obj3 As Reflector";
mostCurrent._obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj3 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 37;BA.debugLine="Dim la,la1,la2 As Label";
mostCurrent._la = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._la1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._la2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim ima As ImageView";
mostCurrent._ima = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim icon As BitmapDrawable";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 40;BA.debugLine="Dim logo As Bitmap";
mostCurrent._logo = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim bat As Batut";
mostCurrent._bat = new com.batcat.batut();
 //BA.debugLineNum = 42;BA.debugLine="Private CheckBox1 As CheckBox";
mostCurrent._checkbox1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private CheckBox2 As CheckBox";
mostCurrent._checkbox2 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim temp,level1 As String";
mostCurrent._temp = "";
mostCurrent._level1 = "";
 //BA.debugLineNum = 45;BA.debugLine="Private Panel2,panel3,lip,smlp As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._panel3 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._lip = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._smlp = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Dim kvs,kvs2,kvs3,kvs4,kvs4sub As KeyValueStore";
mostCurrent._kvs = new com.batcat.keyvaluestore();
mostCurrent._kvs2 = new com.batcat.keyvaluestore();
mostCurrent._kvs3 = new com.batcat.keyvaluestore();
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvs4sub = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 48;BA.debugLine="Dim optmap As Map";
mostCurrent._optmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 49;BA.debugLine="Private Label5 As Label";
mostCurrent._label5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim proc As List";
mostCurrent._proc = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 52;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private showtip As MSShowTipsBuilder";
mostCurrent._showtip = new com.maximussoft.showtips.BuilderWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private slm As ClsSlidingSideBar";
mostCurrent._slm = new com.batcat.clsslidingsidebar();
 //BA.debugLineNum = 55;BA.debugLine="Dim cas As Int";
_cas = 0;
 //BA.debugLineNum = 56;BA.debugLine="Dim size,flags As Int";
_size = 0;
_flags = 0;
 //BA.debugLineNum = 57;BA.debugLine="Dim Types(1), name,packName,date,time,l,ramsize A";
mostCurrent._types = new String[(int) (1)];
java.util.Arrays.fill(mostCurrent._types,"");
mostCurrent._name = "";
mostCurrent._packname = "";
mostCurrent._date = "";
mostCurrent._time = "";
mostCurrent._l = "";
mostCurrent._ramsize = "";
 //BA.debugLineNum = 58;BA.debugLine="Dim List,list1,list2,list3,list4,list5,list6,list";
mostCurrent._list = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list2 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list3 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list4 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list5 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list6 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list7 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list8 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list9 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._phlis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._lis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._setlist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._datalist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 60;BA.debugLine="Dim cd2,cd As CustomDialog2";
mostCurrent._cd2 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
mostCurrent._cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 62;BA.debugLine="Dim FakeActionBar  As Panel";
mostCurrent._fakeactionbar = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Dim sd,sdi As Bitmap";
mostCurrent._sd = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._sdi = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 69;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
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
 //BA.debugLineNum = 70;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 71;BA.debugLine="Dim Mclist,ramlist As List";
mostCurrent._mclist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ramlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 72;BA.debugLine="Dim cat As Cache";
mostCurrent._cat = new flm.b4a.cache.Cache();
 //BA.debugLineNum = 75;BA.debugLine="Dim fbc As ColorDrawable";
mostCurrent._fbc = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 76;BA.debugLine="Dim xml As XmlLayoutBuilder";
mostCurrent._xml = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 77;BA.debugLine="Private ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private apm As ArcProgressMaster";
mostCurrent._apm = new circleprogressmasterwrapper.arcProgressMasterWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 84;BA.debugLine="Private Button4 As Button";
mostCurrent._button4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private ims1 As ImageView";
mostCurrent._ims1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private ims2 As ImageView";
mostCurrent._ims2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private ims3 As ImageView";
mostCurrent._ims3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Private ims4 As ImageView";
mostCurrent._ims4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private prb1 As ProgressRoundButton";
mostCurrent._prb1 = new progressroundbuttonwrapper.progressRoundButtonWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Dim volt,root1 As String";
mostCurrent._volt = "";
mostCurrent._root1 = "";
 //BA.debugLineNum = 91;BA.debugLine="Dim ffiles,ffolders As List";
mostCurrent._ffiles = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ffolders = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _inputlist_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 936;BA.debugLine="Sub InputList_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 938;BA.debugLine="End Sub";
return "";
}
public static String  _label_text() throws Exception{
 //BA.debugLineNum = 604;BA.debugLine="Sub label_text";
 //BA.debugLineNum = 605;BA.debugLine="lk.TextSize=13";
mostCurrent._lk.setTextSize((float) (13));
 //BA.debugLineNum = 606;BA.debugLine="lk.TextColor=Colors.Black";
mostCurrent._lk.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 607;BA.debugLine="lk.Text=\"Version: \"&pak.GetVersionName(\"com.batca";
mostCurrent._lk.setText(BA.ObjectToCharSequence("Version: "+_pak.GetVersionName("com.batcat")+", Integer: "+BA.NumberToString(_pak.GetVersionCode("com.batcat"))+". Coded in 'Basic' and 'Sun Java OpenSource' by D. Trojan, published by SuloMedia™. All Rights Reserved ©2017 "+_pak.GetApplicationLabel("com.batcat")));
 //BA.debugLineNum = 608;BA.debugLine="End Sub";
return "";
}
public static String  _label1_click() throws Exception{
 //BA.debugLineNum = 730;BA.debugLine="Sub Label1_Click";
 //BA.debugLineNum = 731;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 732;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 733;BA.debugLine="End Sub";
return "";
}
public static String  _label4_click() throws Exception{
 //BA.debugLineNum = 853;BA.debugLine="Sub Label4_Click";
 //BA.debugLineNum = 854;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 855;BA.debugLine="End Sub";
return "";
}
public static String  _li2() throws Exception{
int _si = 0;
int _st = 0;
int _math1 = 0;
int _math2 = 0;
int _ti = 0;
int _z = 0;
int _h = 0;
 //BA.debugLineNum = 626;BA.debugLine="Sub li2";
 //BA.debugLineNum = 627;BA.debugLine="list2.Clear";
mostCurrent._list2.Clear();
 //BA.debugLineNum = 628;BA.debugLine="list4.Clear";
mostCurrent._list4.Clear();
 //BA.debugLineNum = 629;BA.debugLine="list5.Clear";
mostCurrent._list5.Clear();
 //BA.debugLineNum = 630;BA.debugLine="list7.Clear";
mostCurrent._list7.Clear();
 //BA.debugLineNum = 631;BA.debugLine="proc.Clear";
mostCurrent._proc.Clear();
 //BA.debugLineNum = 632;BA.debugLine="ramlist.Clear";
mostCurrent._ramlist.Clear();
 //BA.debugLineNum = 633;BA.debugLine="cat.Initialize(25,100*1024*1024,File.DirDefaultEx";
mostCurrent._cat.Initialize(processBA,(byte) (25),(long) (100*1024*1024),anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache");
 //BA.debugLineNum = 634;BA.debugLine="Dim si,st As Int";
_si = 0;
_st = 0;
 //BA.debugLineNum = 635;BA.debugLine="st=ra.AvailableMemory*1024*1024";
_st = (int) (mostCurrent._ra.getAvailableMemory()*1024*1024);
 //BA.debugLineNum = 636;BA.debugLine="Log(FormatFileSize(st))";
anywheresoftware.b4a.keywords.Common.Log(_formatfilesize((float) (_st)));
 //BA.debugLineNum = 638;BA.debugLine="si=ra.AvailableInternalMemorySize";
_si = (int) (mostCurrent._ra.getAvailableInternalMemorySize());
 //BA.debugLineNum = 639;BA.debugLine="proc=ra.getRecentTasks(99,0)";
mostCurrent._proc.setObject((java.util.List)(mostCurrent._ra.getRecentTasks((int) (99),(int) (0))));
 //BA.debugLineNum = 640;BA.debugLine="ramlist=ra.RunningServiceInfo(999,list7,list8,lis";
mostCurrent._ramlist.setObject((java.util.List)(mostCurrent._ra.RunningServiceInfo((int) (999),(java.util.List)(mostCurrent._list7.getObject()),(java.util.List)(mostCurrent._list8.getObject()),(java.util.List)(mostCurrent._list9.getObject()))));
 //BA.debugLineNum = 641;BA.debugLine="list2=ra.RunningAppProcesses'Info(list4,list5,lis";
mostCurrent._list2.setObject((java.util.List)(mostCurrent._ra.getRunningAppProcesses()));
 //BA.debugLineNum = 642;BA.debugLine="Dim math1,math2 As Int";
_math1 = 0;
_math2 = 0;
 //BA.debugLineNum = 645;BA.debugLine="Dim ti As Int=list2.Size+list7.Size+st";
_ti = (int) (mostCurrent._list2.getSize()+mostCurrent._list7.getSize()+_st);
 //BA.debugLineNum = 646;BA.debugLine="math1=ti*10*1024*1024";
_math1 = (int) (_ti*10*1024*1024);
 //BA.debugLineNum = 647;BA.debugLine="Log(FormatFileSize(math1))";
anywheresoftware.b4a.keywords.Common.Log(_formatfilesize((float) (_math1)));
 //BA.debugLineNum = 649;BA.debugLine="math2=cat.DiskFree";
_math2 = (int) (mostCurrent._cat.DiskFree());
 //BA.debugLineNum = 650;BA.debugLine="Label5.Text=list7.size&\"% \"&FormatFileSize(math1+";
mostCurrent._label5.setText(BA.ObjectToCharSequence(BA.NumberToString(mostCurrent._list7.getSize())+"% "+_formatfilesize((float) (_math1+_math2))));
 //BA.debugLineNum = 651;BA.debugLine="For z = 0 To  list7.Size-1";
{
final int step21 = 1;
final int limit21 = (int) (mostCurrent._list7.getSize()-1);
for (_z = (int) (0) ; (step21 > 0 && _z <= limit21) || (step21 < 0 && _z >= limit21); _z = ((int)(0 + _z + step21)) ) {
 //BA.debugLineNum = 653;BA.debugLine="For h = 0 To  list2.size-1";
{
final int step22 = 1;
final int limit22 = (int) (mostCurrent._list2.getSize()-1);
for (_h = (int) (0) ; (step22 > 0 && _h <= limit22) || (step22 < 0 && _h >= limit22); _h = ((int)(0 + _h + step22)) ) {
 //BA.debugLineNum = 656;BA.debugLine="ProgressBar1.Progress=z";
mostCurrent._progressbar1.setProgress(_z);
 }
};
 }
};
 //BA.debugLineNum = 659;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 660;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 857;BA.debugLine="Sub listview1_ItemClick (Position As Int, value As";
 //BA.debugLineNum = 858;BA.debugLine="If Position=0 Then";
if (_position==0) { 
 //BA.debugLineNum = 859;BA.debugLine="rl_lo";
_rl_lo();
 };
 //BA.debugLineNum = 861;BA.debugLine="End Sub";
return "";
}
public static String  _lvmenu_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 531;BA.debugLine="Sub lvMenu_ItemClick (Position As Int, Value As Ob";
 //BA.debugLineNum = 532;BA.debugLine="If Value = 0 Then";
if ((_value).equals((Object)(0))) { 
 //BA.debugLineNum = 533;BA.debugLine="opt_click";
_opt_click();
 //BA.debugLineNum = 534;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 535;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 537;BA.debugLine="If Value=1 Then";
if ((_value).equals((Object)(1))) { 
 //BA.debugLineNum = 538;BA.debugLine="cl_click";
_cl_click();
 //BA.debugLineNum = 539;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 540;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 542;BA.debugLine="If Value=2 Then";
if ((_value).equals((Object)(2))) { 
 //BA.debugLineNum = 543;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 544;BA.debugLine="i.Initialize( \"android.intent.action.POWER_USAGE";
_i.Initialize("android.intent.action.POWER_USAGE_SUMMARY","");
 //BA.debugLineNum = 545;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 546;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 547;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 549;BA.debugLine="If Value=3 Then";
if ((_value).equals((Object)(3))) { 
 //BA.debugLineNum = 550;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 551;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 552;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 554;BA.debugLine="If Value=4 Then";
if ((_value).equals((Object)(4))) { 
 //BA.debugLineNum = 555;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 556;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 557;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 559;BA.debugLine="If Value=5 Then";
if ((_value).equals((Object)(5))) { 
 //BA.debugLineNum = 560;BA.debugLine="pci_click";
_pci_click();
 //BA.debugLineNum = 561;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 562;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 564;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onfullyclosed() throws Exception{
 //BA.debugLineNum = 326;BA.debugLine="Sub Menu_onFullyClosed";
 //BA.debugLineNum = 334;BA.debugLine="Label1.SetLayout(40%x,40%y,40%x,90dip)";
mostCurrent._label1.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 335;BA.debugLine="Button4.TextSize=18";
mostCurrent._button4.setTextSize((float) (18));
 //BA.debugLineNum = 337;BA.debugLine="Label2.SetVisibleAnimated(100,True)";
mostCurrent._label2.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 338;BA.debugLine="Label4.SetVisibleAnimated(100,True)";
mostCurrent._label4.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 339;BA.debugLine="Button4.SetLayoutAnimated(100,110dip,55%y,95dip,9";
mostCurrent._button4.SetLayoutAnimated((int) (100),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (110)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (55),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (95)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 340;BA.debugLine="Button4.SetVisibleAnimated(80,True)";
mostCurrent._button4.SetVisibleAnimated((int) (80),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 342;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onfullyopen() throws Exception{
 //BA.debugLineNum = 312;BA.debugLine="Sub Menu_onFullyOpen";
 //BA.debugLineNum = 313;BA.debugLine="lvMenu.BringToFront";
mostCurrent._lvmenu.BringToFront();
 //BA.debugLineNum = 314;BA.debugLine="smlp.BringToFront";
mostCurrent._smlp.BringToFront();
 //BA.debugLineNum = 315;BA.debugLine="slm.Sidebar.BringToFront";
mostCurrent._slm._sidebar().BringToFront();
 //BA.debugLineNum = 316;BA.debugLine="lvMenu.BringToFront";
mostCurrent._lvmenu.BringToFront();
 //BA.debugLineNum = 318;BA.debugLine="Button4.SendToBack";
mostCurrent._button4.SendToBack();
 //BA.debugLineNum = 319;BA.debugLine="ProgressBar1.SendToBack";
mostCurrent._progressbar1.SendToBack();
 //BA.debugLineNum = 321;BA.debugLine="Button4.SetVisibleAnimated(150,False)";
mostCurrent._button4.SetVisibleAnimated((int) (150),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 322;BA.debugLine="Label1.SetLayout(40%x,40%y,40%x,90dip)";
mostCurrent._label1.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 323;BA.debugLine="Label2.SetVisibleAnimated(100,False)";
mostCurrent._label2.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 324;BA.debugLine="Label4.SetVisibleAnimated(100,False)";
mostCurrent._label4.SetVisibleAnimated((int) (100),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 325;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onmove(boolean _isopening) throws Exception{
 //BA.debugLineNum = 344;BA.debugLine="Sub Menu_onMove (IsOpening As Boolean)";
 //BA.debugLineNum = 345;BA.debugLine="If IsOpening=True Then";
if (_isopening==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 346;BA.debugLine="Button4.SetLayoutAnimated(100,1dip,50%y,5%x,180d";
mostCurrent._button4.SetLayoutAnimated((int) (100),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180)));
 //BA.debugLineNum = 347;BA.debugLine="Button4.SetVisibleAnimated(80,False)";
mostCurrent._button4.SetVisibleAnimated((int) (80),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 349;BA.debugLine="End Sub";
return "";
}
public static String  _minutes_hours(int _ms) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
 //BA.debugLineNum = 845;BA.debugLine="Sub minutes_hours ( ms As Int ) As String";
 //BA.debugLineNum = 846;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 847;BA.debugLine="val = ms";
_val = _ms;
 //BA.debugLineNum = 848;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 849;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 850;BA.debugLine="Return NumberFormat(hours, 1, 0) & \":\" & NumberFo";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_hours,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (2),(int) (0));
 //BA.debugLineNum = 851;BA.debugLine="End Sub";
return "";
}
public static String  _opt_click() throws Exception{
 //BA.debugLineNum = 591;BA.debugLine="Sub opt_click";
 //BA.debugLineNum = 592;BA.debugLine="StartActivity(settings)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._settings.getObject()));
 //BA.debugLineNum = 593;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 594;BA.debugLine="End Sub";
return "";
}
public static String  _panel2_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 619;BA.debugLine="Sub Panel2_Touch (Action As Int, X As Float, Y As";
 //BA.debugLineNum = 621;BA.debugLine="End Sub";
return "";
}
public static String  _pci_click() throws Exception{
 //BA.debugLineNum = 610;BA.debugLine="Sub pci_click";
 //BA.debugLineNum = 611;BA.debugLine="cd.AddView(panel3,-1,350)";
mostCurrent._cd.AddView((android.view.View)(mostCurrent._panel3.getObject()),(int) (-1),(int) (350));
 //BA.debugLineNum = 612;BA.debugLine="cd.Show(\"About Batt-Cat: \",\"\",\"Got It\",\"\",LoadBit";
mostCurrent._cd.Show("About Batt-Cat: ","","Got It","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Rss.png").getObject()));
 //BA.debugLineNum = 613;BA.debugLine="If Not (cd2.Response=DialogResponse.CANCEL) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._cd2.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL)) { 
 //BA.debugLineNum = 614;BA.debugLine="ct.ShowBitmap(\"©2017 SuloMedia™\",30,Gravity.BOTT";
mostCurrent._ct.ShowBitmap(BA.ObjectToCharSequence("©2017 SuloMedia™"),(int) (30),anywheresoftware.b4a.keywords.Common.Gravity.BOTTOM,(int) (0),(int) (0),(android.graphics.Bitmap)(mostCurrent._logo.getObject()));
 };
 //BA.debugLineNum = 616;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
klo._process_globals();
hw._process_globals();
starter._process_globals();
sys._process_globals();
xmlviewex._process_globals();
cool._process_globals();
setanimation._process_globals();
settings._process_globals();
statemanager._process_globals();
dbutils._process_globals();
charts._process_globals();
webhost._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 21;BA.debugLine="Dim pak As PackageManager";
_pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 23;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 24;BA.debugLine="Dim t1 As Timer";
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _readdir(String _folder,boolean _recursive) throws Exception{
anywheresoftware.b4a.objects.collections.List _lst = null;
int _i = 0;
String _v = "";
 //BA.debugLineNum = 686;BA.debugLine="Sub ReadDir(folder As String, recursive As Boolean";
 //BA.debugLineNum = 688;BA.debugLine="Dim lst As List = File.ListFiles(folder)";
_lst = new anywheresoftware.b4a.objects.collections.List();
_lst = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
 //BA.debugLineNum = 689;BA.debugLine="For i = 0 To lst.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_lst.getSize()-1);
for (_i = (int) (0) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 690;BA.debugLine="If File.IsDirectory(folder,lst.Get(i)) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,BA.ObjectToString(_lst.Get(_i)))) { 
 //BA.debugLineNum = 691;BA.debugLine="Dim v As String";
_v = "";
 //BA.debugLineNum = 692;BA.debugLine="v = folder&\"/\"&lst.Get(i)";
_v = _folder+"/"+BA.ObjectToString(_lst.Get(_i));
 //BA.debugLineNum = 694;BA.debugLine="ffolders.Add(v.SubString(root1.Length+1))";
mostCurrent._ffolders.Add((Object)(_v.substring((int) (mostCurrent._root1.length()+1))));
 //BA.debugLineNum = 695;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 696;BA.debugLine="ReadDir(v,recursive)";
_readdir(_v,_recursive);
 };
 }else {
 //BA.debugLineNum = 699;BA.debugLine="ffiles.Add(folder&\"/\"&lst.Get(i))";
mostCurrent._ffiles.Add((Object)(_folder+"/"+BA.ObjectToString(_lst.Get(_i))));
 };
 }
};
 //BA.debugLineNum = 703;BA.debugLine="End Sub";
return "";
}
public static String  _rl_lo() throws Exception{
 //BA.debugLineNum = 596;BA.debugLine="Sub rl_lo";
 //BA.debugLineNum = 597;BA.debugLine="cd2.Show(\"User App´s\",\"Check\",\"\",\"\",LoadBitmap(Fi";
mostCurrent._cd2.Show("User App´s","Check","","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Android.png").getObject()));
 //BA.debugLineNum = 598;BA.debugLine="If cd2.Response=DialogResponse.POSITIVE Then";
if (mostCurrent._cd2.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 599;BA.debugLine="ToastMessageShow(\"closed...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("closed..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 601;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 602;BA.debugLine="End Sub";
return "";
}
public static String  _ser_start() throws Exception{
 //BA.debugLineNum = 623;BA.debugLine="Sub ser_start";
 //BA.debugLineNum = 624;BA.debugLine="ProgressDialogShow(\"loading\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("loading"));
 //BA.debugLineNum = 625;BA.debugLine="End Sub";
return "";
}
public static String  _show_2() throws Exception{
 //BA.debugLineNum = 303;BA.debugLine="Sub show_2";
 //BA.debugLineNum = 304;BA.debugLine="slm.OpenSidebar";
mostCurrent._slm._opensidebar();
 //BA.debugLineNum = 305;BA.debugLine="showtip.setDelay(400)";
mostCurrent._showtip.setDelay((int) (400));
 //BA.debugLineNum = 306;BA.debugLine="showtip.setCircleColor(Colors.ARGB(195,255,255,25";
mostCurrent._showtip.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (195),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 307;BA.debugLine="showtip.setTitle(pak.GetApplicationLabel(\"com.bat";
mostCurrent._showtip.setTitle(_pak.GetApplicationLabel("com.batcat")).build();
 //BA.debugLineNum = 308;BA.debugLine="showtip.build.setDescription(\"Tippe hier um dein";
mostCurrent._showtip.build().setDescription("Tippe hier um dein Akku zu Boosten...");
 //BA.debugLineNum = 309;BA.debugLine="showtip.setTarget(Button4)";
mostCurrent._showtip.setTarget((android.view.View)(mostCurrent._button4.getObject()));
 //BA.debugLineNum = 310;BA.debugLine="showtip.show";
mostCurrent._showtip.show();
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static String  _show_tip() throws Exception{
 //BA.debugLineNum = 295;BA.debugLine="Sub show_tip";
 //BA.debugLineNum = 296;BA.debugLine="showtip.setDelay(400)";
mostCurrent._showtip.setDelay((int) (400));
 //BA.debugLineNum = 297;BA.debugLine="showtip.setCircleColor(Colors.ARGB(195,255,255,2";
mostCurrent._showtip.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (195),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 298;BA.debugLine="showtip.setTitle(pak.GetApplicationLabel(\"com.ba";
mostCurrent._showtip.setTitle(_pak.GetApplicationLabel("com.batcat")).build();
 //BA.debugLineNum = 299;BA.debugLine="showtip.build.setDescription(\"Swipe nach links z";
mostCurrent._showtip.build().setDescription("Swipe nach links zum öffnen und nach rechts zum schließen des Menü´s");
 //BA.debugLineNum = 300;BA.debugLine="showtip.setTarget(smlp).build";
mostCurrent._showtip.setTarget((android.view.View)(mostCurrent._smlp.getObject())).build();
 //BA.debugLineNum = 301;BA.debugLine="showtip.show";
mostCurrent._showtip.show();
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _slem_click() throws Exception{
 //BA.debugLineNum = 520;BA.debugLine="Sub slem_click";
 //BA.debugLineNum = 521;BA.debugLine="If slm.IsSidebarVisible Then";
if (mostCurrent._slm._issidebarvisible()) { 
 //BA.debugLineNum = 522;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 }else {
 //BA.debugLineNum = 525;BA.debugLine="slm.OpenSidebar";
mostCurrent._slm._opensidebar();
 };
 //BA.debugLineNum = 528;BA.debugLine="End Sub";
return "";
}
public static String  _slem1_click() throws Exception{
 //BA.debugLineNum = 516;BA.debugLine="Sub slem1_click";
 //BA.debugLineNum = 517;BA.debugLine="slem_click";
_slem_click();
 //BA.debugLineNum = 518;BA.debugLine="End Sub";
return "";
}
public static String  _st_click() throws Exception{
 //BA.debugLineNum = 579;BA.debugLine="Sub st_click";
 //BA.debugLineNum = 580;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 581;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 582;BA.debugLine="End Sub";
return "";
}
public static String  _stat_me() throws Exception{
 //BA.debugLineNum = 569;BA.debugLine="Sub stat_me";
 //BA.debugLineNum = 570;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 571;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 572;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 352;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 353;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 354;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 355;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 356;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 357;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 358;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 359;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 360;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 361;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 362;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 363;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 364;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 365;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 366;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 367;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 368;BA.debugLine="c16=mcl.md_light_blue_A700";
_c16 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 369;BA.debugLine="If kvs4sub.ContainsKey(\"off\") Then";
if (mostCurrent._kvs4sub._containskey("off")) { 
 //BA.debugLineNum = 370;BA.debugLine="StopService(Starter)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 };
 //BA.debugLineNum = 372;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 373;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 374;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 //BA.debugLineNum = 375;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 376;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 377;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 378;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 381;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 382;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 383;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 //BA.debugLineNum = 384;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 385;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 386;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 387;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 391;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 392;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 393;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 //BA.debugLineNum = 394;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 395;BA.debugLine="Label4.TextColor=Colors.Black";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 396;BA.debugLine="Label5.TextColor=Colors.Black";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 397;BA.debugLine="Label3.TextColor=Colors.Black";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 400;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 401;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 402;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 //BA.debugLineNum = 403;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 404;BA.debugLine="Label4.TextColor=Colors.Black";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 405;BA.debugLine="Label5.TextColor=Colors.Black";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 406;BA.debugLine="Label3.TextColor=Colors.Black";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 409;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 410;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 411;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 //BA.debugLineNum = 412;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 413;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 414;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 415;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 418;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 419;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 420;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 //BA.debugLineNum = 421;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 422;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 423;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 424;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 427;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 428;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 429;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 //BA.debugLineNum = 430;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 431;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 432;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 433;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 436;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 437;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 438;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 //BA.debugLineNum = 439;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 440;BA.debugLine="Label4.TextColor=Colors.Black";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 441;BA.debugLine="Label5.TextColor=Colors.Black";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 442;BA.debugLine="Label3.TextColor=Colors.Black";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 445;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 446;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 447;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 //BA.debugLineNum = 448;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 449;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 450;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 451;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 454;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 455;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 456;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 //BA.debugLineNum = 457;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 458;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 459;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 460;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 463;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 464;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 465;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 //BA.debugLineNum = 466;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 467;BA.debugLine="apm.FinishedStrokeColor=Colors.ARGB(200,255,255,";
mostCurrent._apm.setFinishedStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 468;BA.debugLine="apm.UnfinishedStrokeColor=Colors.ARGB(100,255,25";
mostCurrent._apm.setUnfinishedStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 469;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 470;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 471;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 474;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 475;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 476;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 //BA.debugLineNum = 477;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 478;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 479;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 480;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 483;BA.debugLine="panel3.Color=Colors.ARGB(100,255,255,255)";
mostCurrent._panel3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 };
 //BA.debugLineNum = 485;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 486;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 487;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 //BA.debugLineNum = 488;BA.debugLine="apm.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 489;BA.debugLine="Label4.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 490;BA.debugLine="Label5.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 491;BA.debugLine="Label3.TextColor=Colors.ARGB(200,255,255,255)";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 492;BA.debugLine="panel3.Color=Colors.ARGB(100,0,0,0)";
mostCurrent._panel3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (0),(int) (0),(int) (0)));
 };
 //BA.debugLineNum = 495;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 496;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 497;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 //BA.debugLineNum = 498;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 499;BA.debugLine="Label4.TextColor=Colors.Black";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 500;BA.debugLine="Label5.TextColor=Colors.Black";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 501;BA.debugLine="Label3.TextColor=Colors.Black";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 504;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 505;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 506;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 //BA.debugLineNum = 507;BA.debugLine="apm.TextColor=Colors.Black";
mostCurrent._apm.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 508;BA.debugLine="Label4.TextColor=Colors.Black";
mostCurrent._label4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 509;BA.debugLine="Label5.TextColor=Colors.Black";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 510;BA.debugLine="Label3.TextColor=Colors.Black";
mostCurrent._label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 };
 //BA.debugLineNum = 513;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 514;BA.debugLine="End Sub";
return "";
}
public static String  _sy_click() throws Exception{
 //BA.debugLineNum = 574;BA.debugLine="Sub sy_click";
 //BA.debugLineNum = 575;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 576;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 577;BA.debugLine="End Sub";
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
