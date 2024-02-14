; SpeedText 2
; Copyright Christian Klaussner
; 变量
Const TEXT_ANSI = 0
Const TEXT_UTF8 = 1

Const TEXT_LEFT	= 0
Const TEXT_CENTER	= 1
Const TEXT_RIGHT	= 2

Const TEXT_TOP	= 0
Const TEXT_MIDDLE	= 1
Const TEXT_BOTTOM	= 2

Const TEXT_DEFAULT			= 0
Const TEXT_NONANTIALIASED	= 1
Const TEXT_ANTIALIASED		= 2
Const TEXT_CLEARTYPE		= 3

Const TEXT_WORDWRAP	= 1
Const TEXT_DONTCLIP	= 2

Global encoding = TEXT_ANSI

;抗锯齿文本变量

Global EnableSubtitle% = GetINIInt(OptionFile, "options", "subtitle")
Global AASelectedFont%
Global AATextCam%,AATextSprite%[150]
Global AACharW%,AACharH%
Global EnableSubtitle_Prev% = EnableSubtitle

Global AACamViewW%,AACamViewH%

;SpeedText函数
Function SetEncoding(encoding_st)
	encoding = encoding_st
End Function

Function LoadFont(fontname$, height = 10, bold = False, italic = False, underline = False)
	Return TextLoadFont(fontname, height, bold, italic, underline, TEXT_ANTIALIASED, "")
End Function

Function FreeFont(font)
	TextFreeFont(font)
End Function

Function SetFont(font)
	TextSetFont(font)
End Function

Function Text(x, y, txt$, centre_x = False, centre_y = False)
	Local ax, ay
	If centre_x ax = TEXT_CENTER
	If centre_y ay = TEXT_MIDDLE
	
	TextSetColor ColorRed(), ColorGreen(), ColorBlue()
	
	TextDraw(x, y, txt, ax, ay, encoding)
End Function

Function FontWidth()
	Return TextFontWidth()
End Function

Function FontHeight()
	Return TextFontHeight()
End Function

Function FontAscent()
	Return TextFontAscent()
End Function

Function FontDescent()
	Return TextFontDescent()
End Function

Function StringWidth(txt$)
	Return TextStringWidth(txt, encoding)
End Function

Function StringHeight(txt$)
	Return TextStringHeight(txt, encoding)
End Function

;抗锯齿文本函数

Type AAFont
	Field texture%
	Field backup% ;images don't get erased by clearworld
	
	Field x%[128] ;not going to bother with unicode
	Field y%[128]
	Field w%[128]
	Field h%[128]
	
	Field lowResFont% ;for use on other buffers
	
	Field mW%
	Field mH%
	Field texH%
	
	Field isAA%
End Type
 
Function InitAAFont()
	TextInitialize BackBuffer()
End Function

Function AASpritePosition(ind%,x%,y%)
	;THE HORROR
	Local nx# = (((Float(x-(AACamViewW/2))/Float(AACamViewW))*2))
	Local ny# = -(((Float(y-(AACamViewH/2))/Float(AACamViewW))*2))
	
	;how does this work pls help
	nx = nx-((1.0/Float(AACamViewW))*(((AACharW-2) Mod 2)))+(1.0/Float(AACamViewW))
	ny = ny-((1.0/Float(AACamViewW))*(((AACharH-2) Mod 2)))+(1.0/Float(AACamViewW))
	
	PositionEntity AATextSprite[ind],nx,ny,1.0
End Function

Function AASpriteScale(ind%,w%,h%)
	ScaleEntity AATextSprite[ind],1.0/Float(AACamViewW)*Float(w), 1/Float(AACamViewW)*Float(h), 1
	AACharW = w : AACharH = h
End Function

Function ReloadAAFont() ;CALL ONLY AFTER CLEARWORLD
	If EnableSubtitle Then
		InitAAFont()
		For font.AAFont = Each AAFont
			If font\isAA Then
				font\texture = CreateTexture(1024,1024,3)
				LockBuffer ImageBuffer(font\backup)
				LockBuffer TextureBuffer(font\texture)
				For ix%=0 To 1023
					For iy%=0 To font\texH
						px% = ReadPixelFast(ix,iy,ImageBuffer(font\backup)) Shl 24
						WritePixelFast(ix,iy,$FFFFFF+px,TextureBuffer(font\texture))
					Next
				Next
				UnlockBuffer TextureBuffer(font\texture)
				UnlockBuffer ImageBuffer(font\backup)
			EndIf
		Next
	EndIf
End Function

Function AASetFont(fnt%)
	TextSetFont fnt
End Function

Function AAStringWidth%(txt$)
	Return TextStringWidth(txt, encoding)
End Function

