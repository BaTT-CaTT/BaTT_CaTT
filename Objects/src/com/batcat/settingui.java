package com.batcat;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class settingui extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "com.batcat.settingui");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", com.batcat.settingui.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.PanelWrapper _parents = null;
public int _top = 0;
public String _sfontname = "";
public de.amberhome.objects.preferenceactivity.PreferenceManager _manager = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _sv = null;
public com.batcat.main _main = null;
public com.batcat.klo _klo = null;
public com.batcat.settings _settings = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
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
public String  _addbullet(int _stop,int _color) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _cd = null;
 //BA.debugLineNum = 207;BA.debugLine="Sub AddBullet(sTop As Int,Color As Int)";
 //BA.debugLineNum = 209;BA.debugLine="Dim lb As Label";
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 210;BA.debugLine="lb.Initialize(\"\")";
_lb.Initialize(ba,"");
 //BA.debugLineNum = 211;BA.debugLine="sv.Panel.AddView(lb,sv.Panel.Width-15dip,sTop+8di";
_sv.getPanel().AddView((android.view.View)(_lb.getObject()),(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (15))),(int) (_stop+__c.DipToCurrent((int) (8))),__c.DipToCurrent((int) (7)),__c.DipToCurrent((int) (7)));
 //BA.debugLineNum = 212;BA.debugLine="Dim cd As ColorDrawable";
_cd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 213;BA.debugLine="cd.Initialize(Color,lb.Width/2)";
_cd.Initialize(_color,(int) (_lb.getWidth()/(double)2));
 //BA.debugLineNum = 214;BA.debugLine="lb.Background = cd";
_lb.setBackground((android.graphics.drawable.Drawable)(_cd.getObject()));
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public String  _addcheckbox(String _key,String _title,boolean _defaultvalue) throws Exception{
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chk = null;
 //BA.debugLineNum = 52;BA.debugLine="Public Sub AddCheckbox(Key As String,Title As Stri";
 //BA.debugLineNum = 54;BA.debugLine="Dim chk As CheckBox";
_chk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 55;BA.debugLine="chk.Initialize(\"checkbox\")";
_chk.Initialize(ba,"checkbox");
 //BA.debugLineNum = 56;BA.debugLine="chk.Tag = Key";
_chk.setTag((Object)(_key));
 //BA.debugLineNum = 57;BA.debugLine="chk.Checked = DefaultValue";
_chk.setChecked(_defaultvalue);
 //BA.debugLineNum = 58;BA.debugLine="sv.Panel.AddView(chk,sv.Panel.Width - 37dip,Top,3";
_sv.getPanel().AddView((android.view.View)(_chk.getObject()),(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (37))),_top,__c.DipToCurrent((int) (30)),__c.DipToCurrent((int) (30)));
 //BA.debugLineNum = 60;BA.debugLine="AddLabel(Title,11,Top+5dip,100%x-chk.Width - 7dip";
_addlabel(_title,(int) (11),(int) (_top+__c.DipToCurrent((int) (5))),(int) (__c.PerXToCurrent((float) (100),ba)-_chk.getWidth()-__c.DipToCurrent((int) (7))),_chk.getHeight(),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_chk.getObject())));
 //BA.debugLineNum = 61;BA.debugLine="Top = Top + 40dip";
_top = (int) (_top+__c.DipToCurrent((int) (40)));
 //BA.debugLineNum = 63;BA.debugLine="If manager.GetAll.ContainsKey(Key) Then chk.Check";
if (_manager.GetAll().ContainsKey((Object)(_key))) { 
_chk.setChecked(_manager.GetBoolean(_key));};
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public String  _adddivider() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
 //BA.debugLineNum = 195;BA.debugLine="Sub AddDivider";
 //BA.debugLineNum = 197;BA.debugLine="Top = Top + 4dip";
