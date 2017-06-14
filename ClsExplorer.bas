Type=Class
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Class module
'Version 2.1
Sub Class_Globals
	Type typResult(Canceled As Boolean, ChosenPath As String, ChosenFile As String)
	Public BorderColor, BackgroundColor As Int
	Public FolderTextColor As Int
	Public FileTextColor1, FileTextColor2 As Int
	Public DividerColor As Int
	Public DialogRect As Rect
	Public FastScrollEnabled As Boolean
	Public Selection As typResult
	Public Ellipsis As Boolean

	Private actEcran As Activity
	Private strChemin As String
	Private lstFiltre As List
	Private bOnlyFolders As Boolean
	Private bVisualiser As Boolean
	Private bMultiFolderSelection As Boolean
	Private bMultiFileSelection As Boolean
	Private strBtnOKTxt As String

	Private pnlMasque As Panel
	Private pnlCadre As Panel
	Private pnlFiles As Panel
	Private svFichiers As ScrollView2D
	Private lstFichiers As ClsCheckList
	Private itemHeight As Int: itemHeight = 55dip
	Private pnlVisu As Panel
	Private lblVisu As Label
	Private ivVisu As ImageView
	Private pnlCartouche As Panel
	Private edtFilename As EditText
	Private btnOK As Button
	Private WaitUntilOK As Boolean

	Private pnlRange As Panel
	Private pnlDisplay As Panel
	Private Anim As Animation
	Private TimeOut As Timer
	Private Duration As Int
	Private MaxPos As Int
	Private bIgnoreEvent As Boolean
	Private bUserMovingPnl As Boolean
	Private bWaitForScroll As Boolean
End Sub

'Initializes the file explorer
'  Activity = current activity
'  Default folder = the first folder to explore
'  Filter = list of allowed extensions (e.g. ".bmp,.gif,.jpg,.png")
'  VisuPnl = if true, displays the visualization panel on selection
'  OnlyFolders = if true, shows only folders (Filter and VisuPnl are ignored)
'  OkText = text of the validation button 
Public Sub Initialize(Activity As Activity, DefaultFolder As String, Filter As String, VisuPnl As Boolean, OnlyFolders As Boolean, OkText As String)
	Dim Ecart As Int: Ecart = 10dip
	actEcran = Activity
	strChemin = DefaultFolder
	lstFiltre.Initialize
	Dim strFiltre As String, PosVirg As Int
	strFiltre = Filter.ToLowerCase
	Do While strFiltre.Contains(",")
		PosVirg = strFiltre.IndexOf(",")
		lstFiltre.Add(strFiltre.SubString2(0, PosVirg).Trim)
		strFiltre = strFiltre.SubString(PosVirg + 1)
	Loop
	lstFiltre.Add(strFiltre.Trim)
	bOnlyFolders = OnlyFolders
	bVisualiser = VisuPnl
	bMultiFolderSelection = False
	bMultiFileSelection = False
	strBtnOKTxt = OkText
	FastScrollEnabled = False
	Ellipsis = True
	BorderColor = Colors.RGB(25, 90, 179)
	BackgroundColor = Colors.RGB(19, 27, 67)
	FolderTextColor = Colors.White
	FileTextColor1 = Colors.RGB(116, 172, 232)
	FileTextColor2 = Colors.Gray
	DividerColor = Colors.DarkGray
	DialogRect.Initialize(Ecart, Ecart, 100%x - Ecart, 100%y - Ecart)
	WaitUntilOK = True
End Sub

Private Sub DisplaySize(SizeOct As Double) As String
	Dim txtUnits(4) As String
	txtUnits = Array As String("bytes", "Kb", "Mb", "Gb")
	Dim Unité As Int
	Unité = 0
	Do While SizeOct > 1024
		Unité = Unité + 1
		SizeOct = SizeOct / 1024
	Loop
	If SizeOct <> Floor(SizeOct) Then
		Return NumberFormat(SizeOct, 1, 1) & " " & txtUnits(Unité)
	Else
		Return SizeOct & " " & txtUnits(Unité)
	End If
End Sub

Private Sub InitializeFileList
	lstFichiers.Initialize(Me, svFichiers, "", "lstFichiers_Click", "lstFichiers_LongClick", 1dip)
	lstFichiers.BackgroundColor = BackgroundColor
	lstFichiers.DividerColor = DividerColor
	svFichiers.VerticalScrollPosition = 0
End Sub

