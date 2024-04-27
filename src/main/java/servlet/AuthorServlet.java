package servlet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AuthorDTO;
import service.impl.AuthorServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/authors")
public class AuthorServlet extends HttpServlet {
    AuthorServiceImpl authorService = new AuthorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        AuthorDTO authorDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json");
        try {
            authorDTO = authorService.findAuthorById(Integer.parseInt(id));
            if (authorDTO == null) {
                throw new NullPointerException("author is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        objectMapper.writeValue(resp.getWriter(), authorDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AuthorDTO authorDTO = objectMapper.readValue(req.getReader(), AuthorDTO.class);
            authorService.addAuthor(authorDTO);
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
            AuthorDTO authorDTO = objectMapper.readValue(req.getReader(), AuthorDTO.class);
            authorService.updateAuthor(authorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            authorDTO = objectMapper.readValue(req.getReader(), AuthorDTO.class);
            authorService.deleteAuthor(authorDTO);
            System.out.println(authorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        System.out.println("author deleted");
        objectMapper.writeValue(resp.getWriter(), authorDTO);
    }
}
