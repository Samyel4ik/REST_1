package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorTest {

    @Test
    void testAuthorConstructorAndGetters() {
        int id = 1;
        String name = "Test Author";
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1, "Book 1", 10.0, 1));
        bookList.add(new Book(2, "Book 2", 20.0, 1));

        Author author = new Author(id, name, bookList);

        assertEquals(id, author.getId());
        assertEquals(name, author.getName());
        assertEquals(bookList, author.getBookList());

        Author mockAuthor = mock(Author.class);
        when(mockAuthor.getId()).thenReturn(id);
        when(mockAuthor.getName()).thenReturn(name);
        when(mockAuthor.getBookList()).thenReturn(bookList);

        assertEquals(id, mockAuthor.getId());
        assertEquals(name, mockAuthor.getName());
        assertEquals(bookList, mockAuthor.getBookList());

        assertNotNull(mockAuthor.getBookList());
    }

}