_top = (int) (_top+__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 198;BA.debugLine="Dim lb As Label";
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 199;BA.debugLine="lb.Initialize(\"\")";
_lb.Initialize(ba,"");
 //BA.debugLineNum = 200;BA.debugLine="lb.Color = Colors.ARGB(20,95,95,95)";
_lb.setColor(__c.Colors.ARGB((int) (20),(int) (95),(int) (95),(int) (95)));
 //BA.debugLineNum = 201;BA.debugLine="sv.Panel.AddView(lb,7dip,Top,sv.Panel.Width - 17d";
_sv.getPanel().AddView((android.view.View)(_lb.getObject()),__c.DipToCurrent((int) (7)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (17))),(int) (1));
 //BA.debugLineNum = 203;BA.debugLine="Top = Top + 10dip";
_top = (int) (_top+__c.DipToCurrent((int) (10)));
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public String  _addedittext(String _key,String _hint,int _inputtype,boolean _singleline,boolean _password) throws Exception{
anywheresoftware.b4a.objects.EditTextWrapper _ed = null;
 //BA.debugLineNum = 67;BA.debugLine="Public Sub AddEditText(Key As String,Hint As Strin";
 //BA.debugLineNum = 69;BA.debugLine="Dim ed As EditText";
_ed = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 70;BA.debugLine="ed.Initialize(\"edittext\")";
_ed.Initialize(ba,"edittext");
 //BA.debugLineNum = 71;BA.debugLine="ed.Tag = Key";
_ed.setTag((Object)(_key));
 //BA.debugLineNum = 72;BA.debugLine="ed.Hint = Hint";
_ed.setHint(_hint);
 //BA.debugLineNum = 73;BA.debugLine="ed.TextColor = Colors.Black";
_ed.setTextColor(__c.Colors.Black);
 //BA.debugLineNum = 74;BA.debugLine="ed.HintColor = Colors.Gray";
_ed.setHintColor(__c.Colors.Gray);
 //BA.debugLineNum = 75;BA.debugLine="ed.TextSize = 12";
_ed.setTextSize((float) (12));
 //BA.debugLineNum = 76;BA.debugLine="ed.PasswordMode = Password";
_ed.setPasswordMode(_password);
 //BA.debugLineNum = 77;BA.debugLine="ed.Typeface = Typeface.LoadFromAssets(sFontname)";
_ed.setTypeface(__c.Typeface.LoadFromAssets(_sfontname));
 //BA.debugLineNum = 78;BA.debugLine="ed.InputType = InputType";
_ed.setInputType(_inputtype);
 //BA.debugLineNum = 79;BA.debugLine="ed.SingleLine = SingleLine";
_ed.setSingleLine(_singleline);
 //BA.debugLineNum = 80;BA.debugLine="ed.Padding = Array As Int(10,10,10,10)";
_ed.setPadding(new int[]{(int) (10),(int) (10),(int) (10),(int) (10)});
 //BA.debugLineNum = 82;BA.debugLine="If SingleLine = False Then";
if (_singleline==__c.False) { 
 //BA.debugLineNum = 83;BA.debugLine="sv.Panel.AddView(ed,7dip,Top,sv.Panel.Width - 14";
_sv.getPanel().AddView((android.view.View)(_ed.getObject()),__c.DipToCurrent((int) (7)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (14))),__c.DipToCurrent((int) (100)));
 //BA.debugLineNum = 84;BA.debugLine="ed.Gravity = Bit.Or(Gravity.TOP,Gravity.LEFT)";
_ed.setGravity(__c.Bit.Or(__c.Gravity.TOP,__c.Gravity.LEFT));
 }else {
 //BA.debugLineNum = 86;BA.debugLine="sv.Panel.AddView(ed,7dip,Top,sv.Panel.Width - 14";
_sv.getPanel().AddView((android.view.View)(_ed.getObject()),__c.DipToCurrent((int) (7)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (14))),__c.DipToCurrent((int) (50)));
 //BA.debugLineNum = 87;BA.debugLine="ed.Gravity = Gravity.left";
_ed.setGravity(__c.Gravity.LEFT);
 };
 //BA.debugLineNum = 90;BA.debugLine="Top = Top + ed.Height + 4dip";
