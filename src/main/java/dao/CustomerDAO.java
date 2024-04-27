package dao;

import dto.CustomerDTO;
import model.Book;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public CustomerDTO getCustomer(int idCustomer) {

        PreparedStatement preparedStatement = null;
        Customer customer = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT* FROM customers WHERE id = ?");

            preparedStatement.setInt(1, idCustomer);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int customer_id = resultSet.getInt("id");
                String nameCustomer = resultSet.getString("name");
                customer.setId(customer_id);
                customer.setName(nameCustomer);
                customer.setBookList(bookList(customer));//метод добавления книг если таков есть
            }
            return mapToDTO(customer);
        } catch (SQLException e) {
            // Обработка исключения
            System.err.println("SQL Exception occurred while retrieving customer with ID " + idCustomer + ": " + e.getMessage());
            return null; // Возвращаем null, чтобы показать, что запрос не удался
        }

    }


    public CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setBookList(customer.getBookList());
        return customerDTO;
    }

    public List<Book> bookList(Customer customer) {
        String sql = "SELECT b.* " +
                "FROM customers_books cb " +
                "JOIN books b ON cb.book_id = b.id " +
                "WHERE cb.customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Book> bookList = new ArrayList<>();
            statement.setInt(1, customer.getId());

            // Выполняем запрос
            try (ResultSet resultSet = statement.executeQuery()) {
                // Обрабатываем результаты запроса
                while (resultSet.next()) {
                    // Создаем объект книги и добавляем его в список
                    Book book = new Book();
                    book.setId(resultSet.getInt("id"));
                    book.setName(resultSet.getString("name"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setAuthorId(customer.getId());
                    // Добавьте другие поля книги при необходимости

                    bookList.add(book);
                }
            }
            return bookList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteCustomer(CustomerDTO customerDTO) {
        String sql = "DELETE FROM book_store.customers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerDTO.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerName(CustomerDTO customerDTO) {

        String updateQuery = "UPDATE customers SET name = ? WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(updateQuery);

            statement.setString(1, customerDTO.getName());
            statement.setInt(2, customerDTO.getId());
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Количество обновленных записей: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(CustomerDTO customerDTO) {

        String customerQuery = "INSERT INTO customers (id,name) VALUES (?,?)";
        String bookQuery = "INSERT INTO books (id,name, price, authors_id) VALUES (?, ?, ?, ?)";
        String customerBookQuery = "INSERT INTO customers_books (customer_id, book_id) VALUES (?, ?)";

        try {
            PreparedStatement customerStatement = connection.prepareStatement(customerQuery);
            PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
            PreparedStatement customerBookStatement = connection.prepareStatement(customerBookQuery);
            {
                customerStatement.setInt(1, customerDTO.getId());
                customerStatement.setString(2, customerDTO.getName());
                customerStatement.executeUpdate();
                int customerId = customerDTO.getId();

                List<Book> books = customerDTO.getBookList();
                for (Book book : books) {
                    bookStatement.setInt(1, book.getId());
                    bookStatement.setString(2, book.getName());
                    bookStatement.setDouble(3, book.getPrice());
                    bookStatement.setInt(4, book.getAuthorId());
                    bookStatement.executeUpdate();
                    int bookId = book.getId();
                    customerBookStatement.setInt(1, customerId);
                    customerBookStatement.setInt(2, bookId);
                    customerBookStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
