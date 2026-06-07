package bancoDeDados;

// BooksQueries.java
// Classe responsável pelo acesso ao banco de dados 'books' via JDBC.
// Fornece as consultas predefinidas exigidas pela Questão 24.2 (a, b, c)
// e a execução de consultas SQL personalizadas digitadas pelo usuário.
// Equipe: Tiago Pitombeira e Priscila

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BooksQueries {

    private static final String DATABASE_URL = "jdbc:derby:books";
    private static final String USERNAME = "deitel";
    private static final String PASSWORD = "deitel";

    private Connection connection;
    private PreparedStatement allAuthorsQuery;
    private PreparedStatement booksByAuthorQuery;
    private PreparedStatement authorsByTitleQuery;

    // Conecta ao banco de dados e prepara as consultas predefinidas
    public BooksQueries() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

            // (a) Todos os autores, em ordem alfabética por sobrenome e nome
            allAuthorsQuery = connection.prepareStatement(
                "SELECT AuthorID, FirstName, LastName FROM Authors " +
                "ORDER BY LastName, FirstName");

            // (b) Livros de um autor específico (título, ano e ISBN), em ordem por título
            booksByAuthorQuery = connection.prepareStatement(
                "SELECT Titles.Title, Titles.Copyright, Titles.ISBN " +
                "FROM Authors " +
                "INNER JOIN AuthorISBN ON Authors.AuthorID = AuthorISBN.AuthorID " +
                "INNER JOIN Titles ON AuthorISBN.ISBN = Titles.ISBN " +
                "WHERE Authors.FirstName = ? AND Authors.LastName = ? " +
                "ORDER BY Titles.Title");

            // (c) Autores de um título específico, em ordem alfabética por sobrenome e nome
            authorsByTitleQuery = connection.prepareStatement(
                "SELECT Authors.AuthorID, Authors.FirstName, Authors.LastName " +
                "FROM Authors " +
                "INNER JOIN AuthorISBN ON Authors.AuthorID = AuthorISBN.AuthorID " +
                "INNER JOIN Titles ON AuthorISBN.ISBN = Titles.ISBN " +
                "WHERE Titles.Title = ? " +
                "ORDER BY Authors.LastName, Authors.FirstName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (a) Retorna todos os autores da tabela Authors
    public List<String[]> getAllAuthors() {
        List<String[]> authors = new ArrayList<>();

        try (ResultSet resultSet = allAuthorsQuery.executeQuery()) {
            while (resultSet.next()) {
                authors.add(new String[] {
                    resultSet.getString("AuthorID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    // (b) Retorna os livros (título, ano e ISBN) de um autor específico
    public List<String[]> getBooksByAuthor(String firstName, String lastName) {
        List<String[]> books = new ArrayList<>();

        try {
            booksByAuthorQuery.setString(1, firstName);
            booksByAuthorQuery.setString(2, lastName);

            try (ResultSet resultSet = booksByAuthorQuery.executeQuery()) {
                while (resultSet.next()) {
                    books.add(new String[] {
                        resultSet.getString("Title"),
                        resultSet.getString("Copyright"),
                        resultSet.getString("ISBN")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // (c) Retorna os autores de um título específico
    public List<String[]> getAuthorsByTitle(String title) {
        List<String[]> authors = new ArrayList<>();

        try {
            authorsByTitleQuery.setString(1, title);

            try (ResultSet resultSet = authorsByTitleQuery.executeQuery()) {
                while (resultSet.next()) {
                    authors.add(new String[] {
                        resultSet.getString("AuthorID"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    // Executa uma consulta SQL personalizada e devolve o resultado encapsulado.
    // O chamador é responsável por chamar QueryResult.close() ao final.
    public QueryResult executeCustomQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new QueryResult(statement, resultSet);
    }

    // Fecha as consultas preparadas e a conexão com o banco de dados
    public void close() {
        try {
            if (allAuthorsQuery != null) allAuthorsQuery.close();
            if (booksByAuthorQuery != null) booksByAuthorQuery.close();
            if (authorsByTitleQuery != null) authorsByTitleQuery.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Encapsula o Statement e o ResultSet de uma consulta personalizada,
    // permitindo que o chamador feche ambos com uma única chamada a close()
    public static class QueryResult {
        public final ResultSet resultSet;
        private final Statement statement;

        public QueryResult(Statement statement, ResultSet resultSet) {
            this.statement = statement;
            this.resultSet = resultSet;
        }

        public void close() {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
