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

public class klo extends Activity implements B4AActivity{
	public static klo mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.klo");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (klo).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.klo");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.klo", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (klo) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (klo) Resume **");
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
		return klo.class;
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
        BA.LogInfo("** Activity (klo) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (klo) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _sql = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.collections.Map _m = null;
public static String _time2 = "";
public com.batcat.batut _bat = null;
public static String _volt = "";
public static String _temp = "";
public static String _usb = "";
public static String _ac = "";
public anywheresoftware.b4a.phone.PackageManagerWrapper _pak1 = null;
public com.batcat.keyvaluestore _kvs2 = null;
public com.batcat.keyvaluestore _kvs3 = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.keyvaluestore _kvsvolt = null;
public com.batcat.keyvaluestore _kvstemp = null;
public anywheresoftware.b4a.objects.collections.List _dt = null;
public anywheresoftware.b4a.objects.collections.List _kl = null;
public anywheresoftware.b4a.objects.collections.List _suc = null;
public com.batcat.charts._graph _g = null;
public com.batcat.charts._graph _g2 = null;
public com.batcat.charts._linepoint _ls = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _batt = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _pl = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _pk = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public de.amberhome.objects.appcompat.ACPopupMenuWrapper _popa = null;
public de.amberhome.objects.appcompat.ACButtonWrapper _acbutton1 = null;
public static int _level = 0;
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
public anywheresoftware.b4a.phone.PhoneEvents _dev = null;
public de.amberhome.objects.appcompat.ACButtonWrapper _acbutton2 = null;
public b4a.example.osstats _osstat = null;
public com.maximussoft.msos.MSOS _metric = null;
public mpandroidchartwrapper.multiBubbleChartWrapper _multibubblechart1 = null;
public mpandroidchartwrapper.barChartWrapper _mbc1 = null;
public mpandroidchartwrapper.lineChartWrapper _mlc1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel3 = null;
public static String _fn2 = "";
public static int _fg2 = 0;
public com.batcat.charts._bardata _ld2 = null;
public static String _fn = "";
public static int _fg = 0;
public com.batcat.charts._linedata _ld = null;
public com.simplysoftware.slidingmenustandard.slidingmenustd _sm = null;
public com.circlebuttonwrapper.CircleButtonWrapper _cb1 = null;
public com.batcat.main _main = null;
public com.batcat.set2 _set2 = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.pman _pman = null;
public com.batcat.wait _wait = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.charts _charts = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _acbutton1_click() throws Exception{
 //BA.debugLineNum = 319;BA.debugLine="Sub ACButton1_Click";
 //BA.debugLineNum = 320;BA.debugLine="graph_clear";
_graph_clear();
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public static String  _acbutton2_click() throws Exception{
 //BA.debugLineNum = 449;BA.debugLine="Sub ACButton2_Click";
 //BA.debugLineNum = 450;BA.debugLine="ccl_click";
_ccl_click();
 //BA.debugLineNum = 451;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd1 = null;
 //BA.debugLineNum = 66;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 68;BA.debugLine="Activity.LoadLayout(\"2\")";
mostCurrent._activity.LoadLayout("2",mostCurrent.activityBA);
 //BA.debugLineNum = 69;BA.debugLine="Activity.Title=pak1.GetApplicationLabel(\"com.batc";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(mostCurrent._pak1.GetApplicationLabel("com.batcat")+" - "+mostCurrent._pak1.GetVersionName("com.batcat")));
 //BA.debugLineNum = 70;BA.debugLine="Activity.AddMenuItem(\"Clear Stats\",\"ccl\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Clear Stats"),"ccl");
 //BA.debugLineNum = 71;BA.debugLine="Activity.AddMenuItem(\"Exit\",\"cl\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Exit"),"cl");
 //BA.debugLineNum = 72;BA.debugLine="suc.Initialize";
mostCurrent._suc.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 74;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 75;BA.debugLine="l3.Initialize(\"l3\")";
mostCurrent._l3.Initialize(mostCurrent.activityBA,"l3");
 //BA.debugLineNum = 77;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 78;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 79;BA.debugLine="c3=mcl.md_lime_A700";
_c3 = mostCurrent._mcl.getmd_lime_A700();
 //BA.debugLineNum = 80;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 81;BA.debugLine="Activity.Color=mcl.md_white_1000";
mostCurrent._activity.setColor(mostCurrent._mcl.getmd_white_1000());
 //BA.debugLineNum = 82;BA.debugLine="dev.Initialize(\"dev\")";
mostCurrent._dev.Initialize(processBA,"dev");
 //BA.debugLineNum = 85;BA.debugLine="m.Initialize";
mostCurrent._m.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="bat.Initialize";
mostCurrent._bat._initialize(processBA);
 //BA.debugLineNum = 88;BA.debugLine="kl.Initialize";
mostCurrent._kl.Initialize();
 //BA.debugLineNum = 89;BA.debugLine="dt.Initialize";
mostCurrent._dt.Initialize();
 //BA.debugLineNum = 90;BA.debugLine="ls.Initialize";
mostCurrent._ls.Initialize();
 //BA.debugLineNum = 91;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 92;BA.debugLine="temp=bat.BatteryInformation(6)/10";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 93;BA.debugLine="usb =bat.BatteryInformation(9)";
mostCurrent._usb = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (9)]);
 //BA.debugLineNum = 94;BA.debugLine="ac =bat.BatteryInformation(8)";
mostCurrent._ac = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (8)]);
 //BA.debugLineNum = 95;BA.debugLine="G.Initialize";