Private Sub AddEntry(ID As Int, Text1 As String, Text2 As String, WithCheckbox As Boolean)
	Dim pnl As Panel: pnl.Initialize("")
	Dim Margin As Int: Margin = 5dip
	Dim PosX As Int: PosX = Margin

	Dim chk As CheckBox
	If WithCheckbox Then
		chk.Initialize("lstMulti")
		pnl.AddView(chk, PosX, 0, 40dip, itemHeight)
		PosX = chk.Width + chk.Left
	End If

	Dim LargeurLabel As Int
	LargeurLabel = svFichiers.Width - PosX - Margin
	Dim lbl1 As Label: lbl1.Initialize("")
	lbl1.Gravity = Gravity.CENTER_VERTICAL
	lbl1.Text = Text1
	lbl1.TextSize = 18
	If Text2 = "" Then
		' Folder
		lbl1.TextColor = FolderTextColor
		lbl1.Typeface = Typeface.DEFAULT_BOLD
		pnl.AddView(lbl1, PosX, 2dip, LargeurLabel, itemHeight - 4dip)
	Else
		' File
		lbl1.TextColor = FileTextColor1
		lbl1.Typeface = Typeface.DEFAULT
		pnl.AddView(lbl1, PosX, 2dip, LargeurLabel, Bit.ShiftRight(itemHeight, 1))

		Dim lbl2 As Label: lbl2.Initialize("")
		lbl2.Gravity = Gravity.TOP
		lbl2.Text = Text2
		lbl2.TextColor = FileTextColor2
		lbl2.TextSize = 14
		lbl2.Typeface = Typeface.DEFAULT
		pnl.AddView(lbl2, PosX, lbl1.Top + lbl1.Height, LargeurLabel, itemHeight - lbl1.Top - lbl1.Height)
	End If

	If Ellipsis Then
		Dim r As Reflector
		r.Target = lbl1
		r.RunMethod2("setLines", 1, "java.lang.int")
		r.RunMethod2("setHorizontallyScrolling", True, "java.lang.boolean") 
		r.RunMethod2("setEllipsize", "MIDDLE", "android.text.TextUtils$TruncateAt")
	End If

	lstFichiers.AddCustomItem(ID, pnl, itemHeight)
End Sub

' Reads the folder contents (files are filtered by extension)
Private Sub ReadFolder(Chemin As String)
	Dim lst, lstD, lstF As List
	Try
		lst = File.ListFiles(Chemin)
	Catch
		lst = Null
	End Try
	If lst.IsInitialized Then
		InitializeFileList
		DoEvents
		Dim lblWait As Label
		lblWait.Initialize("")
		If lst.Size > 30 Then
			' "Please wait" is displayed if the list contains more than 30 files
			lblWait.Gravity = Gravity.CENTER_HORIZONTAL + Gravity.CENTER_VERTICAL
			lblWait.Text = "Please wait..."
			lblWait.TextColor = FileTextColor1
			lblWait.TextSize = 18
			lblWait.Typeface = Typeface.DEFAULT_BOLD
			pnlFiles.AddView(lblWait, 20dip, pnlFiles.Height / 2 - 13dip, pnlFiles.Width - 40dip, 26dip)
			DoEvents
		End If
		lstD.Initialize
		lstF.Initialize
		If Chemin <> "/" Then AddEntry(0, "/ ..", "", False)
		For i = 0 To lst.Size - 1
			If File.IsDirectory(Chemin, lst.Get(i)) Then
				lstD.Add(lst.Get(i))
			Else If Not(bOnlyFolders) Then
				If lstFiltre.Size = 0 Then
					lstF.Add(lst.Get(i))
				Else
					Dim NomFichier As String
					NomFichier = lst.Get(i)
					NomFichier = NomFichier.ToLowerCase
					For f = 0 To lstFiltre.Size - 1
						If NomFichier.EndsWith(lstFiltre.Get(f)) Then
							lstF.Add(lst.Get(i))
							Exit
						End If
					Next
				End If
			End If
		Next
		lstD.SortCaseInsensitive(True)
		For i = 0 To lstD.Size - 1
			AddEntry(lstFichiers.NumberOfItems, "/ " & lstD.Get(i), "", bMultiFolderSelection)
		Next
		lstF.SortCaseInsensitive(True)
		For i = 0 To lstF.Size - 1
			AddEntry(lstFichiers.NumberOfItems, lstF.Get(i), DisplaySize(File.Size(Chemin, lstF.Get(i))), bMultiFileSelection)
		Next
		lstFichiers.ResizePanel
		strChemin = Chemin
		lblWait.RemoveView
	Else
		' No access -> back to the previous folder
		ToastMessageShow("Unable to access folder", False)
		Return
	End If
