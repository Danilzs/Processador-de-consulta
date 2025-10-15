#!/bin/bash

# Script para executar o Processador de Consultas SQL

echo "Compilando o projeto..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo ""
    echo "Iniciando a aplicação..."
    echo "Nota: Este é um aplicativo GUI. Certifique-se de estar em um ambiente com interface gráfica."
    echo ""
    java -cp target/classes br.edu.processador.Main
else
    echo "Erro na compilação!"
    exit 1
fi
