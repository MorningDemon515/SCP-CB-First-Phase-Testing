Global BurntNote%

Const MaxItemAmount% = 10
Global ItemAmount%
Dim Inventory.Items(MaxItemAmount + 1)
Global InvSelect%, SelectedItem.Items

Global ClosestItem.Items

Global LastItemID%

Type ItemTemplates
	Field name$
	Field tempname$
	
	Field sound%
	
	Field found%
	
	Field obj%, objpath$, parentobjpath$
	Field invimg%,invimg2%,invimgpath$
	Field imgpath$, img%
	
	Field isAnim%
	
	Field scale#
	;Field bumptex%
	Field tex%, texpath$
End Type 

Function CreateItemTemplate.ItemTemplates(name$, tempname$, objpath$, invimgpath$, imgpath$, scale#, texturepath$ = "",invimgpath2$="",Anim%=0, texflags%=9)
	Local it.ItemTemplates = New ItemTemplates, n
	
	
	;if another item shares the same object, copy it
	For it2.itemtemplates = Each ItemTemplates
		If it2\objpath = objpath And it2\obj <> 0 Then it\obj = CopyEntity(it2\obj) : it\parentobjpath=it2\objpath : Exit
	Next
	
	If it\obj = 0 Then; it\obj = LoadMesh(objpath)
		If Anim<>0 Then
			it\obj = LoadAnimMesh_Strict(objpath)
			it\isAnim=True
		Else
			it\obj = LoadMesh_Strict(objpath)
			it\isAnim=False
		EndIf
		it\objpath = objpath
	EndIf
	it\objpath = objpath
	
	Local texture%
	
	If texturepath <> "" Then
		For it2.itemtemplates = Each ItemTemplates
			If it2\texpath = texturepath And it2\tex<>0 Then
				texture = it2\tex
				Exit
			EndIf
		Next
		If texture=0 Then texture=LoadTexture_Strict(texturepath,texflags%) : it\texpath = texturepath; : Debug��־texturepath
		EntityTexture it\obj, texture
		it\tex = texture
	EndIf  
	
	it\scale = scale
	ScaleEntity it\obj, scale, scale, scale, True
	
	;if another item shares the same object, copy it
	For it2.itemtemplates = Each ItemTemplates
		If it2\invimgpath = invimgpath And it2\invimg <> 0 Then
			it\invimg = it2\invimg ;CopyImage()
			If it2\invimg2<>0 Then
				it\invimg2=it2\invimg2 ;CopyImage()
			EndIf
			Exit
		EndIf
	Next
	If it\invimg=0 Then
		it\invimg = LoadImage_Strict(invimgpath)
		it\invimgpath = invimgpath
		MaskImage(it\invimg, 255, 0, 255)
	EndIf
	
	If (invimgpath2 <> "") Then
		If it\invimg2=0 Then
			it\invimg2 = LoadImage_Strict(invimgpath2)
			MaskImage(it\invimg2,255,0,255)
		EndIf
	Else
		it\invimg2 = 0
	EndIf
	
	it\imgpath = imgpath
	
	;If imgpath<>"" Then
	;	it\img=LoadImage(imgpath)
	;	
	;	;DebugLog imgpath
	;	
	;	If it\img<>0 Then ResizeImage(it\img, ImageWidth(it\img) * MenuScale, ImageHeight(it\img) * MenuScale)
	;EndIf
	
	it\tempname = tempname
	it\name = name
	
	it\sound = 1

	HideEntity it\obj
	
	Return it
	
End Function

Function InitItemTemplates()
	Local it.ItemTemplates,it2.ItemTemplates
	
	it = CreateItemTemplate("һЩSCP-420-J", "420", "GFX\items\420.x", "GFX\items\INV420.jpg", "", 0.0005)
	it\sound = 2
	
	CreateItemTemplate("һ��Կ�׿�", "key1",  "GFX\items\keycard.x", "GFX\items\INVkey1.jpg", "", 0.0004,"GFX\items\keycard1.jpg")
	CreateItemTemplate("����Կ�׿�", "key2",  "GFX\items\keycard.x", "GFX\items\INVkey2.jpg", "", 0.0004,"GFX\items\keycard2.jpg")
	CreateItemTemplate("����Կ�׿�", "key3",  "GFX\items\keycard.x", "GFX\items\INVkey3.jpg", "", 0.0004,"GFX\items\keycard3.jpg")
	CreateItemTemplate("�ļ�Կ�׿�", "key4",  "GFX\items\keycard.x", "GFX\items\INVkey4.jpg", "", 0.0004,"GFX\items\keycard4.jpg")
	CreateItemTemplate("�弶Կ�׿�", "key5", "GFX\items\keycard.x", "GFX\items\INVkey5.jpg", "", 0.0004,"GFX\items\keycard5.jpg")
	CreateItemTemplate("�˿���", "misc", "GFX\items\keycard.x", "GFX\items\INVcard.jpg", "", 0.0004,"GFX\items\card.jpg")
	CreateItemTemplate("���´����ÿ�", "misc", "GFX\items\keycard.x", "GFX\items\INVmastercard.jpg", "", 0.0004,"GFX\items\mastercard.jpg")
	CreateItemTemplate("����Կ�׿�", "key6", "GFX\items\keycard.x", "GFX\items\INVkeyomni.jpg", "", 0.0004,"GFX\items\keycardomni.jpg")
	
	it = CreateItemTemplate("SCP-860", "scp860", "GFX\items\key.b3d", "GFX\items\INVkey.jpg", "", 0.001)
	it\sound = 3
	
	it = CreateItemTemplate("SCP-079�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc079.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-895�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc895.jpg", 0.003) : it\sound = 0 
	it = CreateItemTemplate("SCP-860�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc860.jpg", 0.003) : it\sound = 0 	
	it = CreateItemTemplate("SCP-860-1�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc8601.jpg", 0.003) : it\sound = 0 	
	it = CreateItemTemplate("SCP-093��������", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc093rm.jpg", 0.003) : it\sound = 0 	
	it = CreateItemTemplate("SCP-106�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc106.jpg", 0.003) : it\sound = 0	
	it = CreateItemTemplate("Allok��ʿ�ı���", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc106_2.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("�ٻ�Э��RP-106-N", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docRP.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("SCP-682�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc682.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-173�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc173.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-372�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc372.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-049�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc049.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-096�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc096.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-008�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc008.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-012�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc012.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-500�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc500.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-714�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc714.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-513�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc513.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-035�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc035.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-035����", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc035ad.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-939�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc939.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-966�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc966.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-970�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc970.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-1048�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1048.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-1123�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1123.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-1162�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1162.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("SCP-1499�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1499.png", 0.003) : it\sound = 0
	it = CreateItemTemplate("�¹ʱ���SCP-1048-A", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1048a.jpg", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("�滭", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1048.jpg", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("����", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\leaflet.jpg", 0.003, "GFX\items\notetexture.jpg") : it\sound = 0
	
	it = CreateItemTemplate("L��ʿ�ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docL1.jpg", 0.0025, "GFX\items\notetexture.jpg") : it\sound = 0
	it = CreateItemTemplate("L��ʿ �ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docL2.jpg", 0.0025, "GFX\items\notetexture.jpg") : it\sound = 0
	it = CreateItemTemplate("մѪ�ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docL3.jpg", 0.0025, "GFX\items\notetexture.jpg") : it\sound = 0
	it = CreateItemTemplate("�ս���L��ʿ�ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVbn.jpg", "GFX\items\docL4.jpg", 0.0025, "GFX\items\BurntNoteTexture.jpg") : it\sound = 0
	it = CreateItemTemplate("�ս���L��ʿ �ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVbn.jpg", "GFX\items\docL5.jpg", 0.0025, "GFX\items\BurntNoteTexture.jpg") : it\sound = 0
	it = CreateItemTemplate("�ս��ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVbn.jpg", "GFX\items\docL6.jpg", 0.0025, "GFX\items\BurntNoteTexture.jpg") : it\sound = 0
	
	it = CreateItemTemplate("һҳ�ռ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docGonzales.jpg", 0.0025) : it\sound = 0
	
	
	it = CreateItemTemplate("��־#1", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\f4.jpg", 0.004, "GFX\items\f4.jpg") : it\sound = 0
	it = CreateItemTemplate("��־#2", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\f5.jpg", 0.004, "GFX\items\f4.jpg") : it\sound = 0
	it = CreateItemTemplate("��־#3", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\f6.jpg", 0.004, "GFX\items\f4.jpg") : it\sound = 0
	
	it = CreateItemTemplate("��ֵıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docStrange.jpg", 0.0025, "GFX\items\notetexture.jpg") : it\sound = 0
	
	it = CreateItemTemplate("���豸�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docNDP.jpg", 0.003) : it\sound = 0	
	it = CreateItemTemplate("D����Ա��ѵ����", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docORI.jpg", 0.003) : it\sound = 0	
	
	it = CreateItemTemplate("Daniel�ı���", "paper", "GFX\items\note.x", "GFX\items\INVnote2.jpg", "GFX\items\docdan.jpg", 0.0025) : it\sound = 0
	
	it = CreateItemTemplate("�ս����ļ�", "paper", "GFX\items\paper.x", "GFX\items\INVbn.jpg", "GFX\items\bn.jpg", 0.003, "GFX\items\BurntNoteTexture.jpg")
	it\img = BurntNote : it\sound = 0
	
	it = CreateItemTemplate("�������ıʼ�", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\sn.jpg", 0.003, "GFX\items\notetexture.jpg") : it\sound = 0	
	
	it = CreateItemTemplate("������ǲ���ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docMTF.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("�����ȼ��ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docSC.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("��Ŀ�ȼ��ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docOBJC.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docRAND3.jpg", 0.003) : it\sound = 0 
	it = CreateItemTemplate("������ 5/14������־", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docRAND2.jpg", 0.003, "GFX\items\notetexture.jpg") : it\sound = 0 
	it = CreateItemTemplate("֪ͨ", "paper", "GFX\items\paper.x", "GFX\items\INVnote.jpg", "GFX\items\docRAND1.jpg", 0.003, "GFX\items\notetexture.jpg") :it\sound = 0 	
	it = CreateItemTemplate("�¹ʱ���SCP-106-0204", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docIR106.jpg", 0.003) : it\sound = 0 
	
	it = CreateItemTemplate("��������", "vest", "GFX\items\vest.x", "GFX\items\INVvest.jpg", "", 0.02,"GFX\items\Vest.png") : it\sound = 2
	it = CreateItemTemplate("���ͷ�������", "finevest", "GFX\items\vest.x", "GFX\items\INVvest.jpg", "", 0.022,"GFX\items\Vest.png")
	it\sound = 2
	it = CreateItemTemplate("���صķ�������", "veryfinevest", "GFX\items\vest.x", "GFX\items\INVvest.jpg", "", 0.025,"GFX\items\Vest.png")
	it\sound = 2
	
	it = CreateItemTemplate("������", "hazmatsuit", "GFX\items\hazmat.b3d", "GFX\items\INVhazmat.jpg", "", 0.013)
	it\sound = 2
	it = CreateItemTemplate("������", "hazmatsuit2", "GFX\items\hazmat.b3d", "GFX\items\INVhazmat.jpg", "", 0.013)
	it\sound = 2
	it = CreateItemTemplate("���ͷ�����", "hazmatsuit3", "GFX\items\hazmat.b3d", "GFX\items\INVhazmat.jpg", "", 0.013)
	it\sound = 2
	
	it = CreateItemTemplate("����", "����", "GFX\items\cup.x", "GFX\items\INVcup.jpg", "", 0.04) : it\sound = 2 ;���ֺ�IDһ�����ҷֲ�����ĸ��������ĸ���ID
	
	it = CreateItemTemplate("�ձ���", "emptycup", "GFX\items\cup.x", "GFX\items\INVcup.jpg", "", 0.04) : it\sound = 2	
	
	it = CreateItemTemplate("SCP-500-01", "scp500", "GFX\items\pill.b3d", "GFX\items\INVpill.jpg", "", 0.0001) : it\sound = 2
	EntityColor it\obj,255,0,0
	
	it = CreateItemTemplate("������", "firstaid", "GFX\items\firstaid.x", "GFX\items\INVfirstaid.jpg", "", 0.05)
	it = CreateItemTemplate("С�ͼ�����", "finefirstaid", "GFX\items\firstaid.x", "GFX\items\INVfirstaid.jpg", "", 0.03)
	it = CreateItemTemplate("��ɫ������", "firstaid2", "GFX\items\firstaid.x", "GFX\items\INVfirstaid2.jpg", "", 0.03, "GFX\items\firstaidkit2.jpg")
	it = CreateItemTemplate("��ֵ�ƿ��", "veryfinefirstaid", "GFX\items\eyedrops.b3d", "GFX\items\INVbottle.jpg", "", 0.002, "GFX\items\bottle.jpg")	
	
	it = CreateItemTemplate("�������", "gasmask", "GFX\items\gasmask.b3d", "GFX\items\INVgasmask.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("�������", "supergasmask", "GFX\items\gasmask.b3d", "GFX\items\INVgasmask.jpg", "", 0.021) : it\sound = 2
	it = CreateItemTemplate("���ͷ������", "gasmask3", "GFX\items\gasmask.b3d", "GFX\items\INVgasmask.jpg", "", 0.021) : it\sound = 2
	
	it = CreateItemTemplate("ǧֽ��", "misc", "GFX\items\origami.b3d", "GFX\items\INVorigami.jpg", "", 0.003) : it\sound = 0
	
	CreateItemTemplate("����Ԫ��", "misc", "GFX\items\electronics.x", "GFX\items\INVelectronics.jpg", "", 0.0011)
	
	it = CreateItemTemplate("������", "scp148", "GFX\items\metalpanel.x", "GFX\items\INVmetalpanel.jpg", "", RoomScale) : it\sound = 2
	it = CreateItemTemplate("�����ڶϺϽ�", "scp148ingot", "GFX\items\scp148.x", "GFX\items\INVscp148.jpg", "", RoomScale) : it\sound = 2
	
	CreateItemTemplate("S-NAV 300������", "nav", "GFX\items\navigator.x", "GFX\items\INVnavigator.jpg", "GFX\items\navigator.png", 0.0008)
	CreateItemTemplate("S-NAV Navigator", "nav", "GFX\items\navigator.x", "GFX\items\INVnavigator.jpg", "GFX\items\navigator.png", 0.0008)
	CreateItemTemplate("S-NAV�ռ�������", "nav", "GFX\items\navigator.x", "GFX\items\INVnavigator.jpg", "GFX\items\navigator.png", 0.0008)
	CreateItemTemplate("S-NAV 310������", "nav", "GFX\items\navigator.x", "GFX\items\INVnavigator.jpg", "GFX\items\navigator.png", 0.0008)
	
	CreateItemTemplate("�Խ���", "radio", "GFX\items\radio.x", "GFX\items\INVradio.jpg", "GFX\items\radioHUD.png", 1.0);0.0010)
	CreateItemTemplate("�Խ���", "fineradio", "GFX\items\radio.x", "GFX\items\INVradio.jpg", "GFX\items\radioHUD.png", 1.0)
	CreateItemTemplate("�Խ���", "veryfineradio", "GFX\items\radio.x", "GFX\items\INVradio.jpg", "GFX\items\radioHUD.png", 1.0)
	CreateItemTemplate("�Խ���", "18vradio", "GFX\items\radio.x", "GFX\items\INVradio.jpg", "GFX\items\radioHUD.png", 1.02)
	
	it = CreateItemTemplate("����", "cigarette", "GFX\items\420.x", "GFX\items\INV420.jpg", "", 0.0004) : it\sound = 2
	
	it = CreateItemTemplate("�̾�", "420s", "GFX\items\420.x", "GFX\items\INV420.jpg", "", 0.0004) : it\sound = 2
	
	it = CreateItemTemplate("���ŵ��̾�", "420s", "GFX\items\420.x", "GFX\items\INV420.jpg", "", 0.0004) : it\sound = 2
	
	it = CreateItemTemplate("����", "hand", "GFX\items\severedhand.b3d", "GFX\items\INVhand.jpg", "", 0.04) : it\sound = 2
	it = CreateItemTemplate("��ɫ����", "hand2", "GFX\items\severedhand.b3d", "GFX\items\INVhand2.jpg", "", 0.04, "GFX\items\shand2.png") : it\sound = 2
	
	CreateItemTemplate("9V���", "bat", "GFX\items\Battery\Battery.x", "GFX\items\Battery\INVbattery9v.jpg", "", 0.008)
	CreateItemTemplate("18V���", "18vbat", "GFX\items\Battery\Battery.x", "GFX\items\Battery\INVbattery18v.jpg", "", 0.01, "GFX\items\Battery\Battery 18V.jpg")
	CreateItemTemplate("��ֵĵ��", "killbat", "GFX\items\Battery\Battery.x", "GFX\items\Battery\INVbattery22900.jpg", "", 0.01,"GFX\items\Battery\Strange Battery.jpg")
	
	CreateItemTemplate("��ҩˮ", "fineeyedrops", "GFX\items\eyedrops.b3d", "GFX\items\INVeyedrops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	CreateItemTemplate("��ҩˮ", "supereyedrops", "GFX\items\eyedrops.b3d", "GFX\items\INVeyedrops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	CreateItemTemplate("ReVision��ҩˮ", "eyedrops","GFX\items\eyedrops.b3d", "GFX\items\INVeyedrops.jpg", "", 0.0012, "GFX\items\eyedrops.jpg")
	CreateItemTemplate("RedVision��ҩˮ", "eyedrops", "GFX\items\eyedrops.b3d", "GFX\items\INVeyedropsred.jpg", "", 0.0012,"GFX\items\eyedropsred.jpg")
	
	it = CreateItemTemplate("SCP-714", "scp714", "GFX\items\scp714.b3d", "GFX\items\INV714.jpg", "", 0.3)
	it\sound = 3
	
	it = CreateItemTemplate("SCP-1025", "scp1025", "GFX\items\scp1025.b3d", "GFX\items\INV1025.jpg", "", 0.1)
	it\sound = 0
	
	it = CreateItemTemplate("SCP-513", "scp513", "GFX\items\513.x", "GFX\items\INV513.jpg", "", 0.1)
	it\sound = 2
	
	;BoH items
	
	it = CreateItemTemplate("�ļ���", "clipboard", "GFX\items\clipboard.b3d", "GFX\items\INVclipboard.jpg", "", 0.003, "", "GFX\items\INVclipboard2.jpg", 1)
	
	it = CreateItemTemplate("SCP-1123", "1123", "GFX\items\HGIB_Skull1.b3d", "GFX\items\inv1123.jpg", "", 0.015) : it\sound = 2
	
	;it = CreateItemTemplate("SCP-1074�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc1074.jpg", 0.003) : it\sound = 0
	;it = CreateItemTemplate("SCP-1074 Containment Notice", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc_arce.jpg", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("ҹ����", "supernv", "GFX\items\NVG.b3d", "GFX\items\INVsupernightvision.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("ҹ����", "nvgoggles", "GFX\items\NVG.b3d", "GFX\items\INVnightvision.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("ҹ����", "finenvgoggles", "GFX\items\NVG.b3d", "GFX\items\INVveryfinenightvision.jpg", "", 0.02) : it\sound = 2
	
	it = CreateItemTemplate("��Ͳ", "syringe", "GFX\items\Syringe\syringe.b3d", "GFX\items\Syringe\inv.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate("��Ͳ", "finesyringe", "GFX\items\Syringe\syringe.b3d", "GFX\items\Syringe\inv.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate("��Ͳ", "veryfinesyringe", "GFX\items\Syringe\syringe.b3d", "GFX\items\Syringe\inv.png", "", 0.005) : it\sound = 2
	
	;.........
	
	;SCP:CB 1.3������Ʒ ����������ENDSHN
	it = CreateItemTemplate("SCP-1499","scp1499","GFX\items\SCP-1499.b3d","GFX\items\INVscp1499.jpg", "", 0.023) : it\sound = 2
	it = CreateItemTemplate("SCP-1499","super1499","GFX\items\SCP-1499.b3d","GFX\items\INVscp1499.jpg", "", 0.023) : it\sound = 2
	CreateItemTemplate("��������˹�����֤", "badge", "GFX\items\badge.x", "GFX\items\INVbadge.jpg", "GFX\items\badge1.jpg", 0.0001, "GFX\items\badge1_tex.jpg")
	it = CreateItemTemplate("��ʧ��Կ��", "key", "GFX\items\key.b3d", "GFX\items\INV1162_1.jpg", "", 0.001, "GFX\items\key2.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("������֤���¼DH-S-4137-17092", "oldpaper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\dh.jpg", 0.003) : it\sound = 0
	it = CreateItemTemplate("Ӳ��", "coin", "GFX\items\key.b3d", "GFX\items\INVcoin.jpg", "", 0.0005, "GFX\items\coin.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("��ӰƱ", "ticket", "GFX\items\key.b3d", "GFX\items\INVticket.jpg", "GFX\items\ticket.png", 0.002, "GFX\items\tickettexture.png","",0,1+2+8) : it\sound = 0
	CreateItemTemplate("�ƾɵ����֤", "badge", "GFX\items\badge.x", "GFX\items\INVoldbadge.jpg", "GFX\items\badge2.png", 0.0001, "GFX\items\badge2_tex.png","",0,1+2+8)
	
	it = CreateItemTemplate("Ӳ��","25ct", "GFX\items\key.b3d", "GFX\items\INVcoin.jpg", "", 0.0005, "GFX\items\coin.png","",0,1+2+8) : it\sound = 3
	it = CreateItemTemplate("Ǯ��","wallet", "GFX\items\wallet.b3d", "GFX\items\INVwallet.jpg", "", 0.0005,"","",1) : it\sound = 2
	
	CreateItemTemplate("SCP-427","scp427","GFX\items\427.b3d","GFX\items\INVscp427.jpg", "", 0.001)
	it = CreateItemTemplate("����ҩ��", "scp500death", "GFX\items\pill.b3d", "GFX\items\INVpill.jpg", "", 0.0001) : it\sound = 2
	EntityColor it\obj,255,0,0
	it = CreateItemTemplate("ҩ��", "pill", "GFX\items\pill.b3d", "GFX\items\INVpillwhite.jpg", "", 0.0001) : it\sound = 2
	EntityColor it\obj,255,255,255
	
	it = CreateItemTemplate("������", "paper", "GFX\items\note.x", "GFX\items\INVnote2.jpg", "GFX\items\note682.jpg", 0.0025) : it\sound = 0
	it = CreateItemTemplate("ģ�黯��ʩ��Ŀ�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docMSP.jpg", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("�о�����-02��Ʒ���", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\docmap.jpg", 0.003) : it\sound = 0
	
	it = CreateItemTemplate("SCP-427�ĵ�", "paper", "GFX\items\paper.x", "GFX\items\INVpaper.jpg", "GFX\items\doc427.jpg", 0.003) : it\sound = 0
	
	For it = Each ItemTemplates
		If (it\tex<>0) Then
			If (it\texpath<>"") Then
				For it2=Each ItemTemplates
					If (it2<>it) And (it2\tex=it\tex) Then
						it2\tex = 0
					EndIf
				Next
			EndIf
			FreeTexture it\tex : it\tex = 0
		EndIf
	Next
	
End Function 

Type Items
	Field name$
	Field collider%,model%
	Field itemtemplate.ItemTemplates
	Field DropSpeed#
	
	Field r%,g%,b%,a#
	
	Field level
	
	Field SoundChn%
	
	Field dist#, disttimer#
	
	Field state#, state2#
	
	Field Picked%,Dropped%
	
	Field invimg%
	Field WontColl% = False
	Field xspeed#,zspeed#
	Field SecondInv.Items[20]
	Field ID%
	Field invSlots%
End Type 

Function CreateItem.Items(name$, tempname$, x#, y#, z#, r%=0,g%=0,b%=0,a#=1.0,invSlots%=0)
	CatchErrors("Uncaught (CreateItem)")
	
	Local i.Items = New Items
	Local it.ItemTemplates
	
	name = Lower(name)
	tempname = Lower (tempname)
	
	For it.ItemTemplates = Each ItemTemplates
		If Lower(it\name) = name Then
			If Lower(it\tempname) = tempname Then
				i\itemtemplate = it
				i\collider = CreatePivot()			
				EntityRadius i\collider, 0.01
				EntityPickMode i\collider, 1, False
				i\model = CopyEntity(it\obj,i\collider)
				i\name = it\name
				ShowEntity i\collider
				ShowEntity i\model
			EndIf
		EndIf
	Next 
	
	i\WontColl = False
	
	If i\itemtemplate = Null Then DebugLog("��Ʒģ��δ�ҵ�("+name+", "+tempname+", "+imgpath+")") ;����ʾ���ƺ�ID��Ϊ��ʾ���ơ�ID����ͼ·��
	
	ResetEntity i\collider		
	PositionEntity(i\collider, x, y, z, True)
	RotateEntity (i\collider, 0, Rand(360), 0)
	i\dist = EntityDistance(Collider, i\collider)
	i\DropSpeed = 0.0
	
	If tempname = "����" Then
		i\r=r
		i\g=g
		i\b=b
		i\a=a
		
		Local liquid = CopyEntity(LiquidObj)
		ScaleEntity liquid, i\itemtemplate\scale,i\itemtemplate\scale,i\itemtemplate\scale,True
		PositionEntity liquid, EntityX(i\collider,True),EntityY(i\collider,True),EntityZ(i\collider,True)
		EntityParent liquid, i\model
		EntityColor liquid, r,g,b
		
		If a < 0 Then 
			EntityFX liquid, 1
			EntityAlpha liquid, Abs(a)
		Else
			EntityAlpha liquid, Abs(a)
		EndIf
		
		
		EntityShininess liquid, 1.0
	EndIf
	
	i\invimg = i\itemtemplate\invimg
	If (tempname="clipboard") And (invSlots=0) Then
		invSlots = 10
		SetAnimTime i\model,17.0
		i\invimg = i\itemtemplate\invimg2
	ElseIf (tempname="wallet") And (invSlots=0) Then
		invSlots = 10
		SetAnimTime i\model,0.0
	EndIf
	
	i\invSlots=invSlots
	
	i\ID=LastItemID+1
	LastItemID=i\ID
	
	CatchErrors("CreateItem")
	Return i
End Function

Function RemoveItem(i.Items)
	CatchErrors("Uncaught (RemoveItem)")
	Local n
	FreeEntity(i\model) : FreeEntity(i\collider) : i\collider = 0
	
	For n% = 0 To MaxItemAmount - 1
		If Inventory(n) = i
			DebugLog "Removed "+i\itemtemplate\name+" from slot "+n
			Inventory(n) = Null
			ItemAmount = ItemAmount-1
			Exit
		EndIf
	Next
	If SelectedItem = i Then
		Select SelectedItem\itemtemplate\tempname 
			Case "nvgoggles", "supernv"
				WearingNightVision = False
			Case "gasmask", "supergasmask", "gasmask2", "gasmask3"
				WearingGasMask = False
			Case "vest", "finevest", "veryfinevest"
				WearingVest = False
			Case "hazmatsuit","hazmatsuit2","hazmatsuit3"
				WearingHazmat = False	
			Case "scp714"
				Wearing714 = False
			Case "scp1499","super1499"
				Wearing1499 = False
			Case "scp427"
				I_427\Using = False
		End Select
		
		SelectedItem = Null
	EndIf
	If i\itemtemplate\img <> 0
		FreeImage i\itemtemplate\img
		i\itemtemplate\img = 0
	EndIf
	Delete i
	
	CatchErrors("RemoveItem")
End Function

Function UpdateItems()
	CatchErrors("Uncaught (UpdateItems)")
	Local n, i.Items, i2.Items
	Local xtemp#, ytemp#, ztemp#
	Local temp%, np.NPCs
	Local pick%
	
	Local HideDist = HideDistance*0.5
	Local deletedItem% = False
	
	ClosestItem = Null
	For i.Items = Each Items
		i\Dropped = 0
		
		If (Not i\Picked) Then
			If i\disttimer < MilliSecs2() Then
				i\dist = EntityDistance(Camera, i\collider)
				i\disttimer = MilliSecs2() + 700
				If i\dist < HideDist Then ShowEntity i\collider
			EndIf
			
			If i\dist < HideDist Then
				ShowEntity i\collider
				
				If i\dist < 1.2 Then
					If ClosestItem = Null Then
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					ElseIf ClosestItem = i Or i\dist < EntityDistance(Camera, ClosestItem\collider) Then 
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					EndIf
				EndIf
				
				If EntityCollided(i\collider, HIT_MAP) Then
					i\DropSpeed = 0
					i\xspeed = 0.0
					i\zspeed = 0.0
				Else
					If ShouldEntitiesFall
						pick = LinePick(EntityX(i\collider),EntityY(i\collider),EntityZ(i\collider),0,-10,0)
						If pick
							i\DropSpeed = i\DropSpeed - 0.0004 * FPSfactor
							TranslateEntity i\collider, i\xspeed*FPSfactor, i\DropSpeed * FPSfactor, i\zspeed*FPSfactor
							If i\WontColl Then ResetEntity(i\collider)
						Else
							i\DropSpeed = 0
							i\xspeed = 0.0
							i\zspeed = 0.0
						EndIf
					Else
						i\DropSpeed = 0
						i\xspeed = 0.0
						i\zspeed = 0.0
					EndIf
				EndIf
				
				If i\dist<HideDist*0.2 Then
					For i2.Items = Each Items
						If i<>i2 And (Not i2\Picked) And i2\dist<HideDist*0.2 Then
							
							xtemp# = (EntityX(i2\collider,True)-EntityX(i\collider,True))
							ytemp# = (EntityY(i2\collider,True)-EntityY(i\collider,True))
							ztemp# = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))
							
							ed# = (xtemp*xtemp+ztemp*ztemp)
							If ed<0.07 And Abs(ytemp)<0.25 Then
								;items are too close together, push away
								If PlayerRoom\RoomTemplate\Name	<> "room2storage" Then
									xtemp = xtemp*(0.07-ed)
									ztemp = ztemp*(0.07-ed)
									
									While Abs(xtemp)+Abs(ztemp)<0.001
										xtemp = xtemp+Rnd(-0.002,0.002)
										ztemp = ztemp+Rnd(-0.002,0.002)
									Wend
									
									TranslateEntity i2\collider,xtemp,0,ztemp
									TranslateEntity i\collider,-xtemp,0,-ztemp
								EndIf
							EndIf
						EndIf
					Next
				EndIf
				
				If EntityY(i\collider) < - 35.0 Then DebugLog"remove: " + i\itemtemplate\name:RemoveItem(i):deletedItem=True
			Else
				HideEntity i\collider
			EndIf
		Else
			i\DropSpeed = 0
			i\xspeed = 0.0
			i\zspeed = 0.0
		EndIf
		
		If Not deletedItem Then
			CatchErrors(Chr(34)+i\itemtemplate\name+Chr(34)+" item")
		EndIf
		deletedItem = False
	Next
	
	If ClosestItem <> Null Then
		;DrawHandIcon = True
		
		If MouseHit1 Then PickItem(ClosestItem)
	EndIf
	
End Function

Function PickItem(item.Items)
	Local n% = 0
	Local canpickitem = True
	Local fullINV% = True
	
	For n% = 0 To MaxItemAmount - 1
		If Inventory(n)=Null
			fullINV = False
			Exit
		EndIf
	Next
	
	If WearingHazmat > 0 Then
		Msg = "�㲻���ڴ��ŷ�����ʱ������Ʒ"
		MsgTimer = 70*5
		Return
	EndIf
	
	CatchErrors("Uncaught (PickItem)")
	If (Not fullINV) Then
		For n% = 0 To MaxItemAmount - 1
			If Inventory(n) = Null Then
				Select item\itemtemplate\tempname
					Case "1123"
						If Not (Wearing714 = 1) Then
							If PlayerRoom\RoomTemplate\Name <> "room1123" Then
								ShowEntity Light
								LightFlash = 7
								PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Touch.ogg"))		
								DeathMSG = "����D-9341����ͼϮ��һ����β����Ա�󱻻��С�"
								DeathMSG = DeathMSG + "���¼����ʾ�������ڴ�Լ9����ǰ���ֳ��ǻ��������ĸߺ��������ĺ�����"
								DeathMSG = DeathMSG + "SCP-1123��[�ѱ༭]�ĸ����ҵ���֤����������������֫��Ӵ���"
								DeathMSG = DeathMSG + "Ŀǰ��δ֪SCP-1123����δ��������뿪�ġ�"
								Kill()
							EndIf
							For e.Events = Each Events
								If e\eventname = "room1123" Then 
									If e\eventstate = 0 Then
										ShowEntity Light
										LightFlash = 3
										PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Touch.ogg"))
									EndIf
									e\eventstate = Max(1, e\eventstate)
									
									Exit
								EndIf
							Next
						EndIf
						
						Return
					Case "killbat"
						ShowEntity Light
						LightFlash = 1.0
						PlaySound_Strict(IntroSFX(11))
						DeathMSG = "����D-9341��ʬ����SCP-914����������ҵ����Ա��ƺ���һ����ͨ�ľŷ���ء�"
						DeathMSG = DeathMSG + "�����������ˣ�����Ϊ�ǵ������Ĵ��������������ѱ����������Թ���һ���о���"
						Kill()
					Case "scp148"
						GiveAchievement(Achv148)	
					Case "scp513"
						GiveAchievement(Achv513)
					Case "scp860"
						GiveAchievement(Achv860)
					Case "key6"
						GiveAchievement(AchvOmni)
					Case "veryfinevest"
						Msg = "������̫����"
						MsgTimer = 70*6
						Exit
					Case "firstaid", "finefirstaid", "veryfinefirstaid", "firstaid2"
						item\state = 0
					Case "navigator", "nav"
						If item\itemtemplate\name = "S-NAV�ռ�������" Then GiveAchievement(AchvSNAV)
					Case "hazmatsuit", "hazmatsuit2", "hazmatsuit3"
						canpickitem = True
						For z% = 0 To MaxItemAmount - 1
							If Inventory(z) <> Null Then
								If Inventory(z)\itemtemplate\tempname="hazmatsuit" Or Inventory(z)\itemtemplate\tempname="hazmatsuit2" Or Inventory(z)\itemtemplate\tempname="hazmatsuit3" Then
									canpickitem% = False
									Exit
								ElseIf Inventory(z)\itemtemplate\tempname="vest" Or Inventory(z)\itemtemplate\tempname="finevest" Then
									canpickitem% = 2
									Exit
								EndIf
							EndIf
						Next
						
						If canpickitem=False Then
							Msg = "�㲻�ܴ�������������"
							MsgTimer = 70 * 5
							Return
						ElseIf canpickitem=2 Then
							Msg = "�㲻���ڴ��ŷ�����ʱ���Ϸ�����"
							MsgTimer = 70 * 5
							Return
						Else
							;TakeOffStuff(1+16)
							SelectedItem = item
						EndIf
					Case "vest","finevest"
						canpickitem = True
						For z% = 0 To MaxItemAmount - 1
							If Inventory(z) <> Null Then
								If Inventory(z)\itemtemplate\tempname="vest" Or Inventory(z)\itemtemplate\tempname="finevest" Then
									canpickitem% = False
									Exit
								ElseIf Inventory(z)\itemtemplate\tempname="hazmatsuit" Or Inventory(z)\itemtemplate\tempname="hazmatsuit2" Or Inventory(z)\itemtemplate\tempname="hazmatsuit3" Then
									canpickitem% = 2
									Exit
								EndIf
							EndIf
						Next
						
						If canpickitem=False Then
							Msg = "�㲻�ܴ�������������"
							MsgTimer = 70 * 5
							Return
						ElseIf canpickitem=2 Then
							Msg = "�㲻���ڴ��ŷ�����ʱ���Ϸ�����"
							MsgTimer = 70 * 5
							Return
						Else
							;TakeOffStuff(2)
							SelectedItem = item
						EndIf
				End Select
				
				If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(item\itemtemplate\sound))
				item\Picked = True
				item\Dropped = -1
				
				item\itemtemplate\found=True
				ItemAmount = ItemAmount + 1
				
				Inventory(n) = item
				HideEntity(item\collider)
				Exit
			EndIf
		Next
	Else
		Msg = "You cannot carry any more items."
		MsgTimer = 70 * 5
	EndIf
	CatchErrors("PickItem")
End Function

Function DropItem(item.Items,playdropsound%=True)
	If WearingHazmat > 0 Then
		Msg = "�㲻���ڴ��ŷ�����ʱ������Ʒ"
		MsgTimer = 70*5
		Return
	EndIf
	
	CatchErrors("Uncaught (DropItem)")
	If playdropsound Then
		If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX(item\itemtemplate\sound))
	EndIf
	
	item\Dropped = 1
	
	ShowEntity(item\collider)
	PositionEntity(item\collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
	RotateEntity(item\collider, EntityPitch(Camera), EntityYaw(Camera)+Rnd(-20,20), 0)
	MoveEntity(item\collider, 0, -0.1, 0.1)
	RotateEntity(item\collider, 0, EntityYaw(Camera)+Rnd(-110,110), 0)
	
	ResetEntity (item\collider)
	
	item\Picked = False
	For z% = 0 To MaxItemAmount - 1
		If Inventory(z) = item Then Inventory(z) = Null
	Next
	Select item\itemtemplate\tempname
		Case "gasmask", "supergasmask", "gasmask3"
			WearingGasMask = False
		Case "hazmatsuit",  "hazmatsuit2", "hazmatsuit3"
			WearingHazmat = False
		Case "vest", "finevest"
			WearingVest = False
		Case "nvgoggles"
			If WearingNightVision = 1 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
		Case "supernv"
			If WearingNightVision = 2 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
		Case "finenvgoggles"
			If WearingNightVision = 3 Then CameraFogFar = StoredCameraFogFar : WearingNightVision = False
		Case "scp714"
			Wearing714 = False
		Case "scp1499","super1499"
			Wearing1499 = False
		Case "scp427"
			I_427\Using = False
	End Select
	
	CatchErrors("DropItem")
	
End Function

;Update any ailments inflicted by SCP-294 drinks.
Function Update294()
	CatchErrors("Uncaught (Update294)")
	
	If CameraShakeTimer > 0 Then
		CameraShakeTimer = CameraShakeTimer - (FPSfactor/70)
		CameraShake = 2
	EndIf
	
	If VomitTimer > 0 Then
		DebugLog"VomitTimer"
		VomitTimer = VomitTimer - (FPSfactor/70)
		
		If (MilliSecs2() Mod 1600) < Rand(200, 400) Then
			If BlurTimer = 0 Then BlurTimer = Rnd(10, 20)*70
			CameraShake = Rnd(0, 2)
		EndIf
		
;		If (MilliSecs2() Mod 1000) < Rand(1200) Then 
		
		If Rand(50) = 50 And (MilliSecs2() Mod 4000) < 200 Then PlaySound_Strict(CoughSFX(Rand(0,2)))
		
		;Regurgitate when timer is below 10 seconds. (ew)
		If VomitTimer < 10 And Rnd(0, 500 * VomitTimer) < 2 Then
			If (Not ChannelPlaying(VomitCHN)) And (Not Regurgitate) Then
				VomitCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\294\Retch" + Rand(1, 2) + ".ogg"))
				Regurgitate = MilliSecs2() + 50
			EndIf
		EndIf
		
		If Regurgitate > MilliSecs2() And Regurgitate <> 0 Then
			mouse_y_speed_1 = mouse_y_speed_1 + 1.0
		Else
			Regurgitate = 0
		EndIf
		
	ElseIf VomitTimer < 0 Then ;vomit
		VomitTimer = VomitTimer - (FPSfactor/70)
		
		If VomitTimer > -5 Then
			If (MilliSecs2() Mod 400) < 50 Then CameraShake = 4 
			mouse_x_speed_1 = 0.0
			Playable = False
		Else
			Playable = True
		EndIf
		
		If (Not Vomit) Then
			BlurTimer = 40 * 70
			VomitSFX = LoadSound_Strict("SFX\SCP\294\Vomit.ogg")
			VomitCHN = PlaySound_Strict(VomitSFX)
			PrevInjuries = Injuries
			PrevBloodloss = Bloodloss
			Injuries = 1.5
			Bloodloss = 70
			EyeIrritation = 9 * 70
			
			pvt = CreatePivot()
			PositionEntity(pvt, EntityX(Camera), EntityY(Collider) - 0.05, EntityZ(Camera))
			TurnEntity(pvt, 90, 0, 0)
			EntityPick(pvt, 0.3)
			de.decals = CreateDecal(5, PickedX(), PickedY() + 0.005, PickedZ(), 90, 180, 0)
			de\Size = 0.001 : de\SizeChange = 0.001 : de\MaxSize = 0.6 : EntityAlpha(de\obj, 1.0) : EntityColor(de\obj, 0.0, Rnd(200, 255), 0.0) : ScaleSprite de\obj, de\size, de\size
			FreeEntity pvt
			Vomit = True
		EndIf
		
		UpdateDecals()
		
		mouse_y_speed_1 = mouse_y_speed_1 + Max((1.0 + VomitTimer / 10), 0.0)
		
		If VomitTimer < -15 Then
			FreeSound_Strict(VomitSFX)
			VomitTimer = 0
			If KillTimer >= 0 Then
				PlaySound_Strict(BreathSFX(0,0))
			EndIf
			Injuries = PrevInjuries
			Bloodloss = PrevBloodloss
			Vomit = False
		EndIf
	EndIf
	
	CatchErrors("Update294")
End Function








;~IDEal Editor Parameters:
;~F#B#1E
;~C#Blitz3D