End Sub

'Opens the file explorer (using custom theme and custom position)
'  
'If your activity contains a TabHost, add these lines before the call to Explorer:
'  <code> Dim r As Reflector
'  r.Target = TabHost1
'  r.RunMethod2("setDescendantFocusability", 0x00060000, "java.lang.int") 'FOCUS_BLOCK_DESCENDANTS
'  </code>
'And these lines after:
'  <code> r.RunMethod2("setDescendantFocusability", 0x00040000, "java.lang.int") 'FOCUS_AFTER_DESCENDANTS
'  TabHost1.RequestFocus
'  </code>
'That will prevent the TabHost from stealing the focus (a known bug, still not fixed in Android 4.0.3).
Public Sub Explorer
	' This sub uses four panels: the first (pnlMasque) is just there to separate the dialog from the underlying
	' objets, the second (pnlCadre) is used as a frame, the third (pnlFiles) is the files display panel and
	' the fourth (pnlCartouche) contains the EditText and OK Button.
	Dim r As Reflector
	Dim MarginBord As Int: MarginBord = 3dip
	Dim Margin As Int: Margin = 8dip
	Dim MarginCartouche As Int: MarginCartouche = 4dip
	Dim HauteurCartouche As Int: HauteurCartouche = 50dip
	Dim LargeurBtn As Int: LargeurBtn = 70dip
	Dim cd_pnlCadre, cd_pnl As ColorDrawable 

	pnlMasque.Initialize("")
	pnlMasque.Color = Colors.Transparent
	r.Target = pnlMasque
	r.SetOnTouchListener("pnl_BlockTouch")

	pnlCadre.Initialize("")
	cd_pnlCadre.Initialize(BorderColor, 12)
	pnlCadre.Background = cd_pnlCadre

	pnlFiles.Initialize("")
	cd_pnl.Initialize(BackgroundColor, 10)
	pnlFiles.Background = cd_pnl
	svFichiers.Initialize(-1, 0, "SVF")
	svFichiers.Color = BackgroundColor
	Dim Largeur, Hauteur As Int
	Largeur = DialogRect.Right - DialogRect.Left
	Hauteur = DialogRect.Bottom - DialogRect.Top
	pnlFiles.AddView(svFichiers, Margin, Margin, Largeur - (2*MarginBord) - (2*Margin), Hauteur - (2*MarginBord) - (2*Margin) - HauteurCartouche)
	r.Target = svFichiers
	r.SetOnKeyListener("dlg_KeyPress")
	r.SetOnFocusListener("dlg_HasFocus")

	pnlCartouche.Initialize("")
	Dim gd_pnlCartouche As GradientDrawable, Clrs(2) As Int
	Clrs(0) = Colors.Black
	Clrs(1) = BackgroundColor
	gd_pnlCartouche.Initialize("TOP_BOTTOM", Clrs)
	gd_pnlCartouche.CornerRadius = 10
	pnlCartouche.Background = gd_pnlCartouche
	edtFilename.Initialize("")
	edtFilename.TextSize = 16
	edtFilename.InputType = Bit.Or(edtFilename.InputType, 0x80000)
	edtFilename.SingleLine = True
	edtFilename.Wrap = False
	r.Target = edtFilename
	r.SetOnKeyListener("dlg_KeyPress")
	r.SetOnFocusListener("dlg_HasFocus")
	pnlCartouche.AddView(edtFilename, MarginCartouche + 1dip, MarginCartouche + 1dip, Largeur - (2*MarginBord) - (3*MarginCartouche) - LargeurBtn, HauteurCartouche - MarginCartouche)
	btnOK.Initialize("btnOK")
	btnOK.Text = strBtnOKTxt
	pnlCartouche.AddView(btnOK, edtFilename.Width + (2*MarginCartouche), MarginCartouche + 1dip, LargeurBtn, HauteurCartouche - MarginCartouche)

	pnlCadre.AddView(pnlFiles, MarginBord, MarginBord, Largeur - (2*MarginBord), Hauteur - HauteurCartouche - (2*MarginBord))
	pnlCadre.AddView(pnlCartouche, MarginBord, Hauteur - HauteurCartouche - MarginBord, Largeur - (2*MarginBord), HauteurCartouche)
	pnlMasque.AddView(pnlCadre, DialogRect.Left, DialogRect.Top, Largeur, Hauteur)
	actEcran.AddView(pnlMasque, 0, 0, 100%x, 100%y)

	If strChemin.EndsWith("/") And strChemin <> "/" Then strChemin = strChemin.SubString2(0, strChemin.Length)
	ReadFolder(strChemin)
	CommonExplorer