_top = (int) (_top+_ed.getHeight()+__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 92;BA.debugLine="If manager.GetAll.ContainsKey(Key) Then ed.Text =";
if (_manager.GetAll().ContainsKey((Object)(_key))) { 
_ed.setText(BA.ObjectToCharSequence(_manager.GetString(_key)));};
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public String  _addlabel(String _title,int _fontsize,int _stop,int _swidth,int _sheight,anywheresoftware.b4a.objects.ConcreteViewWrapper _depencyview) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
 //BA.debugLineNum = 176;BA.debugLine="Private Sub AddLabel(Title As String,FontSize As I";
 //BA.debugLineNum = 178;BA.debugLine="Dim lb As Label";
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 179;BA.debugLine="lb.Initialize(\"lblcheckbox\")";
_lb.Initialize(ba,"lblcheckbox");
 //BA.debugLineNum = 180;BA.debugLine="lb.TextColor = Colors.Black";
_lb.setTextColor(__c.Colors.Black);
 //BA.debugLineNum = 181;BA.debugLine="lb.TextSize = FontSize";
_lb.setTextSize((float) (_fontsize));
 //BA.debugLineNum = 182;BA.debugLine="lb.Text = Title";
_lb.setText(BA.ObjectToCharSequence(_title));
 //BA.debugLineNum = 183;BA.debugLine="Try";
try { //BA.debugLineNum = 184;BA.debugLine="lb.Tag = DepencyView";
_lb.setTag((Object)(_depencyview.getObject()));
 } 
       catch (Exception e9) {
			ba.setLastException(e9); };
 //BA.debugLineNum = 189;BA.debugLine="lb.Typeface = Typeface.LoadFromAssets(sFontname)";
_lb.setTypeface(__c.Typeface.LoadFromAssets(_sfontname));
 //BA.debugLineNum = 190;BA.debugLine="lb.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gra";
_lb.setGravity(__c.Bit.Or(__c.Gravity.CENTER_HORIZONTAL,__c.Gravity.LEFT));
 //BA.debugLineNum = 191;BA.debugLine="sv.Panel.AddView(lb,0,sTop,sWidth-3dip,sHeight)";
_sv.getPanel().AddView((android.view.View)(_lb.getObject()),(int) (0),_stop,(int) (_swidth-__c.DipToCurrent((int) (3))),_sheight);
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
public String  _addlistview(String _key,String _title,anywheresoftware.b4a.objects.collections.List _listdata) throws Exception{
anywheresoftware.b4a.objects.ListViewWrapper _ed = null;
int _lblheight = 0;
int _i = 0;
 //BA.debugLineNum = 146;BA.debugLine="Public Sub AddListview(Key As String,Title As Stri";
 //BA.debugLineNum = 148;BA.debugLine="AddBullet(Top + 10dip,Colors.Red)";
_addbullet((int) (_top+__c.DipToCurrent((int) (10))),__c.Colors.Red);
 //BA.debugLineNum = 149;BA.debugLine="AddLabel(Title,12,Top+10dip,sv.Panel.Width - 17di";
_addlabel(_title,(int) (12),(int) (_top+__c.DipToCurrent((int) (10))),(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (17))),__c.DipToCurrent((int) (20)),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(__c.Null)));
 //BA.debugLineNum = 151;BA.debugLine="Top = Top + 45dip";
_top = (int) (_top+__c.DipToCurrent((int) (45)));
 //BA.debugLineNum = 153;BA.debugLine="Dim ED As ListView";
_ed = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 154;BA.debugLine="ED.Initialize(\"listview\")";
_ed.Initialize(ba,"listview");
 //BA.debugLineNum = 155;BA.debugLine="ED.Tag = Key";
_ed.setTag((Object)(_key));
 //BA.debugLineNum = 156;BA.debugLine="ED.FastScrollEnabled = True";
_ed.setFastScrollEnabled(__c.True);
 //BA.debugLineNum = 157;BA.debugLine="ED.SingleLineLayout.Label.Gravity = Gravity.left";
_ed.getSingleLineLayout().Label.setGravity(__c.Gravity.LEFT);
 //BA.debugLineNum = 158;BA.debugLine="ED.SingleLineLayout.Label.TextSize = 13";
