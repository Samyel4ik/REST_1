package dao;

import dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BookDAOTest {
    @Mock
    private ResultSet resultSetMock;
    private BookDAO bookDAO;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;

    @BeforeEach
    public void setUp() {
        connectionMock = mock(Connection.class);

        bookDAO = new BookDAO();
        bookDAO.setConnection(connectionMock);
        preparedStatementMock = mock(PreparedStatement.class);
    }

    @Test
    public void testConstructor() {

        assertEquals(connectionMock, bookDAO.getConnection());
    }

    @Test
    public void testAddBook() throws SQLException, SQLException {
        BookDTO bookDTO = new BookDTO(1, "Test Book", 10.99, 1);

        doNothing().when(preparedStatementMock).setInt(anyInt(), anyInt());
        doNothing().when(preparedStatementMock).setString(anyInt(), anyString());
        doNothing().when(preparedStatementMock).setDouble(anyInt(), anyDouble());
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        bookDAO.addBook(bookDTO);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Test Book");
        verify(preparedStatementMock).setDouble(3, 10.99);
        verify(preparedStatementMock).setInt(4, 1);
        verify(preparedStatementMock).executeUpdate();

    }

    @Test
    public void testUpdateNameBook() throws SQLException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setName("New Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        doNothing().when(preparedStatementMock).setString(anyInt(), anyString());
        doNothing().when(preparedStatementMock).setInt(anyInt(), anyInt());
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        bookDAO.updateNameBook(bookDTO);

        verify(preparedStatementMock).setString(1, bookDTO.getName());
        verify(preparedStatementMock).setInt(2, bookDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }


    @Test
    public void testDeleteBookById() throws SQLException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);

        doNothing().when(preparedStatementMock).setInt(anyInt(), eq(bookDTO.getId()));
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        Connection connectionMock = mock(Connection.class);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        BookDAO bookDAO = new BookDAO(connectionMock);

        bookDAO.deletedBookById(bookDTO);

        verify(preparedStatementMock).setInt(1, bookDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeletedBookById_SuccessfulDeletion() throws SQLException {
        // Создание объекта BookDTO для теста
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        bookDAO.deletedBookById(bookDTO);

        verify(connectionMock, times(1)).prepareStatement("DELETE FROM books WHERE id = ?");
        verify(preparedStatementMock, times(1)).setInt(1, bookDTO.getId());
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testDeletedBookById_HandleSQLException() throws SQLException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        doThrow(SQLException.class).when(preparedStatementMock).executeUpdate();

        try {
            bookDAO.deletedBookById(bookDTO);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testGettingABook() throws SQLException {
        // Создание мок объектов для PreparedStatement и ResultSet
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        int idBook = 1;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true); // Указание, что результат не пустой
        when(resultSetMock.getString("name")).thenReturn("Test Book");
        when(resultSetMock.getInt("authors_id")).thenReturn(1);
        when(resultSetMock.getDouble("price")).thenReturn(10.0);

        BookDTO result = bookDAO.gettingABook(idBook);

        assertNotNull(result);
        assertEquals(idBook, result.getId());
        assertEquals("Test Book", result.getName());
        assertEquals(10.0, result.getPrice(), 0.001); // Точность проверки double
        assertEquals(1, result.getAuthorId());
    }

    @Test
    public void testUpdateNameBook_Success() throws SQLException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setName("New Book Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        bookDAO.updateNameBook(bookDTO);

        verify(preparedStatementMock).setString(1, bookDTO.getName());
        verify(preparedStatementMock).setInt(2, bookDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

}


