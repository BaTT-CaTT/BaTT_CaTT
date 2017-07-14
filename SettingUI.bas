Type=Class
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'BaTT CaTT source Project 
'Copyrights D.Trojan(trOw) and SM/Media ©2017
'Code Module created by trOw
'STATUS EXPERIMENTAL build 275
Private Sub Class_Globals	
	Private parents As Panel
	Private Top As Int
	Private sFontname As String
	Private manager As AHPreferenceManager
	Private sv As ScrollView
End Sub
' Shared Code Modules created by d.Trojan
Public Sub Initialize(Parent As Panel,HeaderColor As Int,Fontname As String,Title As String,SubTitle As String)
	
	parents = Parent
	sFontname=Fontname
	sv.Initialize(0)
	
	Dim p As Panel
	p.Initialize("")
	p.Color = HeaderColor
	Parent.AddView(p,0,0,Parent.Width,100dip)
	
	Dim lb As Label
	lb.Initialize("")
	lb.Text = Title
	lb.TextColor = Colors.White
	lb.TextSize = 20
	lb.Typeface = Typeface.LoadFromAssets(Fontname)
	lb.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL)
	parents.AddView(lb,0,15dip,Parent.Width,50dip)
	
	Top = Top + 45dip
	
	Dim lb2 As Label
	lb2.Initialize("")
	lb2.Text = SubTitle
	lb2.TextColor = Colors.RGB(218,218,218)
	lb2.TextSize = 12
	lb2.Typeface = Typeface.LoadFromAssets(Fontname)
	lb2.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL)
	parents.AddView(lb2,0,Top,Parent.Width,50dip)
	
	Top = p.Height + 10dip
	
	Parent.AddView(sv,0,Top,Parent.Width,Parent.Height - p.Height)
	sv.Panel.Width = Parent.Width
	Top = 0
	
End Sub

Public Sub AddCheckbox(Key As String,Title As String,DefaultValue As Boolean)
	
	Dim chk As CheckBox
	chk.Initialize("checkbox")
	chk.Tag = Key
	chk.Checked = DefaultValue
	sv.Panel.AddView(chk,sv.Panel.Width - 37dip,Top,30dip,30dip)
	
	AddLabel(Title,11,Top+5dip,100%x-chk.Width - 7dip,chk.Height,chk)
	Top = Top + 40dip
	
	If manager.GetAll.ContainsKey(Key) Then chk.Checked = manager.GetBoolean(Key)
	
End Sub

Public Sub AddEditText(Key As String,Hint As String,InputType As Int,SingleLine As Boolean,Password As Boolean)
	
	Dim ed As EditText
	ed.Initialize("edittext")
	ed.Tag = Key
	ed.Hint = Hint
	ed.TextColor = Colors.Black
	ed.HintColor = Colors.Gray
	ed.TextSize = 12
	ed.PasswordMode = Password
	ed.Typeface = Typeface.LoadFromAssets(sFontname)
	ed.InputType = InputType
	ed.SingleLine = SingleLine
	ed.Padding = Array As Int(10,10,10,10)
	
	If SingleLine = False Then
		sv.Panel.AddView(ed,7dip,Top,sv.Panel.Width - 14dip,100dip)
		ed.Gravity = Bit.Or(Gravity.TOP,Gravity.LEFT)
	Else
		sv.Panel.AddView(ed,7dip,Top,sv.Panel.Width - 14dip,50dip)
		ed.Gravity = Gravity.left
	End If

	Top = Top + ed.Height + 4dip
	
	If manager.GetAll.ContainsKey(Key) Then ed.Text = manager.GetString(Key)
	
End Sub

Public Sub AddSeekbar(Key As String,Title As String,MaxValue As Int,Value As Int)
	
	AddBullet(Top + 10dip,Colors.Red)
	AddLabel(Title,13,Top+10dip,sv.Panel.Width - 17dip,20dip,Null)
	
	Top = Top + 30dip
	
	Dim ED As SeekBar
	ED.Initialize("seekbar")
	ED.Tag = Key
	ED.Max = MaxValue
	ED.Value = Value
	sv.Panel.AddView(ED,4dip,Top,sv.Panel.Width - 8dip,30dip)

	Top = Top + ED.Height + 4dip
		
	If manager.GetAll.ContainsKey(Key) Then ED.Value = manager.GetString(Key)
	
End Sub

Public Sub AddSpinner(Key As String,Prompt As String,ListData As List)
	
	AddBullet(Top + 10dip,Colors.Red)
	AddLabel(Prompt,12,Top+10dip,sv.Panel.Width - 17dip,20dip,Null)
	Top = Top + 45dip
	
	Dim ED As Spinner
	ED.Initialize("spinner")
	ED.Tag = Key
	ED.Prompt = Prompt
	ED.TextColor = Colors.Black
	ED.TextSize = 13
	ED.DropdownBackgroundColor = Colors.RGB(225,225,225)
	ED.DropdownTextColor = Colors.Black
	ED.Padding = Array As Int(10,10,10,10)
	ED.AddAll(ListData)
	sv.Panel.AddView(ED,7dip,Top,sv.Panel.Width - 14dip,50dip)

	Top = Top + ED.Height + 4dip
	
	If manager.GetAll.ContainsKey(Key) Then
		Dim su As String
		su = manager.GetString(Key)
		For i = 0 To ListData.Size - 1
			If su = ListData.Get(i) Then ED.SelectedIndex = i
		Next
	End If
	
