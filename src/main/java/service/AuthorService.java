package service;

import dto.AuthorDTO;

public interface AuthorService {
    /**
     *Получение информации о авторе.
     *
     * @param authorId Идентификатор автора.
     * @return объект типа AuthorDTO, содержащий информацию о авторе.
     */
    AuthorDTO findAuthorById(int authorId);

    /**
     * Метод удаление автора.
     *
     * @param authorDTO информация о авторе.
     */
    void deleteAuthor(AuthorDTO authorDTO);

    /**
     * Метод добаление автораю
     *
     * @param authorDTO информация о авторе.
     */
    void addAuthor(AuthorDTO authorDTO);
}