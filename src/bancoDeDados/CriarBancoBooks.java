package bancoDeDados;

//CriarBancoBooks.java
//Execute esta classe UMA VEZ antes de rodar o aplicativo principal.
//Ela cria o banco de dados 'books' com todas as tabelas e dados.
//Equipe: Tiago Pitombeira e Priscila

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CriarBancoBooks {

 // Usa "create=true" para criar o banco caso não exista
 private static final String DATABASE_URL = "jdbc:derby:books;create=true";
 private static final String USERNAME = "deitel";
 private static final String PASSWORD = "deitel";

 public static void main(String[] args) {
     System.out.println("Criando banco de dados books...");

     try (Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
          Statement stmt = conn.createStatement()) {

         // ── Criação das tabelas ──
         stmt.executeUpdate(
             "CREATE TABLE Authors (" +
             "  AuthorID  INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY, " +
             "  FirstName VARCHAR(20)  NOT NULL, " +
             "  LastName  VARCHAR(20)  NOT NULL, " +
             "  PRIMARY KEY (AuthorID)" +
             ")"
         );
         System.out.println("Tabela Authors criada.");

         stmt.executeUpdate(
             "CREATE TABLE Titles (" +
             "  ISBN          CHAR(10)     NOT NULL, " +
             "  Title         VARCHAR(100) NOT NULL, " +
             "  EditionNumber INTEGER      NOT NULL, " +
             "  Copyright     CHAR(4)      NOT NULL, " +
             "  PRIMARY KEY (ISBN)" +
             ")"
         );
         System.out.println("Tabela Titles criada.");

         stmt.executeUpdate(
             "CREATE TABLE AuthorISBN (" +
             "  AuthorID INTEGER  NOT NULL, " +
             "  ISBN     CHAR(10) NOT NULL, " +
             "  PRIMARY KEY (AuthorID, ISBN), " +
             "  FOREIGN KEY (AuthorID) REFERENCES Authors (AuthorID), " +
             "  FOREIGN KEY (ISBN)     REFERENCES Titles  (ISBN)" +
             ")"
         );
         System.out.println("Tabela AuthorISBN criada.");

         // ── Dados: Authors ──
         stmt.executeUpdate("INSERT INTO Authors (FirstName, LastName) VALUES ('Paul',    'Deitel')");
         stmt.executeUpdate("INSERT INTO Authors (FirstName, LastName) VALUES ('Harvey',  'Deitel')");
         stmt.executeUpdate("INSERT INTO Authors (FirstName, LastName) VALUES ('Abbey',   'Deitel')");
         stmt.executeUpdate("INSERT INTO Authors (FirstName, LastName) VALUES ('Dan',     'Quirk')");
         stmt.executeUpdate("INSERT INTO Authors (FirstName, LastName) VALUES ('Michael', 'Morgano')");
         System.out.println("Dados de Authors inseridos.");

         // ── Dados: Titles ──
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0132151006', 'Internet & World Wide Web How to Program', 5, '2012')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133807800', 'Java How to Program', 10, '2015')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0132575655', 'Java How to Program, Late Objects Version', 10, '2015')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('013299044X', 'C How to Program', 7, '2013')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0132990601', 'Simply Visual Basic 2010', 4, '2013')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133406954', 'Visual Basic 2012 How to Program', 6, '2014')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133379337', 'Visual C# 2012 How to Program', 5, '2014')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0136151574', 'Visual C++ 2008 How to Program', 2, '2008')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133378713', 'C++ How to Program', 9, '2014')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133570924', 'Android How to Program', 2, '2015')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0133764036', 'Android for Programmers: An App-Driven Approach, Volume 1', 2, '2014')");
         stmt.executeUpdate("INSERT INTO Titles VALUES ('0132121360', 'Android for Programmers: An App-Driven Approach', 1, '2012')");
         System.out.println("Dados de Titles inseridos.");

         // ── Dados: AuthorISBN ──
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0132151006')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0132151006')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0132151006')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133807800')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133807800')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0132575655')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0132575655')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '013299044X')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '013299044X')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0132990601')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0132990601')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0132990601')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133406954')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133406954')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0133406954')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133379337')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133379337')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0136151574')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0136151574')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0136151574')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (4, '0136151574')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133378713')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133378713')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133764036')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133764036')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0133764036')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0133570924')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0133570924')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0133570924')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (1, '0132121360')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (2, '0132121360')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (3, '0132121360')");
         stmt.executeUpdate("INSERT INTO AuthorISBN VALUES (5, '0132121360')");
         System.out.println("Dados de AuthorISBN inseridos.");

         System.out.println("\n✅ Banco de dados 'books' criado com sucesso!");
         System.out.println("Agora execute a classe DisplayBooks.java para usar o aplicativo.");

     } catch (SQLException e) {
         // Se as tabelas já existem, informa que o banco já foi criado
         if (e.getSQLState().equals("X0Y32")) {
             System.out.println("ℹ️  O banco de dados 'books' já existe. Tudo certo!");
         } else {
             System.out.println("Erro: " + e.getMessage());
             e.printStackTrace();
         }
     }
 }
}
