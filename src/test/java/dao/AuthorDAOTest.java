package dao;

import dto.AuthorDTO;

import model.Author;
import model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class AuthorDAOTest {
    private Connection connectionMock;

    private PreparedStatement preparedStatementMock;


    Statement statementMock;

    private ResultSet resultSetMock;

    private AuthorDAO authorDAO;

    private BookDAO bookDAO;

    @BeforeEach
    public void setUp() {
        connectionMock = mock(Connection.class);

        authorDAO = new AuthorDAO(connectionMock);
        bookDAO = new BookDAO(connectionMock);
        preparedStatementMock = mock(PreparedStatement.class);
        statementMock = mock(Statement.class);
        resultSetMock = mock(ResultSet.class);
        bookDAO = new BookDAO(connectionMock);
    }

    @Test
    public void testConstructor() {

        assertEquals(connectionMock, authorDAO.getConnection());
    }


    @Test
    public void testDeleteAuthor() throws SQLException {
        preparedStatementMock = mock(PreparedStatement.class);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        authorDAO.deleteAuthor(authorDTO);

        verify(preparedStatementMock).setInt(1, authorDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteAuthor_HandleSQLException() throws SQLException {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);

        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);

        AuthorDAO authorDAO = new AuthorDAO(connectionMock);

        assertThrows(RuntimeException.class, () -> authorDAO.deleteAuthor(authorDTO));
    }

    @Test
    public void testFindAuthorById_HandleSQLException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);

        AuthorDTO authorDTO = authorDAO.findAuthorById(1);

        assertNull(authorDTO);
    }

    @Test
    public void testUpdateAuthorName() throws SQLException {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        AuthorDAO authorDAO = new AuthorDAO(connectionMock);

        authorDAO.updateAuthorName(authorDTO);

        verify(preparedStatementMock).setString(1, authorDTO.getName());
        verify(preparedStatementMock).setInt(2, authorDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdateAuthorName_Success() throws SQLException {
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        Connection connectionMock = mock(Connection.class);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        AuthorDAO authorDAO = new AuthorDAO(connectionMock);

        authorDAO.updateAuthorName(authorDTO);

        verify(preparedStatementMock).setString(1, authorDTO.getName());
        verify(preparedStatementMock).setInt(2, authorDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdateAuthorName_SuccessfulUpdate() throws SQLException {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        authorDAO.updateAuthorName(authorDTO);

        verify(preparedStatementMock).setString(1, authorDTO.getName());
        verify(preparedStatementMock).setInt(2, authorDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdateAuthorName_SQLException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(SQLException.class);
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");
        authorDAO.updateAuthorName(authorDTO);
    }

    @Test
    public void testMapToDTO() {
        // Создаем мок объект Author
        Author author = mock(Author.class);
        when(author.getId()).thenReturn(1);
        when(author.getName()).thenReturn("John Doe");
        // Мы также должны создать мок для списка книг, если он используется в вашем коде
        List<Book> bookList = Arrays.asList(new Book(), new Book());
        when(author.getBookList()).thenReturn(bookList);

        // Создаем объект вашего класса
        AuthorDAO authorDAO= new AuthorDAO(connectionMock);

        // Вызываем метод, который мы хотим протестировать
        AuthorDTO authorDTO = authorDAO.mapToDTO(author);

        // Проверяем, что метод правильно скопировал данные из Author в AuthorDTO
        assertEquals(1, authorDTO.getId());
        assertEquals("John Doe", authorDTO.getName());
        assertEquals(bookList, authorDTO.getBookList());
    }



    @Test
    public void testAddAuthor_SQLException() throws Exception {
        // Создаем мок объект Connection и выбрасываем SQLException при вызове createStatement
        Connection connectionMock = mock(Connection.class);
        when(connectionMock.createStatement()).thenThrow(SQLException.class);

        // Создаем объект AuthorDTO для тестирования
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("John Doe");

        // Создаем объект AuthorDAO с мок Connection
        AuthorDAO authorDAO = new AuthorDAO(connectionMock);

        // Вызываем тестируемый метод и ожидаем выбрасывание RuntimeException
        try {
            authorDAO.addAuthor(authorDTO);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Убеждаемся, что исключение было выброшено

        }
    }
    @Test
    public void testUpdateAuthorName_SuccessfulExecution() throws SQLException {

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        // Создаем объект AuthorDTO для тестирования
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");

        // Создаем объект AuthorDAO с мок Connection
        AuthorDAO authorDAO = new AuthorDAO(connectionMock);

        // Вызываем тестируемый метод
        authorDAO.updateAuthorName(authorDTO);

        // Проверяем, что установлены параметры и вызван метод executeUpdate() у PreparedStatement
        verify(preparedStatementMock).setString(1, authorDTO.getName());
        verify(preparedStatementMock).setInt(2, authorDTO.getId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testAllBooks_SuccessfulExecution() throws SQLException {
        // Создание ожидаемого списка книг
        List<Book> expectedBooks = Arrays.asList(
                new Book(1, "Book 1", 10.0, 1),
                new Book(2, "Book 2", 20.0, 2)
        );

        // Настройка mock объектов для executeQuery
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("id")).thenReturn(1, 2);
        when(resultSetMock.getString("name")).thenReturn("Book 1", "Book 2");
        when(resultSetMock.getDouble("price")).thenReturn(10.0, 20.0);
        when(resultSetMock.getInt("authors_id")).thenReturn(1, 2);

        // Вызов тестируемого метода
        List<Book> actualBooks = authorDAO.allBooks();

        // Проверка результата
        Assertions.assertEquals(expectedBooks, actualBooks);
    }

    @Test
    public void testAllBooks_SQLException() throws SQLException {
        // Настройка mock объекта для statement, чтобы выбросить SQLException
        when(connectionMock.createStatement()).thenThrow(SQLException.class);

        // Вызов тестируемого метода, который должен обработать исключение и выбросить RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> {
           bookDAO.allBooks();
        });
    }

}



