@echo off
REM Script de compilacion para Windows

echo Limpiando directorio out...
if exist out rmdir /s /q out
if exist rushhour.jar del /q rushhour.jar
mkdir out

echo.
echo Compilando archivos Java...
javac -d out src\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Error en la compilacion
    exit /b 1
)

echo.
echo Creando JAR...
jar cfm rushhour.jar src\MANIFEST.MF -C out .

if %ERRORLEVEL% EQU 0 (
    echo.
    echo JAR creado exitosamente: rushhour.jar
    echo.
    echo Verificando MANIFEST...
    jar xf rushhour.jar META-INF\MANIFEST.MF
    type META-INF\MANIFEST.MF
    rmdir /s /q META-INF
    echo.
    echo Ejecuta con: java -jar rushhour.jar verify BBJoooHoJDDMHAAooMHoKEEMIoKLFFIGGLoo
) else (
    echo Error al crear el JAR
    exit /b 1
)

echo.
echo Contenido del JAR:
jar tf rushhour.jar

pause
