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
views.get("panel2").vw.setTop((int)((60d / 100 * height)));
views.get("panel2").vw.setHeight((int)((100d / 100 * height) - ((60d / 100 * height))));
views.get("imageview1").vw.setLeft((int)((35d / 100 * width)));
views.get("imageview1").vw.setWidth((int)((65d / 100 * width) - ((35d / 100 * width))));

}
}