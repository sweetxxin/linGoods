@echo off
taskkill /f /t /im javaw.exe
start javaw -jar goods-0.0.1.jar

:method_ping
echo 程序启动中，请稍等...
@ping 127.0.0.1 -n 6 >nul
echo 正在启动浏览器...
start D:\360极速浏览器X\360ChromeX\Chrome\Application\360ChromeX.exe "http://localhost:8080/linGoods/index.html"
@ping 127.0.0.1 -n 4 >nul
