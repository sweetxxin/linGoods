@echo off & setlocal EnableDelayedExpansion

title kill port

for %%a in (8080) do (
  set pid=0
  for /f "tokens=2,5" %%b in ('netstat -ano ^| findstr ":%%a"') do (
	set temp=%%b
	for /f "usebackq delims=: tokens=1,2" %%i in (`set temp`) do (
	  if %%j==%%a (
		taskkill /f /pid %%c
		set pid=%%c
	  )
	)
  )
)
echo �������л���׼���ɹ�

start javaw -jar goods-0.0.1.jar

:method_ping
echo ���������У����Ե�...

@ping 127.0.0.1 -n 5 >nul
echo �������������...
start S:\360cse\360Chrome\Chrome\Application\360chrome.exe "http://localhost:8080/linGoods/index.html"
@ping 127.0.0.1 -n 4 >nul
