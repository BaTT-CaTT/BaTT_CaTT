package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_8{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("credits").vw.setLeft((int)((0d * scale)));
views.get("credits").vw.setWidth((int)((100d / 100 * width) - ((0d * scale))));
views.get("credits").vw.setTop((int)((0d * scale)));
views.get("credits").vw.setHeight((int)((100d / 100 * height) - ((0d * scale))));

}
}