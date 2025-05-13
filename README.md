
Gerador de Boletos Bancários
Descrição :
Este projeto é uma aplicação em Java que cria boletos bancários para diferentes bancos brasileiros. O sistema calcula tudo que precisa estar no boleto, como Nosso Número, Campo Livre, Código de Barras e Linha Digitável, de acordo com cada banco. Além disso, ele pode gerar uma versão em PDF do boleto.(vai depender do humor do código)

A aplicação usa o padrão de projeto Builder, que permite criar boletos de forma flexível e personalizada para cada banco.

Principais Funcionalidades
Criação de boletos para:
Banco do Brasil
Itaú Unibanco
Bradesco
Cálculo automático do Campo Livre para cada banco.
Cálculo do Fator de Vencimento conforme as regras da FEBRABAN (incluindo mudanças para 2025).
Geração do Código de Barras com 44 números.
Geração da Linha Digitável com os dígitos verificadores corretos.
Interface de linha de comando (CLI) para inserir os dados do boleto.
Opção de gerar o boleto em PDF usando a biblioteca iText.
Tecnologias Usadas
Linguagem: Java 17
Gerenciamento de Dependências: Apache Maven
Geração de PDF: iText 5.5.13.3
Pré-requisitos
Para compilar e rodar este projeto, você precisa ter:

bash
Copy
# Exemplo com git clone (se aplicável)
# git clone <url_do_repositorio>
Vá até a pasta do projeto (onde está o arquivo pom.xml).

Como Compilar o Projeto
No diretório do projeto, execute o seguinte comando Maven para compilar o código e baixar as dependências:

bash
Copy
mvn clean package

Link vídeo: 
