package service.impl;

import JDBC_Conector.DB_Connector;
import dao.BookDAO;
import dto.BookDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceImplTest {

    private BookDAO bookDAOMock;
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        bookDAOMock = mock(BookDAO.class);
        DB_Connector connectorMock = mock(DB_Connector.class);
        when(connectorMock.getConnection()).thenReturn(mock(Connection.class));

        bookService = new BookServiceImpl();
        bookService.setBookDAO(bookDAOMock);
    }

    @Test
    void testGettingABook() {
        int authorId = 1;
        BookDTO expectedBook = new BookDTO();
        expectedBook.setId(1);
        expectedBook.setName("Test Book");
        expectedBook.setAuthorId(authorId);
        when(bookDAOMock.gettingABook(authorId)).thenReturn(expectedBook);
        BookDTO actualBook = bookService.gettingABook(authorId);
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @Test
    void testAddBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setName("Test Book");
        bookDTO.setAuthorId(1);
        doNothing().when(bookDAOMock).addBook(bookDTO);
        bookService.addBook(bookDTO);
        verify(bookDAOMock, times(1)).addBook(bookDTO);
    }

    @Test
    void testDeleteBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(123);
        bookService.deleteBook(bookDTO);
        verify(bookDAOMock).deletedBookById(bookDTO);
    }

    @Test
    void testUpdateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("New Book Name");
        bookService.updateBook(bookDTO);
        verify(bookDAOMock).updateNameBook(bookDTO);
    }
}