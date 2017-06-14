package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class progress extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "com.batcat.progress");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", com.batcat.progress.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.PanelWrapper _pn = null;
public anywheresoftware.b4a.objects.PanelWrapper _pn2 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper _canvas1 = null;
public int _nb = 0;
public anywheresoftware.b4a.objects.ActivityWrapper _myact = null;
public int _mstroke = 0;
public int _scolor = 0;
public int _cs = 0;
public int _emptypathcolor = 0;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.settings _settings = null;
public com.batcat.xmlviewex _xmlviewex = null;
public com.batcat.cool _cool = null;
public com.batcat.charts _charts = null;
public String  _bringtofront() throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub BringToFront";
 //BA.debugLineNum = 81;BA.debugLine="pn2.Visible = True";
_pn2.setVisible(__c.True);
 //BA.debugLineNum = 82;BA.debugLine="pn2.BringToFront";
_pn2.BringToFront();
 //BA.debugLineNum = 83;BA.debugLine="pn.BringToFront";
_pn.BringToFront();
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Private Sub Class_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private pn As Panel";
_pn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Private pn2 As Panel";
_pn2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Private canvas1 As Canvas";
_canvas1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Private nb As Int";
_nb = 0;
 //BA.debugLineNum = 10;BA.debugLine="Private MyAct As Activity";
_myact = new anywheresoftware.b4a.objects.ActivityWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Private mStroke As Int";
_mstroke = 0;
 //BA.debugLineNum = 12;BA.debugLine="Private Scolor As Int";
_scolor = 0;
 //BA.debugLineNum = 13;BA.debugLine="Private CS As Int";
_cs = 0;
 //BA.debugLineNum = 14;BA.debugLine="Private EmptyPathColor As Int";
_emptypathcolor = 0;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public String  _clearprogress() throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub ClearProgress";
 //BA.debugLineNum = 73;BA.debugLine="nb = 360";
_nb = (int) (360);
 //BA.debugLineNum = 74;BA.debugLine="ProgressDraw(canvas1, (pn.Width/2), (pn.Height/2)";
_progressdraw(_canvas1,(float) ((_pn.getWidth()/(double)2)),(float) ((_pn.getHeight()/(double)2)),(float) ((_pn.getHeight()/(double)2)-_mstroke),(float) (0),(float) (_nb),_emptypathcolor,(int) (_mstroke+2));
 //BA.debugLineNum = 75;BA.debugLine="nb = 0";
_nb = (int) (0);
 //BA.debugLineNum = 76;BA.debugLine="pn.Invalidate";
_pn.Invalidate();
 //BA.debugLineNum = 77;BA.debugLine="DoEvents";
__c.DoEvents();
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ActivityWrapper _pmyact,int _left,int _top,int _circlesize,int _strokewidth,int _linecolor,int _empty_pathcolor,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _backgroundimage) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 26;BA.debugLine="Public Sub Initialize(pMyAct As Activity, Left As";
 //BA.debugLineNum = 27;BA.debugLine="EmptyPathColor = Empty_PathColor";
_emptypathcolor = _empty_pathcolor;
 //BA.debugLineNum = 28;BA.debugLine="MyAct = pMyAct";
_myact = _pmyact;
 //BA.debugLineNum = 29;BA.debugLine="Scolor = LineColor";
_scolor = _linecolor;
 //BA.debugLineNum = 30;BA.debugLine="mStroke = StrokeWidth";
_mstroke = _strokewidth;
 //BA.debugLineNum = 31;BA.debugLine="CS = CircleSize /2";
_cs = (int) (_circlesize/(double)2);
 //BA.debugLineNum = 32;BA.debugLine="pn.Initialize(\"pn\")";
_pn.Initialize(ba,"pn");
 //BA.debugLineNum = 33;BA.debugLine="pn.Color = Colors.Transparent";
_pn.setColor(__c.Colors.Transparent);
 //BA.debugLineNum = 34;BA.debugLine="pn2.Initialize(\"pn2\")";
_pn2.Initialize(ba,"pn2");
 //BA.debugLineNum = 35;BA.debugLine="pn2.Color = Colors.Transparent";
