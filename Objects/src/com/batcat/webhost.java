package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class webhost extends  android.app.Service{
	public static class webhost_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, webhost.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static webhost mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return webhost.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "com.batcat", "com.batcat.webhost");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.webhost", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("** Service (webhost) Create **");
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
                    BA.LogInfo("** Service (webhost) Create **");
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
    	BA.LogInfo("** Service (webhost) Start **");
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
        BA.LogInfo("** Service (webhost) Destroy **");
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
public static anywheresoftware.b4a.net.FTPWrapper _ftp = null;
public com.batcat.main _main = null;
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
public static String  _ftp_downloadcompleted(String _serverpath,boolean _success) throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub ftp_DownloadCompleted (ServerPath As String, S";
 //BA.debugLineNum = 48;BA.debugLine="Log(ServerPath & \", Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(_serverpath+", Success="+BA.ObjectToString(_success));
 //BA.debugLineNum = 49;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 50;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage());
 }else {
 //BA.debugLineNum = 52;BA.debugLine="Log(\" Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(" Success="+BA.ObjectToString(_success));
 };
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_downloadprogress(String _serverpath,long _totaldownloaded,long _total) throws Exception{
String _s = "";
 //BA.debugLineNum = 40;BA.debugLine="Sub ftp_DownloadProgress (ServerPath As String, To";
 //BA.debugLineNum = 41;BA.debugLine="Dim s As String";
_s = "";
 //BA.debugLineNum = 42;BA.debugLine="s = \"Downloaded \" & Round(TotalDownloaded / 1000)";
_s = "Downloaded "+BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round(_totaldownloaded/(double)1000))+"KB";
 //BA.debugLineNum = 43;BA.debugLine="If Total > 0 Then s = s & \" out of \" & Round(Tota";
if (_total>0) { 
_s = _s+" out of "+BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round(_total/(double)1000))+"KB";};
 //BA.debugLineNum = 44;BA.debugLine="ToastMessageShow(\"Check for Updates.. \"&s,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Check for Updates.. "+_s),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_listcompleted(String _serverpath,boolean _success,anywheresoftware.b4a.net.FTPWrapper.FTPFileWrapper[] _folders,anywheresoftware.b4a.net.FTPWrapper.FTPFileWrapper[] _files) throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub ftp_ListCompleted (ServerPath As String, Succe";
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_start() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub ftp_start";
 //BA.debugLineNum = 26;BA.debugLine="ftp.Initialize(\"ftp\",\"battcatt.bplaced.net\",\"21\",";
_ftp.Initialize(processBA,"ftp","battcatt.bplaced.net",(int)(Double.parseDouble("21")),"battcatt_app","recall0000");
 //BA.debugLineNum = 27;BA.debugLine="ftp.DownloadFile(\"/bc.txt\",True,File.DirDefaultEx";
_ftp.DownloadFile(processBA,"/bc.txt",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data","bc.txt");
 //BA.debugLineNum = 28;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/data";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data","vid.txt")) { 
 //BA.debugLineNum = 29;BA.debugLine="ftp.UploadFile(File.DirDefaultExternal&\"/mnt/data";
_ftp.UploadFile(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data","vid.txt",anywheresoftware.b4a.keywords.Common.True,"/vid.txt");
 }else {
 //BA.debugLineNum = 31;BA.debugLine="ToastMessageShow(\"Nothing to check..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nothing to check.."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_uploadcompleted(String _serverpath,boolean _success) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub ftp_UploadCompleted (ServerPath As String, Suc";
 //BA.debugLineNum = 58;BA.debugLine="Log(ServerPath & \", Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(_serverpath+", Success="+BA.ObjectToString(_success));
 //BA.debugLineNum = 59;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 60;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage());
 }else {
 //BA.debugLineNum = 62;BA.debugLine="log(Success)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_success));
 };
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ftp As FTP";
_ftp = new anywheresoftware.b4a.net.FTPWrapper();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 13;BA.debugLine="ftp_start";
_ftp_start();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
}
