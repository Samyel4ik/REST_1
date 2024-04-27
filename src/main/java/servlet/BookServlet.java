package servlet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BookDTO;
import service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    BookServiceImpl bookService = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BookDTO bookDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json");
        try {
            bookDTO = bookService.gettingABook(Integer.parseInt(id));
            if (bookDTO == null) {
                throw new NullPointerException("book is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        objectMapper.writeValue(resp.getWriter(), bookDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BookDTO authorDTO = objectMapper.readValue(req.getReader(), BookDTO.class);
            bookService.addBook(authorDTO);
            System.out.println("book added successfully");
            System.out.println(authorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BookDTO bookDTO = objectMapper.readValue(req.getReader(), BookDTO.class);
            bookService.updateBook(bookDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            bookDTO = objectMapper.readValue(req.getReader(), BookDTO.class);
            bookService.deleteBook(bookDTO);
            System.out.println(bookDTO + " удален!!!");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        objectMapper.writeValue(resp.getWriter(), bookDTO);
    }
}
