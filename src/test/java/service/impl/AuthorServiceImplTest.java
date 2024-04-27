package service.impl;

import JDBC_Conector.DB_Connector;
import dao.AuthorDAO;
import dto.AuthorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorServiceImplTest {


    private AuthorDAO authorDAOMock;
    private AuthorServiceImpl authorService;

    @BeforeEach
    public void setUp() {
        authorDAOMock = mock(AuthorDAO.class);
        DB_Connector connectorMock = mock(DB_Connector.class);
        when(connectorMock.getConnection()).thenReturn(mock(Connection.class));

        authorService = new AuthorServiceImpl();
        authorService.setAuthorDAO(authorDAOMock);
    }

    @Test
    void testFindAuthorById() {
        AuthorDTO expectedAuthor = new AuthorDTO();
        expectedAuthor.setId(1);
        expectedAuthor.setName("John Doe");

        when(authorDAOMock.findAuthorById(1)).thenReturn(expectedAuthor);

        AuthorDTO actualAuthor = authorService.findAuthorById(1);

        Assertions.assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    void testAddAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("John Doe");

        authorService.addAuthor(authorDTO);

        verify(authorDAOMock).addAuthor(authorDTO);
    }

    @Test
    void testDeleteAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("John Doe");

        authorService.deleteAuthor(authorDTO);

        verify(authorDAOMock).deleteAuthor(authorDTO);
    }

    @Test
    void getConnector() {
        DB_Connector connectorMock = mock(DB_Connector.class);

        authorService.setConnector(connectorMock);

        Assertions.assertEquals(connectorMock, authorService.getConnector());
    }

    @Test
    void getAuthorDAO() {
        AuthorDAO authorDAOMock = mock(AuthorDAO.class);

        authorService.setAuthorDAO(authorDAOMock);

        Assertions.assertEquals(authorDAOMock, authorService.getAuthorDAO());
    }

    @Test
    void testUpdateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1);
        authorDTO.setName("New Author Name");

        doNothing().when(authorDAOMock).updateAuthorName(authorDTO);

        authorService.updateAuthor(authorDTO);

        verify(authorDAOMock, times(1)).updateAuthorName(authorDTO);
    }
}