@echo off
REM Script para executar o Processador de Consultas SQL no Windows

echo Compilando o projeto...
call mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Iniciando a aplicacao...
    echo Nota: Este e um aplicativo GUI.
    echo.
    java -cp target\classes br.edu.processador.Main
) else (
    echo Erro na compilacao!
    exit /b 1
)