_ed.getSingleLineLayout().Label.setTextSize((float) (13));
 //BA.debugLineNum = 159;BA.debugLine="ED.SingleLineLayout.Label.TextColor = Colors.Blac";
_ed.getSingleLineLayout().Label.setTextColor(__c.Colors.Black);
 //BA.debugLineNum = 160;BA.debugLine="ED.SingleLineLayout.Label.Gravity = Bit.Or(Gravit";
_ed.getSingleLineLayout().Label.setGravity(__c.Bit.Or(__c.Gravity.CENTER_VERTICAL,__c.Gravity.LEFT));
 //BA.debugLineNum = 161;BA.debugLine="ED.SingleLineLayout.Label.Left = 0";
_ed.getSingleLineLayout().Label.setLeft((int) (0));
 //BA.debugLineNum = 162;BA.debugLine="ED.SingleLineLayout.Label.Width = sv.Panel.Width-";
_ed.getSingleLineLayout().Label.setWidth((int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (20))));
 //BA.debugLineNum = 164;BA.debugLine="Dim lblheight As Int";
_lblheight = 0;
 //BA.debugLineNum = 165;BA.debugLine="lblheight = ED.SingleLineLayout.ItemHeight * List";
_lblheight = (int) (_ed.getSingleLineLayout().getItemHeight()*_listdata.getSize());
 //BA.debugLineNum = 166;BA.debugLine="sv.Panel.AddView(ED,7dip,Top,sv.Panel.Width - 14d";
_sv.getPanel().AddView((android.view.View)(_ed.getObject()),__c.DipToCurrent((int) (7)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (14))),_lblheight);
 //BA.debugLineNum = 168;BA.debugLine="For i = 0 To ListData.Size - 1";
{
final int step17 = 1;
final int limit17 = (int) (_listdata.getSize()-1);
for (_i = (int) (0) ; (step17 > 0 && _i <= limit17) || (step17 < 0 && _i >= limit17); _i = ((int)(0 + _i + step17)) ) {
 //BA.debugLineNum = 169;BA.debugLine="ED.AddSingleLine(ListData.Get(i))";
_ed.AddSingleLine(BA.ObjectToCharSequence(_listdata.Get(_i)));
 }
};
 //BA.debugLineNum = 172;BA.debugLine="Top = Top + ED.Height + 4dip";
_top = (int) (_top+_ed.getHeight()+__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public String  _addseekbar(String _key,String _title,int _maxvalue,int _value) throws Exception{
anywheresoftware.b4a.objects.SeekBarWrapper _ed = null;
 //BA.debugLineNum = 96;BA.debugLine="Public Sub AddSeekbar(Key As String,Title As Strin";
 //BA.debugLineNum = 98;BA.debugLine="AddBullet(Top + 10dip,Colors.Red)";
_addbullet((int) (_top+__c.DipToCurrent((int) (10))),__c.Colors.Red);
 //BA.debugLineNum = 99;BA.debugLine="AddLabel(Title,13,Top+10dip,sv.Panel.Width - 17di";
_addlabel(_title,(int) (13),(int) (_top+__c.DipToCurrent((int) (10))),(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (17))),__c.DipToCurrent((int) (20)),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(__c.Null)));
 //BA.debugLineNum = 101;BA.debugLine="Top = Top + 30dip";
_top = (int) (_top+__c.DipToCurrent((int) (30)));
 //BA.debugLineNum = 103;BA.debugLine="Dim ED As SeekBar";
_ed = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 104;BA.debugLine="ED.Initialize(\"seekbar\")";
_ed.Initialize(ba,"seekbar");
 //BA.debugLineNum = 105;BA.debugLine="ED.Tag = Key";
_ed.setTag((Object)(_key));
 //BA.debugLineNum = 106;BA.debugLine="ED.Max = MaxValue";
_ed.setMax(_maxvalue);
 //BA.debugLineNum = 107;BA.debugLine="ED.Value = Value";
_ed.setValue(_value);
 //BA.debugLineNum = 108;BA.debugLine="sv.Panel.AddView(ED,4dip,Top,sv.Panel.Width - 8di";