mostCurrent._g.Initialize();
 //BA.debugLineNum = 96;BA.debugLine="g2.Initialize";
mostCurrent._g2.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="If FirstTime=True Then";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 98;BA.debugLine="Msgbox(\"Wenn die Statistik zum ersten Mal gelade";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Wenn die Statistik zum ersten Mal geladen wird, kann es eine Weile dauern, bis die Werte korrekt angezeigt werden, zB: V,C°,% der letzten 10 Einträge. Bitte beachte das die Werte nur ca sind da sie immer leicht Zeit versetzt gespeichert werden und nicht  den 'Live' Zustand anzeigen!"),BA.ObjectToCharSequence("Wichtig!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 100;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 101;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 102;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 103;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 104;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 //BA.debugLineNum = 105;BA.debugLine="time2=DateTime.Time(DateTime.Now)";
mostCurrent._time2 = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 106;BA.debugLine="batt=LoadBitmap(File.DirAssets,\"Battery Icons - W";
mostCurrent._batt = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - White 64px (40).png");
 //BA.debugLineNum = 107;BA.debugLine="pl=LoadBitmap(File.DirAssets,\"Battery Icons - Whi";
mostCurrent._pl = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - White 64px (28).png");
 //BA.debugLineNum = 109;BA.debugLine="popa.Initialize(\"popa\",Panel3)";
mostCurrent._popa.Initialize(mostCurrent.activityBA,"popa",(android.view.View)(mostCurrent._panel3.getObject()));
 //BA.debugLineNum = 110;BA.debugLine="Dim bd,bd1 As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bd1 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 111;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets,\"ic_clear";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_clear_black_48dp.png").getObject()));
 //BA.debugLineNum = 112;BA.debugLine="bd1.Initialize(LoadBitmap(File.DirAssets,\"ic_auto";
_bd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_autorenew_black_48dp.png").getObject()));
 //BA.debugLineNum = 113;BA.debugLine="popa.AddMenuItem(0,\"Reload Stats\",bd1)";
mostCurrent._popa.AddMenuItem((int) (0),BA.ObjectToCharSequence("Reload Stats"),(android.graphics.drawable.Drawable)(_bd1.getObject()));
 //BA.debugLineNum = 114;BA.debugLine="popa.AddMenuItem(1,\"Schließen\",bd)";