End Sub

'Opens the file explorer (using custom theme and custom position). Multi-selection is allowed.
'  
'If your activity contains a TabHost, add these lines before the call to Explorer:
'  <code> Dim r As Reflector
'  r.Target = TabHost1
'  r.RunMethod2("setDescendantFocusability", 0x00060000, "java.lang.int") 'FOCUS_BLOCK_DESCENDANTS
'  </code>
'And these lines after:
'  <code> r.RunMethod2("setDescendantFocusability", 0x00040000, "java.lang.int") 'FOCUS_AFTER_DESCENDANTS
'  TabHost1.RequestFocus
'  </code>
'That will prevent the TabHost from stealing the focus (a known bug, still not fixed in Android 4.0.3).
Public Sub ExplorerMulti
	bMultiFolderSelection = bOnlyFolders
	bMultiFileSelection = Not(bOnlyFolders)
	Explorer
End Sub

'Opens the file explorer (using android dialog theme and fixed position)
'  
'If your activity contains a TabHost, add these lines before the call to Explorer:
'  <code> Dim r As Reflector
'  r.Target = TabHost1
'  r.RunMethod2("setDescendantFocusability", 0x00060000, "java.lang.int") 'FOCUS_BLOCK_DESCENDANTS
'  </code>
'And these lines after:
'  <code> r.RunMethod2("setDescendantFocusability", 0x00040000, "java.lang.int") 'FOCUS_AFTER_DESCENDANTS
'  TabHost1.RequestFocus
'  </code>
'That will prevent the TabHost from stealing the focus (a known bug, still not fixed in Android 4.0.3).
Public Sub Explorer2(DarkTheme As Boolean)
	' This sub uses three panels: the first (pnlMasque) contains the frame layout and separates the dialog
	' from the underlying objets, the second (pnlFiles) is the files display panel and the third (pnlCartouche)
	' contains the EditText and OK Button.
	Dim r As Reflector
	Dim MarginBord As Int: MarginBord = 19dip
	Dim Margin As Int: Margin = 4dip
	Dim MarginCartouche As Int: MarginCartouche = 4dip
	Dim HauteurCartouche As Int: HauteurCartouche = 50dip
	Dim LargeurBtn As Int: LargeurBtn = 70dip

	pnlMasque.Initialize("")
	Dim id As Int
	If DarkTheme Then
		id = r.GetStaticField("android.R$drawable", "alert_dark_frame")
	Else
		id = r.GetStaticField("android.R$drawable", "alert_light_frame")
	End If
	r.Target = r.GetContext
	r.Target = r.RunMethod("getResources")
	pnlMasque.Background = r.RunMethod2("getDrawable", id, "java.lang.int")
	r.Target = pnlMasque
	r.SetOnTouchListener("pnl_BlockTouch")

	BackgroundColor = Colors.Transparent
	pnlFiles.Initialize("")
	pnlFiles.Color = BackgroundColor
	svFichiers.Initialize(-1, 0, "SVF")
	svFichiers.Color = BackgroundColor
	Dim Largeur, Hauteur As Int
	Largeur = 100%x - (2*MarginBord)
	Hauteur = 100%y - (2*MarginBord)
	pnlFiles.AddView(svFichiers, Margin, Margin, Largeur - (2*Margin), Hauteur - (2*Margin) - HauteurCartouche)
	r.Target = svFichiers
	r.SetOnKeyListener("dlg_KeyPress")
	r.SetOnFocusListener("dlg_HasFocus")
	If DarkTheme Then
		FolderTextColor = Colors.White
		FileTextColor1 = Colors.ARGB(220, 255, 255, 255)
		FileTextColor2 = Colors.ARGB(128, 255, 255, 255)
		DividerColor = Colors.DarkGray
	Else
		FolderTextColor = Colors.Black
		FileTextColor1 = Colors.ARGB(200, 0, 0, 0)
		FileTextColor2 = Colors.ARGB(128, 0, 0, 0)
		DividerColor = Colors.LightGray
	End If

	pnlCartouche.Initialize("")
	pnlCartouche.Color = Colors.Transparent
	edtFilename.Initialize("")
	edtFilename.TextSize = 16
	edtFilename.InputType = Bit.Or(edtFilename.InputType, 0x80000)
	edtFilename.SingleLine = True
	edtFilename.Wrap = False
	r.Target = edtFilename
	r.SetOnKeyListener("dlg_KeyPress")
	r.SetOnFocusListener("dlg_HasFocus")
	pnlCartouche.AddView(edtFilename, MarginCartouche + 1dip, MarginCartouche, Largeur - (3*MarginCartouche) - LargeurBtn, HauteurCartouche - MarginCartouche)
	btnOK.Initialize("btnOK")
	btnOK.Text = strBtnOKTxt
	pnlCartouche.AddView(btnOK, edtFilename.Width + (2*MarginCartouche), MarginCartouche, LargeurBtn, HauteurCartouche - MarginCartouche)

	pnlMasque.AddView(pnlFiles, MarginBord, MarginBord - Margin, Largeur, Hauteur - HauteurCartouche)
	pnlMasque.AddView(pnlCartouche, MarginBord, Hauteur - HauteurCartouche + pnlFiles.Top, Largeur, HauteurCartouche)
	actEcran.AddView(pnlMasque, 0, 0, 100%x, 100%y)

	If strChemin.EndsWith("/") And strChemin <> "/" Then strChemin = strChemin.SubString2(0, strChemin.Length)
	ReadFolder(strChemin)
	CommonExplorer
