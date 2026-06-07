# Trabalho AV2 - Java (SENAC)

Aplicativo de consulta ao banco de dados **books**, desenvolvido para a Questão 24.2 do
livro *Java: Como Programar* (Deitel & Deitel) — Capítulo 24 (Acesso a Banco de Dados com JDBC).

Equipe: Tiago Pitombeira e Priscila

## Sobre o projeto

A aplicação se conecta a um banco de dados Apache Derby (`books`) e oferece:

- Três consultas predefinidas:
  - (a) Todos os autores, em ordem alfabética por sobrenome e nome
  - (b) Livros de um autor específico (título, ano e ISBN)
  - (c) Autores de um título específico
- Um campo para digitar e executar consultas SQL personalizadas
- Exibição dos resultados em uma tabela (`JTable`) através de uma interface gráfica Swing

## Estrutura

- `src/bancoDeDados/CriarBancoBooks.java` — cria o banco `books` (tabelas `Authors`, `Titles`, `AuthorISBN`) e popula com os dados iniciais. Executar **uma única vez** antes do aplicativo principal.
- `src/bancoDeDados/BooksQueries.java` — acesso ao banco via JDBC (consultas predefinidas e personalizadas).
- `src/bancoDeDados/BooksQueryFrame.java` — interface gráfica (Swing) com `JComboBox` de consultas predefinidas e área de consulta SQL personalizada.
- `src/bancoDeDados/DisplayBooks.java` — classe principal que inicia a aplicação.

## Como executar

1. Tenha o Apache Derby instalado e o `derby.jar` adicionado ao Build Path do projeto (já configurado em `.classpath`).
2. Execute `CriarBancoBooks` uma vez para criar e popular o banco `books`.
3. Execute `DisplayBooks` para abrir a interface gráfica do aplicativo.

## Requisitos

- Java 21
- Apache Derby (`db-derby-10.17.1.0`)