_sv.getPanel().AddView((android.view.View)(_ed.getObject()),__c.DipToCurrent((int) (4)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (8))),__c.DipToCurrent((int) (30)));
 //BA.debugLineNum = 110;BA.debugLine="Top = Top + ED.Height + 4dip";
_top = (int) (_top+_ed.getHeight()+__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 112;BA.debugLine="If manager.GetAll.ContainsKey(Key) Then ED.Value";
if (_manager.GetAll().ContainsKey((Object)(_key))) { 
_ed.setValue((int)(Double.parseDouble(_manager.GetString(_key))));};
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public String  _addspinner(String _key,String _prompt,anywheresoftware.b4a.objects.collections.List _listdata) throws Exception{
anywheresoftware.b4a.objects.SpinnerWrapper _ed = null;
String _su = "";
int _i = 0;
 //BA.debugLineNum = 116;BA.debugLine="Public Sub AddSpinner(Key As String,Prompt As Stri";
 //BA.debugLineNum = 118;BA.debugLine="AddBullet(Top + 10dip,Colors.Red)";
_addbullet((int) (_top+__c.DipToCurrent((int) (10))),__c.Colors.Red);
 //BA.debugLineNum = 119;BA.debugLine="AddLabel(Prompt,12,Top+10dip,sv.Panel.Width - 17d";
_addlabel(_prompt,(int) (12),(int) (_top+__c.DipToCurrent((int) (10))),(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (17))),__c.DipToCurrent((int) (20)),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(__c.Null)));
 //BA.debugLineNum = 120;BA.debugLine="Top = Top + 45dip";
_top = (int) (_top+__c.DipToCurrent((int) (45)));
 //BA.debugLineNum = 122;BA.debugLine="Dim ED As Spinner";
_ed = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 123;BA.debugLine="ED.Initialize(\"spinner\")";
_ed.Initialize(ba,"spinner");
 //BA.debugLineNum = 124;BA.debugLine="ED.Tag = Key";
_ed.setTag((Object)(_key));
 //BA.debugLineNum = 125;BA.debugLine="ED.Prompt = Prompt";
_ed.setPrompt(BA.ObjectToCharSequence(_prompt));
 //BA.debugLineNum = 126;BA.debugLine="ED.TextColor = Colors.Black";
_ed.setTextColor(__c.Colors.Black);
 //BA.debugLineNum = 127;BA.debugLine="ED.TextSize = 13";
_ed.setTextSize((float) (13));
 //BA.debugLineNum = 128;BA.debugLine="ED.DropdownBackgroundColor = Colors.RGB(225,225,2";
_ed.setDropdownBackgroundColor(__c.Colors.RGB((int) (225),(int) (225),(int) (225)));
 //BA.debugLineNum = 129;BA.debugLine="ED.DropdownTextColor = Colors.Black";
_ed.setDropdownTextColor(__c.Colors.Black);
 //BA.debugLineNum = 130;BA.debugLine="ED.Padding = Array As Int(10,10,10,10)";
_ed.setPadding(new int[]{(int) (10),(int) (10),(int) (10),(int) (10)});
 //BA.debugLineNum = 131;BA.debugLine="ED.AddAll(ListData)";
_ed.AddAll(_listdata);
 //BA.debugLineNum = 132;BA.debugLine="sv.Panel.AddView(ED,7dip,Top,sv.Panel.Width - 14d";
_sv.getPanel().AddView((android.view.View)(_ed.getObject()),__c.DipToCurrent((int) (7)),_top,(int) (_sv.getPanel().getWidth()-__c.DipToCurrent((int) (14))),__c.DipToCurrent((int) (50)));
 //BA.debugLineNum = 134;BA.debugLine="Top = Top + ED.Height + 4dip";
_top = (int) (_top+_ed.getHeight()+__c.DipToCurrent((int) (4)));
 //BA.debugLineNum = 136;BA.debugLine="If manager.GetAll.ContainsKey(Key) Then";