Function AAStringHeight%(txt$)
	Return TextStringHeight(txt, encoding)
End Function

Function AAText(x%,y%,txt$,cx%=False,cy%=False,a#=1.0)
	Text x,y,txt,cx,cy
End Function

Function AALoadFont%(file$="Tahoma", height=13, bold=0, italic=0, underline=0, AATextScaleFactor%=2)
	Local newFont.AAFont = New AAFont
	
	newFont\lowResFont = LoadFont(file,height,bold,italic,underline)
	
	SetFont newFont\lowResFont
	newFont\mW = FontWidth()
	newFont\mH = FontHeight()
	
	If EnableSubtitle And AATextScaleFactor>1 Then
		Local hResFont% = LoadFont(file,height*AATextScaleFactor,bold,italic,underline)
		Local tImage% = CreateTexture(1024,1024,3)
		Local tX% = 0 : Local tY% = 1
		
		SetFont hResFont
		Local tCharImage% = CreateImage(FontWidth()+2*AATextScaleFactor,FontHeight()+2*AATextScaleFactor)
		ClsColor 0,0,0
		LockBuffer TextureBuffer(tImage)
		
		Local miy% = newFont\mH*((newFont\mW*95/1024)+2)
		DebugLog miy
		
		newFont\mW = 0
		
		For ix%=0 To 1023
			For iy%=0 To miy
				WritePixelFast(ix,iy,$FFFFFF,TextureBuffer(tImage))
			Next
		Next
		
		For i=32 To 126
			SetBuffer ImageBuffer(tCharImage)
			Cls

			Color 255,255,255
			SetFont hResFont
			Text AATextScaleFactor/2,AATextScaleFactor/2,Chr(i)
			Local tw% = StringWidth(Chr(i)) : Local th% = FontHeight()
			SetFont newFont\lowResFont
			Local dsw% = StringWidth(Chr(i)) : Local dsh% = FontHeight()
			
			Local wRatio# = Float(tw)/Float(dsw)
			Local hRatio# = Float(th)/Float(dsh)
			
			SetBuffer BackBuffer()
				
			LockBuffer ImageBuffer(tCharImage)
			
			For iy%=0 To dsh-1
				For ix%=0 To dsw-1
					Local rsx% = Int(Float(ix)*wRatio-(wRatio*0.0))
					If (rsx<0) Then rsx=0
					Local rsy% = Int(Float(iy)*hRatio-(hRatio*0.0))
					If (rsy<0) Then rsy=0
					Local rdx% = Int(Float(ix)*wRatio+(wRatio*1.0))
					If (rdx>tw) Then rdx=tw-1
					Local rdy% = Int(Float(iy)*hRatio+(hRatio*1.0))
					If (rdy>th) Then rdy=th-1
					Local ar% = 0
					If Abs(rsx-rdx)*Abs(rsy-rdy)>0 Then
						For iiy%=rsy To rdy-1
							For iix%=rsx To rdx-1
								ar=ar+((ReadPixelFast(iix,iiy,ImageBuffer(tCharImage)) And $FF))
							Next
						Next
						ar = ar/(Abs(rsx-rdx)*Abs(rsy-rdy))
						If ar>255 Then ar=255
						ar = ((Float(ar)/255.0)^(0.5))*255
					EndIf
					WritePixelFast(ix+tX,iy+tY,$FFFFFF+(ar Shl 24),TextureBuffer(tImage))
				Next
			Next
			
			UnlockBuffer ImageBuffer(tCharImage)
	 
			newFont\x[i]=tX
			newFont\y[i]=tY
			newFont\w[i]=dsw+2
			
			If newFont\mW<newFont\w[i]-3 Then newFont\mW=newFont\w[i]-3
			
			newFont\h[i]=dsh+2
			tX=tX+newFont\w[i]+2
			If (tX>1024-FontWidth()-4) Then
				tX=0
				tY=tY+FontHeight()+6
			EndIf
		Next
		
		newFont\texH = miy
		
		Local backup% = CreateImage(1024,1024)
		LockBuffer ImageBuffer(backup)
		For ix%=0 To 1023
			For iy%=0 To newFont\texH
				px% = ReadPixelFast(ix,iy,TextureBuffer(tImage)) Shr 24
				px=px+(px Shl 8)+(px Shl 16)
				WritePixelFast(ix,iy,$FF000000+px,ImageBuffer(backup))
			Next
		Next
		UnlockBuffer ImageBuffer(backup)
		newFont\backup = backup
		
		UnlockBuffer TextureBuffer(tImage)
		
		
		FreeImage tCharImage
		FreeFont hResFont
		newFont\texture = tImage
		newFont\isAA = True
	Else
		newFont\isAA = False
	EndIf
	Return Handle(newFont)
End Function