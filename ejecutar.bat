@echo off
chcp 65001 > nul
cd /d "%~dp0"
if exist out rmdir /s /q out
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 --release 17 -d out @fuentes.txt
del fuentes.txt
java -cp out pruebas.PruebasDescuento
java -cp "out;lib\mysql-connector-j-9.1.0.jar" App
