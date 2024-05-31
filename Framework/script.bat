
@echo off 

set "src=src"
set "bin=bin"
set "testLib=..\test\lib\"
set "jarName=controller"

for /R "%src%" %%f in (*.java) do (
    copy "%%f" "%bin%"
)

rem Compilation des fichiers
javac -d "%bin%" "%src%\*.java"

del /s /q "%bin%\*.java" 

rem Archivage des fichiers
jar -cvf "%testLib%%jarName%.jar" -C "%bin%" .