if (_manager.GetAll().ContainsKey((Object)(_key))) { 
 //BA.debugLineNum = 137;BA.debugLine="Dim su As String";
_su = "";
 //BA.debugLineNum = 138;BA.debugLine="su = manager.GetString(Key)";
_su = _manager.GetString(_key);
 //BA.debugLineNum = 139;BA.debugLine="For i = 0 To ListData.Size - 1";
{
final int step19 = 1;
final int limit19 = (int) (_listdata.getSize()-1);
for (_i = (int) (0) ; (step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19); _i = ((int)(0 + _i + step19)) ) {
 //BA.debugLineNum = 140;BA.debugLine="If su = ListData.Get(i) Then ED.SelectedIndex =";
if ((_su).equals(BA.ObjectToString(_listdata.Get(_i)))) { 
_ed.setSelectedIndex(_i);};
 }
};
 };
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public String  _applyheightpanel() throws Exception{
 //BA.debugLineNum = 238;BA.debugLine="Sub ApplyHeightPanel";
 //BA.debugLineNum = 239;BA.debugLine="sv.Panel.Height = Top";
_sv.getPanel().setHeight(_top);
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public String  _checkbox_checkedchange(boolean _checked) throws Exception{
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _ch = null;
 //BA.debugLineNum = 256;BA.debugLine="Private Sub checkbox_CheckedChange(Checked As Bool";
 //BA.debugLineNum = 258;BA.debugLine="Dim ch As CheckBox";
_ch = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 259;BA.debugLine="ch = Sender";
_ch.setObject((android.widget.CheckBox)(__c.Sender(ba)));
 //BA.debugLineNum = 260;BA.debugLine="manager.SetBoolean(ch.Tag,Checked)";
_manager.SetBoolean(BA.ObjectToString(_ch.getTag()),_checked);
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Private Sub Class_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private parents As Panel";
_parents = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Private Top As Int";
_top = 0;
 //BA.debugLineNum = 8;BA.debugLine="Private sFontname As String";
_sfontname = "";
 //BA.debugLineNum = 9;BA.debugLine="Private manager As AHPreferenceManager";
_manager = new de.amberhome.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 10;BA.debugLine="Private sv As ScrollView";
_sv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public String  _edittext_textchanged(String _old,String _new) throws Exception{
anywheresoftware.b4a.objects.EditTextWrapper _ed = null;
 //BA.debugLineNum = 264;BA.debugLine="Private Sub edittext_TextChanged (Old As String, N";
 //BA.debugLineNum = 266;BA.debugLine="Dim ed As EditText";
_ed = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 267;BA.debugLine="ed = Sender";
_ed.setObject((android.widget.EditText)(__c.Sender(ba)));
 //BA.debugLineNum = 268;BA.debugLine="manager.SetString(ed.Tag,ed.Text)";
_manager.SetString(BA.ObjectToString(_ed.getTag()),_ed.getText());
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.collections.Map  _getallkey() throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Sub GetAllKey As Map";
 //BA.debugLineNum = 235;BA.debugLine="Return manager.GetAll";
if (true) return _manager.GetAll();
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return null;
}
public boolean  _getkeyboolean(String _key) throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub GetKeyBoolean(Key As String) As Boolean";
 //BA.debugLineNum = 227;BA.debugLine="Return manager.GetBoolean(Key)";
if (true) return _manager.GetBoolean(_key);
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return false;
}
public String  _getkeystring(String _key) throws Exception{
 //BA.debugLineNum = 230;BA.debugLine="Sub GetKeyString(Key As String) As String";
 //BA.debugLineNum = 231;BA.debugLine="Return manager.GetString(Key)";
if (true) return _manager.GetString(_key);
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.PanelWrapper _parent,int _headercolor,String _fontname,String _title,String _subtitle) throws Exception{
innerInitialize(_ba);
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
anywheresoftware.b4a.objects.LabelWrapper _lb2 = null;
 //BA.debugLineNum = 13;BA.debugLine="Public Sub Initialize(Parent As Panel,HeaderColor";
 //BA.debugLineNum = 15;BA.debugLine="parents = Parent";
_parents = _parent;
 //BA.debugLineNum = 16;BA.debugLine="sFontname=Fontname";
_sfontname = _fontname;
 //BA.debugLineNum = 17;BA.debugLine="sv.Initialize(0)";
_sv.Initialize(ba,(int) (0));
 //BA.debugLineNum = 19;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="p.Initialize(\"\")";
_p.Initialize(ba,"");
 //BA.debugLineNum = 21;BA.debugLine="p.Color = HeaderColor";
_p.setColor(_headercolor);
 //BA.debugLineNum = 22;BA.debugLine="Parent.AddView(p,0,0,Parent.Width,100dip)";
_parent.AddView((android.view.View)(_p.getObject()),(int) (0),(int) (0),_parent.getWidth(),__c.DipToCurrent((int) (100)));
 //BA.debugLineNum = 24;BA.debugLine="Dim lb As Label";
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="lb.Initialize(\"\")";
_lb.Initialize(ba,"");
 //BA.debugLineNum = 26;BA.debugLine="lb.Text = Title";
_lb.setText(BA.ObjectToCharSequence(_title));
 //BA.debugLineNum = 27;BA.debugLine="lb.TextColor = Colors.White";
_lb.setTextColor(__c.Colors.White);
 //BA.debugLineNum = 28;BA.debugLine="lb.TextSize = 20";
_lb.setTextSize((float) (20));
 //BA.debugLineNum = 29;BA.debugLine="lb.Typeface = Typeface.LoadFromAssets(Fontname)";
_lb.setTypeface(__c.Typeface.LoadFromAssets(_fontname));
 //BA.debugLineNum = 30;BA.debugLine="lb.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gra";
_lb.setGravity(__c.Bit.Or(__c.Gravity.CENTER_HORIZONTAL,__c.Gravity.CENTER_VERTICAL));
 //BA.debugLineNum = 31;BA.debugLine="parents.AddView(lb,0,15dip,Parent.Width,50dip)";
_parents.AddView((android.view.View)(_lb.getObject()),(int) (0),__c.DipToCurrent((int) (15)),_parent.getWidth(),__c.DipToCurrent((int) (50)));
 //BA.debugLineNum = 33;BA.debugLine="Top = Top + 45dip";
_top = (int) (_top+__c.DipToCurrent((int) (45)));
 //BA.debugLineNum = 35;BA.debugLine="Dim lb2 As Label";
_lb2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="lb2.Initialize(\"\")";
_lb2.Initialize(ba,"");
 //BA.debugLineNum = 37;BA.debugLine="lb2.Text = SubTitle";
_lb2.setText(BA.ObjectToCharSequence(_subtitle));
 //BA.debugLineNum = 38;BA.debugLine="lb2.TextColor = Colors.RGB(218,218,218)";
_lb2.setTextColor(__c.Colors.RGB((int) (218),(int) (218),(int) (218)));
 //BA.debugLineNum = 39;BA.debugLine="lb2.TextSize = 12";
_lb2.setTextSize((float) (12));
 //BA.debugLineNum = 40;BA.debugLine="lb2.Typeface = Typeface.LoadFromAssets(Fontname)";
_lb2.setTypeface(__c.Typeface.LoadFromAssets(_fontname));
 //BA.debugLineNum = 41;BA.debugLine="lb2.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gr";
_lb2.setGravity(__c.Bit.Or(__c.Gravity.CENTER_HORIZONTAL,__c.Gravity.CENTER_VERTICAL));
 //BA.debugLineNum = 42;BA.debugLine="parents.AddView(lb2,0,Top,Parent.Width,50dip)";
_parents.AddView((android.view.View)(_lb2.getObject()),(int) (0),_top,_parent.getWidth(),__c.DipToCurrent((int) (50)));
 //BA.debugLineNum = 44;BA.debugLine="Top = p.Height + 10dip";
_top = (int) (_p.getHeight()+__c.DipToCurrent((int) (10)));
 //BA.debugLineNum = 46;BA.debugLine="Parent.AddView(sv,0,Top,Parent.Width,Parent.Heigh";
_parent.AddView((android.view.View)(_sv.getObject()),(int) (0),_top,_parent.getWidth(),(int) (_parent.getHeight()-_p.getHeight()));
 //BA.debugLineNum = 47;BA.debugLine="sv.Panel.Width = Parent.Width";
_sv.getPanel().setWidth(_parent.getWidth());
 //BA.debugLineNum = 48;BA.debugLine="Top = 0";
_top = (int) (0);
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public String  _lblcheckbox_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lb = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _ch = null;
 //BA.debugLineNum = 242;BA.debugLine="Private Sub lblcheckbox_Click";
 //BA.debugLineNum = 244;BA.debugLine="Dim lb As Label";
_lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 245;BA.debugLine="lb = Sender";
_lb.setObject((android.widget.TextView)(__c.Sender(ba)));
 //BA.debugLineNum = 247;BA.debugLine="Try";
try { //BA.debugLineNum = 248;BA.debugLine="Dim ch As CheckBox";
_ch = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 249;BA.debugLine="ch = lb.Tag";
_ch.setObject((android.widget.CheckBox)(_lb.getTag()));
 //BA.debugLineNum = 250;BA.debugLine="ch.Checked = Not(ch.Checked)";
_ch.setChecked(__c.Not(_ch.getChecked()));
 } 
       catch (Exception e8) {
			ba.setLastException(e8); };
 //BA.debugLineNum = 254;BA.debugLine="End Sub";
return "";
}
public String  _listview_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.ListViewWrapper _ls = null;
 //BA.debugLineNum = 288;BA.debugLine="Private Sub listview_ItemClick (Position As Int, V";
 //BA.debugLineNum = 290;BA.debugLine="Dim ls As ListView";
_ls = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 291;BA.debugLine="ls = Sender";
_ls.setObject((anywheresoftware.b4a.objects.ListViewWrapper.SimpleListView)(__c.Sender(ba)));
 //BA.debugLineNum = 292;BA.debugLine="manager.SetString(ls.Tag,Value)";
_manager.SetString(BA.ObjectToString(_ls.getTag()),BA.ObjectToString(_value));
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
return "";
}
public String  _seekbar_valuechanged(int _value,boolean _userchanged) throws Exception{
anywheresoftware.b4a.objects.SeekBarWrapper _ed = null;
 //BA.debugLineNum = 272;BA.debugLine="Private Sub seekbar_ValueChanged (Value As Int, Us";
 //BA.debugLineNum = 274;BA.debugLine="Dim ed As SeekBar";
_ed = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 275;BA.debugLine="ed = Sender";
_ed.setObject((android.widget.SeekBar)(__c.Sender(ba)));
 //BA.debugLineNum = 276;BA.debugLine="manager.SetString(ed.Tag,ed.Value)";
_manager.SetString(BA.ObjectToString(_ed.getTag()),BA.NumberToString(_ed.getValue()));
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public String  _setkeyboolean(String _key,boolean _value) throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub SetKeyBoolean(Key As String,Value As Boolean)";
 //BA.debugLineNum = 219;BA.debugLine="manager.SetBoolean(Key,Value)";
_manager.SetBoolean(_key,_value);
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return "";
}
public String  _setkeystring(String _key,String _value) throws Exception{
 //BA.debugLineNum = 222;BA.debugLine="Sub SetKeyString(Key As String,Value)";
 //BA.debugLineNum = 223;BA.debugLine="manager.SetString(Key,Value)";
_manager.SetString(_key,_value);
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public String  _spinner_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.SpinnerWrapper _sp = null;
 //BA.debugLineNum = 280;BA.debugLine="Private Sub spinner_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 282;BA.debugLine="Dim sp As Spinner";
_sp = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 283;BA.debugLine="sp = Sender";
_sp.setObject((anywheresoftware.b4a.objects.SpinnerWrapper.B4ASpinner)(__c.Sender(ba)));
 //BA.debugLineNum = 284;BA.debugLine="manager.SetString(sp.Tag,Value)";
_manager.SetString(BA.ObjectToString(_sp.getTag()),BA.ObjectToString(_value));
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
