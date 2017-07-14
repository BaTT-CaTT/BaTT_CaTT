Type=Class
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Class Module created by trOw
Sub Class_Globals
	Private nativeMe As JavaObject
	
End Sub
'Initializes the object.
Public Sub Initialize
	nativeMe = Me
End Sub

Public Sub getBatteryInformation () As Int()

	Dim dummy As Int = 1
	Dim batteryInfo(11)  As Int
	batteryInfo = nativeMe.RunMethod("getBatteryInformation",Array(dummy))
	Return batteryInfo

End Sub

#If Java

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
#end if 