@echo off
set PATH_TO_FX="javafx-sdk-21.0.2\lib"
set MYSQL_JAR="lib\mysql-connector-j-8.0.33.jar"

echo Cleaning old classes...
if exist out rmdir /s /q out
mkdir out

echo Compiling...
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp %MYSQL_JAR% -d out cabinet\DatabaseConnection.java cabinet\model\*.java cabinet\dao\*.java cabinet\ui\*.java

echo Copying resources...
xcopy /y cabinet\ui\*.fxml out\cabinet\ui\
xcopy /y cabinet\ui\*.css out\cabinet\ui\

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

echo Starting Cabinet Medical...
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp "out;%MYSQL_JAR%" cabinet.ui.LoginFx
pause