End Sub

'Opens the file explorer (using android dialog theme and fixed position). Multi-selection is allowed.
'
'If your activity contains a TabHost, add these lines before the call to Explorer:
'  <code> Dim r As Reflector
'  r.Target = TabHost1
'  r.RunMethod2("setDescendantFocusability", 0x00060000, "java.lang.int") 'FOCUS_BLOCK_DESCENDANTS
'  </code>
'And these lines after:
'  <code> r.RunMethod2("setDescendantFocusability", 0x00040000, "java.lang.int") 'FOCUS_AFTER_DESCENDANTS
'  TabHost1.RequestFocus
'  </code>
'That will prevent the TabHost from stealing the focus (a known bug, still not fixed in Android 4.0.3).
Public Sub ExplorerMulti2(DarkTheme As Boolean)
	bMultiFolderSelection = bOnlyFolders
	bMultiFileSelection = Not(bOnlyFolders)
	Explorer2(DarkTheme)
End Sub

Private Sub CommonExplorer
	If FastScrollEnabled Then InitializeScrollPanel

	Selection.Canceled = True
	Selection.ChosenPath = ""
	Selection.ChosenFile = ""
	edtFilename.RequestFocus

	Do While WaitUntilOK
		' Main loop - we wait until the OK btn is pressed or the back key is used
		DoEvents
	Loop

	pnlMasque.RemoveView
	pnlMasque = Null
End Sub

' Prevents underlying objects from catching touch events
Private Sub pnl_BlockTouch(ViewTag As Object, Action As Int, X As Float, Y As Float, MotionEvent As Object) As Boolean
	Return True
End Sub

' Prevents underlying objects from gaining focus
Private Sub dlg_HasFocus(ViewTag As Object, HasFocus As Boolean)
 	If Not(HasFocus) Then edtFilename.RequestFocus
End Sub

' Intercepts the keys pressed
Private Sub dlg_KeyPress(ViewTag As Object, KeyCode As Int, KeyEvent As Object) As Boolean
	Dim r As Reflector
	r.Target = KeyEvent
	Dim KeyAction As Int = r.RunMethod("getAction")
	Select Case KeyCode
		Case KeyCodes.KEYCODE_BACK
			If KeyAction = 1 Then 'ACTION_UP
				If pnlVisu.IsInitialized Then
					pnlVisu_Close(Null)
				Else
					WaitUntilOK = False
				End If
			End If
			Return True
		Case KeyCodes.KEYCODE_MENU
			Return True
		Case KeyCodes.KEYCODE_SEARCH
			Return True
	End Select
	Return False
End Sub

'Is the file explorer currently on screen ?
Public Sub IsActive As Boolean
	Return pnlMasque.IsInitialized
End Sub

#Region Visualization
Private Sub pnlVisu_Close(ViewTag As Object)
	svFichiers.Visible = True
	pnlVisu.RemoveView
	pnlVisu = Null
End Sub

Private Sub IsImage(NomFichier As String) As Boolean
	Dim Minus As String
	Minus = NomFichier.ToLowerCase
	Return (Minus.EndsWith(".bmp") OR Minus.EndsWith(".gif") OR Minus.EndsWith(".jpg") OR Minus.EndsWith(".png"))
End Sub

