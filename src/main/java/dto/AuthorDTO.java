package dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Book;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
public class AuthorDTO {
    private int id;
    private String name;
    private List<Book> bookList;
}