mostCurrent._popa.AddMenuItem((int) (1),BA.ObjectToCharSequence("Schließen"),(android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 118;BA.debugLine="cb1.ButtonColor=mcl.md_lime_A200";
mostCurrent._cb1.setButtonColor(mostCurrent._mcl.getmd_lime_A200());
 //BA.debugLineNum = 119;BA.debugLine="cb1.ImageBitmap=LoadBitmap(File.DirAssets,\"Bar-ch";
mostCurrent._cb1.setImageBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Bar-chart48.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="build";
_build();
 //BA.debugLineNum = 122;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 123;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 304;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 305;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 306;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 307;BA.debugLine="ToastMessageShow(\"BCT - Backround  Statistic sta";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BCT - Backround  Statistic started.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 308;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\"";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 310;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 128;BA.debugLine="build";
_build();
 //BA.debugLineNum = 129;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 130;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
public static String  _build() throws Exception{
anywheresoftware.b4a.objects.ListViewWrapper _lv = null;
anywheresoftware.b4a.objects.LabelWrapper _lva = null;
anywheresoftware.b4a.objects.LabelWrapper _lvb = null;
 //BA.debugLineNum = 151;BA.debugLine="Sub build";
 //BA.debugLineNum = 152;BA.debugLine="Dim lv As ListView";
_lv = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 153;BA.debugLine="lv.Initialize(\"lv\")";
_lv.Initialize(mostCurrent.activityBA,"lv");
 //BA.debugLineNum = 154;BA.debugLine="Dim lva,lvb As Label";
_lva = new anywheresoftware.b4a.objects.LabelWrapper();
_lvb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 155;BA.debugLine="lva.Initialize(\"lva\")";
_lva.Initialize(mostCurrent.activityBA,"lva");
 //BA.debugLineNum = 156;BA.debugLine="lvb.Initialize(\"lvb\")";
_lvb.Initialize(mostCurrent.activityBA,"lvb");
 //BA.debugLineNum = 157;BA.debugLine="lva=lv.TwoLinesAndBitmap.Label";
_lva = _lv.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 158;BA.debugLine="lvb=lv.TwoLinesAndBitmap.SecondLabel";
_lvb = _lv.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 159;BA.debugLine="lva.TextSize=13";
_lva.setTextSize((float) (13));
 //BA.debugLineNum = 160;BA.debugLine="lva.TextColor=mcl.md_black_1000";
_lva.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 161;BA.debugLine="lvb.TextSize=11";
_lvb.setTextSize((float) (11));
 //BA.debugLineNum = 162;BA.debugLine="lvb.TextColor=mcl.md_grey_700";
_lvb.setTextColor(mostCurrent._mcl.getmd_grey_700());
 //BA.debugLineNum = 163;BA.debugLine="lv.TwoLinesAndBitmap.ImageView.Height=40dip";
_lv.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 164;BA.debugLine="lv.TwoLinesAndBitmap.ImageView.Width=40dip";
_lv.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 165;BA.debugLine="lv.TwoLinesAndBitmap.ItemHeight=60dip";
_lv.getTwoLinesAndBitmap().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 166;BA.debugLine="lv.Enabled=True";
_lv.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 167;BA.debugLine="Panel3.AddView(lv,0dip,0dip,60%x,100%y)";
mostCurrent._panel3.AddView((android.view.View)(_lv.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 168;BA.debugLine="lv.AddTwoLinesAndBitmap2(\"Reset C°\",\"setzt alle W";
_lv.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Reset C°"),BA.ObjectToCharSequence("setzt alle Werte wieder auf 0 und löscht die Temperatur Ansicht"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_data_usage_black_48dp.png").getObject()),(Object)(0));
 //BA.debugLineNum = 169;BA.debugLine="lv.AddTwoLinesAndBitmap2(\"Reset Level\",\"setzt all";
_lv.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Reset Level"),BA.ObjectToCharSequence("setzt alle Werte wieder auf 0 und löscht die Batterie-Level Ansicht"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_battery_alert_black_48dp.png").getObject()),(Object)(1));
 //BA.debugLineNum = 170;BA.debugLine="lv.AddTwoLinesAndBitmap2(\"OS Power\",\"Android Powe";
_lv.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("OS Power"),BA.ObjectToCharSequence("Android Power Menü"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()),(Object)(2));
 //BA.debugLineNum = 171;BA.debugLine="lv.AddTwoLinesAndBitmap2(\"Close\",\"zurück zum Haup";
_lv.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Close"),BA.ObjectToCharSequence("zurück zum Hauptmenü"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_clear_black_48dp.png").getObject()),(Object)(3));
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _c_start() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Sub c_start";
 //BA.debugLineNum = 224;BA.debugLine="If kvs2.IsInitialized Then";
