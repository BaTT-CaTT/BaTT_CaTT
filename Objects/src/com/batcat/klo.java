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
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
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
public mpandroidchartwrapper.pieViewWrapper _mpc1 = null;
public de.amberhome.objects.appcompat.ACButtonWrapper _acbutton2 = null;
public b4a.example.osstats _osstat = null;
public com.maximussoft.msos.MSOS _metric = null;
public com.batcat.main _main = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _acbutton1_click() throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub ACButton1_Click";
 //BA.debugLineNum = 245;BA.debugLine="graph_clear";
_graph_clear();
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _acbutton2_click() throws Exception{
 //BA.debugLineNum = 371;BA.debugLine="Sub ACButton2_Click";
 //BA.debugLineNum = 372;BA.debugLine="ccl_click";
_ccl_click();
 //BA.debugLineNum = 373;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _la = null;
anywheresoftware.b4a.objects.LabelWrapper _la1 = null;
anywheresoftware.b4a.objects.LabelWrapper _la2 = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd1 = null;
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 52;BA.debugLine="Activity.LoadLayout(\"2\")";
mostCurrent._activity.LoadLayout("2",mostCurrent.activityBA);
 //BA.debugLineNum = 53;BA.debugLine="Activity.Title=pak1.GetApplicationLabel(\"com.batc";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(mostCurrent._pak1.GetApplicationLabel("com.batcat")+" - "+mostCurrent._pak1.GetVersionName("com.batcat")));
 //BA.debugLineNum = 54;BA.debugLine="Activity.AddMenuItem(\"Clear Stats\",\"ccl\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Clear Stats"),"ccl");
 //BA.debugLineNum = 55;BA.debugLine="Activity.AddMenuItem(\"Exit\",\"cl\")";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Exit"),"cl");
 //BA.debugLineNum = 56;BA.debugLine="suc.Initialize";
mostCurrent._suc.Initialize();
 //BA.debugLineNum = 57;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 58;BA.debugLine="l2.Initialize(\"l2\")";
mostCurrent._l2.Initialize(mostCurrent.activityBA,"l2");
 //BA.debugLineNum = 59;BA.debugLine="l3.Initialize(\"l3\")";
mostCurrent._l3.Initialize(mostCurrent.activityBA,"l3");
 //BA.debugLineNum = 61;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 62;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 63;BA.debugLine="c3=mcl.md_lime_A700";
_c3 = mostCurrent._mcl.getmd_lime_A700();
 //BA.debugLineNum = 64;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 65;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 //BA.debugLineNum = 68;BA.debugLine="dev.Initialize(\"dev\")";
mostCurrent._dev.Initialize(processBA,"dev");
 //BA.debugLineNum = 71;BA.debugLine="m.Initialize";
mostCurrent._m.Initialize();
 //BA.debugLineNum = 72;BA.debugLine="bat.Initialize";
mostCurrent._bat._initialize(processBA);
 //BA.debugLineNum = 74;BA.debugLine="kl.Initialize";
mostCurrent._kl.Initialize();
 //BA.debugLineNum = 75;BA.debugLine="dt.Initialize";
mostCurrent._dt.Initialize();
 //BA.debugLineNum = 76;BA.debugLine="ls.Initialize";
mostCurrent._ls.Initialize();
 //BA.debugLineNum = 77;BA.debugLine="l1=ListView1.SingleLineLayout.Label";
mostCurrent._l1 = mostCurrent._listview1.getSingleLineLayout().Label;
 //BA.debugLineNum = 78;BA.debugLine="l1.TextSize=15";
mostCurrent._l1.setTextSize((float) (15));
 //BA.debugLineNum = 79;BA.debugLine="l1.TextColor=Colors.White";
mostCurrent._l1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 80;BA.debugLine="l2=ListView1.TwoLinesLayout.Label";
mostCurrent._l2 = mostCurrent._listview1.getTwoLinesLayout().Label;
 //BA.debugLineNum = 81;BA.debugLine="l3=ListView1.TwoLinesLayout.SecondLabel";
mostCurrent._l3 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 82;BA.debugLine="l2.TextSize=18";
mostCurrent._l2.setTextSize((float) (18));
 //BA.debugLineNum = 83;BA.debugLine="l3.TextSize=12";
mostCurrent._l3.setTextSize((float) (12));
 //BA.debugLineNum = 84;BA.debugLine="l2.TextColor=Colors.Black";
mostCurrent._l2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 85;BA.debugLine="l3.TextColor=Colors.Cyan";
mostCurrent._l3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 86;BA.debugLine="ListView1.SingleLineLayout.ItemHeight=90";
mostCurrent._listview1.getSingleLineLayout().setItemHeight((int) (90));
 //BA.debugLineNum = 87;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 88;BA.debugLine="temp=bat.BatteryInformation(6)/10";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 89;BA.debugLine="usb =bat.BatteryInformation(9)";
mostCurrent._usb = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (9)]);
 //BA.debugLineNum = 90;BA.debugLine="ac =bat.BatteryInformation(8)";
