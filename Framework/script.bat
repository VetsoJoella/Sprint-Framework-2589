
@echo off 

set "src=src"
set "bin=bin"
set "pages=pages"
set "lib=lib"
set "testLib=D:\Itu\S4\Web dynamique\WORKING-SPACE\POC - ticketing\Ticketing\lib\"
@REM set "testLib=D:\Itu\S4\Web dynamique\WORKING-SPACE\POC - ticketing\Ticketing\lib"
set "testPages=..\test\pages\"
set "jarName=controller"

for /R "%src%" %%f in (*.java) do (
    copy "%%f" "%bin%"
)

rem Compilation des fichiers
javac -cp ".;%lib%/*" -d "%bin%" %bin%\*.java

rem Vérification de la compilation
if %errorlevel% neq 0 ( 
    echo Erreur de la compilation
    exit /b 1
)

del /s /q "%bin%\*.java" 

rem Archivage des fichiers
jar -cvf "%testLib%%jarName%.jar" -C "%bin%" .

rem Copie du contenu du lib du framework dans le lib du developpeur
@REM copy "%lib%\*.jar" "%testLib%"
@REM copy "%lib%\*.jar" "%testLib%"

rem copier des pages dans le dossier pages de 

xcopy "%pages%" "%testPages%" /E

echo Script terminé avec succès.
exit /b 0