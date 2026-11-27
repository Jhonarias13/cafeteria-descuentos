#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
rm -rf out
javac -encoding UTF-8 --release 17 -d out $(find src -name "*.java")
java -cp out pruebas.PruebasDescuento
java -cp "out:lib/mysql-connector-j-9.1.0.jar" App