if (mostCurrent._kvs2.IsInitialized()) { 
 //BA.debugLineNum = 225;BA.debugLine="Log(\"KVS -> true\")";
anywheresoftware.b4a.keywords.Common.Log("KVS -> true");
 }else {
 //BA.debugLineNum = 227;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datast";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 228;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datast";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 229;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"dat";
mostCurrent._kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 230;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"dat";
mostCurrent._kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 };
 //BA.debugLineNum = 233;BA.debugLine="chart_2";
_chart_2();
 //BA.debugLineNum = 234;BA.debugLine="chart_start";
_chart_start();
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _cb1_click() throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub cb1_Click";
 //BA.debugLineNum = 137;BA.debugLine="If Not (Panel3.Visible=True) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._panel3.getVisible()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 139;BA.debugLine="Panel2.SendToBack";
mostCurrent._panel2.SendToBack();
 //BA.debugLineNum = 140;BA.debugLine="Panel1.SendToBack";
mostCurrent._panel1.SendToBack();
 //BA.debugLineNum = 141;BA.debugLine="Panel2.SetVisibleAnimated(550,False)";
mostCurrent._panel2.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="Panel1.SetVisibleAnimated(550,False)";
mostCurrent._panel1.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="Panel3.SetVisibleAnimated(550,True)";
mostCurrent._panel3.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 145;BA.debugLine="Panel2.SetVisibleAnimated(550,True)";
mostCurrent._panel2.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="Panel1.SetVisibleAnimated(550,True)";
mostCurrent._panel1.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="Panel3.SetVisibleAnimated(550,False)";
mostCurrent._panel3.SetVisibleAnimated((int) (550),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static String  _ccl_click() throws Exception{
 //BA.debugLineNum = 205;BA.debugLine="Sub ccl_click";
 //BA.debugLineNum = 206;BA.debugLine="kvstemp.DeleteAll";
mostCurrent._kvstemp._deleteall();
 //BA.debugLineNum = 207;BA.debugLine="kvsvolt.DeleteAll";
mostCurrent._kvsvolt._deleteall();
 //BA.debugLineNum = 208;BA.debugLine="kvstemp.PutSimple(bat.BatteryInformation(6)/10,ti";
mostCurrent._kvstemp._putsimple(BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10),(Object)(mostCurrent._time2));
 //BA.debugLineNum = 209;BA.debugLine="ToastMessageShow(\"warte auf Aktuelle werte..!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("warte auf Aktuelle werte..!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 210;BA.debugLine="chart_2";
_chart_2();
 //BA.debugLineNum = 211;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _chart_2() throws Exception{
Object _h = null;
 //BA.debugLineNum = 273;BA.debugLine="Sub chart_2";
 //BA.debugLineNum = 275;BA.debugLine="If LD2.IsInitialized Then";
if (mostCurrent._ld2.IsInitialized) { 
 }else {
 //BA.debugLineNum = 277;BA.debugLine="LD2.Initialize";
mostCurrent._ld2.Initialize();
 };
 //BA.debugLineNum = 279;BA.debugLine="LD2.Target =Panel1";
mostCurrent._ld2.Target = mostCurrent._panel1;
 //BA.debugLineNum = 280;BA.debugLine="LD2.BarsWidth=10";
mostCurrent._ld2.BarsWidth = (int) (10);
 //BA.debugLineNum = 283;BA.debugLine="Charts.AddBarColor(LD2, mcl.md_lime_A200) 'Second";
mostCurrent._charts._addbarcolor(mostCurrent.activityBA,mostCurrent._ld2,mostCurrent._mcl.getmd_lime_A200());
 //BA.debugLineNum = 284;BA.debugLine="For Each h Step 4 As String  In kvstemp.ListKeys";
final anywheresoftware.b4a.BA.IterableList group8 = mostCurrent._kvstemp._listkeys();
final int groupLen8 = group8.getSize();
for (int index8 = 0;index8 < groupLen8 ;index8++){
_h = group8.Get(index8);
 //BA.debugLineNum = 285;BA.debugLine="fg2=h";
_fg2 = (int)(BA.ObjectToNumber(_h));
 //BA.debugLineNum = 286;BA.debugLine="fn2=kvstemp.GetSimple(h)";
mostCurrent._fn2 = mostCurrent._kvstemp._getsimple(BA.ObjectToString(_h));
 //BA.debugLineNum = 287;BA.debugLine="Log(\"Bar Key-> \"&h)";
anywheresoftware.b4a.keywords.Common.Log("Bar Key-> "+BA.ObjectToString(_h));
 //BA.debugLineNum = 289;BA.debugLine="Charts.AddBarPoint(LD2, fn2,Array As Float(fg2))";
mostCurrent._charts._addbarpoint(mostCurrent.activityBA,mostCurrent._ld2,mostCurrent._fn2,new float[]{(float) (_fg2)});
 }
;
 //BA.debugLineNum = 292;BA.debugLine="g2.Title = \"Temp. in C°\"";
mostCurrent._g2.Title = "Temp. in C°";
 //BA.debugLineNum = 293;BA.debugLine="g2.XAxis = \"\"";
mostCurrent._g2.XAxis = "";
 //BA.debugLineNum = 294;BA.debugLine="g2.YAxis = \"Temperature\"";
mostCurrent._g2.YAxis = "Temperature";
 //BA.debugLineNum = 295;BA.debugLine="g2.YStart = 10";
mostCurrent._g2.YStart = (float) (10);
 //BA.debugLineNum = 296;BA.debugLine="g2.YEnd = 60";
mostCurrent._g2.YEnd = (float) (60);
 //BA.debugLineNum = 297;BA.debugLine="g2.YInterval = 5";
mostCurrent._g2.YInterval = (float) (5);
 //BA.debugLineNum = 298;BA.debugLine="g2.AxisColor = Colors.White";
mostCurrent._g2.AxisColor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 300;BA.debugLine="Charts.DrawBarsChart(g2, LD2, Colors.Transparent)";
mostCurrent._charts._drawbarschart(mostCurrent.activityBA,mostCurrent._g2,mostCurrent._ld2,anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _chart_start() throws Exception{
String _h = "";
 //BA.debugLineNum = 238;BA.debugLine="Sub chart_start";
 //BA.debugLineNum = 239;BA.debugLine="If LD.IsInitialized Then";
if (mostCurrent._ld.IsInitialized) { 
 }else {
 //BA.debugLineNum = 241;BA.debugLine="LD.Initialize";
mostCurrent._ld.Initialize();
 };
 //BA.debugLineNum = 243;BA.debugLine="LD.Target =Panel2";
mostCurrent._ld.Target = mostCurrent._panel2;
 //BA.debugLineNum = 244;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 245;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 247;BA.debugLine="Charts.AddLineColor(LD, Colors.Blue) 'First line";
mostCurrent._charts._addlinecolor(mostCurrent.activityBA,mostCurrent._ld,anywheresoftware.b4a.keywords.Common.Colors.Blue);
 }else {
 //BA.debugLineNum = 249;BA.debugLine="Charts.AddLineColor(LD, Colors.Red) 'First line";
mostCurrent._charts._addlinecolor(mostCurrent.activityBA,mostCurrent._ld,anywheresoftware.b4a.keywords.Common.Colors.Red);
 };
 //BA.debugLineNum = 253;BA.debugLine="For Each h As String  In kvs2.ListKeys";
final anywheresoftware.b4a.BA.IterableList group12 = mostCurrent._kvs2._listkeys();
final int groupLen12 = group12.getSize();
for (int index12 = 0;index12 < groupLen12 ;index12++){
_h = BA.ObjectToString(group12.Get(index12));
 //BA.debugLineNum = 254;BA.debugLine="fg=h";
_fg = (int)(Double.parseDouble(_h));
 //BA.debugLineNum = 255;BA.debugLine="fn=kvs2.GetSimple(h)";
mostCurrent._fn = mostCurrent._kvs2._getsimple(_h);
 //BA.debugLineNum = 256;BA.debugLine="Log(\"Map Key-> \"&fg)";
anywheresoftware.b4a.keywords.Common.Log("Map Key-> "+BA.NumberToString(_fg));
 //BA.debugLineNum = 258;BA.debugLine="Charts.AddLinePoint(LD, fn,fg, True)";
mostCurrent._charts._addlinepoint(mostCurrent.activityBA,mostCurrent._ld,mostCurrent._fn,(float) (_fg),anywheresoftware.b4a.keywords.Common.True);
 }
;
 //BA.debugLineNum = 261;BA.debugLine="G.Title = \"Level in %\"";
mostCurrent._g.Title = "Level in %";
 //BA.debugLineNum = 262;BA.debugLine="G.XAxis = \"<- Battery Stats ->\"";
mostCurrent._g.XAxis = "<- Battery Stats ->";
 //BA.debugLineNum = 263;BA.debugLine="G.YAxis = \"Level\"";
mostCurrent._g.YAxis = "Level";
 //BA.debugLineNum = 264;BA.debugLine="G.YStart = 0";
mostCurrent._g.YStart = (float) (0);
 //BA.debugLineNum = 265;BA.debugLine="G.YEnd = 100";
mostCurrent._g.YEnd = (float) (100);
 //BA.debugLineNum = 266;BA.debugLine="G.YInterval = 5";
mostCurrent._g.YInterval = (float) (5);
 //BA.debugLineNum = 267;BA.debugLine="G.AxisColor = Colors.White";
mostCurrent._g.AxisColor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 269;BA.debugLine="Charts.DrawLineChart(G, LD, Colors.Transparent)";
mostCurrent._charts._drawlinechart(mostCurrent.activityBA,mostCurrent._g,mostCurrent._ld,anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
return "";
}
public static String  _db_update() throws Exception{
 //BA.debugLineNum = 313;BA.debugLine="Sub db_update";
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static String  _dev_batterychanged(int _level1,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
anywheresoftware.b4a.objects.collections.List _vl = null;
int _v = 0;
 //BA.debugLineNum = 324;BA.debugLine="Sub dev_BatteryChanged (level1 As Int, Scale As In";
 //BA.debugLineNum = 325;BA.debugLine="Dim vl As List";
_vl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 326;BA.debugLine="vl.Initialize";
_vl.Initialize();
 //BA.debugLineNum = 327;BA.debugLine="For v = 0 To Scale Step 2";
{
final int step3 = (int) (2);
final int limit3 = _scale;
for (_v = (int) (0) ; (step3 > 0 && _v <= limit3) || (step3 < 0 && _v >= limit3); _v = ((int)(0 + _v + step3)) ) {
 //BA.debugLineNum = 328;BA.debugLine="vl.Add(v)";
_vl.Add((Object)(_v));
 //BA.debugLineNum = 329;BA.debugLine="If level1=v Then";
if (_level1==_v) { 
 //BA.debugLineNum = 330;BA.debugLine="store_check";
_store_check();
 };
 }
};
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 22;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim m As Map";
mostCurrent._m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 29;BA.debugLine="Dim time2 As String";
mostCurrent._time2 = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim bat As Batut";
mostCurrent._bat = new com.batcat.batut();
 //BA.debugLineNum = 31;BA.debugLine="Dim volt,temp,usb,ac As String";
mostCurrent._volt = "";
mostCurrent._temp = "";
mostCurrent._usb = "";
mostCurrent._ac = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim pak1 As PackageManager";
mostCurrent._pak1 = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim kvs2,kvs3,kvs4,kvsvolt,kvstemp As KeyValueSto";
mostCurrent._kvs2 = new com.batcat.keyvaluestore();
mostCurrent._kvs3 = new com.batcat.keyvaluestore();
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvsvolt = new com.batcat.keyvaluestore();
mostCurrent._kvstemp = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 35;BA.debugLine="Dim dt As List";
mostCurrent._dt = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 36;BA.debugLine="Dim kl As List";
mostCurrent._kl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 37;BA.debugLine="Dim suc As List";
mostCurrent._suc = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 38;BA.debugLine="Dim g,g2 As Graph";
mostCurrent._g = new com.batcat.charts._graph();
mostCurrent._g2 = new com.batcat.charts._graph();
 //BA.debugLineNum = 39;BA.debugLine="Dim ls As LinePoint";
mostCurrent._ls = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 40;BA.debugLine="Dim batt,pl,pk As Bitmap";
mostCurrent._batt = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._pl = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._pk = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 42;BA.debugLine="Dim popa As ACPopupMenu";
mostCurrent._popa = new de.amberhome.objects.appcompat.ACPopupMenuWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private ACButton1 As ACButton";
mostCurrent._acbutton1 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim level As Int";
_level = 0;
 //BA.debugLineNum = 45;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
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
 //BA.debugLineNum = 46;BA.debugLine="Private dev As PhoneEvents";
mostCurrent._dev = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 48;BA.debugLine="Private ACButton2 As ACButton";
mostCurrent._acbutton2 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Dim osstat As OSStats";
mostCurrent._osstat = new b4a.example.osstats();
 //BA.debugLineNum = 50;BA.debugLine="Dim metric As MSOS";
mostCurrent._metric = new com.maximussoft.msos.MSOS();
 //BA.debugLineNum = 52;BA.debugLine="Private MultiBubbleChart1 As MultiBubbleChart";
mostCurrent._multibubblechart1 = new mpandroidchartwrapper.multiBubbleChartWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private mbc1 As BarChart";
mostCurrent._mbc1 = new mpandroidchartwrapper.barChartWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private mlc1 As LineChart";
mostCurrent._mlc1 = new mpandroidchartwrapper.lineChartWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private Panel3 As Panel";
mostCurrent._panel3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Dim fn2 As String";
mostCurrent._fn2 = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim fg2 As Int";
_fg2 = 0;
 //BA.debugLineNum = 58;BA.debugLine="Dim LD2 As BarData";
mostCurrent._ld2 = new com.batcat.charts._bardata();
 //BA.debugLineNum = 59;BA.debugLine="Dim fn As String";
mostCurrent._fn = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim fg As Int";
_fg = 0;
 //BA.debugLineNum = 61;BA.debugLine="Dim LD As LineData";
mostCurrent._ld = new com.batcat.charts._linedata();
 //BA.debugLineNum = 62;BA.debugLine="Private sm As SlidingMenuStd";
mostCurrent._sm = new com.simplysoftware.slidingmenustandard.slidingmenustd();
 //BA.debugLineNum = 63;BA.debugLine="Private cb1 As Circlebutton";
mostCurrent._cb1 = new com.circlebuttonwrapper.CircleButtonWrapper();
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _graph_clear() throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub graph_clear";
 //BA.debugLineNum = 216;BA.debugLine="kvs2.DeleteAll";
mostCurrent._kvs2._deleteall();
 //BA.debugLineNum = 217;BA.debugLine="kvs2.PutSimple(bat.BatteryInformation(0),time2)";
mostCurrent._kvs2._putsimple(BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (0)]),(Object)(mostCurrent._time2));
 //BA.debugLineNum = 218;BA.debugLine="ToastMessageShow(\"warte auf Aktuelle werte..!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("warte auf Aktuelle werte..!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 219;BA.debugLine="chart_start";
_chart_start();
 //BA.debugLineNum = 220;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public static String  _lv_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _ipo = null;
 //BA.debugLineNum = 174;BA.debugLine="Sub lv_ItemClick (Position As Int, Value As Object";
 //BA.debugLineNum = 175;BA.debugLine="Dim ipo As Intent";
_ipo = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 176;BA.debugLine="If Value=0 Then";
if ((_value).equals((Object)(0))) { 
 //BA.debugLineNum = 177;BA.debugLine="ccl_click";
_ccl_click();
 //BA.debugLineNum = 178;BA.debugLine="cb1_Click";
_cb1_click();
 //BA.debugLineNum = 179;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 181;BA.debugLine="If Value=1 Then";
if ((_value).equals((Object)(1))) { 
 //BA.debugLineNum = 182;BA.debugLine="graph_clear";
_graph_clear();
 //BA.debugLineNum = 183;BA.debugLine="cb1_Click";
_cb1_click();
 //BA.debugLineNum = 184;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 186;BA.debugLine="If Value=2 Then";
if ((_value).equals((Object)(2))) { 
 //BA.debugLineNum = 187;BA.debugLine="ipo.Initialize( \"android.intent.action.POWER_USA";
_ipo.Initialize("android.intent.action.POWER_USAGE_SUMMARY","");
 //BA.debugLineNum = 188;BA.debugLine="cb1_Click";
_cb1_click();
 //BA.debugLineNum = 189;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 191;BA.debugLine="If Value= 3 Then";
if ((_value).equals((Object)(3))) { 
 //BA.debugLineNum = 192;BA.debugLine="Activity_KeyPress(KeyCodes.KEYCODE_BACK)";
_activity_keypress(anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK);
 };
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static String  _mlc1_value_selected(int _index,float _value) throws Exception{
 //BA.debugLineNum = 456;BA.debugLine="Sub mlc1_value_selected(index As Int, value As Flo";
 //BA.debugLineNum = 458;BA.debugLine="End Sub";
return "";
}
public static String  _popa_itemclicked(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 196;BA.debugLine="Sub popa_ItemClicked (Item As ACMenuItem)";
 //BA.debugLineNum = 197;BA.debugLine="If Item = popa.GetItem(0) Then";
if ((_item).equals((android.view.MenuItem)(mostCurrent._popa.GetItem((int) (0))))) { 
 //BA.debugLineNum = 198;BA.debugLine="ACButton1_Click";
_acbutton1_click();
 };
 //BA.debugLineNum = 200;BA.debugLine="If Item = popa.GetItem(1) Then";
if ((_item).equals((android.view.MenuItem)(mostCurrent._popa.GetItem((int) (1))))) { 
 //BA.debugLineNum = 201;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 338;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 339;BA.debugLine="c1=mcl.md_light_blue_A400";
_c1 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 340;BA.debugLine="c2=mcl.md_amber_A400";
_c2 = mostCurrent._mcl.getmd_amber_A400();
 //BA.debugLineNum = 341;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 342;BA.debugLine="c4=mcl.md_teal_A400";
_c4 = mostCurrent._mcl.getmd_teal_A400();
 //BA.debugLineNum = 343;BA.debugLine="c5=mcl.md_deep_purple_A400";
_c5 = mostCurrent._mcl.getmd_deep_purple_A400();
 //BA.debugLineNum = 344;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 345;BA.debugLine="c7=mcl.md_indigo_A400";
_c7 = mostCurrent._mcl.getmd_indigo_A400();
 //BA.debugLineNum = 346;BA.debugLine="c8=mcl.md_blue_A400";
_c8 = mostCurrent._mcl.getmd_blue_A400();
 //BA.debugLineNum = 347;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 348;BA.debugLine="c10=mcl.md_grey_600";
_c10 = mostCurrent._mcl.getmd_grey_600();
 //BA.debugLineNum = 349;BA.debugLine="c11=mcl.md_green_A400";
_c11 = mostCurrent._mcl.getmd_green_A400();
 //BA.debugLineNum = 350;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 351;BA.debugLine="c13=mcl.md_light_green_A400";
_c13 = mostCurrent._mcl.getmd_light_green_A400();
 //BA.debugLineNum = 352;BA.debugLine="c14=mcl.md_cyan_A400";
_c14 = mostCurrent._mcl.getmd_cyan_A400();
 //BA.debugLineNum = 353;BA.debugLine="c15=mcl.md_blue_grey_400";
_c15 = mostCurrent._mcl.getmd_blue_grey_400();
 //BA.debugLineNum = 354;BA.debugLine="c16=mcl.md_light_blue_A400";
_c16 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 355;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 356;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 357;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 359;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 360;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 361;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 }else {
 };
 //BA.debugLineNum = 365;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 366;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 367;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 //BA.debugLineNum = 368;BA.debugLine="g.AxisColor = Colors.Black";
mostCurrent._g.AxisColor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 369;BA.debugLine="g2.AxisColor = Colors.Black";
mostCurrent._g2.AxisColor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 }else {
 };
 //BA.debugLineNum = 373;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 374;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 375;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 };
 //BA.debugLineNum = 379;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 380;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 381;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 }else {
 };
 //BA.debugLineNum = 385;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 386;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 387;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 }else {
 };
 //BA.debugLineNum = 392;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 393;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 394;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 }else {
 };
 //BA.debugLineNum = 398;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 399;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 400;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 }else {
 };
 //BA.debugLineNum = 404;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 405;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 406;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 }else {
 };
 //BA.debugLineNum = 410;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 411;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 412;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 }else {
 };
 //BA.debugLineNum = 416;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 417;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 418;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 }else {
 };
 //BA.debugLineNum = 422;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 423;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 424;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 }else {
 };
 //BA.debugLineNum = 428;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 429;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 430;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 }else {
 };
 //BA.debugLineNum = 434;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 435;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 436;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 }else {
 };
 //BA.debugLineNum = 440;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 441;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 442;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 }else {
 };
 //BA.debugLineNum = 446;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 447;BA.debugLine="End Sub";
return "";
}
}
