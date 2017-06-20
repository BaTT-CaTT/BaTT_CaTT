package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class charts {
private static charts mostCurrent = new charts();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
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
public com.batcat.statemanager _statemanager = null;
public static class _pieitem{
public boolean IsInitialized;
public String Name;
public float Value;
public int Color;
public void Initialize() {
IsInitialized = true;
Name = "";
Value = 0f;
Color = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _piedata{
public boolean IsInitialized;
public anywheresoftware.b4a.objects.collections.List Items;
public anywheresoftware.b4a.objects.PanelWrapper Target;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper Canvas;
public int GapDegrees;
public float LegendTextSize;
public int LegendBackColor;
public void Initialize() {
IsInitialized = true;
Items = new anywheresoftware.b4a.objects.collections.List();
Target = new anywheresoftware.b4a.objects.PanelWrapper();
Canvas = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
GapDegrees = 0;
LegendTextSize = 0f;
LegendBackColor = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _graphinternal{
public boolean IsInitialized;
public int originX;
public int zeroY;
public int originY;
public int maxY;
public float intervalX;
public int gw;
public int gh;
public void Initialize() {
IsInitialized = true;
originX = 0;
zeroY = 0;
originY = 0;
maxY = 0;
intervalX = 0f;
gw = 0;
gh = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _graph{
public boolean IsInitialized;
public com.batcat.charts._graphinternal GI;
public String Title;
public String YAxis;
public String XAxis;
public float YStart;
public float YEnd;
public float YInterval;
public int AxisColor;
public void Initialize() {
IsInitialized = true;
GI = new com.batcat.charts._graphinternal();
Title = "";
YAxis = "";
XAxis = "";
YStart = 0f;
YEnd = 0f;
YInterval = 0f;
AxisColor = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _linepoint{
public boolean IsInitialized;
public String X;
public float Y;
public float[] YArray;
public boolean ShowTick;
public void Initialize() {
IsInitialized = true;
X = "";
Y = 0f;
YArray = new float[0];
;
ShowTick = false;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _linedata{
public boolean IsInitialized;
public anywheresoftware.b4a.objects.collections.List Points;
public anywheresoftware.b4a.objects.collections.List LinesColors;
public anywheresoftware.b4a.objects.PanelWrapper Target;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper Canvas;
public void Initialize() {
IsInitialized = true;
Points = new anywheresoftware.b4a.objects.collections.List();
LinesColors = new anywheresoftware.b4a.objects.collections.List();
Target = new anywheresoftware.b4a.objects.PanelWrapper();
Canvas = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _bardata{
public boolean IsInitialized;
public anywheresoftware.b4a.objects.collections.List Points;
public anywheresoftware.b4a.objects.collections.List BarsColors;
public anywheresoftware.b4a.objects.PanelWrapper Target;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper Canvas;
public boolean Stacked;
public int BarsWidth;
public void Initialize() {
IsInitialized = true;
Points = new anywheresoftware.b4a.objects.collections.List();
BarsColors = new anywheresoftware.b4a.objects.collections.List();
Target = new anywheresoftware.b4a.objects.PanelWrapper();
Canvas = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
Stacked = false;
BarsWidth = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _addbarcolor(anywheresoftware.b4a.BA _ba,com.batcat.charts._bardata _bd,int _color) throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub AddBarColor(BD As BarData, Color As Int)";
 //BA.debugLineNum = 35;BA.debugLine="If BD.BarsColors.IsInitialized = False Then BD.Ba";
if (_bd.BarsColors.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
_bd.BarsColors.Initialize();};
 //BA.debugLineNum = 36;BA.debugLine="BD.BarsColors.Add(Color)";
_bd.BarsColors.Add((Object)(_color));
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _addbarpoint(anywheresoftware.b4a.BA _ba,com.batcat.charts._bardata _bd,String _x,float[] _yarray) throws Exception{
com.batcat.charts._linepoint _b = null;
 //BA.debugLineNum = 16;BA.debugLine="Sub AddBarPoint (BD As BarData, X As String, YArra";
 //BA.debugLineNum = 17;BA.debugLine="If BD.Points.IsInitialized = False Then";
if (_bd.Points.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 18;BA.debugLine="BD.Points.Initialize";
_bd.Points.Initialize();
 //BA.debugLineNum = 20;BA.debugLine="Dim b As LinePoint";
_b = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 21;BA.debugLine="b.Initialize";
_b.Initialize();
 //BA.debugLineNum = 22;BA.debugLine="b.X = \"\"";
_b.X = "";
 //BA.debugLineNum = 23;BA.debugLine="b.ShowTick = False";
_b.ShowTick = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 24;BA.debugLine="BD.Points.Add(b)";
_bd.Points.Add((Object)(_b));
 };
 //BA.debugLineNum = 26;BA.debugLine="Dim b As LinePoint 'using the same structure of L";
_b = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 27;BA.debugLine="b.Initialize";
_b.Initialize();
 //BA.debugLineNum = 28;BA.debugLine="b.X = X";
_b.X = _x;
 //BA.debugLineNum = 29;BA.debugLine="b.YArray = YArray";
_b.YArray = _yarray;
 //BA.debugLineNum = 30;BA.debugLine="b.ShowTick = True";
_b.ShowTick = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 31;BA.debugLine="BD.Points.Add(b)";
_bd.Points.Add((Object)(_b));
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public static String  _addlinecolor(anywheresoftware.b4a.BA _ba,com.batcat.charts._linedata _ld,int _color) throws Exception{
 //BA.debugLineNum = 182;BA.debugLine="Sub AddLineColor(LD As LineData, Color As Int)";
 //BA.debugLineNum = 183;BA.debugLine="If LD.LinesColors.IsInitialized = False Then LD.L";
if (_ld.LinesColors.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
_ld.LinesColors.Initialize();};
 //BA.debugLineNum = 184;BA.debugLine="LD.LinesColors.Add(Color)";
_ld.LinesColors.Add((Object)(_color));
 //BA.debugLineNum = 185;BA.debugLine="End Sub";
return "";
}
public static String  _addlinemultiplepoints(anywheresoftware.b4a.BA _ba,com.batcat.charts._linedata _ld,String _x,float[] _yarray,boolean _showtick) throws Exception{
com.batcat.charts._linepoint _p = null;
 //BA.debugLineNum = 172;BA.debugLine="Sub AddLineMultiplePoints(LD As LineData, X As Str";
 //BA.debugLineNum = 173;BA.debugLine="If LD.Points.IsInitialized = False Then LD.Points";
if (_ld.Points.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
_ld.Points.Initialize();};
 //BA.debugLineNum = 174;BA.debugLine="Dim p As LinePoint";
_p = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 175;BA.debugLine="p.Initialize";
_p.Initialize();
 //BA.debugLineNum = 176;BA.debugLine="p.X = X";
_p.X = _x;
 //BA.debugLineNum = 177;BA.debugLine="p.YArray = YArray";
_p.YArray = _yarray;
 //BA.debugLineNum = 178;BA.debugLine="p.ShowTick = ShowTick";
_p.ShowTick = _showtick;
 //BA.debugLineNum = 179;BA.debugLine="LD.Points.Add(p)";
_ld.Points.Add((Object)(_p));
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _addlinepoint(anywheresoftware.b4a.BA _ba,com.batcat.charts._linedata _ld,String _x,float _y,boolean _showtick) throws Exception{
com.batcat.charts._linepoint _p = null;
 //BA.debugLineNum = 162;BA.debugLine="Sub AddLinePoint (LD As LineData, X As String, Y A";
 //BA.debugLineNum = 163;BA.debugLine="If LD.Points.IsInitialized = False Then LD.Points";
if (_ld.Points.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
_ld.Points.Initialize();};
 //BA.debugLineNum = 164;BA.debugLine="Dim p As LinePoint";
_p = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 165;BA.debugLine="p.Initialize";
_p.Initialize();
 //BA.debugLineNum = 166;BA.debugLine="p.X = X";
_p.X = _x;
 //BA.debugLineNum = 167;BA.debugLine="p.Y = Y";
_p.Y = _y;
 //BA.debugLineNum = 168;BA.debugLine="p.ShowTick = ShowTick";
_p.ShowTick = _showtick;
 //BA.debugLineNum = 169;BA.debugLine="LD.Points.Add(p)";
_ld.Points.Add((Object)(_p));
 //BA.debugLineNum = 170;BA.debugLine="End Sub";
return "";
}
public static String  _addpieitem(anywheresoftware.b4a.BA _ba,com.batcat.charts._piedata _pd,String _name,float _value,int _color) throws Exception{
com.batcat.charts._pieitem _i = null;
 //BA.debugLineNum = 237;BA.debugLine="Sub AddPieItem(PD As PieData, Name As String, Valu";
 //BA.debugLineNum = 238;BA.debugLine="If PD.Items.IsInitialized = False Then PD.Items.I";
if (_pd.Items.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
_pd.Items.Initialize();};
 //BA.debugLineNum = 239;BA.debugLine="If Color = 0 Then Color = Colors.RGB(Rnd(0, 255),";
if (_color==0) { 
_color = anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (255)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (255)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (255)));};
 //BA.debugLineNum = 240;BA.debugLine="Dim i As PieItem";
_i = new com.batcat.charts._pieitem();
 //BA.debugLineNum = 241;BA.debugLine="i.Initialize";
_i.Initialize();
 //BA.debugLineNum = 242;BA.debugLine="i.Name = Name";
_i.Name = _name;
 //BA.debugLineNum = 243;BA.debugLine="i.Value = Value";
_i.Value = _value;
 //BA.debugLineNum = 244;BA.debugLine="i.Color = Color";
_i.Color = _color;
 //BA.debugLineNum = 245;BA.debugLine="PD.Items.Add(i)";
_pd.Items.Add((Object)(_i));
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static int  _calcpointtopixel(anywheresoftware.b4a.BA _ba,float _py,com.batcat.charts._graph _g) throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Sub calcPointToPixel(py As Float, G As Graph) As I";
 //BA.debugLineNum = 228;BA.debugLine="If G.YStart < 0 And G.YEnd > 0 Then";
if (_g.YStart<0 && _g.YEnd>0) { 
 //BA.debugLineNum = 229;BA.debugLine="Return G.GI.zeroY - (G.GI.originY - G.GI.maxY) *";
if (true) return (int) (_g.GI.zeroY-(_g.GI.originY-_g.GI.maxY)*_py/(double)(_g.YEnd-_g.YStart));
 }else {
 //BA.debugLineNum = 231;BA.debugLine="Return G.GI.originY - (G.GI.originY - G.GI.maxY)";
if (true) return (int) (_g.GI.originY-(_g.GI.originY-_g.GI.maxY)*(_py-_g.YStart)/(double)(_g.YEnd-_g.YStart));
 };
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return 0;
}
public static float  _calcslice(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.drawable.CanvasWrapper _canvas,int _radius,float _startingdegree,float _percent,int _color,int _gapdegrees) throws Exception{
float _b = 0f;
int _cx = 0;
int _cy = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper _p = null;
float _gap = 0f;
int _i = 0;
 //BA.debugLineNum = 282;BA.debugLine="Sub calcSlice(Canvas As Canvas, Radius As Int, _";
 //BA.debugLineNum = 284;BA.debugLine="Dim b As Float";
_b = 0f;
 //BA.debugLineNum = 285;BA.debugLine="b = 360 * Percent";
_b = (float) (360*_percent);
 //BA.debugLineNum = 287;BA.debugLine="Dim cx, cy As Int";
_cx = 0;
_cy = 0;
 //BA.debugLineNum = 288;BA.debugLine="cx = Canvas.Bitmap.Width / 2";
_cx = (int) (_canvas.getBitmap().getWidth()/(double)2);
 //BA.debugLineNum = 289;BA.debugLine="cy = Canvas.Bitmap.Height / 2";
_cy = (int) (_canvas.getBitmap().getHeight()/(double)2);
 //BA.debugLineNum = 290;BA.debugLine="Dim p As Path";
_p = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.PathWrapper();
 //BA.debugLineNum = 291;BA.debugLine="p.Initialize(cx, cy)";
_p.Initialize((float) (_cx),(float) (_cy));
 //BA.debugLineNum = 292;BA.debugLine="Dim gap As Float";
_gap = 0f;
 //BA.debugLineNum = 293;BA.debugLine="gap = Percent * GapDegrees / 2";
_gap = (float) (_percent*_gapdegrees/(double)2);
 //BA.debugLineNum = 294;BA.debugLine="For i = StartingDegree + gap To StartingDegree +";
{
final int step10 = (int) (10);
final int limit10 = (int) (_startingdegree+_b-_gap);
for (_i = (int) (_startingdegree+_gap) ; (step10 > 0 && _i <= limit10) || (step10 < 0 && _i >= limit10); _i = ((int)(0 + _i + step10)) ) {
 //BA.debugLineNum = 295;BA.debugLine="p.LineTo(cx + 2 * Radius * SinD(i), cy + 2 * Rad";
_p.LineTo((float) (_cx+2*_radius*anywheresoftware.b4a.keywords.Common.SinD(_i)),(float) (_cy+2*_radius*anywheresoftware.b4a.keywords.Common.CosD(_i)));
 }
};
 //BA.debugLineNum = 297;BA.debugLine="p.LineTo(cx + 2 * Radius * SinD(StartingDegree +";
_p.LineTo((float) (_cx+2*_radius*anywheresoftware.b4a.keywords.Common.SinD(_startingdegree+_b-_gap)),(float) (_cy+2*_radius*anywheresoftware.b4a.keywords.Common.CosD(_startingdegree+_b-_gap)));
 //BA.debugLineNum = 298;BA.debugLine="p.LineTo(cx, cy)";
_p.LineTo((float) (_cx),(float) (_cy));
 //BA.debugLineNum = 299;BA.debugLine="Canvas.ClipPath(p) 'We are limiting the drawings";
_canvas.ClipPath((android.graphics.Path)(_p.getObject()));
 //BA.debugLineNum = 300;BA.debugLine="Canvas.DrawCircle(cx, cy, Radius, Color, True, 0)";
_canvas.DrawCircle((float) (_cx),(float) (_cy),(float) (_radius),_color,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 301;BA.debugLine="Canvas.RemoveClip";
_canvas.RemoveClip();
 //BA.debugLineNum = 302;BA.debugLine="Return b";
if (true) return _b;
 //BA.debugLineNum = 303;BA.debugLine="End Sub";
return 0f;
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _createlegend(anywheresoftware.b4a.BA _ba,com.batcat.charts._piedata _pd) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
float _textheight = 0f;
float _textwidth = 0f;
int _i = 0;
com.batcat.charts._pieitem _it = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _c = null;
 //BA.debugLineNum = 305;BA.debugLine="Sub createLegend(PD As PieData) As Bitmap";
 //BA.debugLineNum = 306;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 307;BA.debugLine="If PD.LegendTextSize = 0 Then PD.LegendTextSize =";
if (_pd.LegendTextSize==0) { 
_pd.LegendTextSize = (float) (15);};
 //BA.debugLineNum = 308;BA.debugLine="Dim textHeight, textWidth As Float";
_textheight = 0f;
_textwidth = 0f;
 //BA.debugLineNum = 309;BA.debugLine="textHeight = PD.Canvas.MeasureStringHeight(\"M\", T";
_textheight = _pd.Canvas.MeasureStringHeight("M",anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,_pd.LegendTextSize);
 //BA.debugLineNum = 310;BA.debugLine="For i = 0 To PD.Items.Size - 1";
{
final int step5 = 1;
final int limit5 = (int) (_pd.Items.getSize()-1);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 311;BA.debugLine="Dim it As PieItem";
_it = new com.batcat.charts._pieitem();
 //BA.debugLineNum = 312;BA.debugLine="it = PD.Items.Get(i)";
_it = (com.batcat.charts._pieitem)(_pd.Items.Get(_i));
 //BA.debugLineNum = 313;BA.debugLine="textWidth = Max(textWidth, PD.Canvas.MeasureStri";
_textwidth = (float) (anywheresoftware.b4a.keywords.Common.Max(_textwidth,_pd.Canvas.MeasureStringWidth(_it.Name,anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,_pd.LegendTextSize)));
 }
};
 //BA.debugLineNum = 315;BA.debugLine="bmp.InitializeMutable(textWidth + 20dip, 10dip +(";
_bmp.InitializeMutable((int) (_textwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))+(_textheight+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))*_pd.Items.getSize()));
 //BA.debugLineNum = 316;BA.debugLine="Dim c As Canvas";
_c = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 317;BA.debugLine="c.Initialize2(bmp)";
_c.Initialize2((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 318;BA.debugLine="c.DrawColor(PD.LegendBackColor)";
_c.DrawColor(_pd.LegendBackColor);
 //BA.debugLineNum = 319;BA.debugLine="For i = 0 To PD.Items.Size - 1";
{
final int step14 = 1;
final int limit14 = (int) (_pd.Items.getSize()-1);
for (_i = (int) (0) ; (step14 > 0 && _i <= limit14) || (step14 < 0 && _i >= limit14); _i = ((int)(0 + _i + step14)) ) {
 //BA.debugLineNum = 320;BA.debugLine="Dim it As PieItem";
_it = new com.batcat.charts._pieitem();
 //BA.debugLineNum = 321;BA.debugLine="it = PD.Items.Get(i)";
_it = (com.batcat.charts._pieitem)(_pd.Items.Get(_i));
 //BA.debugLineNum = 322;BA.debugLine="c.DrawText(it.Name, 10dip, (i + 1) * (textHeight";
_c.DrawText(_ba,_it.Name,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(float) ((_i+1)*(_textheight+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,_pd.LegendTextSize,_it.Color,BA.getEnumFromString(android.graphics.Paint.Align.class,"LEFT"));
 }
};
 //BA.debugLineNum = 325;BA.debugLine="Return bmp";
if (true) return _bmp;
 //BA.debugLineNum = 326;BA.debugLine="End Sub";
return null;
}
public static String  _drawbarschart(anywheresoftware.b4a.BA _ba,com.batcat.charts._graph _g,com.batcat.charts._bardata _bd,int _backcolor) throws Exception{
com.batcat.charts._linepoint _point = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rect = null;
int _numberofbars = 0;
int _i = 0;
int _a = 0;
 //BA.debugLineNum = 39;BA.debugLine="Sub DrawBarsChart(G As Graph, BD As BarData, BackC";
 //BA.debugLineNum = 40;BA.debugLine="If BD.Points.Size = 0 Then";
if (_bd.Points.getSize()==0) { 
 //BA.debugLineNum = 41;BA.debugLine="ToastMessageShow(\"Missing bars points.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Missing bars points."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 42;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 44;BA.debugLine="BD.Canvas.Initialize(BD.Target)";
_bd.Canvas.Initialize((android.view.View)(_bd.Target.getObject()));
 //BA.debugLineNum = 45;BA.debugLine="BD.Canvas.DrawColor(BackColor)";
_bd.Canvas.DrawColor(_backcolor);
 //BA.debugLineNum = 46;BA.debugLine="Dim point As LinePoint";
_point = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 47;BA.debugLine="point = BD.Points.Get(1)";
_point = (com.batcat.charts._linepoint)(_bd.Points.Get((int) (1)));
 //BA.debugLineNum = 48;BA.debugLine="If BD.Stacked = False Then";
if (_bd.Stacked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 49;BA.debugLine="drawGraph(G, BD.Canvas, BD.Target, BD.Points, Tr";
_drawgraph(_ba,_g,_bd.Canvas,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_bd.Target.getObject())),_bd.Points,anywheresoftware.b4a.keywords.Common.True,_bd.BarsWidth);
 }else {
 //BA.debugLineNum = 52;BA.debugLine="drawGraph(G, BD.Canvas, BD.Target, BD.Points, Tr";
_drawgraph(_ba,_g,_bd.Canvas,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_bd.Target.getObject())),_bd.Points,anywheresoftware.b4a.keywords.Common.True,(int) (_bd.BarsWidth/(double)_point.YArray.length));
 };
 //BA.debugLineNum = 56;BA.debugLine="Dim Rect As Rect";
_rect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Rect.Initialize(0, 0, 0, G.GI.originY)";
_rect.Initialize((int) (0),(int) (0),(int) (0),_g.GI.originY);
 //BA.debugLineNum = 58;BA.debugLine="Dim numberOfBars As Int";
_numberofbars = 0;
 //BA.debugLineNum = 59;BA.debugLine="numberOfBars = point.YArray.Length";
_numberofbars = _point.YArray.length;
 //BA.debugLineNum = 61;BA.debugLine="For i = 1 To BD.Points.Size - 1";
{
final int step18 = 1;
final int limit18 = (int) (_bd.Points.getSize()-1);
for (_i = (int) (1) ; (step18 > 0 && _i <= limit18) || (step18 < 0 && _i >= limit18); _i = ((int)(0 + _i + step18)) ) {
 //BA.debugLineNum = 62;BA.debugLine="point = BD.Points.Get(i)";
_point = (com.batcat.charts._linepoint)(_bd.Points.Get(_i));
 //BA.debugLineNum = 63;BA.debugLine="For a = 0 To numberOfBars - 1";
{
final int step20 = 1;
final int limit20 = (int) (_numberofbars-1);
for (_a = (int) (0) ; (step20 > 0 && _a <= limit20) || (step20 < 0 && _a >= limit20); _a = ((int)(0 + _a + step20)) ) {
 //BA.debugLineNum = 64;BA.debugLine="If BD.Stacked = False Then";
if (_bd.Stacked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 65;BA.debugLine="Rect.Left = G.GI.originX + G.GI.intervalX * i";
_rect.setLeft((int) (_g.GI.originX+_g.GI.intervalX*_i+(_a-_numberofbars/(double)2)*_bd.BarsWidth));
 //BA.debugLineNum = 66;BA.debugLine="Rect.Right = Rect.Left + BD.BarsWidth";
_rect.setRight((int) (_rect.getLeft()+_bd.BarsWidth));
 //BA.debugLineNum = 67;BA.debugLine="If G.YStart < 0 And G.YEnd > 0 Then";
if (_g.YStart<0 && _g.YEnd>0) { 
 //BA.debugLineNum = 68;BA.debugLine="If point.YArray(a) > 0 Then";
if (_point.YArray[_a]>0) { 
 //BA.debugLineNum = 69;BA.debugLine="Rect.Top = calcPointToPixel(point.YArray(a),";
_rect.setTop(_calcpointtopixel(_ba,_point.YArray[_a],_g));
 //BA.debugLineNum = 70;BA.debugLine="Rect.Bottom = G.GI.zeroY";
_rect.setBottom(_g.GI.zeroY);
 }else {
 //BA.debugLineNum = 72;BA.debugLine="Rect.Bottom = calcPointToPixel(point.YArray(";
_rect.setBottom(_calcpointtopixel(_ba,_point.YArray[_a],_g));
 //BA.debugLineNum = 73;BA.debugLine="Rect.Top = G.GI.zeroY";
_rect.setTop(_g.GI.zeroY);
 };
 }else {
 //BA.debugLineNum = 76;BA.debugLine="Rect.Top = calcPointToPixel(point.YArray(a),";
_rect.setTop(_calcpointtopixel(_ba,_point.YArray[_a],_g));
 //BA.debugLineNum = 77;BA.debugLine="Rect.Bottom = G.GI.originY";
_rect.setBottom(_g.GI.originY);
 };
 }else {
 //BA.debugLineNum = 80;BA.debugLine="Rect.Left = G.GI.originX + G.GI.intervalX * i";
_rect.setLeft((int) (_g.GI.originX+_g.GI.intervalX*_i-_bd.BarsWidth/(double)2));
 //BA.debugLineNum = 81;BA.debugLine="Rect.Right = Rect.Left + BD.BarsWidth";
_rect.setRight((int) (_rect.getLeft()+_bd.BarsWidth));
 //BA.debugLineNum = 82;BA.debugLine="If a = 0 Then";
if (_a==0) { 
 //BA.debugLineNum = 83;BA.debugLine="Rect.Top = calcPointToPixel(0, G)";
_rect.setTop(_calcpointtopixel(_ba,(float) (0),_g));
 };
 //BA.debugLineNum = 85;BA.debugLine="Rect.Bottom = Rect.Top";
_rect.setBottom(_rect.getTop());
 //BA.debugLineNum = 86;BA.debugLine="Rect.Top = Rect.Bottom + calcPointToPixel(poin";
_rect.setTop((int) (_rect.getBottom()+_calcpointtopixel(_ba,_point.YArray[_a],_g)-_g.GI.originY));
 };
 //BA.debugLineNum = 88;BA.debugLine="BD.Canvas.DrawRect(Rect, BD.BarsColors.Get(a),";
_bd.Canvas.DrawRect((android.graphics.Rect)(_rect.getObject()),(int)(BA.ObjectToNumber(_bd.BarsColors.Get(_a))),anywheresoftware.b4a.keywords.Common.True,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 }
};
 }
};
 //BA.debugLineNum = 91;BA.debugLine="BD.Target.Invalidate";
_bd.Target.Invalidate();
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _drawgraph(anywheresoftware.b4a.BA _ba,com.batcat.charts._graph _g,anywheresoftware.b4a.objects.drawable.CanvasWrapper _canvas,anywheresoftware.b4a.objects.ConcreteViewWrapper _target,anywheresoftware.b4a.objects.collections.List _points,boolean _bars,int _barswidth) throws Exception{
com.batcat.charts._graphinternal _gi = null;
com.batcat.charts._linepoint _p = null;
int _i = 0;
int _y = 0;
float _yvalue = 0f;
int _x = 0;
 //BA.debugLineNum = 96;BA.debugLine="Sub drawGraph (G As Graph, Canvas As Canvas, Targe";
 //BA.debugLineNum = 97;BA.debugLine="Dim GI As GraphInternal";
_gi = new com.batcat.charts._graphinternal();
 //BA.debugLineNum = 98;BA.debugLine="G.GI = GI";
_g.GI = _gi;
 //BA.debugLineNum = 99;BA.debugLine="GI.Initialize";
_gi.Initialize();
 //BA.debugLineNum = 100;BA.debugLine="GI.maxY = 40dip";
_gi.maxY = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40));
 //BA.debugLineNum = 101;BA.debugLine="GI.originX = 50dip";
_gi.originX = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 102;BA.debugLine="GI.originY = Target.Height - 60dip";
_gi.originY = (int) (_target.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 103;BA.debugLine="GI.gw = Target.Width - 70dip 'graph width";
_gi.gw = (int) (_target.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (70)));
 //BA.debugLineNum = 104;BA.debugLine="GI.gh = GI.originY - GI.maxY 'graph height";
_gi.gh = (int) (_gi.originY-_gi.maxY);
 //BA.debugLineNum = 105;BA.debugLine="If G.YStart < 0 And G.YEnd > 0 Then";
if (_g.YStart<0 && _g.YEnd>0) { 
 //BA.debugLineNum = 106;BA.debugLine="GI.zeroY = GI.maxY + GI.gh * G.YEnd / (G.YEnd -";
_gi.zeroY = (int) (_gi.maxY+_gi.gh*_g.YEnd/(double)(_g.YEnd-_g.YStart));
 }else {
 //BA.debugLineNum = 108;BA.debugLine="GI.zeroY = GI.originY";
_gi.zeroY = _gi.originY;
 };
 //BA.debugLineNum = 110;BA.debugLine="If Bars Then";
if (_bars) { 
 //BA.debugLineNum = 111;BA.debugLine="Dim p As LinePoint";
_p = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 112;BA.debugLine="p = Points.Get(1)";
_p = (com.batcat.charts._linepoint)(_points.Get((int) (1)));
 //BA.debugLineNum = 113;BA.debugLine="GI.intervalX = (GI.gw - p.YArray.Length / 2 * Ba";
_gi.intervalX = (float) ((_gi.gw-_p.YArray.length/(double)2*_barswidth)/(double)(_points.getSize()-1));
 }else {
 //BA.debugLineNum = 115;BA.debugLine="GI.intervalX = GI.gw / (Points.Size - 1)";
_gi.intervalX = (float) (_gi.gw/(double)(_points.getSize()-1));
 };
 //BA.debugLineNum = 118;BA.debugLine="Canvas.DrawLine(GI.originX, GI.originY + 2dip, GI";
_canvas.DrawLine((float) (_gi.originX),(float) (_gi.originY+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),(float) (_gi.originX),(float) (_gi.maxY-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 119;BA.debugLine="For i = 0 To (G.YEnd - G.YStart) / G.Yinterval +";
{
final int step22 = 1;
final int limit22 = (int) ((_g.YEnd-_g.YStart)/(double)_g.YInterval+1);
for (_i = (int) (0) ; (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22)) ) {
 //BA.debugLineNum = 120;BA.debugLine="Dim y As Int";
_y = 0;
 //BA.debugLineNum = 121;BA.debugLine="Dim yValue As Float";
_yvalue = 0f;
 //BA.debugLineNum = 122;BA.debugLine="yValue = G.YStart + G.YInterval * i";
_yvalue = (float) (_g.YStart+_g.YInterval*_i);
 //BA.debugLineNum = 123;BA.debugLine="If yValue > G.YEnd Then Continue";
if (_yvalue>_g.YEnd) { 
if (true) continue;};
 //BA.debugLineNum = 124;BA.debugLine="y = calcPointToPixel(yValue, G)";
_y = _calcpointtopixel(_ba,_yvalue,_g);
 //BA.debugLineNum = 125;BA.debugLine="Canvas.DrawLine(GI.originX, y, GI.originX - 5dip";
_canvas.DrawLine((float) (_gi.originX),(float) (_y),(float) (_gi.originX-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),(float) (_y),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 126;BA.debugLine="If i < (G.YEnd - G.YStart) / G.Yinterval Then";
if (_i<(_g.YEnd-_g.YStart)/(double)_g.YInterval) { 
 //BA.debugLineNum = 127;BA.debugLine="Canvas.DrawLine(GI.originX, y, GI.originX + GI.";
_canvas.DrawLine((float) (_gi.originX),(float) (_y),(float) (_gi.originX+_gi.gw),(float) (_y),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 }else {
 //BA.debugLineNum = 129;BA.debugLine="Canvas.DrawLine(GI.originX, y, GI.originX + GI.";
_canvas.DrawLine((float) (_gi.originX),(float) (_y),(float) (_gi.originX+_gi.gw),(float) (_y),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 };
 //BA.debugLineNum = 131;BA.debugLine="Canvas.DrawText(NumberFormat(yValue, 1, 2), GI.o";
_canvas.DrawText(_ba,anywheresoftware.b4a.keywords.Common.NumberFormat(_yvalue,(int) (1),(int) (2)),(float) (_gi.originX-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (_y+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT,(float) (12),_g.AxisColor,BA.getEnumFromString(android.graphics.Paint.Align.class,"RIGHT"));
 }
};
 //BA.debugLineNum = 134;BA.debugLine="Canvas.DrawText(G.Title, Target.Width / 2, 30dip,";
_canvas.DrawText(_ba,_g.Title,(float) (_target.getWidth()/(double)2),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (15),_g.AxisColor,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 //BA.debugLineNum = 135;BA.debugLine="Canvas.DrawText(G.XAxis, Target.Width / 2, GI.ori";
_canvas.DrawText(_ba,_g.XAxis,(float) (_target.getWidth()/(double)2),(float) (_gi.originY+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT,(float) (14),_g.AxisColor,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 //BA.debugLineNum = 136;BA.debugLine="Canvas.DrawTextRotated(G.YAxis, 15dip, Target.Hei";
_canvas.DrawTextRotated(_ba,_g.YAxis,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (15))),(float) (_target.getHeight()/(double)2),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT,(float) (14),_g.AxisColor,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"),(float) (-90));
 //BA.debugLineNum = 138;BA.debugLine="Canvas.DrawLine(GI.originX, GI.originY, GI.origin";
_canvas.DrawLine((float) (_gi.originX),(float) (_gi.originY),(float) (_gi.originX+_gi.gw),(float) (_gi.originY),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 139;BA.debugLine="For i = 0 To Points.Size - 1";
{
final int step40 = 1;
final int limit40 = (int) (_points.getSize()-1);
for (_i = (int) (0) ; (step40 > 0 && _i <= limit40) || (step40 < 0 && _i >= limit40); _i = ((int)(0 + _i + step40)) ) {
 //BA.debugLineNum = 140;BA.debugLine="Dim p As LinePoint";
_p = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 141;BA.debugLine="p = Points.Get(i)";
_p = (com.batcat.charts._linepoint)(_points.Get(_i));
 //BA.debugLineNum = 142;BA.debugLine="If p.ShowTick Then";
if (_p.ShowTick) { 
 //BA.debugLineNum = 143;BA.debugLine="Dim x As Int";
_x = 0;
 //BA.debugLineNum = 144;BA.debugLine="x = GI.originX + i * GI.intervalX";
_x = (int) (_gi.originX+_i*_gi.intervalX);
 //BA.debugLineNum = 145;BA.debugLine="Canvas.DrawLine(x, GI.originY, x, GI.originY +";
_canvas.DrawLine((float) (_x),(float) (_gi.originY),(float) (_x),(float) (_gi.originY+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 146;BA.debugLine="If Bars = False Then";
if (_bars==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 147;BA.debugLine="If i < Points.Size - 1 Then";
if (_i<_points.getSize()-1) { 
 //BA.debugLineNum = 148;BA.debugLine="Canvas.DrawLine(x, GI.originY, x, GI.maxY, G.";
_canvas.DrawLine((float) (_x),(float) (_gi.originY),(float) (_x),(float) (_gi.maxY),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 }else {
 //BA.debugLineNum = 150;BA.debugLine="Canvas.DrawLine(x, GI.originY, x, GI.maxY, G.";
_canvas.DrawLine((float) (_x),(float) (_gi.originY),(float) (_x),(float) (_gi.maxY),_g.AxisColor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 };
 };
 //BA.debugLineNum = 153;BA.debugLine="If p.x.Length > 0 Then";
if (_p.X.length()>0) { 
 //BA.debugLineNum = 154;BA.debugLine="Canvas.DrawTextRotated(p.x, x, GI.originY + 12";
_canvas.DrawTextRotated(_ba,_p.X,(float) (_x),(float) (_gi.originY+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT,(float) (12),_g.AxisColor,BA.getEnumFromString(android.graphics.Paint.Align.class,"RIGHT"),(float) (-45));
 };
 };
 }
};
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _drawlinechart(anywheresoftware.b4a.BA _ba,com.batcat.charts._graph _g,com.batcat.charts._linedata _ld,int _backcolor) throws Exception{
com.batcat.charts._linepoint _point = null;
float[] _py2 = null;
int _i = 0;
int _a = 0;
float _py = 0f;
 //BA.debugLineNum = 187;BA.debugLine="Sub DrawLineChart(G As Graph, LD As LineData, Back";
 //BA.debugLineNum = 188;BA.debugLine="If LD.Points.Size = 0 Then";
if (_ld.Points.getSize()==0) { 
 //BA.debugLineNum = 189;BA.debugLine="ToastMessageShow(\"Missing line points.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Missing line points."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 190;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 192;BA.debugLine="LD.Canvas.Initialize(LD.Target)";
_ld.Canvas.Initialize((android.view.View)(_ld.Target.getObject()));
 //BA.debugLineNum = 193;BA.debugLine="LD.Canvas.DrawColor(BackColor)";
_ld.Canvas.DrawColor(_backcolor);
 //BA.debugLineNum = 194;BA.debugLine="drawGraph(G, LD.Canvas, LD.Target, LD.Points, Fal";
_drawgraph(_ba,_g,_ld.Canvas,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ld.Target.getObject())),_ld.Points,anywheresoftware.b4a.keywords.Common.False,(int) (0));
 //BA.debugLineNum = 196;BA.debugLine="Dim point As LinePoint";
_point = new com.batcat.charts._linepoint();
 //BA.debugLineNum = 197;BA.debugLine="point = LD.Points.Get(0)";
_point = (com.batcat.charts._linepoint)(_ld.Points.Get((int) (0)));
 //BA.debugLineNum = 198;BA.debugLine="If point.YArray.Length > 0 Then";
if (_point.YArray.length>0) { 
 //BA.debugLineNum = 200;BA.debugLine="Dim py2(point.YArray.Length) As Float";
_py2 = new float[_point.YArray.length];
;
 //BA.debugLineNum = 202;BA.debugLine="For i = 0 To py2.Length - 1";
{
final int step12 = 1;
final int limit12 = (int) (_py2.length-1);
for (_i = (int) (0) ; (step12 > 0 && _i <= limit12) || (step12 < 0 && _i >= limit12); _i = ((int)(0 + _i + step12)) ) {
 //BA.debugLineNum = 203;BA.debugLine="py2(i) = point.YArray(i)";
_py2[_i] = _point.YArray[_i];
 }
};
 //BA.debugLineNum = 206;BA.debugLine="For i = 1 To LD.Points.Size - 1";
{
final int step15 = 1;
final int limit15 = (int) (_ld.Points.getSize()-1);
for (_i = (int) (1) ; (step15 > 0 && _i <= limit15) || (step15 < 0 && _i >= limit15); _i = ((int)(0 + _i + step15)) ) {
 //BA.debugLineNum = 207;BA.debugLine="point = LD.Points.Get(i)";
_point = (com.batcat.charts._linepoint)(_ld.Points.Get(_i));
 //BA.debugLineNum = 208;BA.debugLine="For a = 0 To py2.Length - 1";
{
final int step17 = 1;
final int limit17 = (int) (_py2.length-1);
for (_a = (int) (0) ; (step17 > 0 && _a <= limit17) || (step17 < 0 && _a >= limit17); _a = ((int)(0 + _a + step17)) ) {
 //BA.debugLineNum = 209;BA.debugLine="LD.Canvas.DrawLine(G.GI.originX + G.GI.interva";
_ld.Canvas.DrawLine((float) (_g.GI.originX+_g.GI.intervalX*(_i-1)),(float) (_calcpointtopixel(_ba,_py2[_a],_g)),(float) (_g.GI.originX+_g.GI.intervalX*_i),(float) (_calcpointtopixel(_ba,_point.YArray[_a],_g)),(int)(BA.ObjectToNumber(_ld.LinesColors.Get(_a))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 210;BA.debugLine="py2(a) = point.YArray(a)";
_py2[_a] = _point.YArray[_a];
 }
};
 }
};
 }else {
 //BA.debugLineNum = 215;BA.debugLine="Dim py As Float";
_py = 0f;
 //BA.debugLineNum = 216;BA.debugLine="py = point.Y";
_py = _point.Y;
 //BA.debugLineNum = 217;BA.debugLine="For i = 1 To LD.Points.Size - 1";
{
final int step25 = 1;
final int limit25 = (int) (_ld.Points.getSize()-1);
for (_i = (int) (1) ; (step25 > 0 && _i <= limit25) || (step25 < 0 && _i >= limit25); _i = ((int)(0 + _i + step25)) ) {
 //BA.debugLineNum = 218;BA.debugLine="point = LD.Points.Get(i)";
_point = (com.batcat.charts._linepoint)(_ld.Points.Get(_i));
 //BA.debugLineNum = 219;BA.debugLine="LD.Canvas.DrawLine(G.GI.originX + G.GI.interval";
_ld.Canvas.DrawLine((float) (_g.GI.originX+_g.GI.intervalX*(_i-1)),(float) (_calcpointtopixel(_ba,_py,_g)),(float) (_g.GI.originX+_g.GI.intervalX*_i),(float) (_calcpointtopixel(_ba,_point.Y,_g)),(int)(BA.ObjectToNumber(_ld.LinesColors.Get((int) (0)))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 221;BA.debugLine="py = point.Y";
_py = _point.Y;
 }
};
 };
 //BA.debugLineNum = 224;BA.debugLine="LD.Target.Invalidate";
_ld.Target.Invalidate();
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _drawpie(anywheresoftware.b4a.BA _ba,com.batcat.charts._piedata _pd,int _backcolor,boolean _createlegendbitmap) throws Exception{
int _radius = 0;
int _total = 0;
int _i = 0;
com.batcat.charts._pieitem _it = null;
float _startingangle = 0f;
int _gapdegrees = 0;
 //BA.debugLineNum = 248;BA.debugLine="Sub DrawPie (PD As PieData, BackColor As Int, Crea";
 //BA.debugLineNum = 249;BA.debugLine="If PD.Items.Size = 0 Then";
if (_pd.Items.getSize()==0) { 
 //BA.debugLineNum = 250;BA.debugLine="ToastMessageShow(\"Missing pie values.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Missing pie values."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 251;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 253;BA.debugLine="PD.Canvas.Initialize(PD.Target)";
_pd.Canvas.Initialize((android.view.View)(_pd.Target.getObject()));
 //BA.debugLineNum = 254;BA.debugLine="PD.Canvas.DrawColor(BackColor)";
_pd.Canvas.DrawColor(_backcolor);
 //BA.debugLineNum = 255;BA.debugLine="Dim Radius As Int";
_radius = 0;
 //BA.debugLineNum = 256;BA.debugLine="Radius = Min(PD.Canvas.Bitmap.Width, PD.Canvas.Bi";
_radius = (int) (anywheresoftware.b4a.keywords.Common.Min(_pd.Canvas.getBitmap().getWidth(),_pd.Canvas.getBitmap().getHeight())*0.8/(double)2);
 //BA.debugLineNum = 257;BA.debugLine="Dim total As Int";
_total = 0;
 //BA.debugLineNum = 258;BA.debugLine="For i = 0 To PD.Items.Size - 1";
{
final int step10 = 1;
final int limit10 = (int) (_pd.Items.getSize()-1);
for (_i = (int) (0) ; (step10 > 0 && _i <= limit10) || (step10 < 0 && _i >= limit10); _i = ((int)(0 + _i + step10)) ) {
 //BA.debugLineNum = 259;BA.debugLine="Dim it As PieItem";
_it = new com.batcat.charts._pieitem();
 //BA.debugLineNum = 260;BA.debugLine="it = PD.Items.Get(i)";
_it = (com.batcat.charts._pieitem)(_pd.Items.Get(_i));
 //BA.debugLineNum = 261;BA.debugLine="total = total + it.Value";
_total = (int) (_total+_it.Value);
 }
};
 //BA.debugLineNum = 263;BA.debugLine="Dim startingAngle As Float";
_startingangle = 0f;
 //BA.debugLineNum = 264;BA.debugLine="startingAngle = 0";
_startingangle = (float) (0);
 //BA.debugLineNum = 265;BA.debugLine="Dim GapDegrees As Int";
_gapdegrees = 0;
 //BA.debugLineNum = 266;BA.debugLine="If PD.Items.Size = 1 Then GapDegrees = 0 Else Gap";
if (_pd.Items.getSize()==1) { 
_gapdegrees = (int) (0);}
else {
_gapdegrees = _pd.GapDegrees;};
 //BA.debugLineNum = 267;BA.debugLine="For i = 0 To PD.Items.Size - 1";
{
final int step19 = 1;
final int limit19 = (int) (_pd.Items.getSize()-1);
for (_i = (int) (0) ; (step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19); _i = ((int)(0 + _i + step19)) ) {
 //BA.debugLineNum = 268;BA.debugLine="Dim it As PieItem";
_it = new com.batcat.charts._pieitem();
 //BA.debugLineNum = 269;BA.debugLine="it = PD.Items.Get(i)";
_it = (com.batcat.charts._pieitem)(_pd.Items.Get(_i));
 //BA.debugLineNum = 270;BA.debugLine="startingAngle = startingAngle + _  			calcSlice(";
_startingangle = (float) (_startingangle+_calcslice(_ba,_pd.Canvas,_radius,_startingangle,(float) (_it.Value/(double)_total),_it.Color,_gapdegrees));
 }
};
 //BA.debugLineNum = 273;BA.debugLine="PD.Target.Invalidate";
_pd.Target.Invalidate();
 //BA.debugLineNum = 274;BA.debugLine="If CreateLegendBitmap Then";
if (_createlegendbitmap) { 
 //BA.debugLineNum = 275;BA.debugLine="Return createLegend(PD)";
if (true) return _createlegend(_ba,_pd);
 }else {
 //BA.debugLineNum = 277;BA.debugLine="Return Null";
if (true) return (anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 279;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 4;BA.debugLine="Type PieItem (Name As String, Value As Float, Col";
;
 //BA.debugLineNum = 5;BA.debugLine="Type PieData (Items As List, Target As Panel, Can";
;
 //BA.debugLineNum = 7;BA.debugLine="Type GraphInternal (originX As Int, zeroY As Int,";
;
 //BA.debugLineNum = 8;BA.debugLine="Type Graph (GI As GraphInternal, Title As String,";
;
 //BA.debugLineNum = 10;BA.debugLine="Type LinePoint (X As String, Y As Float, YArray()";
;
 //BA.debugLineNum = 11;BA.debugLine="Type LineData (Points As List, LinesColors As Lis";
;
 //BA.debugLineNum = 12;BA.debugLine="Type BarData (Points As List, BarsColors As List,";
;
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
}