' Resize a picture
Private Sub CreateScaledBitmap(Original As Bitmap, Width As Int, Height As Int) As Bitmap
	Dim r As Reflector
	Dim b As Bitmap
	b = r.RunStaticMethod("android.graphics.Bitmap", "createScaledBitmap", _
			Array As Object(Original, Width, Height, True), _
			Array As String("android.graphics.Bitmap", "java.lang.int", "java.lang.int", "java.lang.boolean"))
	Return b
End Sub

' Display the selected picture
Private Sub AfficherImage(Image As String)
	Dim Marge As Int: Marge = 2dip
	pnlVisu.Initialize("")
	pnlVisu.Color = Colors.Transparent
	ivVisu.Initialize("")
	pnlVisu.AddView(ivVisu, 0, 0, pnlFiles.Width - (2*Marge), pnlFiles.Height - (2*Marge))
	lblVisu.Initialize("")
	lblVisu.Text = "Please wait..."
	lblVisu.TextColor = FileTextColor1
	lblVisu.TextSize = 18
	lblVisu.Typeface = Typeface.DEFAULT_BOLD
	pnlVisu.AddView(lblVisu, 10dip, 10dip, pnlFiles.Width - (2*Marge) - 20dip, 30dip)
	pnlFiles.AddView(pnlVisu, Marge, Marge, pnlFiles.Width - (2*Marge), pnlFiles.Height - (2*Marge))
	svFichiers.Visible = False
	DoEvents: DoEvents
	Try
		Dim bmp As Bitmap
		bmp.InitializeSample(strChemin, Image, pnlVisu.Width, pnlVisu.Height)
		If bmp.Height <= pnlVisu.Height AND bmp.Width <= pnlVisu.Width Then
			' The picture is smaller than the ImgView -> we just center it
			ivVisu.Gravity = Gravity.CENTER
		Else
			Dim RatioBmp, RatioImg As Float
			RatioBmp = bmp.Width / bmp.Height
			RatioImg = pnlVisu.Width / pnlVisu.Height
			If NumberFormat(RatioBmp, 1, 2) = NumberFormat(RatioImg, 1, 2) Then
				' Same aspect ratio -> the picture can fill the ImgView
				ivVisu.Gravity = Gravity.FILL
			Else
				' Different aspect ratio -> the picture is resized to fit the ImgView
				Dim Diviseur As Float
				If RatioImg > RatioBmp Then
					Diviseur = bmp.Height / pnlVisu.Height
					bmp = CreateScaledBitmap(bmp, Round(bmp.Width / Diviseur / Density), _
															Round(pnlVisu.Height / Density))
				Else
					Diviseur = bmp.Width / pnlVisu.Width
					bmp = CreateScaledBitmap(bmp, Round(pnlVisu.Width / Density), _
															Round(bmp.Height / Diviseur / Density))
				End If
				ivVisu.Gravity = Gravity.NO_GRAVITY
			End If
		End If
		ivVisu.Bitmap = bmp
		lblVisu.Text = ""
		Dim r As Reflector
		r.Target = pnlVisu
		r.SetOnClickListener("pnlVisu_Close") 'We cannot use here the usual B4A click listener
	Catch
		Msgbox(LastException.Message, "Oooops")
		pnlVisu_Close(Null)
	End Try
End Sub

Private Sub IsText(NomFichier As String) As Boolean
	Dim Minus As String
	Minus = NomFichier.ToLowerCase
	Return (Minus.EndsWith(".css") OR Minus.EndsWith(".htm") OR Minus.EndsWith(".html") _
		  OR Minus.EndsWith(".txt") OR Minus.EndsWith(".xml"))
End Sub

' Display the selected text file
Private Sub AfficherTexte(Texte As String)
	Dim Marge As Int: Marge = 2dip
	pnlVisu.Initialize("")
	lblVisu.Initialize("")
	pnlVisu.AddView(lblVisu, 10dip, 10dip, pnlFiles.Width - (2*Marge) - 20dip, pnlFiles.Height - (2*Marge) - 20dip)
	pnlFiles.AddView(pnlVisu, Marge, Marge, pnlFiles.Width - (2*Marge), pnlFiles.Height - (2*Marge))
	pnlVisu.Color = Colors.Transparent
	svFichiers.Visible = False
	lblVisu.TextColor = FileTextColor1
	lblVisu.TextSize = 16
	lblVisu.Typeface = Typeface.DEFAULT
	Try
		Dim Contenu As StringBuilder: Contenu.Initialize
		Dim Reader As TextReader, Ligne As String, Cpt As Int
		Reader.Initialize(File.OpenInput(strChemin, Texte))
		Ligne = Reader.ReadLine
		Do While Ligne <> Null
			Cpt = Cpt + 1
			If Cpt > 50 Then
				Contenu.Append("--- Lines after 50 are skipped ---")
				Exit
			End If
			Contenu.Append(Ligne).Append(CRLF)
			Ligne = Reader.ReadLine
		Loop
		Reader.Close
		lblVisu.Text = Contenu
		Dim r As Reflector
		r.Target = pnlVisu
		r.SetOnClickListener("pnlVisu_Close") 'We cannot use here the usual B4A click listener
	Catch
		Msgbox(LastException.Message, "Oooops")
		Reader.Close
		pnlVisu_Close(Null)
	End Try
