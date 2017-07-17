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

public class set2 extends Activity implements B4AActivity{
	public static set2 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.set2");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (set2).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.set2");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.set2", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (set2) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (set2) Resume **");
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
		return set2.class;
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
        BA.LogInfo("** Activity (set2) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (set2) Resume **");
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
public com.batcat.settingui _set = null;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.keyvaluestore _kvs4sub = null;
public anywheresoftware.b4a.objects.collections.List _clist = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _cb = null;
public de.amberhome.objects.appcompat.ACSpinnerWrapper _cs = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc1 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc2 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc3 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc4 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc5 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc6 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc7 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc8 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc9 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc10 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc11 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc12 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc13 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc14 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc15 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _cc16 = null;
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
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.pman _pman = null;
public com.batcat.wait _wait = null;
public com.batcat.charts _charts = null;
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
public static String  _acspinner1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 275;BA.debugLine="Sub ACSpinner1_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 276;BA.debugLine="Value=cs.SelectedIndex";
_value = (Object)(mostCurrent._cs.getSelectedIndex());
 //BA.debugLineNum = 277;BA.debugLine="cs.SelectedIndex=Position";
mostCurrent._cs.setSelectedIndex(_position);
 //BA.debugLineNum = 280;BA.debugLine="If Position=0 Then";
if (_position==0) { 
 //BA.debugLineNum = 281;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 282;BA.debugLine="kvs4.PutSimple(\"0\",Value)";
mostCurrent._kvs4._putsimple("0",_value);
 //BA.debugLineNum = 283;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 285;BA.debugLine="Log(\"Is in Store\")";
anywheresoftware.b4a.keywords.Common.Log("Is in Store");
 };
 //BA.debugLineNum = 287;BA.debugLine="If Position=1 Then";
if (_position==1) { 
 //BA.debugLineNum = 288;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 289;BA.debugLine="kvs4.PutSimple(\"1\",Value)";
mostCurrent._kvs4._putsimple("1",_value);
 //BA.debugLineNum = 290;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="Log(\"New Store\")";
anywheresoftware.b4a.keywords.Common.Log("New Store");
 };
 //BA.debugLineNum = 294;BA.debugLine="If Position=2 Then";
