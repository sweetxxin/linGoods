@echo off
taskkill /f /t /im javaw.exe
start javaw -jar goods-0.0.1.jar

:method_ping
echo ���������У����Ե�...
@ping 127.0.0.1 -n 6 >nul
echo �������������...
start D:\360���������X\360ChromeX\Chrome\Application\360ChromeX.exe "http://localhost:8080/linGoods/index.html"
@ping 127.0.0.1 -n 4 >nul
