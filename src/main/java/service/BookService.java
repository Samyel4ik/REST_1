package service;

import dto.BookDTO;

/**
 * Сервис для работы работы с книгами.
 * Этот сервис предоставляет методы ля получения, добавление, удаление, изменение информации о книгах.
 */
public interface BookService {

    /**
     * Метод получения книг автора по индетефикатору автора.
     *
     * @param authorId Идентификатор автора.
     * @return
     */
    BookDTO gettingABook(int authorId);

    /**
     * Метод добавления книг.
     *
     * @param bookDTO
     */
    void addBook(BookDTO bookDTO);

    /**
     * Метов удаления книги.
     *
     * @param bookDTO Информация о книге.
     */
    void deleteBook(BookDTO bookDTO);

    /**
     * Метод обновления информации о книге.
     *
     * @param bookDTO Информация о книге.
     */
    void updateBook(BookDTO bookDTO);
}
