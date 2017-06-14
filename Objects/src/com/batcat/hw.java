package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class hw extends  android.app.Service{
	public static class hw_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, hw.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static hw mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return hw.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "com.batcat", "com.batcat.hw");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.hw", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("** Service (hw) Create **");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
			if (ServiceHelper.StarterHelper.waitForLayout != null)
				BA.handler.post(ServiceHelper.StarterHelper.waitForLayout);
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA))
			handleStart(intent);
		else {
			ServiceHelper.StarterHelper.waitForLayout = new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (hw) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
				}
			};
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (hw) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = new anywheresoftware.b4a.objects.IntentWrapper();
    			if (intent != null) {
    				if (intent.hasExtra("b4a_internal_intent"))
    					iw.setObject((android.content.Intent) intent.getParcelableExtra("b4a_internal_intent"));
    				else
    					iw.setObject(intent);
    			}
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        BA.LogInfo("** Service (hw) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
        processBA.runHook("ondestroy", this, null);
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.RemoteViewsWrapper _rv = null;
public static anywheresoftware.b4a.phone.PhoneEvents _bat = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.settings _settings = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.charts _charts = null;
public static String  _bat_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
String _temp = "";
 //BA.debugLineNum = 54;BA.debugLine="Sub bat_BatteryChanged (Level As Int, Scale As Int";
 //BA.debugLineNum = 55;BA.debugLine="Dim temp As String";
_temp = "";
 //BA.debugLineNum = 56;BA.debugLine="temp=Intent.GetExtra(\"temperature\")/10";
_temp = BA.NumberToString((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))/(double)10);
 //BA.debugLineNum = 57;BA.debugLine="rv.SetProgress(\"Progressbar1\",Level)";
_rv.SetProgress(processBA,"Progressbar1",_level);
 //BA.debugLineNum = 58;BA.debugLine="rv.SetText(\"label1\",temp&\"°C\")";
_rv.SetText(processBA,"label1",BA.ObjectToCharSequence(_temp+"°C"));
 //BA.debugLineNum = 59;BA.debugLine="rv.SetText(\"label2\",Intent.GetExtra(\"technology\")";
_rv.SetText(processBA,"label2",BA.ObjectToCharSequence(_intent.GetExtra("technology")));
 //BA.debugLineNum = 60;BA.debugLine="rv.SetText(\"label3\",Level&\"%\")";
_rv.SetText(processBA,"label3",BA.ObjectToCharSequence(BA.NumberToString(_level)+"%"));
 //BA.debugLineNum = 61;BA.debugLine="If Plugged=True Then";
if (_plugged==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 62;BA.debugLine="rv.SetImage(\"im1\",LoadBitmap(File.DirAssets, \"Ba";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()));
 //BA.debugLineNum = 63;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 65;BA.debugLine="If Level < 15 Then";
if (_level<15) { 
 //BA.debugLineNum = 66;BA.debugLine="rv.SetImage(\"im1\",LoadBitmap(File.DirAssets, \"Ba";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()));
 //BA.debugLineNum = 67;BA.debugLine="rv.SetTextColor(\"label3\",Colors.ARGB(150,255,255";
_rv.SetTextColor(processBA,"label3",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 68;BA.debugLine="rv.SetTextSize(\"lablel3\",18)";
_rv.SetTextSize(processBA,"lablel3",(float) (18));
 //BA.debugLineNum = 69;BA.debugLine="rv.SetText(\"label2\",\"ATTENTION!\")";
_rv.SetText(processBA,"label2",BA.ObjectToCharSequence("ATTENTION!"));
 //BA.debugLineNum = 70;BA.debugLine="rv.SetText(\"label3\",\"Battery low! \"&Level)";
_rv.SetText(processBA,"label3",BA.ObjectToCharSequence("Battery low! "+BA.NumberToString(_level)));
 //BA.debugLineNum = 71;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _im1_click() throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub im1_click";
 //BA.debugLineNum = 47;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _label1_click() throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub label1_click";
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim rv As RemoteViews";
_rv = new anywheresoftware.b4a.objects.RemoteViewsWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Dim bat As PhoneEvents";
_bat = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _rv_disabled() throws Exception{
 //BA.debugLineNum = 38;BA.debugLine="Sub rv_Disabled";
 //BA.debugLineNum = 39;BA.debugLine="StopService(\"\")";
anywheresoftware.b4a.keywords.Common.StopService(processBA,(Object)(""));
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _rv_requestupdate() throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub rv_RequestUpdate";
 //BA.debugLineNum = 35;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneId _pi = null;
 //BA.debugLineNum = 12;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 13;BA.debugLine="rv = ConfigureHomeWidget(\"wid\", \"rv\", 30, \"BATT-C";
_rv = anywheresoftware.b4a.objects.RemoteViewsWrapper.createRemoteViews(processBA, R.layout.hw_layout, "wid","rv");
 //BA.debugLineNum = 15;BA.debugLine="rv.SetTextColor(\"label3\",Colors.ARGB(230,0,0,0))";
_rv.SetTextColor(processBA,"label3",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (230),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 16;BA.debugLine="rv.SetTextColor(\"label2\",Colors.ARGB(180,0,0,0))";
_rv.SetTextColor(processBA,"label2",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (180),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 17;BA.debugLine="rv.SetTextColor(\"label1\",Colors.ARGB(180,0,0,0))";
_rv.SetTextColor(processBA,"label1",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (180),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 18;BA.debugLine="rv.SetTextSize(\"label3\",15)";
_rv.SetTextSize(processBA,"label3",(float) (15));
 //BA.debugLineNum = 19;BA.debugLine="rv.SetTextSize(\"label2\",13)";
_rv.SetTextSize(processBA,"label2",(float) (13));
 //BA.debugLineNum = 20;BA.debugLine="rv.SetTextSize(\"label1\",13)";
_rv.SetTextSize(processBA,"label1",(float) (13));
 //BA.debugLineNum = 22;BA.debugLine="rv.SetImage(\"im1\",LoadBitmap(File.DirAssets, \"Bat";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()));
 //BA.debugLineNum = 23;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 24;BA.debugLine="rv.SetVisible(\"Progressbar1\",True)";
_rv.SetVisible(processBA,"Progressbar1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 25;BA.debugLine="Dim pi As PhoneId";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneId();
 //BA.debugLineNum = 26;BA.debugLine="bat.InitializeWithPhoneState(\"bat\",pi)";
_bat.InitializeWithPhoneState(processBA,"bat",_pi);
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 31;BA.debugLine="If rv.HandleWidgetEvents(StartingIntent) Then";
if (_rv.HandleWidgetEvents(processBA,(android.content.Intent)(_startingintent.getObject()))) { 
if (true) return "";};
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
}
