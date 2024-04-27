package service.impl;

import JDBC_Conector.DB_Connector;
import dao.AuthorDAO;

import dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.AuthorService;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthorServiceImpl implements AuthorService {

    DB_Connector connector = new DB_Connector();
    AuthorDAO authorDAO = new AuthorDAO(connector.getConnection());


    public AuthorDTO findAuthorById(int authorId) {
        return authorDAO.findAuthorById(authorId);
    }

    public void addAuthor(AuthorDTO authorDTO) {
        authorDAO.addAuthor(authorDTO);

    }

    public void deleteAuthor(AuthorDTO authorDTO) {
        authorDAO.deleteAuthor(authorDTO);
    }

    public void updateAuthor(AuthorDTO authorDTO) {
        authorDAO.updateAuthorName(authorDTO);

    }
}
