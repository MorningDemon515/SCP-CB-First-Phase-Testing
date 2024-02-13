; SpeedText 2
; Copyright © Christian Klaussner

.lib "SpeedText.dll"

TextInitialize%(buffer%)
TextDeinitialize%()
TextSetBuffer(buffer%)
TextGetBuffer%()
TextLoadFont%(fontname$, height%, bold%, italic%, underline%, quality%, filename$)
TextFreeFont(font%)
TextSetFont(font%)
TextGetFont%()
TextDraw(x%, y%, text$, ax%, ay%, encoding%)
TextDrawRect(x%, y%, width%, height%, text$, ax%, format%, encoding%)
TextLockBuffer()
TextUnlockBuffer()
TextFontWidth%()
TextFontHeight%()
TextFontAscent%()
TextFontDescent%()
TextStringWidth%(text$, encoding%)
TextStringHeight%(text$, encoding%)
TextSetColor(red%, green%, blue%)
TextSetBackground(red%, green%, blue%)
TextColorRed%()
TextColorGreen%()
TextColorBlue%()
TextBackgroundRed%()
TextBackgroundGreen%()
TextBackgroundBlue%()

.lib " "

SetEncoding(encoding%)
FontAscent%()
FontDescent%()
