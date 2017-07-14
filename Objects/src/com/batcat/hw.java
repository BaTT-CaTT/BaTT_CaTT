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
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat20 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat40 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat60 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat80 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bat100 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _batlow = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _usb0 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _usb1 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _usb2 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _usb3 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _usbc = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _ulow = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.set2 _set2 = null;
public com.batcat.settings _settings = null;
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
public static String  _bat_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
String _temp = "";
 //BA.debugLineNum = 73;BA.debugLine="Sub bat_BatteryChanged (Level As Int, Scale As Int";
 //BA.debugLineNum = 74;BA.debugLine="Dim temp As String";
_temp = "";
 //BA.debugLineNum = 75;BA.debugLine="temp=Intent.GetExtra(\"temperature\")/10";
_temp = BA.NumberToString((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))/(double)10);
 //BA.debugLineNum = 76;BA.debugLine="rv.SetText(\"label1\",temp&\"°C\")";
_rv.SetText(processBA,"label1",BA.ObjectToCharSequence(_temp+"°C"));
 //BA.debugLineNum = 77;BA.debugLine="rv.SetText(\"label2\",Intent.GetExtra(\"voltage\")&\"";
_rv.SetText(processBA,"label2",BA.ObjectToCharSequence(BA.ObjectToString(_intent.GetExtra("voltage"))+" mV"));
 //BA.debugLineNum = 78;BA.debugLine="rv.SetText(\"label3\",Level&\"%\")";
_rv.SetText(processBA,"label3",BA.ObjectToCharSequence(BA.NumberToString(_level)+"%"));
 //BA.debugLineNum = 79;BA.debugLine="If Plugged=True Then";
