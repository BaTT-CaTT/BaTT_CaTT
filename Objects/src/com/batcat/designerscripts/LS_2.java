package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_2{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("panel1").vw.setLeft((int)((0d * scale)));
views.get("panel1").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
views.get("panel2").vw.setTop((int)((0d * scale)));
//BA.debugLineNum = 5;BA.debugLine="ListView1.SetLeftAndRight(1dip,100%x)"[2/General script]
views.get("listview1").vw.setLeft((int)((1d * scale)));
views.get("listview1").vw.setWidth((int)((100d / 100 * width) - ((1d * scale))));

}
}