_pn2.setColor(__c.Colors.Transparent);
 //BA.debugLineNum = 36;BA.debugLine="If BackGroundImage.IsInitialized = False Then";
if (_backgroundimage.IsInitialized()==__c.False) { 
 //BA.debugLineNum = 37;BA.debugLine="pn2.SetBackgroundImage(Null)";
_pn2.SetBackgroundImage((android.graphics.Bitmap)(__c.Null));
 }else {
 //BA.debugLineNum = 39;BA.debugLine="pn2.SetBackgroundImage(BackGroundImage)";
_pn2.SetBackgroundImage((android.graphics.Bitmap)(_backgroundimage.getObject()));
 };
 //BA.debugLineNum = 41;BA.debugLine="pn2.Visible = False";
_pn2.setVisible(__c.False);
 //BA.debugLineNum = 42;BA.debugLine="If BackGroundImage.IsInitialized = False Then";
if (_backgroundimage.IsInitialized()==__c.False) { 
 //BA.debugLineNum = 43;BA.debugLine="MyAct.AddView(pn2, Left, Top, CircleSize, Circle";
_myact.AddView((android.view.View)(_pn2.getObject()),_left,_top,_circlesize,_circlesize);
 }else {
 //BA.debugLineNum = 45;BA.debugLine="MyAct.AddView(pn2, Left, Top, BackGroundImage.Wi";
_myact.AddView((android.view.View)(_pn2.getObject()),_left,_top,_backgroundimage.getWidth(),_backgroundimage.getHeight());
 };
 //BA.debugLineNum = 47;BA.debugLine="pn2.AddView(pn, (pn2.Width-CircleSize)/2, (pn2.He";
_pn2.AddView((android.view.View)(_pn.getObject()),(int) ((_pn2.getWidth()-_circlesize)/(double)2),(int) ((_pn2.getHeight()-_circlesize)/(double)2),_circlesize,_circlesize);
 //BA.debugLineNum = 48;BA.debugLine="canvas1.Initialize(pn)";
