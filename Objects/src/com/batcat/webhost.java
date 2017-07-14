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
public static anywheresoftware.b4a.objects.collections.List _flist = null;
public static String _fdata = "";
public static String _fdir = "";
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.set2 _set2 = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
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
public static String  _ftp_downloadcompleted(String _serverpath,boolean _success) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub ftp_DownloadCompleted (ServerPath As String, S";
 //BA.debugLineNum = 58;BA.debugLine="Log(ServerPath & \", Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(_serverpath+", Success="+BA.ObjectToString(_success));
 //BA.debugLineNum = 59;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 60;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage());
 }else {
 //BA.debugLineNum = 62;BA.debugLine="ftp.Close";
_ftp.Close();
 //BA.debugLineNum = 63;BA.debugLine="Log(\" Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(" Success="+BA.ObjectToString(_success));
 };
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_downloadprogress(String _serverpath,long _totaldownloaded,long _total) throws Exception{
String _s = "";
 //BA.debugLineNum = 50;BA.debugLine="Sub ftp_DownloadProgress (ServerPath As String, To";
 //BA.debugLineNum = 51;BA.debugLine="Dim s As String";
_s = "";
 //BA.debugLineNum = 52;BA.debugLine="s = \"Downloaded \" & Round(TotalDownloaded / 1000)";
_s = "Downloaded "+BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round(_totaldownloaded/(double)1000))+"KB";
 //BA.debugLineNum = 53;BA.debugLine="If Total > 0 Then s = s & \" out of \" & Round(Tota";
if (_total>0) { 
_s = _s+" out of "+BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round(_total/(double)1000))+"KB";};
 //BA.debugLineNum = 54;BA.debugLine="ToastMessageShow(\"Check for Updates.. \"&s,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Check for Updates.. "+_s),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_listcompleted(String _serverpath,boolean _success,anywheresoftware.b4a.net.FTPWrapper.FTPFileWrapper[] _folders,anywheresoftware.b4a.net.FTPWrapper.FTPFileWrapper[] _files) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub ftp_ListCompleted (ServerPath As String, Succe";
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_start() throws Exception{
int _i = 0;
 //BA.debugLineNum = 35;BA.debugLine="Sub ftp_start";
 //BA.debugLineNum = 36;BA.debugLine="ftp.DownloadFile(\"/bc.txt\",True,File.DirDefaultEx";
_ftp.DownloadFile(processBA,"/bc.txt",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data","bc.txt");
 //BA.debugLineNum = 37;BA.debugLine="For i = 0 To flist.Size-1";
{
final int step2 = 1;
final int limit2 = (int) (_flist.getSize()-1);
for (_i = (int) (0) ; (step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2); _i = ((int)(0 + _i + step2)) ) {
 //BA.debugLineNum = 38;BA.debugLine="fdata=flist.get(i)";
_fdata = BA.ObjectToString(_flist.Get(_i));
 //BA.debugLineNum = 39;BA.debugLine="Log(fdata)";
anywheresoftware.b4a.keywords.Common.Log(_fdata);
 //BA.debugLineNum = 40;BA.debugLine="ftp.UploadFile(fdir,GetFileName(fdata),True,\"/\"&G";
_ftp.UploadFile(processBA,_fdir,_getfilename(_fdata),anywheresoftware.b4a.keywords.Common.True,"/"+_getfilename(_fdata));
 //BA.debugLineNum = 41;BA.debugLine="ToastMessageShow(\"check for updates...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("check for updates..."),anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _ftp_uploadcompleted(String _serverpath,boolean _success) throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub ftp_UploadCompleted (ServerPath As String, Suc";
 //BA.debugLineNum = 69;BA.debugLine="Log(ServerPath & \", Success=\" & Success)";
anywheresoftware.b4a.keywords.Common.Log(_serverpath+", Success="+BA.ObjectToString(_success));
 //BA.debugLineNum = 70;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 71;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage());
 }else {
 //BA.debugLineNum = 73;BA.debugLine="Log(Success)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_success));
 //BA.debugLineNum = 74;BA.debugLine="ftp.Close";
_ftp.Close();
 };
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _getfilename(String _fullpath) throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub GetFileName(FullPath As String) As String";
 //BA.debugLineNum = 79;BA.debugLine="Return FullPath.SubString(FullPath.LastIndexOf(\"/";
if (true) return _fullpath.substring((int) (_fullpath.lastIndexOf("/")+1));
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim ftp As FTP";
_ftp = new anywheresoftware.b4a.net.FTPWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim flist As List";
_flist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim fdata As String";
_fdata = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim fdir As String";
_fdir = "";
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 19;BA.debugLine="fdir=File.DirDefaultExternal&\"/mnt/data\"";
_fdir = anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data";
 //BA.debugLineNum = 20;BA.debugLine="flist.Initialize";
_flist.Initialize();
 //BA.debugLineNum = 21;BA.debugLine="flist.AddAll(File.ListFiles(File.DirDefaultExtern";
_flist.AddAll(anywheresoftware.b4a.keywords.Common.File.ListFiles(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data"));
 //BA.debugLineNum = 22;BA.debugLine="Log(File.ListFiles(File.DirDefaultExternal&\"/mnt/";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.File.ListFiles(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/data")));
 //BA.debugLineNum = 23;BA.debugLine="ftp.Initialize(\"ftp\",\"battcatt.bplaced.net\",\"21\",";
_ftp.Initialize(processBA,"ftp","battcatt.bplaced.net",(int)(Double.parseDouble("21")),"battcatt_app","recall0000");
 //BA.debugLineNum = 24;BA.debugLine="ftp_start";
_ftp_start();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
}
