package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "com.batcat", "com.batcat.starter");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("** Service (starter) Create **");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
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
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
				}
			};
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
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
        BA.LogInfo("** Service (starter) Destroy **");
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
public static anywheresoftware.b4a.objects.NotificationWrapper _snotif = null;
public static anywheresoftware.b4a.phone.PhoneEvents _device = null;
public static anywheresoftware.b4a.objects.collections.List _list1 = null;
public static com.batcat.keyvaluestore _kvs2 = null;
public static com.batcat.keyvaluestore _kvs3 = null;
public static com.batcat.keyvaluestore _kvs4 = null;
public static com.batcat.keyvaluestore _kvstemp = null;
public static com.batcat.keyvaluestore _kvsvolt = null;
public static String _date = "";
public static String _time = "";
public static String _tt = "";
public static com.batcat.batut _bat = null;
public static int _level1 = 0;
public static anywheresoftware.b4a.objects.collections.List _kl = null;
public static anywheresoftware.b4a.objects.collections.List _dt = null;
public static anywheresoftware.b4a.objects.collections.List _nl = null;
public static anywheresoftware.b4a.objects.CSBuilder _cs = null;
public static String _dir = "";
public static String _labex = "";
public static anywheresoftware.b4a.sql.SQL _sql = null;
public static anywheresoftware.b4a.objects.collections.Map _m = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 80;BA.debugLine="ToastMessageShow(\"Status: Error Service Stop!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Status: Error Service Stop!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 81;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return false;
}
public static String  _device_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
int _acc = 0;
int _ausb = 0;
String _us = "";
String _ac = "";
int _temp = 0;
int _volt = 0;
int _volt1 = 0;
int _temp2 = 0;
int _status = 0;
int _g = 0;
int _vo = 0;
int _rst = 0;
int _rl = 0;
int _rm = 0;
int _val = 0;
int _hours = 0;
int _minutes = 0;
int _v = 0;
int _days = 0;
int _sval = 0;
 //BA.debugLineNum = 90;BA.debugLine="Sub device_BatteryChanged (Level As Int, Scale As";
 //BA.debugLineNum = 91;BA.debugLine="Dim acc,ausb As Int";
_acc = 0;
_ausb = 0;
 //BA.debugLineNum = 92;BA.debugLine="Dim us,ac As String";
_us = "";
_ac = "";
 //BA.debugLineNum = 93;BA.debugLine="acc=bat.BatteryInformation(10)";
_acc = _bat._getbatteryinformation()[(int) (10)];
 //BA.debugLineNum = 94;BA.debugLine="ausb=bat.BatteryInformation(9)";
_ausb = _bat._getbatteryinformation()[(int) (9)];
 //BA.debugLineNum = 95;BA.debugLine="If acc = 1 Then";
if (_acc==1) { 
 //BA.debugLineNum = 96;BA.debugLine="ac=\"AC - Kabel: \"";
_ac = "AC - Kabel: ";
 }else {
 //BA.debugLineNum = 98;BA.debugLine="ac=\"USB - Kabel: \"";
_ac = "USB - Kabel: ";
 };
 //BA.debugLineNum = 100;BA.debugLine="time=DateTime.Time(DateTime.Now)";
_time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 101;BA.debugLine="Dim temp,volt,volt1,temp2 As Int";
_temp = 0;
_volt = 0;
_volt1 = 0;
_temp2 = 0;
 //BA.debugLineNum = 102;BA.debugLine="volt1=Intent.GetExtra(\"voltage\") /1000";
_volt1 = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 103;BA.debugLine="temp2=Intent.GetExtra(\"temperature\") /10";
_temp2 = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))/(double)10);
 //BA.debugLineNum = 104;BA.debugLine="Dim status As Int = Intent.GetExtra(\"status\")";
_status = (int)(BA.ObjectToNumber(_intent.GetExtra("status")));
 //BA.debugLineNum = 105;BA.debugLine="volt=Rnd(volt1,volt1+1)";
_volt = anywheresoftware.b4a.keywords.Common.Rnd(_volt1,(int) (_volt1+1));
 //BA.debugLineNum = 106;BA.debugLine="temp=Rnd(temp2,temp2+1)";
_temp = anywheresoftware.b4a.keywords.Common.Rnd(_temp2,(int) (_temp2+1));
 //BA.debugLineNum = 107;BA.debugLine="If kvsvolt.ListKeys.Size>15 Then";
if (_kvsvolt._listkeys().getSize()>15) { 
 //BA.debugLineNum = 109;BA.debugLine="kvsvolt.DeleteAll";
_kvsvolt._deleteall();
 //BA.debugLineNum = 110;BA.debugLine="kvstemp.DeleteAll";
_kvstemp._deleteall();
 };
 //BA.debugLineNum = 113;BA.debugLine="If kvs2.ListKeys.Size>25 Then";
