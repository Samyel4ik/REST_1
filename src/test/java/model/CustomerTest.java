package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerTest {

    @Test
    void testCustomerConstructorAndGetters(){

        int id = 1;
        String name = "Test Customer";
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1, "Book 1", 10.0, 1));
        bookList.add(new Book(2, "Book 2", 20.0, 1));

        Customer customer = new Customer(id, name, bookList);

        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(bookList, customer.getBookList());

        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn(id);
        when(mockCustomer.getName()).thenReturn(name);
        when(mockCustomer.getBookList()).thenReturn(bookList);

        assertEquals(id, mockCustomer.getId());
        assertEquals(name, mockCustomer.getName());
        assertEquals(bookList, mockCustomer.getBookList());

        assertNotNull(mockCustomer.getBookList());
    }
}
