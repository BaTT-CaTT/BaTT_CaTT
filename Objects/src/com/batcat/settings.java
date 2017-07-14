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

public class settings extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static settings mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.settings");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (settings).");
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
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.settings");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.settings", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (settings) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (settings) Resume **");
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
		return settings.class;
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
        BA.LogInfo("** Activity (settings) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (settings) Resume **");
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
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.keyvaluestore _kvs4sub = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _sublist = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label4 = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
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
public com.tchart.materialcolors.MaterialColors _mcl = null;
public anywheresoftware.b4a.phone.PhoneEvents _dev = null;
public de.amberhome.objects.appcompat.ACSpinnerWrapper _acspinner1 = null;
public de.amberhome.objects.appcompat.ACButtonWrapper _acbutton1 = null;
public de.amberhome.objects.appcompat.ACPopupMenuWrapper _popa = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon = null;
public de.amberhome.objects.appcompat.AppCompatBase _ac = null;
public anywheresoftware.b4a.objects.SaxParser _parser = null;
public anywheresoftware.b4a.objects.collections.List _colist = null;
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public anywheresoftware.b4a.objects.CSBuilder _cs = null;
public de.amberhome.objects.appcompat.ACCheckBoxWrapper _cb1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel3 = null;
public de.amberhome.objects.appcompat.ACSwitchCompatWrapper _arb = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uon = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _uoff = null;
public anywheresoftware.b4a.objects.LabelWrapper _label5 = null;
public static boolean _autoupdate = false;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.set2 _set2 = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.pman _pman = null;
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
public static String  _acbutton1_click() throws Exception{
 //BA.debugLineNum = 180;BA.debugLine="Sub ACButton1_Click";
 //BA.debugLineNum = 181;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 182;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 183;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _acspinner1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 365;BA.debugLine="Sub ACSpinner1_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 366;BA.debugLine="Value=ACSpinner1.SelectedIndex";
_value = (Object)(mostCurrent._acspinner1.getSelectedIndex());
 //BA.debugLineNum = 367;BA.debugLine="ACSpinner1.SelectedIndex=Position";
mostCurrent._acspinner1.setSelectedIndex(_position);
 //BA.debugLineNum = 368;BA.debugLine="ACSpinner1.Prompt=\"Select Color:\"";
mostCurrent._acspinner1.setPrompt(BA.ObjectToCharSequence("Select Color:"));
 //BA.debugLineNum = 369;BA.debugLine="ACSpinner1.DropdownBackgroundColor= ACSpinner1.T";
mostCurrent._acspinner1.setDropdownBackgroundColor(mostCurrent._acspinner1.THEME_LIGHT);
 //BA.debugLineNum = 370;BA.debugLine="If Position=0 Then";
if (_position==0) { 
 //BA.debugLineNum = 371;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 372;BA.debugLine="kvs4.PutSimple(\"0\",Value)";
mostCurrent._kvs4._putsimple("0",_value);
 //BA.debugLineNum = 373;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 374;BA.debugLine="Panel2.Color=c16";
mostCurrent._panel2.setColor(_c16);
 //BA.debugLineNum = 375;BA.debugLine="Log(\"Is in Store\")";
anywheresoftware.b4a.keywords.Common.Log("Is in Store");
 };
 //BA.debugLineNum = 377;BA.debugLine="If Position=1 Then";
if (_position==1) { 
 //BA.debugLineNum = 378;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 379;BA.debugLine="kvs4.PutSimple(\"1\",Value)";
mostCurrent._kvs4._putsimple("1",_value);
 //BA.debugLineNum = 380;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 381;BA.debugLine="Log(\"New Store\")";
anywheresoftware.b4a.keywords.Common.Log("New Store");
 //BA.debugLineNum = 382;BA.debugLine="Panel2.Color=c2";
mostCurrent._panel2.setColor(_c2);
 };
 //BA.debugLineNum = 384;BA.debugLine="If Position=2 Then";
if (_position==2) { 
 //BA.debugLineNum = 385;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 386;BA.debugLine="kvs4.PutSimple(\"2\",Value)";
mostCurrent._kvs4._putsimple("2",_value);
 //BA.debugLineNum = 387;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 388;BA.debugLine="Log(\"Now Stored\")";
anywheresoftware.b4a.keywords.Common.Log("Now Stored");
 //BA.debugLineNum = 389;BA.debugLine="Panel2.Color=c3";
mostCurrent._panel2.setColor(_c3);
 //BA.debugLineNum = 390;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 392;BA.debugLine="If Position=3 Then";
if (_position==3) { 
 //BA.debugLineNum = 393;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 394;BA.debugLine="kvs4.PutSimple(\"3\",Value)";
mostCurrent._kvs4._putsimple("3",_value);
 //BA.debugLineNum = 395;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 396;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 397;BA.debugLine="Panel2.Color=c4";
mostCurrent._panel2.setColor(_c4);
 //BA.debugLineNum = 398;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 400;BA.debugLine="If Position=4 Then";
if (_position==4) { 
 //BA.debugLineNum = 401;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 402;BA.debugLine="kvs4.PutSimple(\"4\",Value)";
mostCurrent._kvs4._putsimple("4",_value);
 //BA.debugLineNum = 403;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 404;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 405;BA.debugLine="Panel2.Color=c5";
mostCurrent._panel2.setColor(_c5);
 };
 //BA.debugLineNum = 407;BA.debugLine="If Position=5 Then";
if (_position==5) { 
 //BA.debugLineNum = 408;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 409;BA.debugLine="kvs4.PutSimple(\"5\",Value)";
mostCurrent._kvs4._putsimple("5",_value);
 //BA.debugLineNum = 410;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 411;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 412;BA.debugLine="Panel2.Color=c6";
mostCurrent._panel2.setColor(_c6);
 };
 //BA.debugLineNum = 414;BA.debugLine="If Position=6 Then";
if (_position==6) { 
 //BA.debugLineNum = 415;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 416;BA.debugLine="kvs4.PutSimple(\"6\",Value)";
mostCurrent._kvs4._putsimple("6",_value);
 //BA.debugLineNum = 417;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 418;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 419;BA.debugLine="Panel2.Color=c7";
mostCurrent._panel2.setColor(_c7);
 };
 //BA.debugLineNum = 421;BA.debugLine="If Position=7 Then";
if (_position==7) { 
 //BA.debugLineNum = 422;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 423;BA.debugLine="kvs4.PutSimple(\"7\",Value)";
mostCurrent._kvs4._putsimple("7",_value);
 //BA.debugLineNum = 424;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 425;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 426;BA.debugLine="Panel2.Color=c8";
mostCurrent._panel2.setColor(_c8);
 };
 //BA.debugLineNum = 428;BA.debugLine="If Position=8 Then";
if (_position==8) { 
 //BA.debugLineNum = 429;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 430;BA.debugLine="kvs4.PutSimple(\"8\",Value)";
mostCurrent._kvs4._putsimple("8",_value);
 //BA.debugLineNum = 431;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 432;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 433;BA.debugLine="Panel2.Color=c9";
mostCurrent._panel2.setColor(_c9);
 };
 //BA.debugLineNum = 435;BA.debugLine="If Position=9 Then";
if (_position==9) { 
 //BA.debugLineNum = 436;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 437;BA.debugLine="kvs4.PutSimple(\"9\",Value)";
mostCurrent._kvs4._putsimple("9",_value);
 //BA.debugLineNum = 438;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 439;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 440;BA.debugLine="Panel2.Color=c10";
mostCurrent._panel2.setColor(_c10);
 };
 //BA.debugLineNum = 442;BA.debugLine="If Position=10 Then";
if (_position==10) { 
 //BA.debugLineNum = 443;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 444;BA.debugLine="kvs4.PutSimple(\"10\",Value)";
mostCurrent._kvs4._putsimple("10",_value);
 //BA.debugLineNum = 445;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 446;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 447;BA.debugLine="Panel2.Color=c11";
mostCurrent._panel2.setColor(_c11);
 //BA.debugLineNum = 448;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 };
 //BA.debugLineNum = 450;BA.debugLine="If Position=11 Then";
if (_position==11) { 
 //BA.debugLineNum = 451;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 452;BA.debugLine="kvs4.PutSimple(\"11\",Value)";
mostCurrent._kvs4._putsimple("11",_value);
 //BA.debugLineNum = 453;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 454;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 455;BA.debugLine="Panel2.Color=c12";
mostCurrent._panel2.setColor(_c12);
 //BA.debugLineNum = 456;BA.debugLine="Label5.TextColor=Colors.White";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 };
 //BA.debugLineNum = 458;BA.debugLine="If Position=12 Then";
if (_position==12) { 
 //BA.debugLineNum = 459;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 460;BA.debugLine="kvs4.PutSimple(\"12\",Value)";
mostCurrent._kvs4._putsimple("12",_value);
 //BA.debugLineNum = 461;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 462;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 463;BA.debugLine="Panel2.Color=c13";
mostCurrent._panel2.setColor(_c13);
 };
 //BA.debugLineNum = 465;BA.debugLine="If Position=13 Then";
if (_position==13) { 
 //BA.debugLineNum = 466;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 467;BA.debugLine="kvs4.PutSimple(\"13\",Value)";
mostCurrent._kvs4._putsimple("13",_value);
 //BA.debugLineNum = 468;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 469;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 470;BA.debugLine="Panel2.Color=c14";
mostCurrent._panel2.setColor(_c14);
 };
 //BA.debugLineNum = 472;BA.debugLine="If Position=14 Then";
if (_position==14) { 
 //BA.debugLineNum = 473;BA.debugLine="kvs4.DeleteAll";
mostCurrent._kvs4._deleteall();
 //BA.debugLineNum = 474;BA.debugLine="kvs4.PutSimple(\"14\",Value)";
mostCurrent._kvs4._putsimple("14",_value);
 //BA.debugLineNum = 475;BA.debugLine="ToastMessageShow(Value,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_value),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 476;BA.debugLine="Log(\"Stored ---\")";
anywheresoftware.b4a.keywords.Common.Log("Stored ---");
 //BA.debugLineNum = 477;BA.debugLine="Panel2.Color=c15";
mostCurrent._panel2.setColor(_c15);
 };
 //BA.debugLineNum = 479;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 58;BA.debugLine="Activity.LoadLayout(\"6\")";
mostCurrent._activity.LoadLayout("6",mostCurrent.activityBA);
 //BA.debugLineNum = 59;BA.debugLine="Activity.color=Colors.ARGB(200,255,255,255)";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (200),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 61;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 62;BA.debugLine="kvs4sub.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvs4sub._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_sub_4");
 //BA.debugLineNum = 63;BA.debugLine="dev.Initialize(\"dev\")";
mostCurrent._dev.Initialize(processBA,"dev");
 //BA.debugLineNum = 64;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 65;BA.debugLine="parser.Initialize";
mostCurrent._parser.Initialize(processBA);
 };
 //BA.debugLineNum = 67;BA.debugLine="uon=LoadBitmap(File.DirAssets,\"son.png\")";
mostCurrent._uon = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"son.png");
 //BA.debugLineNum = 68;BA.debugLine="uoff=LoadBitmap(File.DirAssets,\"soff.png\")";
mostCurrent._uoff = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"soff.png");
 //BA.debugLineNum = 69;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 70;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets,\"ic_data_";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_data_usage_black_48dp.png").getObject()));
 //BA.debugLineNum = 72;BA.debugLine="popa.Initialize(\"popa\",Panel3)";
mostCurrent._popa.Initialize(mostCurrent.activityBA,"popa",(android.view.View)(mostCurrent._panel3.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="colist.Initialize";
mostCurrent._colist.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="popa.AddMenuItem(0,\"Save\",bd)";
mostCurrent._popa.AddMenuItem((int) (0),BA.ObjectToCharSequence("Save"),(android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="nativeMe.InitializeContext";
mostCurrent._nativeme.InitializeContext(processBA);
 //BA.debugLineNum = 88;BA.debugLine="c1=mcl.md_light_blue_A400";
_c1 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 89;BA.debugLine="c2=mcl.md_amber_A400";
_c2 = mostCurrent._mcl.getmd_amber_A400();
 //BA.debugLineNum = 90;BA.debugLine="c3=mcl.md_white_1000";
_c3 = mostCurrent._mcl.getmd_white_1000();
 //BA.debugLineNum = 91;BA.debugLine="c4=mcl.md_teal_A400";
_c4 = mostCurrent._mcl.getmd_teal_A400();
 //BA.debugLineNum = 92;BA.debugLine="c5=mcl.md_deep_purple_A400";
_c5 = mostCurrent._mcl.getmd_deep_purple_A400();
 //BA.debugLineNum = 93;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 94;BA.debugLine="c7=mcl.md_indigo_A400";
_c7 = mostCurrent._mcl.getmd_indigo_A400();
 //BA.debugLineNum = 95;BA.debugLine="c8=mcl.md_blue_A400";
_c8 = mostCurrent._mcl.getmd_blue_A400();
 //BA.debugLineNum = 96;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 97;BA.debugLine="c10=mcl.md_grey_600";
_c10 = mostCurrent._mcl.getmd_grey_600();
 //BA.debugLineNum = 98;BA.debugLine="c11=mcl.md_green_A400";
_c11 = mostCurrent._mcl.getmd_green_A400();
 //BA.debugLineNum = 99;BA.debugLine="c12=mcl.md_black_1000";
_c12 = mostCurrent._mcl.getmd_black_1000();
 //BA.debugLineNum = 100;BA.debugLine="c13=mcl.md_light_green_A400";
_c13 = mostCurrent._mcl.getmd_light_green_A400();
 //BA.debugLineNum = 101;BA.debugLine="c14=mcl.md_cyan_A400";
_c14 = mostCurrent._mcl.getmd_cyan_A400();
 //BA.debugLineNum = 102;BA.debugLine="c15=mcl.md_blue_grey_400";
_c15 = mostCurrent._mcl.getmd_blue_grey_400();
 //BA.debugLineNum = 103;BA.debugLine="c16=mcl.md_light_blue_A400";
_c16 = mostCurrent._mcl.getmd_light_blue_A400();
 //BA.debugLineNum = 105;BA.debugLine="cc1.Initialize(c1,5dip)";
mostCurrent._cc1.Initialize(_c1,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 106;BA.debugLine="cc2.Initialize(c2,5dip)";
mostCurrent._cc2.Initialize(_c2,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 107;BA.debugLine="cc3.Initialize(c3,5dip)";
mostCurrent._cc3.Initialize(_c3,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 108;BA.debugLine="cc4.Initialize(c4,5dip)";
mostCurrent._cc4.Initialize(_c4,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 109;BA.debugLine="cc5.Initialize(c5,5dip)";
mostCurrent._cc5.Initialize(_c5,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 110;BA.debugLine="cc6.Initialize(c6,5dip)";
mostCurrent._cc6.Initialize(_c6,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 111;BA.debugLine="cc7.Initialize(c7,5dip)";
mostCurrent._cc7.Initialize(_c7,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 112;BA.debugLine="cc8.Initialize(c8,5dip)";
mostCurrent._cc8.Initialize(_c8,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 113;BA.debugLine="cc9.Initialize(c9,5dip)";
mostCurrent._cc9.Initialize(_c9,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 114;BA.debugLine="cc10.Initialize(c10,5dip)";
mostCurrent._cc10.Initialize(_c10,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 115;BA.debugLine="cc11.Initialize(c11,5dip)";
mostCurrent._cc11.Initialize(_c11,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 116;BA.debugLine="cc12.Initialize(c12,5dip)";
mostCurrent._cc12.Initialize(_c12,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 117;BA.debugLine="cc13.Initialize(c13,5dip)";
mostCurrent._cc13.Initialize(_c13,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 118;BA.debugLine="cc14.Initialize(c14,5dip)";
mostCurrent._cc14.Initialize(_c14,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 119;BA.debugLine="cc15.Initialize(c15,5dip)";
mostCurrent._cc15.Initialize(_c15,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 120;BA.debugLine="cc16.Initialize(c16,5dip)";
mostCurrent._cc16.Initialize(_c16,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 123;BA.debugLine="ACSpinner1.Prompt=\"Wähle Farbe\"";
mostCurrent._acspinner1.setPrompt(BA.ObjectToCharSequence("Wähle Farbe"));
 //BA.debugLineNum = 125;BA.debugLine="ACSpinner1.Add2(\"light blue\",cc1)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("light blue"),(android.graphics.drawable.Drawable)(mostCurrent._cc1.getObject()));
 //BA.debugLineNum = 126;BA.debugLine="ACSpinner1.Add2(\"amber\",cc2)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("amber"),(android.graphics.drawable.Drawable)(mostCurrent._cc2.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="ACSpinner1.Add2(\"white(Arctic)\",cc3)'-<<<<<<chang";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("white(Arctic)"),(android.graphics.drawable.Drawable)(mostCurrent._cc3.getObject()));
 //BA.debugLineNum = 128;BA.debugLine="ACSpinner1.Add2(\"teal\",cc4)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("teal"),(android.graphics.drawable.Drawable)(mostCurrent._cc4.getObject()));
 //BA.debugLineNum = 129;BA.debugLine="ACSpinner1.Add2(\"purple(dark)\",cc5)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("purple(dark)"),(android.graphics.drawable.Drawable)(mostCurrent._cc5.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="ACSpinner1.Add2(\"red\",cc6)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("red"),(android.graphics.drawable.Drawable)(mostCurrent._cc6.getObject()));
 //BA.debugLineNum = 131;BA.debugLine="ACSpinner1.Add2(\"indigo\",cc7)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("indigo"),(android.graphics.drawable.Drawable)(mostCurrent._cc7.getObject()));
 //BA.debugLineNum = 132;BA.debugLine="ACSpinner1.Add2(\"blue\",cc8)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("blue"),(android.graphics.drawable.Drawable)(mostCurrent._cc8.getObject()));
 //BA.debugLineNum = 133;BA.debugLine="ACSpinner1.Add2(\"orange\",cc9)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("orange"),(android.graphics.drawable.Drawable)(mostCurrent._cc9.getObject()));
 //BA.debugLineNum = 134;BA.debugLine="ACSpinner1.Add2(\"grey\",cc10)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("grey"),(android.graphics.drawable.Drawable)(mostCurrent._cc10.getObject()));
 //BA.debugLineNum = 135;BA.debugLine="ACSpinner1.Add2(\"green\",cc11)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("green"),(android.graphics.drawable.Drawable)(mostCurrent._cc11.getObject()));
 //BA.debugLineNum = 136;BA.debugLine="ACSpinner1.Add2(\"black(Ultra)\",cc12)'-<<<<<<chang";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("black(Ultra)"),(android.graphics.drawable.Drawable)(mostCurrent._cc12.getObject()));
 //BA.debugLineNum = 137;BA.debugLine="ACSpinner1.Add2(\"light green\",cc13)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("light green"),(android.graphics.drawable.Drawable)(mostCurrent._cc13.getObject()));
 //BA.debugLineNum = 138;BA.debugLine="ACSpinner1.Add2(\"cyan\",cc14)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("cyan"),(android.graphics.drawable.Drawable)(mostCurrent._cc14.getObject()));
 //BA.debugLineNum = 139;BA.debugLine="ACSpinner1.Add2(\"blue grey\",cc15)";
mostCurrent._acspinner1.Add2(BA.ObjectToCharSequence("blue grey"),(android.graphics.drawable.Drawable)(mostCurrent._cc15.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="ACSpinner1.Color=Colors.Transparent";
mostCurrent._acspinner1.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 142;BA.debugLine="ACSpinner1.TextColor=mcl.md_black_1000";
mostCurrent._acspinner1.setTextColor(mostCurrent._mcl.getmd_black_1000());
 //BA.debugLineNum = 143;BA.debugLine="If StateManager.RestoreState(Activity, \"Main\", 0)";
if (mostCurrent._statemanager._restorestate(mostCurrent.activityBA,mostCurrent._activity,"Main",(int) (0))==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 145;BA.debugLine="arb.Checked=True";
mostCurrent._arb.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="ACSpinner1_ItemClick(0,0)";
_acspinner1_itemclick((int) (0),(Object)(0));
 };
 //BA.debugLineNum = 148;BA.debugLine="AutoUpdate = StateManager.GetSetting2(\"AutoUpdate";
_autoupdate = BA.ObjectToBoolean(mostCurrent._statemanager._getsetting2(mostCurrent.activityBA,"AutoUpdate",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.False)));
 //BA.debugLineNum = 152;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 171;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 };
 //BA.debugLineNum = 176;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 177;BA.debugLine="Return(False)";
if (true) return (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 161;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 162;BA.debugLine="StateManager.SaveState(Activity, \"Main\")";
mostCurrent._statemanager._savestate(mostCurrent.activityBA,mostCurrent._activity,"Main");
 };
 //BA.debugLineNum = 164;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 //BA.debugLineNum = 165;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 166;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 157;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _arb_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 194;BA.debugLine="Sub arb_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 195;BA.debugLine="If Checked=True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 196;BA.debugLine="kvs4sub.DeleteAll";
mostCurrent._kvs4sub._deleteall();
 //BA.debugLineNum = 197;BA.debugLine="StartService(Starter)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 199;BA.debugLine="StartService(webhost)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._webhost.getObject()));
 //BA.debugLineNum = 200;BA.debugLine="Log(\"start\")";
anywheresoftware.b4a.keywords.Common.Log("start");
 //BA.debugLineNum = 201;BA.debugLine="ToastMessageShow(\"Services started..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Services started.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 205;BA.debugLine="If Checked=False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 206;BA.debugLine="kvs4sub.DeleteAll";
mostCurrent._kvs4sub._deleteall();
 //BA.debugLineNum = 207;BA.debugLine="kvs4sub.PutSimple(\"off\",Checked)";
mostCurrent._kvs4sub._putsimple("off",(Object)(_checked));
 //BA.debugLineNum = 208;BA.debugLine="StopService(Starter)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 209;BA.debugLine="StopService(webhost)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._webhost.getObject()));
 //BA.debugLineNum = 210;BA.debugLine="Log(\"Service Stop\")";
anywheresoftware.b4a.keywords.Common.Log("Service Stop");
 //BA.debugLineNum = 211;BA.debugLine="ToastMessageShow(\"Services closed!\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Services closed!"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 213;BA.debugLine="StateManager.SaveSettings";
mostCurrent._statemanager._savesettings(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 19;BA.debugLine="Private kvs4,kvs4sub As KeyValueStore";
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvs4sub = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 20;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private sublist As ScrollView";
mostCurrent._sublist = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private Label3 As Label";
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Label4 As Label";
mostCurrent._label4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 28;BA.debugLine="Dim cc1,cc2,cc3,cc4,cc5,cc6,cc7,cc8,cc9,cc10,cc11";
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
 //BA.debugLineNum = 29;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
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
 //BA.debugLineNum = 30;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 31;BA.debugLine="Dim dev As PhoneEvents";
mostCurrent._dev = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 35;BA.debugLine="Private ACSpinner1 As ACSpinner";
mostCurrent._acspinner1 = new de.amberhome.objects.appcompat.ACSpinnerWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private ACButton1 As ACButton";
mostCurrent._acbutton1 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim popa As ACPopupMenu";
mostCurrent._popa = new de.amberhome.objects.appcompat.ACPopupMenuWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim icon As Bitmap";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private ac As AppCompat";
mostCurrent._ac = new de.amberhome.objects.appcompat.AppCompatBase();
 //BA.debugLineNum = 41;BA.debugLine="Dim parser As SaxParser";
mostCurrent._parser = new anywheresoftware.b4a.objects.SaxParser();
 //BA.debugLineNum = 42;BA.debugLine="Dim colist As List";
mostCurrent._colist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 43;BA.debugLine="Dim nativeMe As JavaObject";
mostCurrent._nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 44;BA.debugLine="Dim cs As CSBuilder";
mostCurrent._cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 45;BA.debugLine="Private cb1 As ACCheckBox";
mostCurrent._cb1 = new de.amberhome.objects.appcompat.ACCheckBoxWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private Panel3 As Panel";
mostCurrent._panel3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private arb As ACSwitch";
mostCurrent._arb = new de.amberhome.objects.appcompat.ACSwitchCompatWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Dim uon,uoff As Bitmap";
mostCurrent._uon = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._uoff = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private Label5 As Label";
mostCurrent._label5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Dim AutoUpdate As Boolean";
_autoupdate = false;
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _popa_itemclicked(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 186;BA.debugLine="Sub popa_ItemClicked (Item As ACMenuItem)";
 //BA.debugLineNum = 187;BA.debugLine="If Item = popa.GetItem(0) Then";
if ((_item).equals((android.view.MenuItem)(mostCurrent._popa.GetItem((int) (0))))) { 
 //BA.debugLineNum = 188;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 189;BA.debugLine="SetAnimation.setanimati(\"extra_in\", \"extra_out\")";
mostCurrent._setanimation._setanimati(mostCurrent.activityBA,"extra_in","extra_out");
 };
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 232;BA.debugLine="If kvs4sub.ContainsKey(\"off\") Then";
if (mostCurrent._kvs4sub._containskey("off")) { 
 //BA.debugLineNum = 233;BA.debugLine="arb.Checked=False";
mostCurrent._arb.setChecked(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 236;BA.debugLine="arb.Checked=True";
mostCurrent._arb.setChecked(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 240;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 241;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 243;BA.debugLine="ACSpinner1.SelectedIndex=0";
mostCurrent._acspinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 244;BA.debugLine="Panel2.Color=c1";
mostCurrent._panel2.setColor(_c1);
 };
 //BA.debugLineNum = 247;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 248;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 249;BA.debugLine="ACSpinner1.SelectedIndex=1";
mostCurrent._acspinner1.setSelectedIndex((int) (1));
 //BA.debugLineNum = 250;BA.debugLine="Panel2.Color=c2";
mostCurrent._panel2.setColor(_c2);
 //BA.debugLineNum = 251;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 253;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 254;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 255;BA.debugLine="Panel2.Color=c3";
mostCurrent._panel2.setColor(_c3);
 //BA.debugLineNum = 258;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 260;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 261;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 262;BA.debugLine="Panel2.Color=c4";
mostCurrent._panel2.setColor(_c4);
 //BA.debugLineNum = 263;BA.debugLine="ACSpinner1.SelectedIndex=3";
mostCurrent._acspinner1.setSelectedIndex((int) (3));
 //BA.debugLineNum = 264;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 //BA.debugLineNum = 266;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 268;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 269;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 271;BA.debugLine="ACSpinner1.SelectedIndex=4";
mostCurrent._acspinner1.setSelectedIndex((int) (4));
 //BA.debugLineNum = 272;BA.debugLine="Panel2.Color=c5";
mostCurrent._panel2.setColor(_c5);
 //BA.debugLineNum = 274;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 276;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 277;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 278;BA.debugLine="Activity.color=c6";
mostCurrent._activity.setColor(_c6);
 //BA.debugLineNum = 279;BA.debugLine="Panel2.Color=c6";
mostCurrent._panel2.setColor(_c6);
 //BA.debugLineNum = 280;BA.debugLine="ACSpinner1.SelectedIndex=5";
mostCurrent._acspinner1.setSelectedIndex((int) (5));
 }else {
 //BA.debugLineNum = 282;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 284;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 285;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 287;BA.debugLine="ACSpinner1.SelectedIndex=6";
mostCurrent._acspinner1.setSelectedIndex((int) (6));
 //BA.debugLineNum = 288;BA.debugLine="Panel2.Color=c7";
mostCurrent._panel2.setColor(_c7);
 }else {
 //BA.debugLineNum = 290;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 292;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 293;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 294;BA.debugLine="Panel2.Color=c8";
mostCurrent._panel2.setColor(_c8);
 //BA.debugLineNum = 296;BA.debugLine="ACSpinner1.SelectedIndex=7";
mostCurrent._acspinner1.setSelectedIndex((int) (7));
 }else {
 //BA.debugLineNum = 298;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 300;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 301;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 303;BA.debugLine="Activity.color=c9";
mostCurrent._activity.setColor(_c9);
 //BA.debugLineNum = 304;BA.debugLine="Panel2.Color=c9";
mostCurrent._panel2.setColor(_c9);
 //BA.debugLineNum = 305;BA.debugLine="ACSpinner1.SelectedIndex=8";
mostCurrent._acspinner1.setSelectedIndex((int) (8));
 }else {
 //BA.debugLineNum = 307;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 309;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 310;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 311;BA.debugLine="Activity.color=c10";
mostCurrent._activity.setColor(_c10);
 //BA.debugLineNum = 312;BA.debugLine="Panel2.Color=c10";
mostCurrent._panel2.setColor(_c10);
 //BA.debugLineNum = 313;BA.debugLine="ACSpinner1.SelectedIndex=9";
mostCurrent._acspinner1.setSelectedIndex((int) (9));
 }else {
 //BA.debugLineNum = 316;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 318;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 319;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 320;BA.debugLine="Panel2.Color=c11";
mostCurrent._panel2.setColor(_c11);
 //BA.debugLineNum = 321;BA.debugLine="ACSpinner1.SelectedIndex=10";
mostCurrent._acspinner1.setSelectedIndex((int) (10));
 }else {
 //BA.debugLineNum = 324;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 326;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 327;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 329;BA.debugLine="Panel2.Color=c12";
mostCurrent._panel2.setColor(_c12);
 //BA.debugLineNum = 330;BA.debugLine="Label5.TextColor=Colors.White";
mostCurrent._label5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 331;BA.debugLine="ACSpinner1.SelectedIndex=11";
mostCurrent._acspinner1.setSelectedIndex((int) (11));
 //BA.debugLineNum = 332;BA.debugLine="Activity.color=c12";
mostCurrent._activity.setColor(_c12);
 }else {
 //BA.debugLineNum = 334;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 336;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 337;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 339;BA.debugLine="Panel2.Color=c13";
mostCurrent._panel2.setColor(_c13);
 //BA.debugLineNum = 340;BA.debugLine="ACSpinner1.SelectedIndex=12";
mostCurrent._acspinner1.setSelectedIndex((int) (12));
 //BA.debugLineNum = 341;BA.debugLine="Activity.color=c13";
mostCurrent._activity.setColor(_c13);
 }else {
 //BA.debugLineNum = 343;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 345;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 346;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 347;BA.debugLine="ACSpinner1.SelectedIndex=13";
mostCurrent._acspinner1.setSelectedIndex((int) (13));
 //BA.debugLineNum = 348;BA.debugLine="Panel2.Color=c14";
mostCurrent._panel2.setColor(_c14);
 //BA.debugLineNum = 350;BA.debugLine="Activity.color=c14";
mostCurrent._activity.setColor(_c14);
 }else {
 //BA.debugLineNum = 352;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 354;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 355;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 357;BA.debugLine="Panel2.Color=c15";
mostCurrent._panel2.setColor(_c15);
 //BA.debugLineNum = 358;BA.debugLine="ACSpinner1.SelectedIndex=14";
mostCurrent._acspinner1.setSelectedIndex((int) (14));
 //BA.debugLineNum = 359;BA.debugLine="Activity.color=c15";
mostCurrent._activity.setColor(_c15);
 }else {
 //BA.debugLineNum = 361;BA.debugLine="Activity.color=c4";
mostCurrent._activity.setColor(_c4);
 };
 //BA.debugLineNum = 363;BA.debugLine="End Sub";
return "";
}
public static String  _tab_settings() throws Exception{
anywheresoftware.b4a.objects.ListViewWrapper _liv = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _draw = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bdraw = null;
 //BA.debugLineNum = 219;BA.debugLine="Sub tab_settings";
 //BA.debugLineNum = 220;BA.debugLine="Dim liv As ListView";
_liv = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 221;BA.debugLine="Dim draw As BitmapDrawable";
_draw = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 222;BA.debugLine="Dim bdraw As Bitmap";
_bdraw = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 223;BA.debugLine="bdraw.Initialize(File.DirAssets,\"ic_folder_specia";
_bdraw.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_folder_special_black_48dp.png");
 //BA.debugLineNum = 224;BA.debugLine="draw.Initialize(bdraw)";
_draw.Initialize((android.graphics.Bitmap)(_bdraw.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="liv.Initialize(\"liv\")";
_liv.Initialize(mostCurrent.activityBA,"liv");
 //BA.debugLineNum = 226;BA.debugLine="liv.Enabled=True";
_liv.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 227;BA.debugLine="liv.AddSingleLine(\"Text\")";
_liv.AddSingleLine(BA.ObjectToCharSequence("Text"));
 //BA.debugLineNum = 228;BA.debugLine="liv.AddTwoLines(\"Text\",\"Sub text..\")";
_liv.AddTwoLines(BA.ObjectToCharSequence("Text"),BA.ObjectToCharSequence("Sub text.."));
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
}