if (_kvs2._listkeys().getSize()>25) { 
 //BA.debugLineNum = 114;BA.debugLine="kvs2.DeleteAll";
_kvs2._deleteall();
 //BA.debugLineNum = 115;BA.debugLine="ToastMessageShow(\"Reset\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Reset"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 117;BA.debugLine="For g = 0 To temp2";
{
final int step25 = 1;
final int limit25 = _temp2;
for (_g = (int) (0) ; (step25 > 0 && _g <= limit25) || (step25 < 0 && _g >= limit25); _g = ((int)(0 + _g + step25)) ) {
 //BA.debugLineNum = 118;BA.debugLine="If g=temp2 Then";
if (_g==_temp2) { 
 //BA.debugLineNum = 119;BA.debugLine="kvstemp.PutSimple(g,time)";
_kvstemp._putsimple(BA.NumberToString(_g),(Object)(_time));
 //BA.debugLineNum = 120;BA.debugLine="Log(time&\" Put-> \"&temp2&\"C°\")";
anywheresoftware.b4a.keywords.Common.Log(_time+" Put-> "+BA.NumberToString(_temp2)+"C°");
 };
 }
};
 //BA.debugLineNum = 123;BA.debugLine="For vo = 3000 To Intent.GetExtra(\"voltage\")";
{
final int step31 = 1;
final int limit31 = (int)(BA.ObjectToNumber(_intent.GetExtra("voltage")));
for (_vo = (int) (3000) ; (step31 > 0 && _vo <= limit31) || (step31 < 0 && _vo >= limit31); _vo = ((int)(0 + _vo + step31)) ) {
 //BA.debugLineNum = 124;BA.debugLine="If vo =Intent.GetExtra(\"voltage\") Then";
if (_vo==(double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))) { 
 //BA.debugLineNum = 125;BA.debugLine="kvsvolt.PutSimple(vo,time)";
_kvsvolt._putsimple(BA.NumberToString(_vo),(Object)(_time));
 //BA.debugLineNum = 126;BA.debugLine="Log(time&\" Put-> \"&Intent.GetExtra(\"voltage\")&\"";
anywheresoftware.b4a.keywords.Common.Log(_time+" Put-> "+BA.ObjectToString(_intent.GetExtra("voltage"))+"V");
 };
 }
};
 //BA.debugLineNum = 129;BA.debugLine="Dim rst,rl,rm As Int";
_rst = 0;
_rl = 0;
_rm = 0;
 //BA.debugLineNum = 130;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 131;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl.txt",BA.NumberToString(_level));
 //BA.debugLineNum = 132;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","volt.txt",BA.NumberToString(_volt));
 //BA.debugLineNum = 133;BA.debugLine="If Plugged  Then";
