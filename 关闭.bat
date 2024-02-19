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
echo 程序关闭成功
@ping 127.0.0.1 -n 2 >nul