End Sub
#End Region

Private Sub lstFichiers_Click(Item As Panel, ItemTag As Object)
	Dim lbl As Label
	If Item.GetView(0) Is CheckBox Then
		lbl = Item.GetView(1)
	Else
		lbl = Item.GetView(0)
	End If
	If lbl.Text = "/ .." Then
		' Open the parent folder
		Dim PosSlash As Int, ParentPath As String
		PosSlash = strChemin.LastIndexOf("/")
		ParentPath = strChemin.SubString2(0, PosSlash)
		If ParentPath = "" Then ParentPath = "/"
		ReadFolder(ParentPath)
		If bOnlyFolders Then
			edtFilename.Text = ParentPath
			edtFilename.RequestFocus
		Else
			edtFilename.Text = ""
		End If
	Else If lbl.Text.StartsWith("/ ") Then
		' Open the selected folder
		Dim NewPath As String
		If strChemin = "/" Then
			NewPath = strChemin & lbl.Text.SubString(2)
		Else
			NewPath = strChemin & "/" & lbl.Text.SubString(2)
		End If
		ReadFolder(NewPath)
		If bOnlyFolders Then
			edtFilename.Text = NewPath
			edtFilename.SelectionStart = edtFilename.Text.Length
			edtFilename.RequestFocus
		Else
			edtFilename.Text = ""
		End If
	Else
		' Preview the selected file
		If bVisualiser Then
			If IsImage(lbl.Text) Then
				AfficherImage(lbl.Text)
			Else If IsText(lbl.Text) Then
				AfficherTexte(lbl.Text)
			End If
		End If
		If bMultiFileSelection Then
			Dim cbx As CheckBox
			cbx = Item.GetView(0)
			cbx.Checked = True
		Else
			edtFilename.Text = lbl.Text
		End If
		edtFilename.SelectionStart = edtFilename.Text.Length
		edtFilename.RequestFocus
	End If
End Sub

Private Sub lstFichiers_LongClick(Item As Panel, ItemTag As Object)
	Dim lbl As Label
	If Item.GetView(0) Is CheckBox Then
		lbl = Item.GetView(1)
	Else
		lbl = Item.GetView(0)
	End If
	ToastMessageShow(lbl.Text, False)
End Sub

Private Sub lstMulti_CheckedChange(Checked As Boolean)
	Dim L As List = lstFichiers.CheckedPanels
	Dim pnl As Panel, lbl As Label
	edtFilename.Text = ""
	For i = 0 To L.Size - 1
		If edtFilename.Text <> "" Then edtFilename.Text = edtFilename.Text & ";"
		pnl = L.Get(i)
		lbl = pnl.GetView(1)
		If lbl.Text.StartsWith("/ ") Then
			If strChemin = "/" Then
				edtFilename.Text = edtFilename.Text & strChemin & lbl.Text.SubString(2)
			Else
				edtFilename.Text = edtFilename.Text & strChemin & "/" & lbl.Text.SubString(2)
			End If
		Else
			edtFilename.Text = edtFilename.Text & lbl.Text
		End If
	Next
	edtFilename.SelectionStart = edtFilename.Text.Length
End Sub

Private Sub btnOK_Click
	Selection.Canceled = False
	If bOnlyFolders Then
		If edtFilename.Text = "" Then
			Selection.ChosenPath = strChemin
		Else
			Selection.ChosenPath = edtFilename.Text
		End If
		Selection.ChosenFile = ""
	Else
		Selection.ChosenPath = strChemin
		Selection.ChosenFile = edtFilename.Text
	End If
	WaitUntilOK = False
End Sub

