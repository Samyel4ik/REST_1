package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.Book;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String name;
    private List<Book> bookList;
}
