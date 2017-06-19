package com.batcat;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class batut extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "com.batcat.batut");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", com.batcat.batut.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4j.object.JavaObject _nativeme = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private nativeMe As JavaObject";
_nativeme = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
public int[]  _getbatteryinformation() throws Exception{
int _dummy = 0;
int[] _batteryinfo = null;
 //BA.debugLineNum = 21;BA.debugLine="Public Sub getBatteryInformation () As Int()";
 //BA.debugLineNum = 23;BA.debugLine="Dim dummy As Int = 1";
_dummy = (int) (1);
 //BA.debugLineNum = 24;BA.debugLine="Dim batteryInfo(11)  As Int";
_batteryinfo = new int[(int) (11)];
;
 //BA.debugLineNum = 25;BA.debugLine="batteryInfo = nativeMe.RunMethod(\"getBatteryInfor";
_batteryinfo = (int[])(_nativeme.RunMethod("getBatteryInformation",new Object[]{(Object)(_dummy)}));
 //BA.debugLineNum = 26;BA.debugLine="Return batteryInfo";
if (true) return _batteryinfo;
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 6;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 7;BA.debugLine="nativeMe = Me";
_nativeme.setObject((java.lang.Object)(this));
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}


  public int[] getBatteryInformation(int dummy) {

        int[] mybat = new int[11];
 
	Intent batteryIntent = ba.context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
 
	int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        mybat[0] = level;
	int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        mybat[1] = scale;
        int health = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
        mybat[2] = health; 
        int icon_small = batteryIntent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,-1);
        mybat[3] = icon_small;
        int plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        mybat[4] = plugged;
       // boolean present = batteryIntent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT); 
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        mybat[5] = status;
        //int technology = batteryIntent.getIntExtras(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
	    //mybat[11] = technology;
        int temperature = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1);
        mybat[6] = temperature;
        int voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1);
        mybat[7] = voltage; 
      int usb = batteryIntent.getIntExtra("plugged",BatteryManager.BATTERY_PLUGGED_USB);
       mybat[9] = usb; 

        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                     status == BatteryManager.BATTERY_STATUS_FULL;
        mybat[8] = 0;
        if (isCharging == true) {
           mybat[8] = 1;
        }   

        // How are we charging?
        mybat[9] = 0;
        mybat[10] = 0; 
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        if (usbCharge == true) {
           mybat[9] = 2;
        }   

        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (acCharge == true) {
           mybat[10] = 1;
        }   

        return mybat;
  }
}