#Region ScrollPanel
'Initializes the ScrollPanel.
Private Sub InitializeScrollPanel
	Dim spWidth As Int = 64dip
	Dim spHeight As Int = 52dip

	pnlRange.Initialize("")
	pnlFiles.AddView(pnlRange, svFichiers.Left + svFichiers.Width - spWidth, svFichiers.Top, spWidth, svFichiers.Height)
	pnlRange.Visible = False
	Dim r As Reflector
	r.Target = pnlRange
	r.SetOnTouchListener("SP_Touch")

	pnlDisplay.Initialize("")
	pnlRange.AddView(pnlDisplay, 0, 0, spWidth, spHeight)
	pnlDisplay.Background = LoadDrawable("scrollbar_handle_accelerated_anim2")

	r.Target = r.RunStaticMethod("android.view.ViewConfiguration", "get", Array As Object(r.GetContext), Array As String("android.content.Context"))
	Duration = r.RunMethod("getScrollDefaultDelay") + Bit.ShiftRight(r.RunMethod("getScrollBarFadeDuration"), 1)
	TimeOut.Initialize("TimeOut", 0)
	Anim.InitializeTranslate("Anim", 0, 0, spWidth, 0)
	Anim.Duration = Duration
	Anim.RepeatCount = 0

	MaxPos = pnlRange.Height - pnlDisplay.Height
	bUserMovingPnl = False 'Becomes True when the user moves the ScrollPanel
	bWaitForScroll = True
	pnlDisplay.Top = CalcNewTop
End Sub

'Gets a drawable from the Android system resources
Private Sub LoadDrawable(Name As String) As Object
	Dim r As Reflector
	r.Target = r.GetContext
	r.Target = r.RunMethod("getResources")
	r.Target = r.RunMethod("getSystem")
	Dim ID_Drawable As Int
	ID_Drawable = r.RunMethod4("getIdentifier", Array As Object(Name, "drawable", "android"), _
	                                            Array As String("java.lang.String", "java.lang.String", "java.lang.String"))
	r.Target = r.GetContext
	r.Target = r.RunMethod("getResources")
	Return r.RunMethod2("getDrawable", ID_Drawable, "java.lang.int")
End Sub

'Returns the new panel top position
Private Sub CalcNewTop As Int
	Return (svFichiers.VerticalScrollPosition / (svFichiers.Panel.Height - svFichiers.Height) * MaxPos)
End Sub

Private Sub TimeOut_Tick
	TimeOut.Enabled = False
	Anim.Start(pnlDisplay)
End Sub

Private Sub Anim_AnimationEnd
	pnlRange.Visible = False
End Sub

Private Sub SVF_ScrollChanged(PosX As Int, PosY As Int)
	If Not(FastScrollEnabled) Then Return

	If bWaitForScroll Then
		' We don't display the panel until the ScrollView starts scrolling
		If pnlDisplay.Top = CalcNewTop Then
			Return
		Else
			bWaitForScroll = False
		End If
	End If

	If svFichiers.Panel.Height > svFichiers.Height Then
		' The panel is displayed only if the ScrollView doesn't show all items (a good reason to scroll)
		pnlRange.Visible = True
	End If

	If Not(bUserMovingPnl) Then
		TimeOut.Enabled = False
		Anim.Stop(pnlDisplay)
 		pnlDisplay.Top = CalcNewTop
		If PosY = svFichiers.VerticalScrollPosition Then
			' The scrolling is over but we wait a little bit before hiding the panel
			TimeOut.Interval = Duration
			TimeOut.Enabled = True
			bWaitForScroll = True
		End If
	End If
End Sub

Private Sub SP_Touch(ViewTag As Object, Action As Int, X As Float, Y As Float, MotionEvent As Object) As Boolean
	If Action = 0 Then
		If Y < pnlDisplay.Top OR Y > pnlDisplay.Top + pnlDisplay.Height Then
			' Le doigt n'est pas sur le ScrollPanel -> l'événement est ignoré
			bIgnoreEvent = True
		Else
			bIgnoreEvent = False
		End If
	End If
	If bIgnoreEvent Then Return False

	Select Case Action
		Case 0, 2:	' ACTION_DOWN or ACTION_MOVE
			bUserMovingPnl = True
			TimeOut.Enabled = False
			Anim.Stop(pnlDisplay)
			pnlDisplay.Top = Min(Max(0, Y * (1 - (pnlDisplay.Height / pnlRange.Height))), MaxPos)
			svFichiers.VerticalScrollPosition = pnlDisplay.Top / MaxPos * (svFichiers.Panel.Height - svFichiers.Height)
		Case Else:	' ACTION_UP
			bUserMovingPnl = False
			TimeOut.Interval = Duration
			TimeOut.Enabled = True
	End Select
	Return True
End Sub
#End Region