if (_position==2) { 
 //BA.debugLineNum = 295;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 296;BA.debugLine="kvs4.PutSimple(\"2\",Value)";
mostCurrent._kvs4._putsimple("2",_value);
 //BA.debugLineNum = 297;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 298;BA.debugLine="Log(\"Now Stored\")";
anywheresoftware.b4a.keywords.Common.Log("Now Stored");
 //BA.debugLineNum = 300;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 302;BA.debugLine="If Position=3 Then";
if (_position==3) { 
 //BA.debugLineNum = 303;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 304;BA.debugLine="kvs4.PutSimple(\"3\",Value)";
mostCurrent._kvs4._putsimple("3",_value);
 //BA.debugLineNum = 305;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 306;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 308;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 310;BA.debugLine="If Position=4 Then";
if (_position==4) { 
 //BA.debugLineNum = 311;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 312;BA.debugLine="kvs4.PutSimple(\"4\",Value)";
mostCurrent._kvs4._putsimple("4",_value);
 //BA.debugLineNum = 313;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 314;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 317;BA.debugLine="If Position=5 Then";
if (_position==5) { 
 //BA.debugLineNum = 318;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 319;BA.debugLine="kvs4.PutSimple(\"5\",Value)";
mostCurrent._kvs4._putsimple("5",_value);
 //BA.debugLineNum = 320;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 321;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 324;BA.debugLine="If Position=6 Then";
if (_position==6) { 
 //BA.debugLineNum = 325;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 326;BA.debugLine="kvs4.PutSimple(\"6\",Value)";
mostCurrent._kvs4._putsimple("6",_value);
 //BA.debugLineNum = 327;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 328;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 331;BA.debugLine="If Position=7 Then";
if (_position==7) { 
 //BA.debugLineNum = 332;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 333;BA.debugLine="kvs4.PutSimple(\"7\",Value)";
mostCurrent._kvs4._putsimple("7",_value);
 //BA.debugLineNum = 334;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 335;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 338;BA.debugLine="If Position=8 Then";
if (_position==8) { 
 //BA.debugLineNum = 339;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 340;BA.debugLine="kvs4.PutSimple(\"8\",Value)";
mostCurrent._kvs4._putsimple("8",_value);
 //BA.debugLineNum = 341;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 342;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 345;BA.debugLine="If Position=9 Then";
if (_position==9) { 
 //BA.debugLineNum = 346;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 347;BA.debugLine="kvs4.PutSimple(\"9\",Value)";
mostCurrent._kvs4._putsimple("9",_value);
 //BA.debugLineNum = 348;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 349;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 352;BA.debugLine="If Position=10 Then";
if (_position==10) { 
 //BA.debugLineNum = 353;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 354;BA.debugLine="kvs4.PutSimple(\"10\",Value)";
mostCurrent._kvs4._putsimple("10",_value);
 //BA.debugLineNum = 355;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 356;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 358;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 360;BA.debugLine="If Position=11 Then";
if (_position==11) { 
 //BA.debugLineNum = 361;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 362;BA.debugLine="kvs4.PutSimple(\"11\",Value)";
mostCurrent._kvs4._putsimple("11",_value);
 //BA.debugLineNum = 363;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 364;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 368;BA.debugLine="If Position=12 Then";
if (_position==12) { 
 //BA.debugLineNum = 369;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 370;BA.debugLine="kvs4.PutSimple(\"12\",Value)";
mostCurrent._kvs4._putsimple("12",_value);
 //BA.debugLineNum = 371;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 372;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 375;BA.debugLine="If Position=13 Then";
if (_position==13) { 
 //BA.debugLineNum = 376;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 377;BA.debugLine="kvs4.PutSimple(\"13\",Value)";
mostCurrent._kvs4._putsimple("13",_value);
 //BA.debugLineNum = 378;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 379;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 382;BA.debugLine="If Position=14 Then";
if (_position==14) { 
 //BA.debugLineNum = 383;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 384;BA.debugLine="kvs4.PutSimple(\"14\",Value)";
mostCurrent._kvs4._putsimple("14",_value);
 //BA.debugLineNum = 385;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 386;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 };
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 30;BA.debugLine="kvs4sub.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvs4sub._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_sub_4");
 //BA.debugLineNum = 31;BA.debugLine="Activity.LoadLayout(\"6_sub\")";
mostCurrent._activity.LoadLayout("6_sub",mostCurrent.activityBA);
 //BA.debugLineNum = 32;BA.debugLine="clist.Initialize";
mostCurrent._clist.Initialize();
 //BA.debugLineNum = 33;BA.debugLine="cb.Initialize(\"cb\")";
mostCurrent._cb.Initialize(mostCurrent.activityBA,"cb");
 //BA.debugLineNum = 34;BA.debugLine="cs.Initialize(\"cs\")";
mostCurrent._cs.Initialize(mostCurrent.activityBA,"cs");
 //BA.debugLineNum = 35;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 36;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 37;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 38;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 39;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 40;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 41;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 42;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 43;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 44;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 45;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 46;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 47;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 48;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 49;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 50;BA.debugLine="c16=mcl.md_light_blue_A700";
_c16 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 52;BA.debugLine="cc1.Initialize(c1,5dip)";
mostCurrent._cc1.Initialize(_c1,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 53;BA.debugLine="cc2.Initialize(c2,5dip)";
mostCurrent._cc2.Initialize(_c2,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 54;BA.debugLine="cc3.Initialize(c3,5dip)";
mostCurrent._cc3.Initialize(_c3,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 55;BA.debugLine="cc4.Initialize(c4,5dip)";
mostCurrent._cc4.Initialize(_c4,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 56;BA.debugLine="cc5.Initialize(c5,5dip)";
mostCurrent._cc5.Initialize(_c5,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 57;BA.debugLine="cc6.Initialize(c6,5dip)";
mostCurrent._cc6.Initialize(_c6,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 58;BA.debugLine="cc7.Initialize(c7,5dip)";
mostCurrent._cc7.Initialize(_c7,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 59;BA.debugLine="cc8.Initialize(c8,5dip)";
mostCurrent._cc8.Initialize(_c8,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 60;BA.debugLine="cc9.Initialize(c9,5dip)";
mostCurrent._cc9.Initialize(_c9,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 61;BA.debugLine="cc10.Initialize(c10,5dip)";
mostCurrent._cc10.Initialize(_c10,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 62;BA.debugLine="cc11.Initialize(c11,5dip)";
mostCurrent._cc11.Initialize(_c11,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 63;BA.debugLine="cc12.Initialize(c12,5dip)";
mostCurrent._cc12.Initialize(_c12,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 64;BA.debugLine="cc13.Initialize(c13,5dip)";
mostCurrent._cc13.Initialize(_c13,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 65;BA.debugLine="cc14.Initialize(c14,5dip)";
mostCurrent._cc14.Initialize(_c14,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 66;BA.debugLine="cc15.Initialize(c15,5dip)";
mostCurrent._cc15.Initialize(_c15,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 67;BA.debugLine="cc16.Initialize(c16,5dip)";
mostCurrent._cc16.Initialize(_c16,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 70;BA.debugLine="c_start";
_c_start();
 //BA.debugLineNum = 71;BA.debugLine="set_set";
_set_set();
 //BA.debugLineNum = 72;BA.debugLine="stor_check";
_stor_check();
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 80;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 81;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 75;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 76;BA.debugLine="stor_check";
_stor_check();
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _c_start() throws Exception{
 //BA.debugLineNum = 84;BA.debugLine="Sub c_start";
 //BA.debugLineNum = 86;BA.debugLine="cs.Prompt=\"Wähle Farbe\"";
mostCurrent._cs.setPrompt(BA.ObjectToCharSequence("Wähle Farbe"));
 //BA.debugLineNum = 87;BA.debugLine="cs.Add2(\"light blue\",cc1)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("light blue"),(android.graphics.drawable.Drawable)(mostCurrent._cc1.getObject()));
 //BA.debugLineNum = 88;BA.debugLine="cs.Add2(\"Amber\",cc2)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("Amber"),(android.graphics.drawable.Drawable)(mostCurrent._cc2.getObject()));
 //BA.debugLineNum = 89;BA.debugLine="cs.Add2(\"White(Arctic)\",cc3)'-<<<<<<change on mai";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("White(Arctic)"),(android.graphics.drawable.Drawable)(mostCurrent._cc3.getObject()));
 //BA.debugLineNum = 90;BA.debugLine="cs.Add2(\"teal\",cc4)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("teal"),(android.graphics.drawable.Drawable)(mostCurrent._cc4.getObject()));
 //BA.debugLineNum = 91;BA.debugLine="cs.Add2(\"purple(dark)\",cc5)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("purple(dark)"),(android.graphics.drawable.Drawable)(mostCurrent._cc5.getObject()));
 //BA.debugLineNum = 92;BA.debugLine="cs.Add2(\"red\",cc6)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("red"),(android.graphics.drawable.Drawable)(mostCurrent._cc6.getObject()));
 //BA.debugLineNum = 93;BA.debugLine="cs.Add2(\"indigo\",cc7)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("indigo"),(android.graphics.drawable.Drawable)(mostCurrent._cc7.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="cs.Add2(\"blue\",cc8)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("blue"),(android.graphics.drawable.Drawable)(mostCurrent._cc8.getObject()));
 //BA.debugLineNum = 95;BA.debugLine="cs.Add2(\"orange\",cc9)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("orange"),(android.graphics.drawable.Drawable)(mostCurrent._cc9.getObject()));
 //BA.debugLineNum = 96;BA.debugLine="cs.Add2(\"grey\",cc10)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("grey"),(android.graphics.drawable.Drawable)(mostCurrent._cc10.getObject()));
 //BA.debugLineNum = 97;BA.debugLine="cs.Add2(\"green\",cc11)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("green"),(android.graphics.drawable.Drawable)(mostCurrent._cc11.getObject()));
 //BA.debugLineNum = 98;BA.debugLine="cs.Add2(\"Black(Ultra)\",cc12)'-<<<<<<change on mai";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("Black(Ultra)"),(android.graphics.drawable.Drawable)(mostCurrent._cc12.getObject()));
 //BA.debugLineNum = 99;BA.debugLine="cs.Add2(\"yellow\",cc13)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("yellow"),(android.graphics.drawable.Drawable)(mostCurrent._cc13.getObject()));
 //BA.debugLineNum = 100;BA.debugLine="cs.Add2(\"cyan\",cc14)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("cyan"),(android.graphics.drawable.Drawable)(mostCurrent._cc14.getObject()));
 //BA.debugLineNum = 101;BA.debugLine="cs.Add2(\"blue grey\",cc15)";
mostCurrent._cs.Add2(BA.ObjectToCharSequence("blue grey"),(android.graphics.drawable.Drawable)(mostCurrent._cc15.getObject()));
 //BA.debugLineNum = 102;BA.debugLine="clist.Add(c1)";
mostCurrent._clist.Add((Object)(_c1));
 //BA.debugLineNum = 103;BA.debugLine="clist.Add(c2)";
mostCurrent._clist.Add((Object)(_c2));
 //BA.debugLineNum = 104;BA.debugLine="clist.Add(c3)";
mostCurrent._clist.Add((Object)(_c3));
 //BA.debugLineNum = 105;BA.debugLine="clist.Add(c4)";
mostCurrent._clist.Add((Object)(_c4));
 //BA.debugLineNum = 106;BA.debugLine="clist.Add(c5)";
mostCurrent._clist.Add((Object)(_c5));
 //BA.debugLineNum = 107;BA.debugLine="clist.Add(c6)";
mostCurrent._clist.Add((Object)(_c6));
 //BA.debugLineNum = 108;BA.debugLine="clist.Add(c7)";
mostCurrent._clist.Add((Object)(_c7));
 //BA.debugLineNum = 109;BA.debugLine="clist.Add(c8)";
mostCurrent._clist.Add((Object)(_c8));
 //BA.debugLineNum = 110;BA.debugLine="clist.Add(c9)";
mostCurrent._clist.Add((Object)(_c9));
 //BA.debugLineNum = 111;BA.debugLine="clist.Add(c10)";
mostCurrent._clist.Add((Object)(_c10));
 //BA.debugLineNum = 112;BA.debugLine="clist.Add(c11)";
mostCurrent._clist.Add((Object)(_c11));
 //BA.debugLineNum = 113;BA.debugLine="clist.Add(c12)";
mostCurrent._clist.Add((Object)(_c12));
 //BA.debugLineNum = 114;BA.debugLine="clist.Add(c13)";
mostCurrent._clist.Add((Object)(_c13));
 //BA.debugLineNum = 115;BA.debugLine="clist.Add(c14)";
mostCurrent._clist.Add((Object)(_c14));
 //BA.debugLineNum = 116;BA.debugLine="clist.Add(c15)";
mostCurrent._clist.Add((Object)(_c15));
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _cb_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Sub cb_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 135;BA.debugLine="If Checked=True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 136;BA.debugLine="kvs4sub.DeleteAll";
mostCurrent._kvs4sub._deleteall();
 //BA.debugLineNum = 137;BA.debugLine="StartService(Starter)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 139;BA.debugLine="StartService(webhost)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._webhost.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="Log(\"start\")";
anywheresoftware.b4a.keywords.Common.Log("start");
 //BA.debugLineNum = 141;BA.debugLine="ToastMessageShow(\"Services started..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Services started.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 145;BA.debugLine="If Checked=False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 146;BA.debugLine="kvs4sub.DeleteAll";
mostCurrent._kvs4sub._deleteall();
 //BA.debugLineNum = 147;BA.debugLine="kvs4sub.PutSimple(\"off\",Checked)";
mostCurrent._kvs4sub._putsimple("off",(Object)(_checked));
 //BA.debugLineNum = 148;BA.debugLine="StopService(Starter)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="StopService(webhost)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._webhost.getObject()));
 //BA.debugLineNum = 150;BA.debugLine="Log(\"Service Stop\")";
anywheresoftware.b4a.keywords.Common.Log("Service Stop");
 //BA.debugLineNum = 151;BA.debugLine="ToastMessageShow(\"Services closed!\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Services closed!"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 153;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private set As SettingUI";
mostCurrent._set = new com.batcat.settingui();
 //BA.debugLineNum = 18;BA.debugLine="Private mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 19;BA.debugLine="Private kvs4,kvs4sub As KeyValueStore";
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvs4sub = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 20;BA.debugLine="Private clist As List";
mostCurrent._clist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Private cb As CheckBox";
mostCurrent._cb = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private cs As ACSpinner";
mostCurrent._cs = new de.amberhome.objects.appcompat.ACSpinnerWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private cc1,cc2,cc3,cc4,cc5,cc6,cc7,cc8,cc9,cc10,";
mostCurrent._cc1 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc3 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc4 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc5 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc6 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc7 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc8 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc9 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc10 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc11 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc12 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc13 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc14 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc15 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._cc16 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
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
 //BA.debugLineNum = 25;BA.debugLine="Dim panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _set_set() throws Exception{
String _b = "";
 //BA.debugLineNum = 120;BA.debugLine="Sub set_set";
 //BA.debugLineNum = 121;BA.debugLine="set.Initialize(Activity,mcl.md_blue_A400,\"OpenSan";
mostCurrent._set._initialize(mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())),mostCurrent._mcl.getmd_blue_A400(),"OpenSans.ttf","Settings Menu","Choose Options");
 //BA.debugLineNum = 122;BA.debugLine="set.AddCheckbox(\"cb\",\"Service Icon\",True)";
mostCurrent._set._addcheckbox("cb","Service Icon",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 123;BA.debugLine="set.SetKeyString(\"cb\",cb_CheckedChange(True))";
mostCurrent._set._setkeystring("cb",_cb_checkedchange(anywheresoftware.b4a.keywords.Common.True));
 //BA.debugLineNum = 124;BA.debugLine="set.AddDivider";
mostCurrent._set._adddivider();
 //BA.debugLineNum = 125;BA.debugLine="For Each b As String In clist";
final anywheresoftware.b4a.BA.IterableList group5 = mostCurrent._clist;
final int groupLen5 = group5.getSize();
for (int index5 = 0;index5 < groupLen5 ;index5++){
_b = BA.ObjectToString(group5.Get(index5));
 //BA.debugLineNum = 126;BA.debugLine="set.AddSpinner(\"cs\",\"Select Backround Color:\",cli";
mostCurrent._set._addspinner("cs","Select Backround Color:",(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(mostCurrent._clist.Get((int)(Double.parseDouble(_b))))));
 }
;
 //BA.debugLineNum = 128;BA.debugLine="set.AddDivider";
mostCurrent._set._adddivider();
 //BA.debugLineNum = 129;BA.debugLine="set.AddListview(\"lv\",\"Test View\",clist)";
mostCurrent._set._addlistview("lv","Test View",mostCurrent._clist);
 //BA.debugLineNum = 130;BA.debugLine="set.AddDivider";
mostCurrent._set._adddivider();
 //BA.debugLineNum = 131;BA.debugLine="set.ApplyHeightPanel";
mostCurrent._set._applyheightpanel();
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _stor_check() throws Exception{
 //BA.debugLineNum = 156;BA.debugLine="Sub stor_check";
 //BA.debugLineNum = 157;BA.debugLine="If kvs4sub.ContainsKey(\"off\") Then";
if (mostCurrent._kvs4sub._containskey("off")) { 
 //BA.debugLineNum = 158;BA.debugLine="set.SetKeyBoolean(\"cb\",False)";
mostCurrent._set._setkeyboolean("cb",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 159;BA.debugLine="cb_CheckedChange(False)";
_cb_checkedchange(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 162;BA.debugLine="set.SetKeyBoolean(\"cb\",True)";
mostCurrent._set._setkeyboolean("cb",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 165;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 166;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 168;BA.debugLine="set.SetKeyBoolean(\"cs\",cs.SelectedIndex)";
mostCurrent._set._setkeyboolean("cs",BA.ObjectToBoolean(mostCurrent._cs.getSelectedIndex()));
 };
 //BA.debugLineNum = 171;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 172;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 173;BA.debugLine="cs.SelectedIndex=1";
mostCurrent._cs.setSelectedIndex((int) (1));
 //BA.debugLineNum = 175;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 177;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 178;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 }else {
 //BA.debugLineNum = 183;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 185;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 186;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 188;BA.debugLine="cs.SelectedIndex=3";
mostCurrent._cs.setSelectedIndex((int) (3));
 //BA.debugLineNum = 189;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 //BA.debugLineNum = 191;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 193;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 194;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 196;BA.debugLine="cs.SelectedIndex=4";
mostCurrent._cs.setSelectedIndex((int) (4));
 //BA.debugLineNum = 198;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 200;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 201;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 202;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 //BA.debugLineNum = 204;BA.debugLine="cs.SelectedIndex=5";
mostCurrent._cs.setSelectedIndex((int) (5));
 //BA.debugLineNum = 205;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 207;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 208;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 210;BA.debugLine="cs.SelectedIndex=6";
mostCurrent._cs.setSelectedIndex((int) (6));
 //BA.debugLineNum = 212;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 214;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 215;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 218;BA.debugLine="cs.SelectedIndex=7";
mostCurrent._cs.setSelectedIndex((int) (7));
 //BA.debugLineNum = 219;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 221;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 222;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 224;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 //BA.debugLineNum = 226;BA.debugLine="cs.SelectedIndex=8";
mostCurrent._cs.setSelectedIndex((int) (8));
 //BA.debugLineNum = 227;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 229;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 230;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 231;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 //BA.debugLineNum = 233;BA.debugLine="cs.SelectedIndex=9";
mostCurrent._cs.setSelectedIndex((int) (9));
 //BA.debugLineNum = 235;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 237;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 238;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 240;BA.debugLine="cs.SelectedIndex=10";
mostCurrent._cs.setSelectedIndex((int) (10));
 //BA.debugLineNum = 242;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 244;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 245;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 249;BA.debugLine="cs.SelectedIndex=11";
mostCurrent._cs.setSelectedIndex((int) (11));
 //BA.debugLineNum = 250;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 252;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 253;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 256;BA.debugLine="cs.SelectedIndex=12";
mostCurrent._cs.setSelectedIndex((int) (12));
 //BA.debugLineNum = 257;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 };
 //BA.debugLineNum = 259;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 260;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 261;BA.debugLine="cs.SelectedIndex=13";
mostCurrent._cs.setSelectedIndex((int) (13));
 //BA.debugLineNum = 264;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 };
 //BA.debugLineNum = 266;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 267;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 270;BA.debugLine="cs.SelectedIndex=14";
mostCurrent._cs.setSelectedIndex((int) (14));
 //BA.debugLineNum = 271;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 };
 //BA.debugLineNum = 273;BA.debugLine="End Sub";
return "";
}
}
