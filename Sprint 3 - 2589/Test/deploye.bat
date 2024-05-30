
@echo off 

set "webapps=D:\apache-tomcat-10.1.7\webapps\"
set "nomProjet=Sprint-3" 

set "temporaire=D:\S4\Web dynamique\Temp"
set "src=src"
set "lib=lib"
set "web=pages" 
set "xml=conf"
set "bin=bin" 

rem Suppression et Creation du dossier temporaire
del /s /q "%temporaire%\*.*" 
rmdir /s /q "%temporaire%"

@REM mkdir "%temporaire%\META-INF"

rem Creation du dossier web, et lib
mkdir "%temporaire%\WEB-INF\lib" && mkdir "%temporaire%\WEB-INF\views\" && mkdir "%bin%

rem Copie du dossier web, we.xml et lib
xcopy "%lib%" "%temporaire%\WEB-INF\lib" /E && xcopy "%web%" "%temporaire%\WEB-INF\views" /E && copy "%xml%\*" "%temporaire%\WEB-INF\"

for /R "%src%" %%f in (*.java) do (
    copy "%%f" "%bin%"
)
rem Compilation des fichiers
javac -cp %lib%\* -d "%temporaire%\WEB-INF\classes" "%src%\*.java"

del /s /q "%bin%\*.*" 

rem Archivage des fichiers
jar -cvf "%webapps%%nomProjet%.war" -C "%temporaire%" .