if (_plugged==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 80;BA.debugLine="rv.SetImage(\"im1\",usb1)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_usb1.getObject()));
 //BA.debugLineNum = 81;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 82;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 83;BA.debugLine="If Level<=100 Then";
if (_level<=100) { 
 //BA.debugLineNum = 84;BA.debugLine="rv.SetImage(\"im1\",usb0)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_usb0.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 87;BA.debugLine="If Level<=50 Then";
if (_level<=50) { 
 //BA.debugLineNum = 88;BA.debugLine="rv.SetImage(\"im1\",usb2)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_usb2.getObject()));
 //BA.debugLineNum = 89;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 91;BA.debugLine="If Level <=5 Then";
if (_level<=5) { 
 //BA.debugLineNum = 92;BA.debugLine="rv.SetImage(\"im1\",usb3)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_usb3.getObject()));
 //BA.debugLineNum = 93;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 }else {
 //BA.debugLineNum = 96;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 97;BA.debugLine="If Level<=100 Then";
if (_level<=100) { 
 //BA.debugLineNum = 98;BA.debugLine="rv.SetImage(\"im1\",bat100)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_bat100.getObject()));
 //BA.debugLineNum = 99;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 101;BA.debugLine="If Level<=80 Then";
if (_level<=80) { 
 //BA.debugLineNum = 102;BA.debugLine="rv.SetImage(\"im1\",bat80)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_bat80.getObject()));
 //BA.debugLineNum = 103;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 105;BA.debugLine="If Level<=60 Then";
if (_level<=60) { 
 //BA.debugLineNum = 106;BA.debugLine="rv.SetImage(\"im1\",bat60)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_bat60.getObject()));
 //BA.debugLineNum = 107;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 109;BA.debugLine="If Level<=40 Then";
if (_level<=40) { 
 //BA.debugLineNum = 110;BA.debugLine="rv.SetImage(\"im1\",bat40)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_bat40.getObject()));
 //BA.debugLineNum = 111;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 113;BA.debugLine="If Level <=20 Then";
if (_level<=20) { 
 //BA.debugLineNum = 114;BA.debugLine="rv.SetImage(\"im1\",bat20)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_bat20.getObject()));
 //BA.debugLineNum = 115;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 117;BA.debugLine="If Level < 15 Then";
if (_level<15) { 
 //BA.debugLineNum = 118;BA.debugLine="rv.SetImage(\"im1\",batlow)";
_rv.SetImage(processBA,"im1",(android.graphics.Bitmap)(_batlow.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="rv.SetTextColor(\"label3\",Colors.Red)";
_rv.SetTextColor(processBA,"label3",anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 120;BA.debugLine="rv.SetTextSize(\"lablel3\",11)";
_rv.SetTextSize(processBA,"lablel3",(float) (11));
 //BA.debugLineNum = 121;BA.debugLine="rv.SetText(\"label2\",\"ACHTUNG:\")";
_rv.SetText(processBA,"label2",BA.ObjectToCharSequence("ACHTUNG:"));
 //BA.debugLineNum = 122;BA.debugLine="rv.SetText(\"label3\",\"Akku fast leer! \"&Level)";
_rv.SetText(processBA,"label3",BA.ObjectToCharSequence("Akku fast leer! "+BA.NumberToString(_level)));
 //BA.debugLineNum = 123;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 124;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 };
 //BA.debugLineNum = 127;BA.debugLine="If temp<=39 Then";
if ((double)(Double.parseDouble(_temp))<=39) { 
 //BA.debugLineNum = 128;BA.debugLine="rv.SetTextColor(\"label1\",Colors.Green)";
_rv.SetTextColor(processBA,"label1",anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 129;BA.debugLine="rv.SetText(\"label1\",temp&\"°C\")";
_rv.SetText(processBA,"label1",BA.ObjectToCharSequence(_temp+"°C"));
 //BA.debugLineNum = 130;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 132;BA.debugLine="If temp>=39 Then";
if ((double)(Double.parseDouble(_temp))>=39) { 
 //BA.debugLineNum = 133;BA.debugLine="rv.SetTextColor(\"label1\",Colors.ARGB(240,244,144";
_rv.SetTextColor(processBA,"label1",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (240),(int) (244),(int) (144),(int) (17)));
 //BA.debugLineNum = 134;BA.debugLine="rv.SetText(\"label1\",temp&\"°C\")";
_rv.SetText(processBA,"label1",BA.ObjectToCharSequence(_temp+"°C"));
 //BA.debugLineNum = 135;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 137;BA.debugLine="If temp >=45 Then";
if ((double)(Double.parseDouble(_temp))>=45) { 
 //BA.debugLineNum = 138;BA.debugLine="rv.SetTextColor(\"label1\",Colors.ARGB(230,218,15,";
_rv.SetTextColor(processBA,"label1",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (230),(int) (218),(int) (15),(int) (15)));
 //BA.debugLineNum = 139;BA.debugLine="rv.SetText(\"label1\",temp&\"°C\")";
_rv.SetText(processBA,"label1",BA.ObjectToCharSequence(_temp+"°C"));
 //BA.debugLineNum = 140;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 };
 //BA.debugLineNum = 142;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _im1_click() throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub im1_click";
 //BA.debugLineNum = 66;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _label1_click() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub label1_click";
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim rv As RemoteViews";
_rv = new anywheresoftware.b4a.objects.RemoteViewsWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Dim bat As PhoneEvents";
_bat = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 11;BA.debugLine="Dim bat20,bat40,bat60,bat80,bat100,batlow,usb0,us";
_bat20 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_bat40 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_bat60 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_bat80 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_bat100 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_batlow = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_usb0 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_usb1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_usb2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_usb3 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_usbc = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ulow = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _rv_disabled() throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub rv_Disabled";
 //BA.debugLineNum = 58;BA.debugLine="StopService(\"\")";
anywheresoftware.b4a.keywords.Common.StopService(processBA,(Object)(""));
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _rv_requestupdate() throws Exception{
 //BA.debugLineNum = 53;BA.debugLine="Sub rv_RequestUpdate";
 //BA.debugLineNum = 54;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneId _pi = null;
 //BA.debugLineNum = 15;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 16;BA.debugLine="rv = ConfigureHomeWidget(\"wid\", \"rv\", 0, \"BATT-Ca";
_rv = anywheresoftware.b4a.objects.RemoteViewsWrapper.createRemoteViews(processBA, R.layout.hw_layout, "wid","rv");
 //BA.debugLineNum = 17;BA.debugLine="Dim pi As PhoneId";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneId();
 //BA.debugLineNum = 18;BA.debugLine="bat.InitializeWithPhoneState(\"bat\",pi)";
_bat.InitializeWithPhoneState(processBA,"bat",_pi);
 //BA.debugLineNum = 19;BA.debugLine="rv.SetVisible(\"im1\",True)";
_rv.SetVisible(processBA,"im1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 20;BA.debugLine="rv.SetVisible(\"label1\",True)";
_rv.SetVisible(processBA,"label1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 21;BA.debugLine="rv.SetVisible(\"label2\",True)";
_rv.SetVisible(processBA,"label2",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 22;BA.debugLine="rv.SetVisible(\"label3\",True)";
_rv.SetVisible(processBA,"label3",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 23;BA.debugLine="rv.SetTextColor(\"label3\",Colors.ARGB(250,255,255,";
_rv.SetTextColor(processBA,"label3",anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (250),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 26;BA.debugLine="rv.SetTextSize(\"label3\",15)";
_rv.SetTextSize(processBA,"label3",(float) (15));
 //BA.debugLineNum = 27;BA.debugLine="rv.SetTextSize(\"label2\",13)";
_rv.SetTextSize(processBA,"label2",(float) (13));
 //BA.debugLineNum = 28;BA.debugLine="rv.SetTextSize(\"label1\",13)";
_rv.SetTextSize(processBA,"label1",(float) (13));
 //BA.debugLineNum = 34;BA.debugLine="bat20.Initialize(File.DirAssets,\"Battery Icons -";
_bat20.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (5).png");
 //BA.debugLineNum = 35;BA.debugLine="bat40.Initialize(File.DirAssets,\"Battery Icons -";
_bat40.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (4).png");
 //BA.debugLineNum = 36;BA.debugLine="bat60.Initialize(File.DirAssets,\"Battery Icons -";
_bat60.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (3).png");
 //BA.debugLineNum = 37;BA.debugLine="bat80.Initialize(File.DirAssets,\"Battery Icons -";
_bat80.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (2).png");
 //BA.debugLineNum = 38;BA.debugLine="bat100.Initialize(File.DirAssets,\"Battery Icons -";
_bat100.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (1).png");
 //BA.debugLineNum = 39;BA.debugLine="usb1.Initialize(File.DirAssets,\"Battery Icons - C";
_usb1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (16).png");
 //BA.debugLineNum = 40;BA.debugLine="usb0.Initialize(File.DirAssets,\"Battery Icons - C";
_usb0.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (9).png");
 //BA.debugLineNum = 41;BA.debugLine="usb2.Initialize(File.DirAssets,\"Battery Icons - C";
_usb2.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (8).png");
 //BA.debugLineNum = 42;BA.debugLine="usb3.Initialize(File.DirAssets,\"Battery Icons - C";
_usb3.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (7).png");
 //BA.debugLineNum = 43;BA.debugLine="batlow.Initialize(File.DirAssets,\"Battery Icons -";
_batlow.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (10).png");
 //BA.debugLineNum = 44;BA.debugLine="ulow.Initialize(File.DirAssets,\"Battery Icons - C";
_ulow.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery Icons - Colorful 128px (14).png");
 //BA.debugLineNum = 45;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 50;BA.debugLine="If rv.HandleWidgetEvents(StartingIntent) Then";
if (_rv.HandleWidgetEvents(processBA,(android.content.Intent)(_startingintent.getObject()))) { 
if (true) return "";};
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
}
