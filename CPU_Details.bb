Graphics 640,480,0,2
SetBuffer BackBuffer()

Cls

Include "SpeedText.bb"
SeedRnd MilliSecs()
TextInitialize BackBuffer()

Color 255,255,255
Text 0,0,"�������������ʾ���CPU��Ϣ����һ��ʼ�ڵ��Խ�����ʾ����"
Color 255,0,0
Text 0,20,"���棺�ó����������м������������ʾ��"
Text 0,40,"��˸ó������Ϸ������ɾ����"
Color 255,255,255
Text 0,80,"�����������"

Flip

WaitKey()

Global kCPUid$, kCPUfamily%, kCPUsteppingId%, kCPUbrand$, kCPUextendedId$, kCPUfeatures$

kCPUid$         = CPUid$()
kCPUfamily%     = CPUfamily%()
kCPUsteppingId% = CPUsteppingId%()
kCPUbrand$      = CPUbrand$()
kCPUextendedId$ = CPUextendedId$()
kCPUfeatures$   = CPUfeatures$()

Repeat
	Cls
	Color 255,255,255
	Text 0,0,LSet("CPU ID: ",18)+kCPUid
	Text 0,20,LSet("CPU ϵ��: ",18)+kCPUfamily
	Text 0,40,LSet("CPU ����ID: ",18)+kCPUsteppingId
	Text 0,60,LSet("CPU Ʒ��: ",18)+kCPUbrand
	Text 0,80,LSet("CPU ����: ",18)+kCPUextendedId
	Text 0,100,LSet("CPU ����: ",18)+kCPUfeatures
	Text 0,140,"��������رճ���"
	Flip
	Delay 8
Until WaitKey()
TextDeinitialize
End
;~IDEal Editor Parameters:
;~C#Blitz3D