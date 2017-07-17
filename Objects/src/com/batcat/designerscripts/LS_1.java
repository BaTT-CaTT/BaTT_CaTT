package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 3;BA.debugLine="AutoScaleAll"[1/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 4;BA.debugLine="Panel2.SetLeftAndRight(0dip,100%x)"[1/General script]
views.get("panel2").vw.setLeft((int)((0d * scale)));
views.get("panel2").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
//BA.debugLineNum = 5;BA.debugLine="Panel1.SetLeftAndRight(0dip,100%x)"[1/General script]
views.get("panel1").vw.setLeft((int)((0d * scale)));
views.get("panel1").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
//BA.debugLineNum = 6;BA.debugLine="Panel3.SetLeftAndRight(0dip,100%x)"[1/General script]
views.get("panel3").vw.setLeft((int)((0d * scale)));
views.get("panel3").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
//BA.debugLineNum = 7;BA.debugLine="Panel4.SetLeftAndRight(0dip,100%x)"[1/General script]
views.get("panel4").vw.setLeft((int)((0d * scale)));
views.get("panel4").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
//BA.debugLineNum = 8;BA.debugLine="Panel2.SetTopAndBottom(0dip,100%y)"[1/General script]
views.get("panel2").vw.setTop((int)((0d * scale)));
views.get("panel2").vw.setHeight((int)((100d / 100 * height) - ((0d * scale))));
//BA.debugLineNum = 9;BA.debugLine="apm.SetTopAndBottom(2dip,55%y)"[1/General script]
views.get("apm").vw.setTop((int)((2d * scale)));
views.get("apm").vw.setHeight((int)((55d / 100 * height) - ((2d * scale))));
//BA.debugLineNum = 10;BA.debugLine="apm.SetLeftAndRight(5%x,95%x)"[1/General script]
views.get("apm").vw.setLeft((int)((5d / 100 * width)));
views.get("apm").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
//BA.debugLineNum = 11;BA.debugLine="Label3.SetLeftAndRight(20%x,80%x)"[1/General script]
views.get("label3").vw.setLeft((int)((20d / 100 * width)));
views.get("label3").vw.setWidth((int)((80d / 100 * width) - ((20d / 100 * width))));
//BA.debugLineNum = 12;BA.debugLine="Label3.Bottom=99%y"[1/General script]
views.get("label3").vw.setTop((int)((99d / 100 * height) - (views.get("label3").vw.getHeight())));

}
}