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
public static com.batcat.keyvaluestore _kvstime = null;
public static String _date = "";
public static String _time = "";
public static com.batcat.batut _bat = null;
public static anywheresoftware.b4a.objects.collections.List _kl = null;
public static anywheresoftware.b4a.objects.collections.List _dt = null;
public static anywheresoftware.b4a.objects.collections.List _nl = null;
public static anywheresoftware.b4a.objects.collections.List _logfile = null;
public static String _dir = "";
public static anywheresoftware.b4a.sql.SQL _sql = null;
public static anywheresoftware.b4a.objects.collections.Map _m = null;
public static anywheresoftware.b4a.objects.Timer _t6 = null;
public static int _acc = 0;
public static int _ausb = 0;
public static String _ac = "";
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.pman _pman = null;
public com.batcat.wait _wait = null;
public com.batcat.charts _charts = null;
public com.batcat.set2 _set2 = null;
public com.batcat.datacount _datacount = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public static String  _ac_check() throws Exception{
String _dat = "";
 //BA.debugLineNum = 194;BA.debugLine="Sub ac_check";
 //BA.debugLineNum = 195;BA.debugLine="Dim dat As String";
_dat = "";
 //BA.debugLineNum = 196;BA.debugLine="dat=date&\" \"&time";
_dat = _date+" "+_time;
 //BA.debugLineNum = 197;BA.debugLine="Dim acc,ausb As Int";
_acc = 0;
_ausb = 0;
 //BA.debugLineNum = 198;BA.debugLine="acc=bat.BatteryInformation(10)";
_acc = _bat._getbatteryinformation()[(int) (10)];
 //BA.debugLineNum = 199;BA.debugLine="ausb=bat.BatteryInformation(9)";
_ausb = _bat._getbatteryinformation()[(int) (9)];
 //BA.debugLineNum = 200;BA.debugLine="If acc = 1 Then";
if (_acc==1) { 
 //BA.debugLineNum = 201;BA.debugLine="ac=\"AC - Kabel: \"";
_ac = "AC - Kabel: ";
 //BA.debugLineNum = 202;BA.debugLine="logfile.Add(dat&\": \"&ac&\" angeschlossen.\")";
_logfile.Add((Object)(_dat+": "+_ac+" angeschlossen."));
 //BA.debugLineNum = 203;BA.debugLine="kvstime.DeleteAll";
_kvstime._deleteall();
 //BA.debugLineNum = 204;BA.debugLine="kvstime.PutSimple(dat&\" - AC\",acc)";
_kvstime._putsimple(_dat+" - AC",(Object)(_acc));
 }else {
 //BA.debugLineNum = 207;BA.debugLine="If ausb=2 Then";
if (_ausb==2) { 
 //BA.debugLineNum = 208;BA.debugLine="ac=\"USB - Kabel: \"";
_ac = "USB - Kabel: ";
 //BA.debugLineNum = 209;BA.debugLine="logfile.Add(dat&\": \"&ac&\" angeschlossen.\")";
_logfile.Add((Object)(_dat+": "+_ac+" angeschlossen."));
 //BA.debugLineNum = 210;BA.debugLine="kvstime.DeleteAll";
_kvstime._deleteall();
 //BA.debugLineNum = 211;BA.debugLine="kvstime.PutSimple(dat&\" - USB\",acc)";
_kvstime._putsimple(_dat+" - USB",(Object)(_acc));
 };
 };
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 89;BA.debugLine="ToastMessageShow(\"Status: Error Service Stop!\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Status: Error Service Stop!"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 90;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return false;
}
public static String  _data_cache() throws Exception{
int _temp = 0;
int _volt = 0;
int _g = 0;
int _vo = 0;
 //BA.debugLineNum = 227;BA.debugLine="Sub data_cache";
 //BA.debugLineNum = 228;BA.debugLine="Dim temp,volt As Int";
_temp = 0;
_volt = 0;
 //BA.debugLineNum = 229;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
_volt = (int) (_bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 230;BA.debugLine="temp=bat.BatteryInformation(6)/10";
_temp = (int) (_bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 231;BA.debugLine="If kvs2.ListKeys.Size=10 Then";
if (_kvs2._listkeys().getSize()==10) { 
 //BA.debugLineNum = 232;BA.debugLine="kvsvolt.DeleteAll";
_kvsvolt._deleteall();
 //BA.debugLineNum = 233;BA.debugLine="kvstemp.DeleteAll";
_kvstemp._deleteall();
 //BA.debugLineNum = 234;BA.debugLine="kvs2.DeleteAll";
_kvs2._deleteall();
 };
 //BA.debugLineNum = 237;BA.debugLine="For g = 0 To 60";
{
final int step9 = 1;
final int limit9 = (int) (60);
for (_g = (int) (0) ; (step9 > 0 && _g <= limit9) || (step9 < 0 && _g >= limit9); _g = ((int)(0 + _g + step9)) ) {
 //BA.debugLineNum = 238;BA.debugLine="If g=temp Then";
if (_g==_temp) { 
 //BA.debugLineNum = 239;BA.debugLine="kvstemp.PutSimple(g,time)";
_kvstemp._putsimple(BA.NumberToString(_g),(Object)(_time));
 //BA.debugLineNum = 240;BA.debugLine="Log(time&\" Put-> \"&temp&\"C°\")";
anywheresoftware.b4a.keywords.Common.Log(_time+" Put-> "+BA.NumberToString(_temp)+"C°");
 };
 }
};
 //BA.debugLineNum = 243;BA.debugLine="For vo = 2500 To 6000";
{
final int step15 = 1;
final int limit15 = (int) (6000);
for (_vo = (int) (2500) ; (step15 > 0 && _vo <= limit15) || (step15 < 0 && _vo >= limit15); _vo = ((int)(0 + _vo + step15)) ) {
 //BA.debugLineNum = 244;BA.debugLine="If vo=volt Then";
if (_vo==_volt) { 
 //BA.debugLineNum = 245;BA.debugLine="kvsvolt.PutSimple(vo,time)";
_kvsvolt._putsimple(BA.NumberToString(_vo),(Object)(_time));
 //BA.debugLineNum = 246;BA.debugLine="Log(time&\" Put-> \"&volt&\"V\")";
anywheresoftware.b4a.keywords.Common.Log(_time+" Put-> "+BA.NumberToString(_volt)+"V");
 };
 }
};
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _device_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
int _temp = 0;
int _volt = 0;
int _volt1 = 0;
int _temp2 = 0;
int _rst = 0;
anywheresoftware.b4a.objects.collections.List _llist = null;
int _val = 0;
int _hours = 0;
int _minutes = 0;
int _p = 0;
int _days = 0;
int _sval = 0;
 //BA.debugLineNum = 99;BA.debugLine="Sub device_BatteryChanged (Level As Int, Scale As";
 //BA.debugLineNum = 100;BA.debugLine="Dim temp,volt,volt1,temp2 As Int";
_temp = 0;
_volt = 0;
_volt1 = 0;
_temp2 = 0;
 //BA.debugLineNum = 101;BA.debugLine="volt1=Intent.GetExtra(\"voltage\") /1000";
_volt1 = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 102;BA.debugLine="temp2=Intent.GetExtra(\"temperature\") /10";
_temp2 = (int) ((double)(BA.ObjectToNumber(_intent.GetExtra("temperature")))/(double)10);
 //BA.debugLineNum = 103;BA.debugLine="volt=Rnd(volt1,volt1+1)";
_volt = anywheresoftware.b4a.keywords.Common.Rnd(_volt1,(int) (_volt1+1));
 //BA.debugLineNum = 104;BA.debugLine="temp=Rnd(temp2,temp2+1)";
_temp = anywheresoftware.b4a.keywords.Common.Rnd(_temp2,(int) (_temp2+1));
 //BA.debugLineNum = 105;BA.debugLine="Dim rst As Int";
_rst = 0;
 //BA.debugLineNum = 106;BA.debugLine="Dim llist As List";
_llist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 107;BA.debugLine="llist.Initialize";
_llist.Initialize();
 //BA.debugLineNum = 108;BA.debugLine="llist.Add(Level)";
_llist.Add((Object)(_level));
 //BA.debugLineNum = 109;BA.debugLine="If llist.Size=100 Then";
if (_llist.getSize()==100) { 
 //BA.debugLineNum = 110;BA.debugLine="llist.Clear";
_llist.Clear();
 };
 //BA.debugLineNum = 112;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 114;BA.debugLine="load_check";
_load_check();
 //BA.debugLineNum = 115;BA.debugLine="data_cache";
_data_cache();
 //BA.debugLineNum = 116;BA.debugLine="If Plugged  Then";
if (_plugged) { 
 //BA.debugLineNum = 117;BA.debugLine="For p = 0 To llist.Size-1 Step 2";
{
final int step17 = (int) (2);
final int limit17 = (int) (_llist.getSize()-1);
for (_p = (int) (0) ; (step17 > 0 && _p <= limit17) || (step17 < 0 && _p >= limit17); _p = ((int)(0 + _p + step17)) ) {
 //BA.debugLineNum = 118;BA.debugLine="Log(\"Ad Value: \"&llist.Get(p))";
anywheresoftware.b4a.keywords.Common.Log("Ad Value: "+BA.ObjectToString(_llist.Get(_p)));
 //BA.debugLineNum = 119;BA.debugLine="kvs2.PutSimple(Level,time)";
_kvs2._putsimple(BA.NumberToString(_level),(Object)(_time));
 }
};
 //BA.debugLineNum = 121;BA.debugLine="rst=Scale-Level";
_rst = (int) (_scale-_level);
 //BA.debugLineNum = 122;BA.debugLine="val = rst*Intent.GetExtra(\"voltage\")/1000";
_val = (int) (_rst*(double)(BA.ObjectToNumber(_intent.GetExtra("voltage")))/(double)1000);
 //BA.debugLineNum = 123;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 124;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 125;BA.debugLine="If Level=100 Then";
if (_level==100) { 
 //BA.debugLineNum = 126;BA.debugLine="sNotif.Icon=\"batusb\"";
_snotif.setIcon("batusb");
 //BA.debugLineNum = 127;BA.debugLine="sNotif.Sound=False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 128;BA.debugLine="sNotif.SetInfo(Level&\"%\",volt&\" V | \"&temp&\"°C";
_snotif.SetInfo(processBA,BA.NumberToString(_level)+"%",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C ",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 129;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 130;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 }else {
 //BA.debugLineNum = 132;BA.debugLine="sNotif.Icon=\"batusb\"";
_snotif.setIcon("batusb");
 //BA.debugLineNum = 133;BA.debugLine="sNotif.Sound=False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="sNotif.SetInfo(ac&Level&\" %\",volt&\" V | \"&temp&";
_snotif.SetInfo(processBA,_ac+BA.NumberToString(_level)+" %",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C | noch: "+BA.NumberToString(_hours)+"h - "+BA.NumberToString(_minutes)+"min",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 135;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 136;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 };
 }else {
 //BA.debugLineNum = 139;BA.debugLine="For p = 0 To llist.Size-1 Step 2";
{
final int step39 = (int) (2);
final int limit39 = (int) (_llist.getSize()-1);
for (_p = (int) (0) ; (step39 > 0 && _p <= limit39) || (step39 < 0 && _p >= limit39); _p = ((int)(0 + _p + step39)) ) {
 //BA.debugLineNum = 140;BA.debugLine="Log(\"Ad Value: \"&llist.Get(p))";
anywheresoftware.b4a.keywords.Common.Log("Ad Value: "+BA.ObjectToString(_llist.Get(_p)));
 //BA.debugLineNum = 141;BA.debugLine="kvs2.PutSimple(Level,time)";
_kvs2._putsimple(BA.NumberToString(_level),(Object)(_time));
 }
};
 //BA.debugLineNum = 143;BA.debugLine="Dim days,sval As Int";
_days = 0;
_sval = 0;
 //BA.debugLineNum = 144;BA.debugLine="sval =Intent.GetExtra(\"voltage\")";
_sval = (int)(BA.ObjectToNumber(_intent.GetExtra("voltage")));
 //BA.debugLineNum = 146;BA.debugLine="val = Scale/sval*Level*1000";
_val = (int) (_scale/(double)_sval*_level*1000);
 //BA.debugLineNum = 147;BA.debugLine="days=Floor(val/60/24)";
_days = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60/(double)24));
 //BA.debugLineNum = 148;BA.debugLine="hours = Floor(val/60 Mod 24)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60%24));
 //BA.debugLineNum = 149;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 150;BA.debugLine="If  Not (Level=0) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_level==0)) { 
 //BA.debugLineNum = 151;BA.debugLine="sNotif.SetInfo(Level&\" %\",volt&\" V | \"&temp&\"°C";
_snotif.SetInfo(processBA,BA.NumberToString(_level)+" %",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C | noch: "+BA.NumberToString(_days)+"d "+BA.NumberToString(_hours)+"h "+BA.NumberToString(_minutes)+"m",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 //BA.debugLineNum = 154;BA.debugLine="If Level < 100 Then";
if (_level<100) { 
 //BA.debugLineNum = 155;BA.debugLine="sNotif.Icon=\"bat100\"";
_snotif.setIcon("bat100");
 };
 //BA.debugLineNum = 157;BA.debugLine="If Level < 80 Then";
if (_level<80) { 
 //BA.debugLineNum = 159;BA.debugLine="sNotif.Icon=\"bat80\"";
_snotif.setIcon("bat80");
 };
 //BA.debugLineNum = 161;BA.debugLine="If Level < 60 Then";
if (_level<60) { 
 //BA.debugLineNum = 163;BA.debugLine="sNotif.Icon=\"bat60\"";
_snotif.setIcon("bat60");
 };
 //BA.debugLineNum = 165;BA.debugLine="If Level < 40 Then";
if (_level<40) { 
 //BA.debugLineNum = 167;BA.debugLine="sNotif.Icon=\"bat40\"";
_snotif.setIcon("bat40");
 };
 //BA.debugLineNum = 169;BA.debugLine="If Level<20 Then";
if (_level<20) { 
 //BA.debugLineNum = 170;BA.debugLine="sNotif.Icon=\"bat20\"";
_snotif.setIcon("bat20");
 };
 //BA.debugLineNum = 172;BA.debugLine="If Level =15  Then";
if (_level==15) { 
 //BA.debugLineNum = 173;BA.debugLine="sNotif.Icon=\"bat5\"";
_snotif.setIcon("bat5");
 //BA.debugLineNum = 174;BA.debugLine="sNotif.SetInfo(Level&\" %\",volt&\" V | \"&temp&\"°C";
_snotif.SetInfo(processBA,BA.NumberToString(_level)+" %",BA.NumberToString(_volt)+" V | "+BA.NumberToString(_temp)+"°C | ca: "+BA.NumberToString(_days)+"d "+BA.NumberToString(_hours)+"h "+BA.NumberToString(_minutes)+"m",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 177;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 //BA.debugLineNum = 179;BA.debugLine="If temp =50 Then";
if (_temp==50) { 
 //BA.debugLineNum = 180;BA.debugLine="logfile.Add(date&\" \"&time&\": \"&temp&\"C°\")";
_logfile.Add((Object)(_date+" "+_time+": "+BA.NumberToString(_temp)+"C°"));
 //BA.debugLineNum = 181;BA.debugLine="sNotif.Icon=\"batheat\"";
_snotif.setIcon("batheat");
 //BA.debugLineNum = 182;BA.debugLine="sNotif.SetInfo(\"Achtung: \"&temp&\"°C !\",\"hier kl";
_snotif.SetInfo(processBA,"Achtung: "+BA.NumberToString(_temp)+"°C !","hier klicken um zum kühlen...",(Object)(mostCurrent._cool.getObject()));
 //BA.debugLineNum = 183;BA.debugLine="sNotif.Light=True";
_snotif.setLight(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 184;BA.debugLine="sNotif.Sound=True";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 185;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 };
 };
 //BA.debugLineNum = 188;BA.debugLine="End Sub";
return "";
}
public static String  _load_check() throws Exception{
String _load = "";
 //BA.debugLineNum = 217;BA.debugLine="Sub load_check";
 //BA.debugLineNum = 218;BA.debugLine="Dim load As String = bat.BatteryInformation(0)";
_load = BA.NumberToString(_bat._getbatteryinformation()[(int) (0)]);
 //BA.debugLineNum = 219;BA.debugLine="If load=100 Then";
if ((_load).equals(BA.NumberToString(100))) { 
 //BA.debugLineNum = 220;BA.debugLine="kvstime.DeleteAll";
_kvstime._deleteall();
 //BA.debugLineNum = 221;BA.debugLine="kvstime.PutSimple(date&\" \"&time&\": \"&load&\"% AC\"";
_kvstime._putsimple(_date+" "+_time+": "+_load+"% AC",(Object)(_load));
 //BA.debugLineNum = 222;BA.debugLine="logfile.Add(date&\" \"&time&\": \"&load&\"% geladen.\"";
_logfile.Add((Object)(_date+" "+_time+": "+_load+"% geladen."));
 };
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _minutes_hours(int _ms) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
 //BA.debugLineNum = 252;BA.debugLine="Sub minutes_hours ( ms As Int ) As String";
 //BA.debugLineNum = 253;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 254;BA.debugLine="val = ms";
_val = _ms;
 //BA.debugLineNum = 255;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 256;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 257;BA.debugLine="Return NumberFormat(hours, 1, 0) & \":\" & NumberFo";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_hours,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (2),(int) (0));
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim sNotif As Notification";
_snotif = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim device As PhoneEvents";
_device = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 16;BA.debugLine="Dim list1 As List";
_list1 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Dim kvs2,kvs3,kvs4,kvstemp,kvsvolt,kvstime As Ke";
_kvs2 = new com.batcat.keyvaluestore();
_kvs3 = new com.batcat.keyvaluestore();
_kvs4 = new com.batcat.keyvaluestore();
_kvstemp = new com.batcat.keyvaluestore();
_kvsvolt = new com.batcat.keyvaluestore();
_kvstime = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 18;BA.debugLine="Private date,time As String";
_date = "";
_time = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim bat As Batut";
_bat = new com.batcat.batut();
 //BA.debugLineNum = 20;BA.debugLine="Dim kl,dt,nl,logfile As List";
_kl = new anywheresoftware.b4a.objects.collections.List();
_dt = new anywheresoftware.b4a.objects.collections.List();
_nl = new anywheresoftware.b4a.objects.collections.List();
_logfile = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Dim dir As String=File.DirDefaultExternal&\"/mnt/c";
_dir = anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache";
 //BA.debugLineNum = 22;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 23;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 24;BA.debugLine="Dim bat As Batut";
_bat = new com.batcat.batut();
 //BA.debugLineNum = 25;BA.debugLine="Dim t6 As Timer";
_t6 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 26;BA.debugLine="Dim acc,ausb As Int";
_acc = 0;
_ausb = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim ac As String";
_ac = "";
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 34;BA.debugLine="t6.Initialize(\"t6\",1000)";
_t6.Initialize(processBA,"t6",(long) (1000));
 //BA.debugLineNum = 35;BA.debugLine="sNotif.Initialize";
_snotif.Initialize();
 //BA.debugLineNum = 36;BA.debugLine="bat.Initialize";
_bat._initialize(processBA);
 //BA.debugLineNum = 37;BA.debugLine="logfile.Initialize";
_logfile.Initialize();
 //BA.debugLineNum = 38;BA.debugLine="sNotif.Light=False";
_snotif.setLight(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 39;BA.debugLine="sNotif.Icon= \"bat100\"";
_snotif.setIcon("bat100");
 //BA.debugLineNum = 40;BA.debugLine="sNotif.SetInfo(\"Bat-Cat\",\"Welcome\",Main)";
_snotif.SetInfo(processBA,"Bat-Cat","Welcome",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 41;BA.debugLine="sNotif.Sound = False";
_snotif.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 42;BA.debugLine="sNotif.Vibrate=False";
_snotif.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 43;BA.debugLine="sNotif.Notify(1)";
_snotif.Notify((int) (1));
 //BA.debugLineNum = 44;BA.debugLine="device.Initialize(\"device\")";
_device.Initialize(processBA,"device");
 //BA.debugLineNum = 45;BA.debugLine="list1.Initialize";
_list1.Initialize();
 //BA.debugLineNum = 46;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 47;BA.debugLine="DateTime.DateFormat=\"dd.MM.yy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd.MM.yy");
 //BA.debugLineNum = 48;BA.debugLine="DateTime.TimeFormat=\"h:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm a");
 //BA.debugLineNum = 50;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cach";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","log.txt")) { 
 }else {
 //BA.debugLineNum = 53;BA.debugLine="File.MakeDir(File.DirDefaultExternal,\"mnt/cache";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 54;BA.debugLine="File.WriteList(dir,\"log.txt\",logfile)";
anywheresoftware.b4a.keywords.Common.File.WriteList(_dir,"log.txt",_logfile);
 };
 //BA.debugLineNum = 56;BA.debugLine="sql.Initialize(File.DirRootExternal, \"1.db\", True";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"1.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 57;BA.debugLine="time=DateTime.Time(DateTime.Now)";
_time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 58;BA.debugLine="date=DateTime.Date(DateTime.Now)";
_date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 59;BA.debugLine="DateTime.DateFormat=\"dd.MM.yy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd.MM.yy");
 //BA.debugLineNum = 60;BA.debugLine="DateTime.TimeFormat=\"h:mm \"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("h:mm ");
 //BA.debugLineNum = 62;BA.debugLine="kl.Initialize";
_kl.Initialize();
 //BA.debugLineNum = 63;BA.debugLine="dt.Initialize";
_dt.Initialize();
 //BA.debugLineNum = 64;BA.debugLine="nl.Initialize";
_nl.Initialize();
 //BA.debugLineNum = 65;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
_kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 66;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
_kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 67;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
_kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 68;BA.debugLine="kvstemp.Initialize(File.DirDefaultExternal, \"data";
_kvstemp._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_temp");
 //BA.debugLineNum = 69;BA.debugLine="kvsvolt.Initialize(File.DirDefaultExternal, \"data";
_kvsvolt._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_volt");
 //BA.debugLineNum = 70;BA.debugLine="kvstime.Initialize(File.DirDefaultExternal,\"datas";
_kvstime._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_time");
 //BA.debugLineNum = 71;BA.debugLine="Service.StartForeground(1,sNotif)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_snotif.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
String _volt = "";
String _level1 = "";
int _cac = 0;
 //BA.debugLineNum = 75;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 77;BA.debugLine="Dim volt As String = bat.BatteryInformation(7)";
_volt = BA.NumberToString(_bat._getbatteryinformation()[(int) (7)]);
 //BA.debugLineNum = 78;BA.debugLine="Dim level1 As String = bat.BatteryInformation(0)";
_level1 = BA.NumberToString(_bat._getbatteryinformation()[(int) (0)]);
 //BA.debugLineNum = 79;BA.debugLine="logfile.Add(\"App StartActivity \"&date&\" - \"&time";
_logfile.Add((Object)("App StartActivity "+_date+" - "+_time));
 //BA.debugLineNum = 81;BA.debugLine="Dim cac As Int = bat.BatteryInformation(8)";
_cac = _bat._getbatteryinformation()[(int) (8)];
 //BA.debugLineNum = 82;BA.debugLine="If cac=1 Then";
if (_cac==1) { 
 //BA.debugLineNum = 83;BA.debugLine="logfile.Add(date&\"/\"&time&\": \"&level1&\" | \"&volt";
_logfile.Add((Object)(_date+"/"+_time+": "+_level1+" | "+_volt));
 //BA.debugLineNum = 84;BA.debugLine="ac_check";
_ac_check();
 };
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static String  _value_check() throws Exception{
 //BA.debugLineNum = 191;BA.debugLine="Sub value_check";
 //BA.debugLineNum = 192;BA.debugLine="Log(\"check...\")";
anywheresoftware.b4a.keywords.Common.Log("check...");
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
}
