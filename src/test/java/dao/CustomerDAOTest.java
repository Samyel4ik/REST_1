package dao;

import dto.CustomerDTO;
import model.Book;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerDAOTest {

    private Connection connectionMock;
    private CustomerDAO customerDAO;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionMock = mock(Connection.class);
        customerDAO = new CustomerDAO(connectionMock);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);

        // Настройка mock объекта для prepareStatement
        when(connectionMock.prepareStatement("SELECT b.* FROM customers_books cb JOIN books b ON cb.book_id = b.id WHERE cb.customer_id = ?"))
                .thenReturn(preparedStatementMock);
        // Настройка mock объекта для executeQuery
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }


    @Test
    public void testConstructor() {

        assertEquals(connectionMock, customerDAO.getConnection());
    }


    @Test
    public void testGetCustomer_HandleSQLException() throws SQLException {
        // Настройка mock объектов
        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);

        // Создание объекта CustomerDAO с mock Connection
        CustomerDAO customerDAO = new CustomerDAO(connectionMock);

        // Вызов тестируемого метода
        CustomerDTO customerDTO = customerDAO.getCustomer(1);

        // Проверка результата
        assertNull(customerDTO);
    }

    @Test
    public void testMapToDTO() {
        // Создание мок объекта Customer
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1);
        when(customer.getName()).thenReturn("John Doe");
        // Предположим, что у нашего мок-объекта нет книг, поэтому getBookList() возвращает null

        // Создание объекта CustomerMapper
        CustomerDAO mapper = new CustomerDAO(connectionMock);

        // Вызов тестируемого метода
        CustomerDTO customerDTO = mapper.mapToDTO(customer);

        // Проверка результатов
        assertEquals(1, customerDTO.getId());
        assertEquals("John Doe", customerDTO.getName());
    }

    @Test
    public void testBookList_EmptyResultSet() throws SQLException {
        // Создание мок-объекта Customer
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1);

        // Подготовка ResultSet
        when(resultSetMock.next()).thenReturn(false);

        // Вызов тестируемого метода
        CustomerDAO customerDAO = new CustomerDAO(connectionMock);
        List<Book> bookList = customerDAO.bookList(customer);

        // Проверка, что список книг пустой
        assertEquals(0, bookList.size());
    }

    @Test
    public void testBookList_NonEmptyResultSet() throws SQLException {
        // Создание мок-объекта Customer
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1);

        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("id")).thenReturn(1, 2);
        when(resultSetMock.getString("name")).thenReturn("Book 1", "Book 2");
        when(resultSetMock.getDouble("price")).thenReturn(10.0, 20.0);

        CustomerDAO customerDAO = new CustomerDAO(connectionMock);
        List<Book> bookList = customerDAO.bookList(customer);

        assertEquals(2, bookList.size());
        assertEquals(1, bookList.get(0).getId());
        assertEquals("Book 1", bookList.get(0).getName());
        assertEquals(10.0, bookList.get(0).getPrice(), 0.001);
        assertEquals(2, bookList.get(1).getId());
        assertEquals("Book 2", bookList.get(1).getName());
        assertEquals(20.0, bookList.get(1).getPrice(), 0.001);
    }

    @Test
    public void testBookList_SQLException() throws SQLException {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1);

        when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);

        CustomerDAO customerDAO = new CustomerDAO(connectionMock);
        try {
            customerDAO.bookList(customer);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            // Проверка, что выброшено исключение RuntimeException
            assertNotNull(e);
        }
    }

    @Test
    public void testUpdateCustomerName_SuccessfulExecution() throws SQLException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("New Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        customerDAO.updateCustomerName(customerDTO);

        verify(preparedStatementMock).setString(1, customerDTO.getName());
        verify(preparedStatementMock).setInt(2, customerDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdateCustomerName_SQLException() throws SQLException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("New Name");

        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertDoesNotThrow(() -> customerDAO.updateCustomerName(customerDTO));
    }


    @Test
    public void testAddCustomer_SuccessfulExecution() throws SQLException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("John Doe");
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", 10.0, 1),
                new Book(2, "Book 2", 20.0, 2)
        );
        customerDTO.setBookList(books);
        PreparedStatement customerStatementMock = mock(PreparedStatement.class);
        PreparedStatement bookStatementMock = mock(PreparedStatement.class);
        PreparedStatement customerBookStatementMock = mock(PreparedStatement.class);

        when(connectionMock.prepareStatement(anyString())).thenReturn(customerStatementMock, bookStatementMock, customerBookStatementMock);
        when(customerStatementMock.executeUpdate()).thenReturn(1);
        when(bookStatementMock.executeUpdate()).thenReturn(1);
        when(customerBookStatementMock.executeUpdate()).thenReturn(1);

        customerDAO.addCustomer(customerDTO);

        verify(customerStatementMock, atLeastOnce()).setInt(1, customerDTO.getId());
        verify(customerStatementMock, atLeastOnce()).setString(2, customerDTO.getName());
        verify(customerStatementMock, atLeastOnce()).executeUpdate();

        for (Book book : books) {
            verify(bookStatementMock, atLeastOnce()).setInt(1, book.getId());
            verify(bookStatementMock, atLeastOnce()).setString(2, book.getName());
            verify(bookStatementMock, atLeastOnce()).setDouble(3, book.getPrice());
            verify(bookStatementMock, atLeastOnce()).setInt(4, book.getAuthorId());
            verify(bookStatementMock, atLeastOnce()).executeUpdate();

            verify(customerBookStatementMock, atLeastOnce()).setInt(1, customerDTO.getId());
            verify(customerBookStatementMock, atLeastOnce()).setInt(2, book.getId());
            verify(customerBookStatementMock, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    public void testAddCustomer_SQLException() throws SQLException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("John Doe");

        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertDoesNotThrow(() -> customerDAO.addCustomer(customerDTO));
    }
}