End Sub

Public Sub AddListview(Key As String,Title As String,ListData As List)
	
	AddBullet(Top + 10dip,Colors.Red)
	AddLabel(Title,12,Top+10dip,sv.Panel.Width - 17dip,20dip,Null)
	
	Top = Top + 45dip
	
	Dim ED As ListView
	ED.Initialize("listview")
	ED.Tag = Key
	ED.FastScrollEnabled = True
	ED.SingleLineLayout.Label.Gravity = Gravity.left
	ED.SingleLineLayout.Label.TextSize = 13
	ED.SingleLineLayout.Label.TextColor = Colors.Black
	ED.SingleLineLayout.Label.Gravity = Bit.Or(Gravity.CENTER_VERTICAL,Gravity.left)
	ED.SingleLineLayout.Label.Left = 0
	ED.SingleLineLayout.Label.Width = sv.Panel.Width-20dip
	
	Dim lblheight As Int
	lblheight = ED.SingleLineLayout.ItemHeight * ListData.Size
	sv.Panel.AddView(ED,7dip,Top,sv.Panel.Width - 14dip,lblheight)
	
	For i = 0 To ListData.Size - 1
		ED.AddSingleLine(ListData.Get(i))	
	Next
	
	Top = Top + ED.Height + 4dip
	
End Sub

Private Sub AddLabel(Title As String,FontSize As Int,sTop As Int,sWidth As Int,sHeight As Int,DepencyView As View)
	
	Dim lb As Label
	lb.Initialize("lblcheckbox")
	lb.TextColor = Colors.Black
	lb.TextSize = FontSize
	lb.Text = Title
	Try
		lb.Tag = DepencyView
	Catch
		
	End Try
	
	lb.Typeface = Typeface.LoadFromAssets(sFontname)
	lb.Gravity = Bit.Or(Gravity.CENTER_HORIZONTAL,Gravity.left)
	sv.Panel.AddView(lb,0,sTop,sWidth-3dip,sHeight)
	
End Sub

Sub AddDivider
	
	Top = Top + 4dip
	Dim lb As Label
	lb.Initialize("")
	lb.Color = Colors.ARGB(20,95,95,95)
	sv.Panel.AddView(lb,7dip,Top,sv.Panel.Width - 17dip,1)
	
	Top = Top + 10dip
	
End Sub

Sub AddBullet(sTop As Int,Color As Int)
	
	Dim lb As Label
	lb.Initialize("")
	sv.Panel.AddView(lb,sv.Panel.Width-15dip,sTop+8dip,7dip,7dip)
	Dim cd As ColorDrawable
	cd.Initialize(Color,lb.Width/2)
	lb.Background = cd
	
End Sub

Sub SetKeyBoolean(Key As String,Value As Boolean)
	manager.SetBoolean(Key,Value)
End Sub

Sub SetKeyString(Key As String,Value)
	manager.SetString(Key,Value)
End Sub

Sub GetKeyBoolean(Key As String) As Boolean
	Return manager.GetBoolean(Key)
End Sub

Sub GetKeyString(Key As String) As String
	Return manager.GetString(Key)
End Sub

Sub GetAllKey As Map
	Return manager.GetAll
End Sub

Sub ApplyHeightPanel
	sv.Panel.Height = Top	
End Sub

Private Sub lblcheckbox_Click
	
	Dim lb As Label
	lb = Sender
	
	Try
		Dim ch As CheckBox
		ch = lb.Tag
		ch.Checked = Not(ch.Checked)
	Catch
	End Try
	
End Sub

Private Sub checkbox_CheckedChange(Checked As Boolean)
	
	Dim ch As CheckBox
	ch = Sender
	manager.SetBoolean(ch.Tag,Checked)
	
End Sub

Private Sub edittext_TextChanged (Old As String, New As String)
	
	Dim ed As EditText
	ed = Sender
	manager.SetString(ed.Tag,ed.Text)
	
End Sub

Private Sub seekbar_ValueChanged (Value As Int, UserChanged As Boolean)
	
	Dim ed As SeekBar
	ed = Sender
	manager.SetString(ed.Tag,ed.Value)
	
End Sub

Private Sub spinner_ItemClick (Position As Int, Value As Object)
	
	Dim sp As Spinner
	sp = Sender
	manager.SetString(sp.Tag,Value)
	
End Sub

Private Sub listview_ItemClick (Position As Int, Value As Object)
	
	Dim ls As ListView
	ls = Sender
	manager.SetString(ls.Tag,Value)
	
End Sub