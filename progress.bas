Type=Class
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Class module: ProgressCircle
'Version: 1.0
'Author: Margret
'Last Modified: 10/02/2012
Private Sub Class_Globals
	Private pn As Panel
	Private pn2 As Panel
	Private canvas1 As Canvas
	Private nb As Int
	Private MyAct As Activity
	Private mStroke As Int
	Private Scolor As Int
	Private CS As Int
	Private EmptyPathColor As Int
End Sub
'pMyAct: Pass Activity
'Left: Position of Left side of ProgressCircle
'Top: Position of Top side of ProgressCircle
'CircleSize: ProgressCircle size in pixels
'StrokeWidth: ProgressCircle line width in pixels
'LineColor: Color of the line drawn by ProgressCircle
'Empty_PathColor: Color behind the LineColor above
'BackGround: BackGround image behind or under the ProgressCircle
'NOTE: Your Background image should have the same dimensions in height and width
'and be at least as large as the size of CircleSize. Pass Null if no background image is needed.
Public Sub Initialize(pMyAct As Activity, Left As Int, Top As Int, CircleSize As Int, StrokeWidth As Int, LineColor As Int, Empty_PathColor As Int, BackGroundImage As Bitmap)
	EmptyPathColor = Empty_PathColor
	MyAct = pMyAct
	Scolor = LineColor
	mStroke = StrokeWidth
	CS = CircleSize /2
	pn.Initialize("pn")
	pn.Color = Colors.Transparent
	pn2.Initialize("pn2")
	pn2.Color = Colors.Transparent
	If BackGroundImage.IsInitialized = False Then
		pn2.SetBackgroundImage(Null)
	Else
		pn2.SetBackgroundImage(BackGroundImage)
	End If
	pn2.Visible = False
	If BackGroundImage.IsInitialized = False Then
		MyAct.AddView(pn2, Left, Top, CircleSize, CircleSize)
	Else
		MyAct.AddView(pn2, Left, Top, BackGroundImage.Width, BackGroundImage.Height)
	End If
	pn2.AddView(pn, (pn2.Width-CircleSize)/2, (pn2.Height-CircleSize)/2, CircleSize, CircleSize)
	canvas1.Initialize(pn)
	nb = 0
	ClearProgress
End Sub
Private Sub ProgressDraw(cnvs As Canvas, x As Float, y As Float, radius As Float, startAngle As Float, endAngle As Float, mColor As Int, myBrush As Int)
	Dim S As Float
	S = startAngle
	startAngle = 180 - endAngle
	endAngle = 180 - S
	If startAngle >= endAngle Then endAngle = endAngle + 360
	Dim p As Path
	p.Initialize(x, y)
	For i = startAngle To endAngle Step 10
		p.LineTo(x + 2 * radius * SinD(i), y + 2 * radius * CosD(i))
	Next
	p.LineTo(x + 2 * radius * SinD(endAngle), y + 2 * radius * CosD(endAngle))
	p.LineTo(x, y)
	SetAntiAlias(cnvs)
	cnvs.ClipPath(p)
	cnvs.DrawCircle(x, y,  radius, mColor, False, myBrush)
	cnvs.RemoveClip
	DoEvents
End Sub
'Clear the progress of the ProgressCircle
Sub ClearProgress
	nb = 360
	ProgressDraw(canvas1, (pn.Width/2), (pn.Height/2), (pn.Height/2) - mStroke, 0, nb, EmptyPathColor, mStroke + 2)
	nb = 0
	pn.Invalidate
	DoEvents
End Sub
'Brings object to the front
Sub BringToFront
	pn2.Visible = True
	pn2.BringToFront
	pn.BringToFront
End Sub
'Sends the object to the back
Sub SendToBack
	pn2.SendToBack
	pn2.Visible = False
End Sub
'Pass UpdateProgress as an Int between 0 and 360
'Example: (360/MediaPlayer.Duration) * (MediaPlayer.Position)
Sub SetProgress(UpdateProgress As Int)
	Dim xx As Int : xx = UpdateProgress
	If xx < 1 Then xx = 1
	ProgressDraw(canvas1, (pn.Width/2), (pn.Height/2), (pn.Height/2) - mStroke, 0, xx, Scolor, mStroke)
	pn.Invalidate
	DoEvents
End Sub
Private Sub SetAntiAlias (c As Canvas)
	Dim r As Reflector
	Dim NativeCanvas As Object
	r.Target = c
	NativeCanvas = r.GetField("canvas")
	Dim PaintFlagsDrawFilter As Object
	PaintFlagsDrawFilter = r.CreateObject2("android.graphics.PaintFlagsDrawFilter", Array As Object(0, 1), Array As String("java.lang.int", "java.lang.int"))
	r.Target = NativeCanvas
	r.RunMethod4("setDrawFilter", Array As Object(PaintFlagsDrawFilter), Array As String("android.graphics.DrawFilter"))
End Sub