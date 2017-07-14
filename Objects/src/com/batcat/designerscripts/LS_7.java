package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_7{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("applist").vw.setTop((int)((0d * scale)));
views.get("applist").vw.setHeight((int)((100d / 100 * height) - ((0d * scale))));
views.get("applist").vw.setLeft((int)((0d * scale)));
views.get("applist").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
views.get("panel1").vw.setLeft((int)((5d / 100 * width)));
views.get("panel1").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
views.get("panel1").vw.setTop((int)((5d / 100 * height)));
views.get("panel1").vw.setHeight((int)((70d / 100 * height) - ((5d / 100 * height))));

}
}