if (_plugged) { 
 //BA.debugLineNum = 135;BA.debugLine="For v = 0 To Scale";
{
final int step42 = 1;
final int limit42 = _scale;
for (_v = (int) (0) ; (step42 > 0 && _v <= limit42) || (step42 < 0 && _v >= limit42); _v = ((int)(0 + _v + step42)) ) {
 //BA.debugLineNum = 137;BA.debugLine="If v=Level Then";
if (_v==_level) { 
 //BA.debugLineNum = 138;BA.debugLine="Log(\"Put-> \"&v)";
anywheresoftware.b4a.keywords.Common.Log("Put-> "+BA.NumberToString(_v));
 //BA.debugLineNum = 139;BA.debugLine="kvs2.PutSimple(v,time)";
_kvs2._putsimple(BA.NumberToString(_v),(Object)(_time));
 };
 }
};
 //BA.debugLineNum = 142;BA.debugLine="rst=Scale-Level";
_rst = (int) (_scale-_level);
 //BA.debugLineNum = 143;BA.debugLine="val = rst*Intent.GetExtra(\"voltage\") /1000";
_val = (int) (_rst*(double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 144;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 145;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 146;BA.debugLine="If Level=100 Then";
if (_level==100) { 
 //BA.debugLineNum = 147;BA.debugLine="sNotif.Icon=\"batusb\"";
_snotif.setIcon("batusb");
 //BA.debugLineNum = 148;BA.debugLine="sNotif.Sound=False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 149;BA.debugLine="sNotif.SetInfo(Level&\"%\",volt&\" V | \"&temp&\"°C";
_snotif.SetInfo(processBA,BA.NumberToString(_level)+"%",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C ",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 150;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 152;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 }else {
 //BA.debugLineNum = 154;BA.debugLine="sNotif.Icon=\"batusb\"";
_snotif.setIcon("batusb");
 //BA.debugLineNum = 155;BA.debugLine="sNotif.Sound=False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 156;BA.debugLine="sNotif.SetInfo(ac&Level&\" %\",volt&\" V | \"&temp&\"";
_snotif.SetInfo(processBA,_ac+BA.NumberToString(_level)+" %",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C | noch: "+BA.NumberToString(_hours)+"h/"+BA.NumberToString(_minutes)+"min",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 157;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 159;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 };
 }else {
 //BA.debugLineNum = 162;BA.debugLine="For v = 0 To Scale";
{
final int step66 = 1;
final int limit66 = _scale;
for (_v = (int) (0) ; (step66 > 0 && _v <= limit66) || (step66 < 0 && _v >= limit66); _v = ((int)(0 + _v + step66)) ) {
 //BA.debugLineNum = 163;BA.debugLine="nl.Add(v)";
_nl.Add((Object)(_v));
 //BA.debugLineNum = 164;BA.debugLine="If v=Level Then";
if (_v==_level) { 
 //BA.debugLineNum = 165;BA.debugLine="Log(\"Put-> \"&v)";
anywheresoftware.b4a.keywords.Common.Log("Put-> "+BA.NumberToString(_v));
 //BA.debugLineNum = 166;BA.debugLine="kvs2.PutSimple(v,time)";
_kvs2._putsimple(BA.NumberToString(_v),(Object)(_time));
 };
 }
};
 //BA.debugLineNum = 169;BA.debugLine="Dim days,sval As Int";
_days = 0;
_sval = 0;
 //BA.debugLineNum = 170;BA.debugLine="sval =Intent.GetExtra(\"status\")/1000";
_sval = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("status")))/(double)1000);
 //BA.debugLineNum = 171;BA.debugLine="Log(sval)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_sval));
 //BA.debugLineNum = 172;BA.debugLine="val = sval";
_val = _sval;
 //BA.debugLineNum = 173;BA.debugLine="days=Floor(val/60/24)";
_days = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60/(double)24));
 //BA.debugLineNum = 174;BA.debugLine="hours = Floor(val/60 Mod 24)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60%24));
 //BA.debugLineNum = 175;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 176;BA.debugLine="If  Not (Level=0) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_level==0)) { 
 //BA.debugLineNum = 177;BA.debugLine="sNotif.SetInfo(Level&\" %\",volt&\" V | \"&temp&\"°C";
_snotif.SetInfo(processBA,BA.NumberToString(_level)+" %",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C | noch: "+BA.NumberToString(_days)+"d "+BA.NumberToString(_hours)+"h "+BA.NumberToString(_minutes)+"m",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 178;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 //BA.debugLineNum = 180;BA.debugLine="If Level <= 100 Then";
if (_level<=100) { 
 //BA.debugLineNum = 183;BA.debugLine="sNotif.Icon=\"bat100\"";
_snotif.setIcon("bat100");
 };
 //BA.debugLineNum = 185;BA.debugLine="If Level <= 80 Then";
if (_level<=80) { 
 //BA.debugLineNum = 187;BA.debugLine="sNotif.Icon=\"bat80\"";
_snotif.setIcon("bat80");
 };
 //BA.debugLineNum = 189;BA.debugLine="If Level <= 60 Then";
if (_level<=60) { 
 //BA.debugLineNum = 191;BA.debugLine="sNotif.Icon=\"bat60\"";
_snotif.setIcon("bat60");
 };
 //BA.debugLineNum = 193;BA.debugLine="If Level <= 40 Then";
if (_level<=40) { 
 //BA.debugLineNum = 195;BA.debugLine="sNotif.Icon=\"bat40\"";
_snotif.setIcon("bat40");
 };
 //BA.debugLineNum = 197;BA.debugLine="If Level<=20 Then";
if (_level<=20) { 
 //BA.debugLineNum = 198;BA.debugLine="sNotif.Icon=\"bat20\"";
_snotif.setIcon("bat20");
 };
 //BA.debugLineNum = 200;BA.debugLine="If Level = 5 Then";
if (_level==5) { 
 //BA.debugLineNum = 201;BA.debugLine="sNotif.Icon=\"bat5\"";
_snotif.setIcon("bat5");
 //BA.debugLineNum = 202;BA.debugLine="sNotif.SetInfo(\"low Battery!: \",\"remain: \"&Level";
_snotif.SetInfo(processBA,"low Battery!: ","remain: "+BA.NumberToString(_level)+"% ",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 203;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 //BA.debugLineNum = 205;BA.debugLine="If Level = 4 Then";
if (_level==4) { 
 //BA.debugLineNum = 206;BA.debugLine="sNotif.Icon=\"batlow\"";
_snotif.setIcon("batlow");
 //BA.debugLineNum = 207;BA.debugLine="sNotif.SetInfo(\"low Power!: \",\"set on low Power";
_snotif.SetInfo(processBA,"low Power!: ","set on low Power: "+BA.NumberToString(_level)+"% ",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 208;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 //BA.debugLineNum = 210;BA.debugLine="If temp >=45 Then";
if (_temp>=45) { 
 //BA.debugLineNum = 211;BA.debugLine="sNotif.Icon=\"batheat\"";
_snotif.setIcon("batheat");
 //BA.debugLineNum = 212;BA.debugLine="sNotif.SetInfo(\"Achtung: \"&temp&\"°C !\",\"hier kli";
_snotif.SetInfo(processBA,"Achtung: "+BA.NumberToString(_temp)+"°C !","hier klicken um zum kühlen...",(Object)(mostCurrent._cool.getObject()));
 //BA.debugLineNum = 213;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 };
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
public static String  _minutes_hours(int _ms) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
 //BA.debugLineNum = 220;BA.debugLine="Sub minutes_hours ( ms As Int ) As String";
 //BA.debugLineNum = 221;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 222;BA.debugLine="val = ms";
_val = _ms;
 //BA.debugLineNum = 223;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 224;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 225;BA.debugLine="Return NumberFormat(hours, 1, 0) & \":\" & NumberFo";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_hours,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (2),(int) (0));
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim sNotif As Notification";
_snotif = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim device As PhoneEvents";
_device = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 13;BA.debugLine="Dim list1 As List";
_list1 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim kvs2,kvs3,kvs4,kvstemp,kvsvolt As KeyValueSt";
_kvs2 = new com.batcat.keyvaluestore();
_kvs3 = new com.batcat.keyvaluestore();
_kvs4 = new com.batcat.keyvaluestore();
_kvstemp = new com.batcat.keyvaluestore();
_kvsvolt = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 15;BA.debugLine="Private date,time,tt As String";
_date = "";
_time = "";
_tt = "";
 //BA.debugLineNum = 16;BA.debugLine="Dim bat As Batut";
_bat = new com.batcat.batut();
 //BA.debugLineNum = 17;BA.debugLine="Dim level1 As Int";
_level1 = 0;
 //BA.debugLineNum = 18;BA.debugLine="Dim kl,dt,nl As List";
_kl = new anywheresoftware.b4a.objects.collections.List();
_dt = new anywheresoftware.b4a.objects.collections.List();
_nl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Dim cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 20;BA.debugLine="Dim dir As String=File.DirDefaultExternal&\"/mnt/c";
_dir = anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache";
 //BA.debugLineNum = 21;BA.debugLine="Dim labex As String";
_labex = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 23;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 24;BA.debugLine="Dim bat As Batut";
_bat = new com.batcat.batut();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 31;BA.debugLine="sNotif.Initialize";
_snotif.Initialize();
 //BA.debugLineNum = 32;BA.debugLine="bat.Initialize";
_bat._initialize(processBA);
 //BA.debugLineNum = 33;BA.debugLine="sNotif.Light=False";
_snotif.setLight(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 34;BA.debugLine="sNotif.Icon= \"bat100\"";
_snotif.setIcon("bat100");
 //BA.debugLineNum = 35;BA.debugLine="sNotif.SetInfo(\"Bat-Cat\",\"Welcome\",Main)";
_snotif.SetInfo(processBA,"Bat-Cat","Welcome",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="sNotif.Sound = False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 37;BA.debugLine="sNotif.Vibrate=False";
_snotif.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 38;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 40;BA.debugLine="device.Initialize(\"device\")";
_device.Initialize(processBA,"device");
 //BA.debugLineNum = 41;BA.debugLine="list1.Initialize";
_list1.Initialize();
 //BA.debugLineNum = 42;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 44;BA.debugLine="date=DateTime.Date(DateTime.Now)";
_date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 46;BA.debugLine="sql.Initialize(File.DirRootExternal, \"1.db\", True";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"1.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 47;BA.debugLine="tt=time";
_tt = _time;
 //BA.debugLineNum = 50;BA.debugLine="kl.Initialize";
_kl.Initialize();
 //BA.debugLineNum = 51;BA.debugLine="dt.Initialize";
_dt.Initialize();
 //BA.debugLineNum = 52;BA.debugLine="nl.Initialize";
_nl.Initialize();
 //BA.debugLineNum = 53;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
_kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 54;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
_kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 55;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
_kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 56;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"data";
_kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 //BA.debugLineNum = 57;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"data";
_kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 58;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 85;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 86;BA.debugLine="ToastMessageShow(\"Status: Service End..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Status: Service End.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 62;BA.debugLine="ToastMessageShow(\"Status: Service Start..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Status: Service Start.."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 63;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cach";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl2.txt")) { 
 }else {
 //BA.debugLineNum = 68;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cache";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 70;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/c";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl2.txt",BA.NumberToString(_level1));
 };
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
}
