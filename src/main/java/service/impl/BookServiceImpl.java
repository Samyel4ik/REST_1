package service.impl;

import JDBC_Conector.DB_Connector;
import dao.BookDAO;
import dto.BookDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.BookService;

@Data
@NoArgsConstructor
@Getter
public class BookServiceImpl implements BookService {

    DB_Connector connector = new DB_Connector();
    BookDAO bookDAO = new BookDAO(connector.getConnection());

    public BookDTO gettingABook(int authorId) {
        return bookDAO.gettingABook(authorId);
    }

    @Override
    public void addBook(BookDTO bookDTO) {
        bookDAO.addBook(bookDTO);
    }

    @Override
    public void deleteBook(BookDTO bookDTO) {
        bookDAO.deletedBookById(bookDTO);
    }

    @Override
    public void updateBook(BookDTO bookDTO) {
        bookDAO.updateNameBook(bookDTO);
    }
}
