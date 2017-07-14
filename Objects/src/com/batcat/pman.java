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

public class pman extends Activity implements B4AActivity{
	public static pman mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.pman");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (pman).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.pman");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.pman", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (pman) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (pman) Resume **");
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
		return pman.class;
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
        BA.LogInfo("** Activity (pman) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (pman) Resume **");
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
public anywheresoftware.b4a.objects.ListViewWrapper _applist = null;
public static String _name = "";
public static String _apath = "";
public static String _l = "";
public static String[] _types = null;
public static String _packname = "";
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _icon = null;
public anywheresoftware.b4a.objects.collections.List _sublist = null;
public anywheresoftware.b4a.objects.collections.List _data = null;
public anywheresoftware.b4a.objects.collections.List _del = null;
public Object[] _args = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj2 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj3 = null;
public static int _size = 0;
public static int _flags = 0;
public com.rootsoft.oslibrary.OSLibrary _os = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public de.amberhome.objects.appcompat.ACFlatButtonWrapper _abf1 = null;
public de.amberhome.objects.appcompat.ACFlatButtonWrapper _abf2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _subapp = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public Object _ion = null;
public com.batcat.keyvaluestore _kvdata = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.set2 _set2 = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.wait _wait = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.charts _charts = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.datacount _datacount = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _abf1_click() throws Exception{
String _ndel = "";
 //BA.debugLineNum = 99;BA.debugLine="Sub abf1_Click";
 //BA.debugLineNum = 102;BA.debugLine="Dim ndel As String";
_ndel = "";
 //BA.debugLineNum = 103;BA.debugLine="ndel=del.Get(0)";
_ndel = BA.ObjectToString(mostCurrent._del.Get((int) (0)));
 //BA.debugLineNum = 110;BA.debugLine="If kvdata.ContainsKey(\"data\") Then";
if (mostCurrent._kvdata._containskey("data")) { 
 //BA.debugLineNum = 111;BA.debugLine="kvdata.DeleteAll";
mostCurrent._kvdata._deleteall();
 //BA.debugLineNum = 112;BA.debugLine="kvdata.PutSimple(\"data\",ndel)";
mostCurrent._kvdata._putsimple("data",(Object)(_ndel));
 //BA.debugLineNum = 113;BA.debugLine="CallSubDelayed(datacount,\"start\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(mostCurrent.activityBA,(Object)(mostCurrent._datacount.getObject()),"start");
 //BA.debugLineNum = 114;BA.debugLine="panset";
_panset();
 }else {
 //BA.debugLineNum = 116;BA.debugLine="kvdata.PutSimple(\"data\",ndel)";
mostCurrent._kvdata._putsimple("data",(Object)(_ndel));
 //BA.debugLineNum = 117;BA.debugLine="panset";
_panset();
 //BA.debugLineNum = 118;BA.debugLine="CallSubDelayed(datacount,\"start\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(mostCurrent.activityBA,(Object)(mostCurrent._datacount.getObject()),"start");
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _abf2_click() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub abf2_Click";
 //BA.debugLineNum = 125;BA.debugLine="panset";
_panset();
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _la = null;
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
anywheresoftware.b4a.objects.LabelWrapper _lc = null;
anywheresoftware.b4a.objects.LabelWrapper _ld = null;
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Activity.LoadLayout(\"7\")";
mostCurrent._activity.LoadLayout("7",mostCurrent.activityBA);
 //BA.debugLineNum = 34;BA.debugLine="os.Initialize(\"os\")";
mostCurrent._os.Initialize(processBA,"os");
 //BA.debugLineNum = 35;BA.debugLine="sublist.Initialize";
mostCurrent._sublist.Initialize();
 //BA.debugLineNum = 36;BA.debugLine="data.Initialize";
mostCurrent._data.Initialize();
 //BA.debugLineNum = 37;BA.debugLine="del.Initialize";
mostCurrent._del.Initialize();
 //BA.debugLineNum = 38;BA.debugLine="kvdata.Initialize(File.DirDefaultExternal,\"datast";
mostCurrent._kvdata._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_data");
 //BA.debugLineNum = 39;BA.debugLine="Dim la,lb,lc,ld As Label";
_la = new anywheresoftware.b4a.objects.LabelWrapper();
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
_lc = new anywheresoftware.b4a.objects.LabelWrapper();
_ld = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="la =applist.TwoLinesAndBitmap.Label";
_la = mostCurrent._applist.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 41;BA.debugLine="lc =subapp.SingleLineLayout.Label";
_lc = mostCurrent._subapp.getSingleLineLayout().Label;
 //BA.debugLineNum = 42;BA.debugLine="lb= applist.TwoLinesAndBitmap.SecondLabel";
_lb = mostCurrent._applist.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 43;BA.debugLine="ld= subapp.TwoLinesAndBitmap.SecondLabel";
_ld = mostCurrent._subapp.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 44;BA.debugLine="applist.TwoLinesAndBitmap.ItemHeight=60dip";
mostCurrent._applist.getTwoLinesAndBitmap().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 45;BA.debugLine="applist.TwoLinesAndBitmap.ImageView.Height=50dip";
mostCurrent._applist.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 46;BA.debugLine="la.TextColor=mcl.md_black_1000";
_la.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 47;BA.debugLine="lc.TextColor=mcl.md_black_1000";
_lc.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 48;BA.debugLine="la.TextSize=18";
_la.setTextSize((float) (18));
 //BA.debugLineNum = 49;BA.debugLine="lc.TextSize=15";
_lc.setTextSize((float) (15));
 //BA.debugLineNum = 50;BA.debugLine="lb.TextSize=13";
_lb.setTextSize((float) (13));
 //BA.debugLineNum = 51;BA.debugLine="ld.TextSize=13";
_ld.setTextSize((float) (13));
 //BA.debugLineNum = 52;BA.debugLine="lb.TextColor=mcl.md_light_blue_300";
_lb.setTextColor(mostCurrent._mcl.getmd_light_blue_300());
 //BA.debugLineNum = 53;BA.debugLine="ld.TextColor=mcl.md_light_blue_300";
_ld.setTextColor(mostCurrent._mcl.getmd_light_blue_300());
 //BA.debugLineNum = 54;BA.debugLine="abf1.Color=mcl.md_red_200";
mostCurrent._abf1.setColor(mostCurrent._mcl.getmd_red_200());
 //BA.debugLineNum = 55;BA.debugLine="abf2.Color=mcl.md_grey_400";
mostCurrent._abf2.setColor(mostCurrent._mcl.getmd_grey_400());
 //BA.debugLineNum = 56;BA.debugLine="abf1.Text=\"deinstall\"";
mostCurrent._abf1.setText(BA.ObjectToCharSequence("deinstall"));
 //BA.debugLineNum = 57;BA.debugLine="abf2.Text=\"close\"";
mostCurrent._abf2.setText(BA.ObjectToCharSequence("close"));
 //BA.debugLineNum = 58;BA.debugLine="app_manage";
_app_manage();
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 70;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 71;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 72;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 74;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 62;BA.debugLine="app_manage";
_app_manage();
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _app_manage() throws Exception{
int _i = 0;
String _total = "";
 //BA.debugLineNum = 166;BA.debugLine="Sub app_manage";
 //BA.debugLineNum = 167;BA.debugLine="applist.Clear";
mostCurrent._applist.Clear();
 //BA.debugLineNum = 168;BA.debugLine="data.Clear";
mostCurrent._data.Clear();
 //BA.debugLineNum = 169;BA.debugLine="sublist=pak.GetInstalledPackages";
mostCurrent._sublist = _pak.GetInstalledPackages();
 //BA.debugLineNum = 170;BA.debugLine="Obj1.Target = Obj1.GetContext";
mostCurrent._obj1.Target = (Object)(mostCurrent._obj1.GetContext(processBA));
 //BA.debugLineNum = 171;BA.debugLine="Obj1.Target = Obj1.RunMethod(\"getPackageManager\")";
mostCurrent._obj1.Target = mostCurrent._obj1.RunMethod("getPackageManager");
 //BA.debugLineNum = 172;BA.debugLine="Obj2.Target = Obj1.RunMethod2(\"getInstalledPackag";
mostCurrent._obj2.Target = mostCurrent._obj1.RunMethod2("getInstalledPackages",BA.NumberToString(0),"java.lang.int");
 //BA.debugLineNum = 173;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 174;BA.debugLine="For i = 0 To size -1";
{
final int step8 = 1;
final int limit8 = (int) (_size-1);
for (_i = (int) (0) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 175;BA.debugLine="Obj3.Target = Obj2.RunMethod2(\"get\", i, \"java.la";
mostCurrent._obj3.Target = mostCurrent._obj2.RunMethod2("get",BA.NumberToString(_i),"java.lang.int");
 //BA.debugLineNum = 176;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 177;BA.debugLine="Obj3.Target = Obj3.GetField(\"applicationInfo\") '";
mostCurrent._obj3.Target = mostCurrent._obj3.GetField("applicationInfo");
 //BA.debugLineNum = 178;BA.debugLine="flags = Obj3.GetField(\"flags\")";
_flags = (int)(BA.ObjectToNumber(mostCurrent._obj3.GetField("flags")));
 //BA.debugLineNum = 179;BA.debugLine="packName = Obj3.GetField(\"packageName\")";
mostCurrent._packname = BA.ObjectToString(mostCurrent._obj3.GetField("packageName"));
 //BA.debugLineNum = 180;BA.debugLine="If Bit.And(flags, 1)  = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_flags,(int) (1))==0) { 
 //BA.debugLineNum = 182;BA.debugLine="args(0) = Obj3.Target";
mostCurrent._args[(int) (0)] = mostCurrent._obj3.Target;
 //BA.debugLineNum = 183;BA.debugLine="Types(0) = \"android.content.pm.ApplicationInfo\"";
mostCurrent._types[(int) (0)] = "android.content.pm.ApplicationInfo";
 //BA.debugLineNum = 184;BA.debugLine="name = Obj1.RunMethod4(\"getApplicationLabel\", a";
mostCurrent._name = BA.ObjectToString(mostCurrent._obj1.RunMethod4("getApplicationLabel",mostCurrent._args,mostCurrent._types));
 //BA.debugLineNum = 185;BA.debugLine="icon = Obj1.RunMethod4(\"getApplicationIcon\", ar";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(mostCurrent._obj1.RunMethod4("getApplicationIcon",mostCurrent._args,mostCurrent._types)));
 //BA.debugLineNum = 186;BA.debugLine="Dim total As String";
_total = "";
 //BA.debugLineNum = 187;BA.debugLine="total = File.Size(GetParentPath(GetSourceDir(Ge";
_total = BA.NumberToString(anywheresoftware.b4a.keywords.Common.File.Size(_getparentpath(_getsourcedir(_getactivitiesinfo(mostCurrent._packname))),_getfilename(_getsourcedir(_getactivitiesinfo(mostCurrent._packname)))));
 //BA.debugLineNum = 188;BA.debugLine="applist.AddTwoLinesAndBitmap2(name,packName&\" |";
mostCurrent._applist.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence(mostCurrent._name),BA.ObjectToCharSequence(mostCurrent._packname+" | "+_formatfilesize((float)(Double.parseDouble(_total)))),mostCurrent._icon.getBitmap(),(Object)(mostCurrent._packname));
 //BA.debugLineNum = 190;BA.debugLine="data.Add(packName)";
mostCurrent._data.Add((Object)(mostCurrent._packname));
 };
 }
};
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static String  _applist_itemclick(int _position,Object _value) throws Exception{
String _f = "";
 //BA.debugLineNum = 128;BA.debugLine="Sub applist_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 129;BA.debugLine="subapp.Clear";
mostCurrent._subapp.Clear();
 //BA.debugLineNum = 130;BA.debugLine="del.Clear";
mostCurrent._del.Clear();
 //BA.debugLineNum = 131;BA.debugLine="For Each  f As String In data";
final anywheresoftware.b4a.BA.IterableList group3 = mostCurrent._data;
final int groupLen3 = group3.getSize();
for (int index3 = 0;index3 < groupLen3 ;index3++){
_f = BA.ObjectToString(group3.Get(index3));
 //BA.debugLineNum = 132;BA.debugLine="packName = f";
mostCurrent._packname = _f;
 //BA.debugLineNum = 133;BA.debugLine="If Value=packName Then";
if ((_value).equals((Object)(mostCurrent._packname))) { 
 //BA.debugLineNum = 134;BA.debugLine="name= pak.GetApplicationLabel(f)";
mostCurrent._name = _pak.GetApplicationLabel(_f);
 //BA.debugLineNum = 135;BA.debugLine="size=File.Size(GetParentPath(GetSourceDir(GetActi";
_size = (int) (anywheresoftware.b4a.keywords.Common.File.Size(_getparentpath(_getsourcedir(_getactivitiesinfo(mostCurrent._packname))),_getfilename(_getsourcedir(_getactivitiesinfo(mostCurrent._packname)))));
 //BA.debugLineNum = 136;BA.debugLine="icon=pak.GetApplicationIcon(packName)";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(_pak.GetApplicationIcon(mostCurrent._packname)));
 //BA.debugLineNum = 137;BA.debugLine="apath=GetParentPath(GetSourceDir(GetActivitiesInf";
mostCurrent._apath = _getparentpath(_getsourcedir(_getactivitiesinfo(mostCurrent._packname)));
 //BA.debugLineNum = 138;BA.debugLine="subapp.AddTwoLinesAndBitmap(\"\",\"\",icon.Bitmap)";
mostCurrent._subapp.AddTwoLinesAndBitmap(BA.ObjectToCharSequence(""),BA.ObjectToCharSequence(""),mostCurrent._icon.getBitmap());
 //BA.debugLineNum = 139;BA.debugLine="subapp.AddSingleLine(\"Größe: \"&FormatFileSize(siz";
mostCurrent._subapp.AddSingleLine(BA.ObjectToCharSequence("Größe: "+_formatfilesize((float) (_size))));
 //BA.debugLineNum = 140;BA.debugLine="subapp.AddSingleLine2(packName,0)";
mostCurrent._subapp.AddSingleLine2(BA.ObjectToCharSequence(mostCurrent._packname),(Object)(0));
 //BA.debugLineNum = 141;BA.debugLine="del.Add(packName)";
mostCurrent._del.Add((Object)(mostCurrent._packname));
 //BA.debugLineNum = 142;BA.debugLine="subapp.AddSingleLine(apath)";
mostCurrent._subapp.AddSingleLine(BA.ObjectToCharSequence(mostCurrent._apath));
 //BA.debugLineNum = 143;BA.debugLine="Label1.Text=name";
mostCurrent._label1.setText(BA.ObjectToCharSequence(mostCurrent._name));
 //BA.debugLineNum = 144;BA.debugLine="panset";
_panset();
 };
 }
;
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _applist_itemlongclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub applist_ItemLongClick (Position As Int, Value";
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _cli_click() throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub cli_click";
 //BA.debugLineNum = 220;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 221;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public static String  _formatfilesize(float _bytes) throws Exception{
String[] _unit = null;
double _po = 0;
double _si = 0;
int _i = 0;
 //BA.debugLineNum = 197;BA.debugLine="Sub FormatFileSize(Bytes As Float) As String";
 //BA.debugLineNum = 199;BA.debugLine="Private Unit() As String = Array As String(\" Byte";
_unit = new String[]{" Byte"," KB"," MB"," GB"," TB"," PB"," EB"," ZB"," YB"};
 //BA.debugLineNum = 201;BA.debugLine="If Bytes = 0 Then";
if (_bytes==0) { 
 //BA.debugLineNum = 202;BA.debugLine="Return \"0 Bytes\"";
if (true) return "0 Bytes";
 }else {
 //BA.debugLineNum = 204;BA.debugLine="Private Po, Si As Double";
_po = 0;
_si = 0;
 //BA.debugLineNum = 205;BA.debugLine="Private I As Int";
_i = 0;
 //BA.debugLineNum = 206;BA.debugLine="Bytes = Abs(Bytes)";
_bytes = (float) (anywheresoftware.b4a.keywords.Common.Abs(_bytes));
 //BA.debugLineNum = 207;BA.debugLine="I = Floor(Logarithm(Bytes, 1024))";
_i = (int) (anywheresoftware.b4a.keywords.Common.Floor(anywheresoftware.b4a.keywords.Common.Logarithm(_bytes,1024)));
 //BA.debugLineNum = 208;BA.debugLine="Po = Power(1024, I)";
_po = anywheresoftware.b4a.keywords.Common.Power(1024,_i);
 //BA.debugLineNum = 209;BA.debugLine="Si = Bytes / Po";
_si = _bytes/(double)_po;
 //BA.debugLineNum = 210;BA.debugLine="Return NumberFormat(Si, 1, 2) & Unit(I)";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_si,(int) (1),(int) (2))+_unit[_i];
 };
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static Object  _getactivitiesinfo(String _package) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 243;BA.debugLine="Sub GetActivitiesInfo(package As String) As Object";
 //BA.debugLineNum = 244;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 245;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(processBA));
 //BA.debugLineNum = 246;BA.debugLine="r.Target = r.RunMethod(\"getPackageManager\")";
_r.Target = _r.RunMethod("getPackageManager");
 //BA.debugLineNum = 247;BA.debugLine="r.Target = r.RunMethod3(\"getPackageInfo\", package";
_r.Target = _r.RunMethod3("getPackageInfo",_package,"java.lang.String",BA.NumberToString(0x00000001),"java.lang.int");
 //BA.debugLineNum = 248;BA.debugLine="Return r.GetField(\"applicationInfo\")";
if (true) return _r.GetField("applicationInfo");
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return null;
}
public static Object  _getba() throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
String _cls = "";
 //BA.debugLineNum = 91;BA.debugLine="Sub GetBA As Object";
 //BA.debugLineNum = 92;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 93;BA.debugLine="Dim cls As String = Me";
_cls = BA.ObjectToString(pman.getObject());
 //BA.debugLineNum = 94;BA.debugLine="cls = cls.SubString(\"class \".Length)";
_cls = _cls.substring("class ".length());
 //BA.debugLineNum = 95;BA.debugLine="jo.InitializeStatic(cls)";
_jo.InitializeStatic(_cls);
 //BA.debugLineNum = 96;BA.debugLine="Return jo.GetField(\"processBA\")";
if (true) return _jo.GetField("processBA");
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return null;
}
public static String  _getfilename(String _fullpath) throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub GetFileName(FullPath As String) As String";
 //BA.debugLineNum = 215;BA.debugLine="Return FullPath.SubString(FullPath.LastIndexOf(\"/";
if (true) return _fullpath.substring((int) (_fullpath.lastIndexOf("/")+1));
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _getparentpath(String _path) throws Exception{
String _path1 = "";
 //BA.debugLineNum = 224;BA.debugLine="Sub GetParentPath(path As String) As String";
 //BA.debugLineNum = 225;BA.debugLine="Dim Path1 As String";
_path1 = "";
 //BA.debugLineNum = 226;BA.debugLine="If path = \"/\" Then";
if ((_path).equals("/")) { 
 //BA.debugLineNum = 227;BA.debugLine="Return \"/\"";
if (true) return "/";
 };
 //BA.debugLineNum = 229;BA.debugLine="L = path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 230;BA.debugLine="If L = path.Length - 1 Then";
if ((mostCurrent._l).equals(BA.NumberToString(_path.length()-1))) { 
 //BA.debugLineNum = 232;BA.debugLine="Path1 = path.SubString2(0,L)";
_path1 = _path.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 }else {
 //BA.debugLineNum = 234;BA.debugLine="Path1 = path";
_path1 = _path;
 };
 //BA.debugLineNum = 236;BA.debugLine="L = path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 237;BA.debugLine="If L = 0 Then";
if ((mostCurrent._l).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 238;BA.debugLine="L = 1";
mostCurrent._l = BA.NumberToString(1);
 };
 //BA.debugLineNum = 240;BA.debugLine="Return Path1.SubString2(0,L)";
if (true) return _path1.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _getsourcedir(Object _appinfo) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 251;BA.debugLine="Sub GetSourceDir(AppInfo As Object) As String";
 //BA.debugLineNum = 252;BA.debugLine="Try";
try { //BA.debugLineNum = 253;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 254;BA.debugLine="r.Target = AppInfo";
_r.Target = _appinfo;
 //BA.debugLineNum = 255;BA.debugLine="Return r.GetField(\"sourceDir\")";
if (true) return BA.ObjectToString(_r.GetField("sourceDir"));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 257;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 259;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 14;BA.debugLine="Private applist As ListView";
mostCurrent._applist = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private name,apath,l,Types(1),packName As String";
mostCurrent._name = "";
mostCurrent._apath = "";
mostCurrent._l = "";
mostCurrent._types = new String[(int) (1)];
java.util.Arrays.fill(mostCurrent._types,"");
mostCurrent._packname = "";
 //BA.debugLineNum = 16;BA.debugLine="Private icon As BitmapDrawable";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 17;BA.debugLine="Private sublist,data,del As List";
mostCurrent._sublist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._data = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._del = new anywheresoftware.b4a.objects.collections.List();
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
 //BA.debugLineNum = 20;BA.debugLine="Dim size,flags As Int";
_size = 0;
_flags = 0;
 //BA.debugLineNum = 21;BA.debugLine="Private os As OperatingSystem";
mostCurrent._os = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 22;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 23;BA.debugLine="Private abf1 As ACFlatButton";
mostCurrent._abf1 = new de.amberhome.objects.appcompat.ACFlatButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private abf2 As ACFlatButton";
mostCurrent._abf2 = new de.amberhome.objects.appcompat.ACFlatButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private subapp As ListView";
mostCurrent._subapp = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private ion As Object";
mostCurrent._ion = new Object();
 //BA.debugLineNum = 29;BA.debugLine="Private kvdata As KeyValueStore";
mostCurrent._kvdata = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static Object  _ion_event(String _methodname,Object[] _args1) throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub ion_Event (MethodName As String, args1() As Ob";
 //BA.debugLineNum = 86;BA.debugLine="If args1(0) = -1 Then 'resultCode = RESULT_OK";
if ((_args1[(int) (0)]).equals((Object)(-1))) { 
 //BA.debugLineNum = 87;BA.debugLine="CallSub(Main,\"rebound\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(mostCurrent.activityBA,(Object)(mostCurrent._main.getObject()),"rebound");
 };
 //BA.debugLineNum = 89;BA.debugLine="Return(True)";
if (true) return (Object)((anywheresoftware.b4a.keywords.Common.True));
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return null;
}
public static String  _panel1_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 161;BA.debugLine="Sub panel1_Touch (Action As Int, X As Float, Y As";
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
return "";
}
public static String  _panset() throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Sub panset";
 //BA.debugLineNum = 153;BA.debugLine="If Not (Panel1.Visible=True) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._panel1.getVisible()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 154;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 156;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Private pak As PackageManager";
_pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _res_bo() throws Exception{
 //BA.debugLineNum = 121;BA.debugLine="Sub res_bo";
 //BA.debugLineNum = 122;BA.debugLine="CallSub(Main,\"rebound\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(mostCurrent.activityBA,(Object)(mostCurrent._main.getObject()),"rebound");
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _startactivityforresult(anywheresoftware.b4a.objects.IntentWrapper _i) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 77;BA.debugLine="Sub StartActivityForResult(i As Intent)";
 //BA.debugLineNum = 78;BA.debugLine="Dim jo As JavaObject =GetBA";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(_getba()));
 //BA.debugLineNum = 79;BA.debugLine="ion = jo.CreateEvent(\"anywheresoftware.b4a.IOnAct";
mostCurrent._ion = _jo.CreateEvent(processBA,"anywheresoftware.b4a.IOnActivityResult","ion",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 80;BA.debugLine="jo.RunMethod(\"startActivityForResult\", Array As O";
_jo.RunMethod("startActivityForResult",new Object[]{mostCurrent._ion,(Object)(_i.getObject())});
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
}
