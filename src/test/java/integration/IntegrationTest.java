package integration;

import dao.AuthorDAO;
import dao.BookDAO;
import dto.AuthorDTO;
import dto.BookDTO;
import model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import service.impl.AuthorServiceImpl;
import service.impl.BookServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {
    static MyPostgreSQLContainer postgreSQLContainer = new MyPostgreSQLContainer();

    @BeforeAll
    static void startContainer() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        postgreSQLContainer.stop();
    }

    @Test
    @Order(1)
    void testAuthorIntegration() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword())) {
            AuthorDAO authorDAO = new AuthorDAO(connection);
            AuthorServiceImpl authorService = new AuthorServiceImpl();
            authorService.setAuthorDAO(authorDAO);
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setName("Test author");
            authorDTO.setId(1);
            Book book = new Book(1, "test_book", 9.99, 1);
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);
            authorDTO.setBookList(bookList);

            authorService.addAuthor(authorDTO);

            AuthorDTO retrievedAuthorDTO = authorService.findAuthorById(authorDTO.getId());
            Assertions.assertNotNull(retrievedAuthorDTO);
            Assertions.assertEquals(authorDTO.getName(), retrievedAuthorDTO.getName());
            Assertions.assertEquals(authorDTO.getId(), retrievedAuthorDTO.getId());
        }
    }

    @Test
    @Order(2)
    void testGetAuthorById() throws SQLException {
        try (Connection connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())) {
            AuthorDAO authorDAO = new AuthorDAO(connection);
            AuthorServiceImpl authorService = new AuthorServiceImpl();
            authorService.setAuthorDAO(authorDAO);
            AuthorDTO retrievedAuthorDTO = authorService.getAuthorDAO().findAuthorById(1);
            assertNotNull(retrievedAuthorDTO);
            assertEquals("Test author", retrievedAuthorDTO.getName());
        }
    }
}

