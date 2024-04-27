package dao;

import dto.BookDTO;
import lombok.Getter;
import lombok.Setter;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BookDAO {
    private Connection connection;

    public BookDAO() {
    }

    public Connection getConnection() {
        return connection;
    }

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public BookDTO gettingABook(int idBook) {
        Book book = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, price, authors_id FROM books WHERE id =?");
            preparedStatement.setInt(1, idBook);
            ResultSet resultSet = preparedStatement.executeQuery();
            book = new Book();
            if (resultSet.next()) {
                book.setId(idBook);
                book.setPrice(resultSet.getDouble("price"));
                book.setName(resultSet.getString("name"));
                book.setAuthorId(resultSet.getInt("authors_id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return mapToDTO(book);
    }

    public BookDTO mapToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setAuthorId(book.getAuthorId());
        return bookDTO;
    }

    public void addBook(BookDTO bookDTO) {
        String sql = "INSERT INTO books (id, name, price, authors_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookDTO.getId());
            statement.setString(2, bookDTO.getName());
            statement.setDouble(3, bookDTO.getPrice());
            statement.setInt(4, bookDTO.getAuthorId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    public void updateNameBook(BookDTO bookDTO) {
        String updateQuery = "UPDATE books SET name = ? WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, bookDTO.getName());
            statement.setInt(2, bookDTO.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletedBookById(BookDTO bookDTO) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book with id " + bookDTO.getId(), e);
        }
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
}
