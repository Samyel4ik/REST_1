package dao;

import dto.AuthorDTO;
import dto.BookDTO;
import lombok.Setter;
import model.Author;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Setter
public class AuthorDAO {
    private Connection connection;

    public AuthorDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public AuthorDTO findAuthorById(int authorId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM authors WHERE id =?");
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Author author = new Author();
            if (resultSet.next()) {
                author.setId(authorId);
                author.setName(resultSet.getString("name"));
            }
            PreparedStatement preparedStatement2 = connection
                    .prepareStatement("SELECT id, name, price FROM books WHERE authors_id=?");
            preparedStatement2.setInt(1, authorId);
            List<Book> books = new ArrayList<>();
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                String name = resultSet2.getString("name");
                int bookId = resultSet2.getInt("id");
                double price = resultSet2.getDouble("price");
                Book book = new Book(bookId, name, price, authorId);
                books.add(book);
            }
            author.setBookList(books);
            return mapToDTO(author);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AuthorDTO mapToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setBookList(author.getBookList());
        return authorDTO;
    }

    public List<Book> allBooks() {

        try (Statement statement = this.connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, name, price, authors_id FROM book_store.books")) {

            List<Book> listBook = new ArrayList<>();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double prise = resultSet.getDouble("price");
                int authorId = resultSet.getInt("authors_id");
                listBook.add(new Book(id, name, prise, authorId));
            }
            return listBook;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> allBooksByThisAuthor(int idAuthor) {
        List<Book> bookList = allBooks();
        List<Book> allBooksByThisAuthor = new ArrayList<>();

        for (Book book : bookList) {
            if (book.getAuthorId() == idAuthor) {
                allBooksByThisAuthor.add(book);
            }
        }
        return allBooksByThisAuthor;
    }

    public void addAuthor(AuthorDTO authorDTO) {
        String insertAuthorSQL = "INSERT INTO authors (id,name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertAuthorSQL)) {
            statement.setInt(1, authorDTO.getId());
            statement.setString(2, authorDTO.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int authorId = authorDTO.getId();
        String insertBookSQL = "INSERT INTO books (id,name,price, authors_id) VALUES (?, ?, ?,?)";
        for (Book book : authorDTO.getBookList()) {
            try {
                try (PreparedStatement bookStatement = connection.prepareStatement(insertBookSQL)) {
                    bookStatement.setInt(1, book.getId());
                    bookStatement.setString(2, book.getName());
                    bookStatement.setDouble(3, book.getPrice());
                    bookStatement.setInt(4, authorId);
                    bookStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAuthor(AuthorDTO authorDTO) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM authors WHERE id = ?")) {
            preparedStatement.setInt(1, authorDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting author with id " + authorDTO.getId(), e);
        }
    }

    public void updateAuthorName(AuthorDTO authorDTO) {

        String updateQuery = "UPDATE authors SET name = ? WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(updateQuery);

            statement.setString(1, authorDTO.getName());
            statement.setInt(2, authorDTO.getId());
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Количество обновленных записей: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

