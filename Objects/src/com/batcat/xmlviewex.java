package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class xmlviewex {
private static xmlviewex mostCurrent = new xmlviewex();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static int _vis_visible = 0;
public static int _vis_invisible = 0;
public static int _vis_gone = 0;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.webhost _webhost = null;
public com.batcat.sys _sys = null;
public com.batcat.cool _cool = null;
public com.batcat.setanimation _setanimation = null;
public com.batcat.settings _settings = null;
public com.batcat.statemanager _statemanager = null;
public com.batcat.dbutils _dbutils = null;
public com.batcat.charts _charts = null;
public static anywheresoftware.b4a.objects.ConcreteViewWrapper  _findview(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _parent,String _viewname) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 32;BA.debugLine="Public Sub FindView(Parent As View, ViewName As St";
 //BA.debugLineNum = 33;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 35;BA.debugLine="jo = Parent";
_jo.setObject((java.lang.Object)(_parent.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="Return jo.RunMethod(\"findViewById\", Array As Obje";
if (true) return (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_jo.RunMethod("findViewById",new Object[]{(Object)(_getresourceid(_ba,"id",_viewname))})));
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return null;
}
public static int  _getresourceid(anywheresoftware.b4a.BA _ba,String _restype,String _name) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 111;BA.debugLine="Private Sub GetResourceId(ResType As String, Name";
 //BA.debugLineNum = 112;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 113;BA.debugLine="Return r.GetStaticField(Application.PackageName &";
if (true) return (int)(BA.ObjectToNumber(_r.GetStaticField(anywheresoftware.b4a.keywords.Common.Application.getPackageName()+".R$"+_restype,_name)));
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return 0;
}
public static anywheresoftware.b4a.objects.ConcreteViewWrapper  _inflatexmllayout(anywheresoftware.b4a.BA _ba,String _layout) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
int _id = 0;
 //BA.debugLineNum = 17;BA.debugLine="Public Sub InflateXmlLayout(Layout As String) As V";
 //BA.debugLineNum = 18;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 19;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 21;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext((_ba.processBA == null ? _ba : _ba.processBA)));
 //BA.debugLineNum = 22;BA.debugLine="r.Target = r.RunMethod2(\"getSystemService\", \"layo";
_r.Target = _r.RunMethod2("getSystemService","layout_inflater","java.lang.String");
 //BA.debugLineNum = 24;BA.debugLine="id = r.GetStaticField(Application.PackageName & \"";
_id = (int)(BA.ObjectToNumber(_r.GetStaticField(anywheresoftware.b4a.keywords.Common.Application.getPackageName()+".R$layout",_layout)));
 //BA.debugLineNum = 25;BA.debugLine="Return r.RunMethod4(\"inflate\", Array As Object(id";
if (true) return (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_r.RunMethod4("inflate",new Object[]{(Object)(_id),anywheresoftware.b4a.keywords.Common.Null},new String[]{"java.lang.int","android.view.ViewGroup"})));
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 4;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Public const VIS_VISIBLE As Int = 0";
_vis_visible = (int) (0);
 //BA.debugLineNum = 9;BA.debugLine="Public const VIS_INVISIBLE As Int = 4";
_vis_invisible = (int) (4);
 //BA.debugLineNum = 10;BA.debugLine="Public const VIS_GONE As Int = 8";
_vis_gone = (int) (8);
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewalpha(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,int _alpha) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 66;BA.debugLine="Public Sub SetXmlViewAlpha(TargetView As View, Alp";
 //BA.debugLineNum = 67;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 69;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 70;BA.debugLine="jo.RunMethod(\"setAlpha\", Array As Object(Alpha))";
_jo.RunMethod("setAlpha",new Object[]{(Object)(_alpha)});
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewcolor(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,int _color) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 88;BA.debugLine="Public Sub SetXmlViewColor(TargetView As View, Col";
 //BA.debugLineNum = 89;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 91;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 92;BA.debugLine="If Color = -1 Then";
if (_color==-1) { 
 //BA.debugLineNum = 93;BA.debugLine="jo.RunMethod(\"clearColorFilter\", Null)";
_jo.RunMethod("clearColorFilter",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 }else {
 //BA.debugLineNum = 95;BA.debugLine="jo.RunMethod(\"setColorFilter\", Array As Object(C";
_jo.RunMethod("setColorFilter",new Object[]{(Object)(_color)});
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewimage(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,anywheresoftware.b4a.objects.drawable.BitmapDrawable _icon) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 103;BA.debugLine="Public Sub SetXmlViewImage(TargetView As View, Ico";
 //BA.debugLineNum = 104;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 106;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 107;BA.debugLine="jo.RunMethod(\"setImageDrawable\", Array As Object(";
_jo.RunMethod("setImageDrawable",new Object[]{(Object)(_icon.getObject())});
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewtext(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,String _text) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 44;BA.debugLine="Public Sub SetXmlViewText(TargetView As View, Text";
 //BA.debugLineNum = 45;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 47;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="jo.RunMethod(\"setText\", Array As Object(Text))";
_jo.RunMethod("setText",new Object[]{(Object)(_text)});
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewtextcolor(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,int _color) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 55;BA.debugLine="Public Sub SetXmlViewTextColor(TargetView As View,";
 //BA.debugLineNum = 56;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 58;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 59;BA.debugLine="jo.RunMethod(\"setTextColor\", Array As Object(Colo";
_jo.RunMethod("setTextColor",new Object[]{(Object)(_color)});
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _setxmlviewvisible(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _targetview,int _visibility) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 77;BA.debugLine="Public Sub SetXmlViewVisible(TargetView As View, V";
 //BA.debugLineNum = 78;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 80;BA.debugLine="jo = TargetView";
_jo.setObject((java.lang.Object)(_targetview.getObject()));
 //BA.debugLineNum = 81;BA.debugLine="jo.RunMethod(\"setVisibility\", Array As Object(Vis";
_jo.RunMethod("setVisibility",new Object[]{(Object)(_visibility)});
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
}
