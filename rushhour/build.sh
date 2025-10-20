#!/bin/bash

JAR_NAME="rushhour.jar"
MAIN_CLASS="main.Rushhour"

rm -rf out
mkdir out

echo "Compilando..."
javac -d out $(find src -name "*.java")

echo "Creando MANIFEST..."
echo "Manifest-Version: 1.0" > MANIFEST.MF
echo "Main-Class: $MAIN_CLASS" >> MANIFEST.MF
echo "" >> MANIFEST.MF 

echo "Empaquetando en $JAR_NAME..."
jar cfm $JAR_NAME MANIFEST.MF -C out .