mostCurrent._ac = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (8)]);
 //BA.debugLineNum = 91;BA.debugLine="G.Initialize";
mostCurrent._g.Initialize();
 //BA.debugLineNum = 93;BA.debugLine="Dim la,la1,la2 As Label";
_la = new anywheresoftware.b4a.objects.LabelWrapper();
_la1 = new anywheresoftware.b4a.objects.LabelWrapper();
_la2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="la.Initialize(\"la\")";
_la.Initialize(mostCurrent.activityBA,"la");
 //BA.debugLineNum = 95;BA.debugLine="la1.Initialize(\"la1\")";
_la1.Initialize(mostCurrent.activityBA,"la1");
 //BA.debugLineNum = 96;BA.debugLine="la2.Initialize(\"la2\")";
_la2.Initialize(mostCurrent.activityBA,"la2");
 //BA.debugLineNum = 97;BA.debugLine="la2=ListView1.TwoLinesLayout.SecondLabel";
_la2 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 98;BA.debugLine="la2.TextSize=11";
_la2.setTextSize((float) (11));
 //BA.debugLineNum = 99;BA.debugLine="la2.TextColor=Colors.ARGB(240,255,255,255)";
_la2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (240),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 100;BA.debugLine="la1=ListView1.TwoLinesAndBitmap.SecondLabel";
_la1 = mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 101;BA.debugLine="la1.TextSize=10";
_la1.setTextSize((float) (10));
 //BA.debugLineNum = 102;BA.debugLine="la1.TextColor=Colors.LightGray";
_la1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 103;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Height=32di";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 104;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Width=32dip";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 105;BA.debugLine="ListView1.TwoLinesAndBitmap.ItemHeight=50dip";
mostCurrent._listview1.getTwoLinesAndBitmap().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 106;BA.debugLine="If FirstTime=True Then";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 107;BA.debugLine="Msgbox(\"Wenn die Statistik zum ersten Mal gelade";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Wenn die Statistik zum ersten Mal geladen wird, kann es eine Weile dauern, bis die Werte korrekt angezeigt werden, zB: V,C°,% der letzten 10 Einträge. Bitte beachte das die Werte nur ca sind da sie immer leicht Zeit versetzt gespeichert werden und nicht  den 'Live' Zustand anzeigen!"),BA.ObjectToCharSequence("Wichtig!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 109;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 110;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 111;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 112;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 113;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 //BA.debugLineNum = 114;BA.debugLine="time2=DateTime.Time(DateTime.Now)";
mostCurrent._time2 = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 115;BA.debugLine="batt=LoadBitmap(File.DirAssets,\"Battery Icons - W";
mostCurrent._batt = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - White 64px (40).png");
 //BA.debugLineNum = 116;BA.debugLine="pl=LoadBitmap(File.DirAssets,\"Battery Icons - Whi";
mostCurrent._pl = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - White 64px (28).png");
 //BA.debugLineNum = 118;BA.debugLine="popa.Initialize(\"popa\",Panel2)";
