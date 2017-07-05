package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_5{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("panel1").vw.setLeft((int)((0d * scale)));
views.get("panel1").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
views.get("panel1").vw.setTop((int)(0d));
views.get("panel1").vw.setHeight((int)((60d / 100 * height) - (0d)));
//BA.debugLineNum = 6;BA.debugLine="Panel2.SetTopAndBottom(60%y,100%y)"[5/General script]
views.get("panel2").vw.setTop((int)((60d / 100 * height)));
views.get("panel2").vw.setHeight((int)((100d / 100 * height) - ((60d / 100 * height))));
//BA.debugLineNum = 7;BA.debugLine="ImageView1.SetLeftAndRight(30%x,70%x)"[5/General script]
views.get("imageview1").vw.setLeft((int)((30d / 100 * width)));
views.get("imageview1").vw.setWidth((int)((70d / 100 * width) - ((30d / 100 * width))));

}
}