_canvas1.Initialize((android.view.View)(_pn.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="nb = 0";
_nb = (int) (0);
 //BA.debugLineNum = 50;BA.debugLine="ClearProgress";
_clearprogress();
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public String  _progressdraw(anywheresoftware.b4a.objects.drawable.CanvasWrapper _cnvs,float _x,float _y,float _radius,float _startangle,float _endangle,int _mcolor,int _mybrush) throws Exception{
float _s = 0f;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper _p = null;
int _i = 0;
 //BA.debugLineNum = 52;BA.debugLine="Private Sub ProgressDraw(cnvs As Canvas, x As Floa";
 //BA.debugLineNum = 53;BA.debugLine="Dim S As Float";
_s = 0f;
 //BA.debugLineNum = 54;BA.debugLine="S = startAngle";
_s = _startangle;
 //BA.debugLineNum = 55;BA.debugLine="startAngle = 180 - endAngle";
_startangle = (float) (180-_endangle);
 //BA.debugLineNum = 56;BA.debugLine="endAngle = 180 - S";
_endangle = (float) (180-_s);
 //BA.debugLineNum = 57;BA.debugLine="If startAngle >= endAngle Then endAngle = endAngl";
if (_startangle>=_endangle) { 
_endangle = (float) (_endangle+360);};
 //BA.debugLineNum = 58;BA.debugLine="Dim p As Path";
_p = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper();
 //BA.debugLineNum = 59;BA.debugLine="p.Initialize(x, y)";
_p.Initialize(_x,_y);
 //BA.debugLineNum = 60;BA.debugLine="For i = startAngle To endAngle Step 10";
{
final int step8 = (int) (10);
final int limit8 = (int) (_endangle);
for (_i = (int) (_startangle) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 61;BA.debugLine="p.LineTo(x + 2 * radius * SinD(i), y + 2 * radiu";
_p.LineTo((float) (_x+2*_radius*__c.SinD(_i)),(float) (_y+2*_radius*__c.CosD(_i)));
 }
};
 //BA.debugLineNum = 63;BA.debugLine="p.LineTo(x + 2 * radius * SinD(endAngle), y + 2 *";
_p.LineTo((float) (_x+2*_radius*__c.SinD(_endangle)),(float) (_y+2*_radius*__c.CosD(_endangle)));
 //BA.debugLineNum = 64;BA.debugLine="p.LineTo(x, y)";
_p.LineTo(_x,_y);
 //BA.debugLineNum = 65;BA.debugLine="SetAntiAlias(cnvs)";
_setantialias(_cnvs);
 //BA.debugLineNum = 66;BA.debugLine="cnvs.ClipPath(p)";
_cnvs.ClipPath((android.graphics.Path)(_p.getObject()));
 //BA.debugLineNum = 67;BA.debugLine="cnvs.DrawCircle(x, y,  radius, mColor, False, myB";
_cnvs.DrawCircle(_x,_y,_radius,_mcolor,__c.False,(float) (_mybrush));
 //BA.debugLineNum = 68;BA.debugLine="cnvs.RemoveClip";
_cnvs.RemoveClip();
 //BA.debugLineNum = 69;BA.debugLine="DoEvents";
__c.DoEvents();
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public String  _sendtoback() throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub SendToBack";
 //BA.debugLineNum = 87;BA.debugLine="pn2.SendToBack";
_pn2.SendToBack();
 //BA.debugLineNum = 88;BA.debugLine="pn2.Visible = False";
_pn2.setVisible(__c.False);
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public String  _setantialias(anywheresoftware.b4a.objects.drawable.CanvasWrapper _c) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
Object _nativecanvas = null;
Object _paintflagsdrawfilter = null;
 //BA.debugLineNum = 99;BA.debugLine="Private Sub SetAntiAlias (c As Canvas)";
 //BA.debugLineNum = 100;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 101;BA.debugLine="Dim NativeCanvas As Object";
_nativecanvas = new Object();
 //BA.debugLineNum = 102;BA.debugLine="r.Target = c";
_r.Target = (Object)(_c);
 //BA.debugLineNum = 103;BA.debugLine="NativeCanvas = r.GetField(\"canvas\")";
_nativecanvas = _r.GetField("canvas");
 //BA.debugLineNum = 104;BA.debugLine="Dim PaintFlagsDrawFilter As Object";
_paintflagsdrawfilter = new Object();
 //BA.debugLineNum = 105;BA.debugLine="PaintFlagsDrawFilter = r.CreateObject2(\"android.g";
_paintflagsdrawfilter = _r.CreateObject2("android.graphics.PaintFlagsDrawFilter",new Object[]{(Object)(0),(Object)(1)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 106;BA.debugLine="r.Target = NativeCanvas";
_r.Target = _nativecanvas;
 //BA.debugLineNum = 107;BA.debugLine="r.RunMethod4(\"setDrawFilter\", Array As Object(Pai";
_r.RunMethod4("setDrawFilter",new Object[]{_paintflagsdrawfilter},new String[]{"android.graphics.DrawFilter"});
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public String  _setprogress(int _updateprogress) throws Exception{
int _xx = 0;
 //BA.debugLineNum = 92;BA.debugLine="Sub SetProgress(UpdateProgress As Int)";
 //BA.debugLineNum = 93;BA.debugLine="Dim xx As Int : xx = UpdateProgress";
_xx = 0;
 //BA.debugLineNum = 93;BA.debugLine="Dim xx As Int : xx = UpdateProgress";
_xx = _updateprogress;
 //BA.debugLineNum = 94;BA.debugLine="If xx < 1 Then xx = 1";
if (_xx<1) { 
_xx = (int) (1);};
 //BA.debugLineNum = 95;BA.debugLine="ProgressDraw(canvas1, (pn.Width/2), (pn.Height/2)";
_progressdraw(_canvas1,(float) ((_pn.getWidth()/(double)2)),(float) ((_pn.getHeight()/(double)2)),(float) ((_pn.getHeight()/(double)2)-_mstroke),(float) (0),(float) (_xx),_scolor,_mstroke);
 //BA.debugLineNum = 96;BA.debugLine="pn.Invalidate";
_pn.Invalidate();
 //BA.debugLineNum = 97;BA.debugLine="DoEvents";
__c.DoEvents();
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