mostCurrent._popa.Initialize(mostCurrent.activityBA,"popa",(android.view.View)(mostCurrent._panel2.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="Dim bd,bd1 As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_bd1 = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 120;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets,\"ic_clear";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_clear_black_48dp.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="bd1.Initialize(LoadBitmap(File.DirAssets,\"ic_auto";
_bd1.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_autorenew_black_48dp.png").getObject()));
 //BA.debugLineNum = 122;BA.debugLine="popa.AddMenuItem(0,\"Reload Stats\",bd1)";
mostCurrent._popa.AddMenuItem((int) (0),BA.ObjectToCharSequence("Reload Stats"),(android.graphics.drawable.Drawable)(_bd1.getObject()));
 //BA.debugLineNum = 123;BA.debugLine="popa.AddMenuItem(1,\"Schließen\",bd)";
mostCurrent._popa.AddMenuItem((int) (1),BA.ObjectToCharSequence("Schließen"),(android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 126;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 127;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 229;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 230;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 231;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 232;BA.debugLine="ToastMessageShow(\"BCT - Backround  Statistic sta";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BCT - Backround  Statistic started.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 233;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\"";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 235;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 132;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 133;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _c_start() throws Exception{
 //BA.debugLineNum = 174;BA.debugLine="Sub c_start";
 //BA.debugLineNum = 175;BA.debugLine="If kvs2.IsInitialized Then";
if (mostCurrent._kvs2.IsInitialized()) { 
 //BA.debugLineNum = 176;BA.debugLine="Log(\"KVS -> true\")";
anywheresoftware.b4a.keywords.Common.Log("KVS -> true");
 }else {
 //BA.debugLineNum = 179;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datast";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 180;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datast";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 181;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"dat";
mostCurrent._kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 182;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"dat";
mostCurrent._kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 };
 //BA.debugLineNum = 185;BA.debugLine="get_log";
_get_log();
 //BA.debugLineNum = 186;BA.debugLine="chart_start";
_chart_start();
 //BA.debugLineNum = 187;BA.debugLine="End Sub";
return "";
}
public static String  _ccl_click() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub ccl_click";
 //BA.debugLineNum = 151;BA.debugLine="volt=bat.BatteryInformation(7)";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]);
 //BA.debugLineNum = 152;BA.debugLine="temp=bat.BatteryInformation(6)";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]);
 //BA.debugLineNum = 154;BA.debugLine="kvstemp.DeleteAll";
mostCurrent._kvstemp._deleteall();
 //BA.debugLineNum = 155;BA.debugLine="kvsvolt.DeleteAll";
mostCurrent._kvsvolt._deleteall();
 //BA.debugLineNum = 157;BA.debugLine="kvstemp.PutSimple(temp,time2)";
mostCurrent._kvstemp._putsimple(mostCurrent._temp,(Object)(mostCurrent._time2));
 //BA.debugLineNum = 158;BA.debugLine="kvsvolt.PutSimple(volt,time2)";
mostCurrent._kvsvolt._putsimple(mostCurrent._volt,(Object)(mostCurrent._time2));
 //BA.debugLineNum = 159;BA.debugLine="ToastMessageShow(\"warte auf Aktuelle werte..!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("warte auf Aktuelle werte..!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="Log(kvs2.ListKeys&\" - clear\")";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._kvs2._listkeys())+" - clear");
 //BA.debugLineNum = 161;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 162;BA.debugLine="get_log";
_get_log();
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
return "";
}
public static String  _chart_start() throws Exception{
String _fn = "";
int _fg = 0;
com.batcat.charts._linedata _ld = null;
String _h = "";
 //BA.debugLineNum = 196;BA.debugLine="Sub chart_start";
 //BA.debugLineNum = 197;BA.debugLine="level=bat.BatteryInformation(0)";
_level = mostCurrent._bat._getbatteryinformation()[(int) (0)];
 //BA.debugLineNum = 198;BA.debugLine="Dim fn As String";
_fn = "";
 //BA.debugLineNum = 199;BA.debugLine="Dim fg As Int";
_fg = 0;
 //BA.debugLineNum = 201;BA.debugLine="g.Initialize";
mostCurrent._g.Initialize();
 //BA.debugLineNum = 202;BA.debugLine="level=bat.BatteryInformation(0)";
_level = mostCurrent._bat._getbatteryinformation()[(int) (0)];
 //BA.debugLineNum = 203;BA.debugLine="Dim fn As String";
_fn = "";
 //BA.debugLineNum = 204;BA.debugLine="Dim fg As Int";
_fg = 0;
 //BA.debugLineNum = 205;BA.debugLine="Dim LD As LineData";
_ld = new com.batcat.charts._linedata();
 //BA.debugLineNum = 206;BA.debugLine="LD.Initialize";
_ld.Initialize();
 //BA.debugLineNum = 207;BA.debugLine="LD.Target =Panel2";
_ld.Target = mostCurrent._panel2;
 //BA.debugLineNum = 208;BA.debugLine="Charts.AddLineColor(LD, Colors.Red) 'First line c";
mostCurrent._charts._addlinecolor(mostCurrent.activityBA,_ld,anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 209;BA.debugLine="Charts.AddLineColor(LD, Colors.Blue) 'Second line";
mostCurrent._charts._addlinecolor(mostCurrent.activityBA,_ld,anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 210;BA.debugLine="For Each h As String  In kvs2.ListKeys";
final anywheresoftware.b4a.BA.IterableList group13 = mostCurrent._kvs2._listkeys();
final int groupLen13 = group13.getSize();
for (int index13 = 0;index13 < groupLen13 ;index13++){
_h = BA.ObjectToString(group13.Get(index13));
 //BA.debugLineNum = 211;BA.debugLine="fg=h";
_fg = (int)(Double.parseDouble(_h));
 //BA.debugLineNum = 212;BA.debugLine="fn=kvs2.GetSimple(h)";
_fn = mostCurrent._kvs2._getsimple(_h);
 //BA.debugLineNum = 213;BA.debugLine="Log(\"Map Key-> \"&fg)";
anywheresoftware.b4a.keywords.Common.Log("Map Key-> "+BA.NumberToString(_fg));
 //BA.debugLineNum = 215;BA.debugLine="Charts.AddLinePoint(LD, fn,fg, True)";
mostCurrent._charts._addlinepoint(mostCurrent.activityBA,_ld,_fn,(float) (_fg),anywheresoftware.b4a.keywords.Common.True);
 }
;
 //BA.debugLineNum = 218;BA.debugLine="G.Title = \"BCT Power Chart\"";
mostCurrent._g.Title = "BCT Power Chart";
 //BA.debugLineNum = 219;BA.debugLine="G.XAxis = time2";
mostCurrent._g.XAxis = mostCurrent._time2;
 //BA.debugLineNum = 220;BA.debugLine="G.YAxis = \"Level:\"";
mostCurrent._g.YAxis = "Level:";
 //BA.debugLineNum = 221;BA.debugLine="G.YStart = 0";
mostCurrent._g.YStart = (float) (0);
 //BA.debugLineNum = 222;BA.debugLine="G.YEnd = 100";
mostCurrent._g.YEnd = (float) (100);
 //BA.debugLineNum = 223;BA.debugLine="G.YInterval = 5";
mostCurrent._g.YInterval = (float) (5);
 //BA.debugLineNum = 224;BA.debugLine="G.AxisColor = Colors.White";
mostCurrent._g.AxisColor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 226;BA.debugLine="Charts.DrawLineChart(G, LD, Colors.Transparent)";
mostCurrent._charts._drawlinechart(mostCurrent.activityBA,mostCurrent._g,_ld,anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 227;BA.debugLine="End Sub";
return "";
}
public static String  _db_update() throws Exception{
 //BA.debugLineNum = 238;BA.debugLine="Sub db_update";
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _dev_batterychanged(int _level1,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
anywheresoftware.b4a.objects.collections.List _vl = null;
int _v = 0;
 //BA.debugLineNum = 249;BA.debugLine="Sub dev_BatteryChanged (level1 As Int, Scale As In";
 //BA.debugLineNum = 250;BA.debugLine="Dim vl As List";
_vl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 251;BA.debugLine="vl.Initialize";
_vl.Initialize();
 //BA.debugLineNum = 252;BA.debugLine="For v = 0 To Scale Step 2";
{
final int step3 = (int) (2);
final int limit3 = _scale;
for (_v = (int) (0) ; (step3 > 0 && _v <= limit3) || (step3 < 0 && _v >= limit3); _v = ((int)(0 + _v + step3)) ) {
 //BA.debugLineNum = 253;BA.debugLine="vl.Add(v)";
_vl.Add((Object)(_v));
 //BA.debugLineNum = 254;BA.debugLine="If level1=v Then";
if (_level1==_v) { 
 //BA.debugLineNum = 255;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 256;BA.debugLine="c_start";
_c_start();
 };
 }
};
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _get_log() throws Exception{
 //BA.debugLineNum = 190;BA.debugLine="Sub get_log";
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim l1,l2,l3 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim m As Map";
mostCurrent._m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 26;BA.debugLine="Dim time2 As String";
mostCurrent._time2 = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim bat As Batut";
mostCurrent._bat = new com.batcat.batut();
 //BA.debugLineNum = 28;BA.debugLine="Dim volt,temp,usb,ac As String";
mostCurrent._volt = "";
mostCurrent._temp = "";
mostCurrent._usb = "";
mostCurrent._ac = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim pak1 As PackageManager";
mostCurrent._pak1 = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim kvs2,kvs3,kvs4,kvsvolt,kvstemp As KeyValueSto";
mostCurrent._kvs2 = new com.batcat.keyvaluestore();
mostCurrent._kvs3 = new com.batcat.keyvaluestore();
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvsvolt = new com.batcat.keyvaluestore();
mostCurrent._kvstemp = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 32;BA.debugLine="Dim dt As List";
mostCurrent._dt = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 33;BA.debugLine="Dim kl As List";
mostCurrent._kl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 34;BA.debugLine="Dim suc As List";
mostCurrent._suc = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 35;BA.debugLine="Dim g As Graph";
mostCurrent._g = new com.batcat.charts._graph();
 //BA.debugLineNum = 36;BA.debugLine="Dim ls As LinePoint";
mostCurrent._ls = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 37;BA.debugLine="Dim batt,pl,pk As Bitmap";
mostCurrent._batt = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._pl = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._pk = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 39;BA.debugLine="Dim popa As ACPopupMenu";
mostCurrent._popa = new de.amberhome.objects.appcompat.ACPopupMenuWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private ACButton1 As ACButton";
mostCurrent._acbutton1 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim level As Int";
_level = 0;
 //BA.debugLineNum = 42;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
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
 //BA.debugLineNum = 43;BA.debugLine="Private dev As PhoneEvents";
mostCurrent._dev = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 44;BA.debugLine="Private mpc1 As PieChart";
mostCurrent._mpc1 = new mpandroidchartwrapper.pieViewWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private ACButton2 As ACButton";
mostCurrent._acbutton2 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Dim osstat As OSStats";
mostCurrent._osstat = new b4a.example.osstats();
 //BA.debugLineNum = 47;BA.debugLine="dim metric as MSOS";
mostCurrent._metric = new com.maximussoft.msos.MSOS();
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _graph_clear() throws Exception{
 //BA.debugLineNum = 165;BA.debugLine="Sub graph_clear";
 //BA.debugLineNum = 166;BA.debugLine="kvs2.DeleteAll";
mostCurrent._kvs2._deleteall();
 //BA.debugLineNum = 167;BA.debugLine="level  = bat.BatteryInformation(0)";
_level = mostCurrent._bat._getbatteryinformation()[(int) (0)];
 //BA.debugLineNum = 168;BA.debugLine="Log(\"put-> \"&level&\"%\")";
anywheresoftware.b4a.keywords.Common.Log("put-> "+BA.NumberToString(_level)+"%");
 //BA.debugLineNum = 169;BA.debugLine="kvs2.PutSimple(level,time2)";
mostCurrent._kvs2._putsimple(BA.NumberToString(_level),(Object)(mostCurrent._time2));
 //BA.debugLineNum = 170;BA.debugLine="ToastMessageShow(\"warte auf Aktuelle werte..!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("warte auf Aktuelle werte..!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _popa_itemclicked(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub popa_ItemClicked (Item As ACMenuItem)";
 //BA.debugLineNum = 141;BA.debugLine="If Item = popa.GetItem(0) Then";
if ((_item).equals((android.view.MenuItem)(mostCurrent._popa.GetItem((int) (0))))) { 
 //BA.debugLineNum = 142;BA.debugLine="ACButton1_Click";
_acbutton1_click();
 };
 //BA.debugLineNum = 144;BA.debugLine="If Item = popa.GetItem(1) Then";
if ((_item).equals((android.view.MenuItem)(mostCurrent._popa.GetItem((int) (1))))) { 
 //BA.debugLineNum = 145;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 264;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 265;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 266;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 267;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 268;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 269;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 270;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 271;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 272;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 273;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 274;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 275;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 276;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 277;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 278;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 279;BA.debugLine="c16=mcl.md_light_blue_A700";
_c16 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 280;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 281;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 282;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 284;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 285;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 286;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 }else {
 };
 //BA.debugLineNum = 290;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 291;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 292;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 }else {
 };
 //BA.debugLineNum = 296;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 297;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 298;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 };
 //BA.debugLineNum = 302;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 303;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 304;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 }else {
 };
 //BA.debugLineNum = 308;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 309;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 310;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 }else {
 };
 //BA.debugLineNum = 314;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 315;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 316;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 }else {
 };
 //BA.debugLineNum = 320;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 321;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 322;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 }else {
 };
 //BA.debugLineNum = 326;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 327;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 328;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 }else {
 };
 //BA.debugLineNum = 332;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 333;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 334;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 }else {
 };
 //BA.debugLineNum = 338;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 339;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 340;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 }else {
 };
 //BA.debugLineNum = 344;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 345;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 346;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 }else {
 };
 //BA.debugLineNum = 350;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 351;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 352;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 }else {
 };
 //BA.debugLineNum = 356;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 357;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 358;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 }else {
 };
 //BA.debugLineNum = 362;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 363;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 364;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 }else {
 };
 //BA.debugLineNum = 368;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 369;BA.debugLine="End Sub";
return "";
}
}
