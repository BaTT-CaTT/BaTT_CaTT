package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 4;BA.debugLine="Panel2.SetLeftAndRight(0dip,100%x)"[1/General script]
views.get("panel2").vw.setLeft((int)((0d * scale)));
views.get("panel2").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
//BA.debugLineNum = 5;BA.debugLine="Panel2.SetTopAndBottom(0dip,100%y)"[1/General script]
views.get("panel2").vw.setTop((int)((0d * scale)));
views.get("panel2").vw.setHeight((int)((100d / 100 * height) - ((0d * scale))));
//BA.debugLineNum = 7;BA.debugLine="Button4.SetLeftAndRight(40%x,60%x)"[1/General script]
views.get("button4").vw.setLeft((int)((40d / 100 * width)));
views.get("button4").vw.setWidth((int)((60d / 100 * width) - ((40d / 100 * width))));
//BA.debugLineNum = 8;BA.debugLine="AutoScaleAll"[1/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);

}
}