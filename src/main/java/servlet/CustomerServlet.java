package servlet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CustomerDTO;
import service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    CustomerServiceImpl customerService = new CustomerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        CustomerDTO customerDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json");
        try {
            customerDTO = customerService.getCustomer(Integer.parseInt(id));
            if (customerDTO == null) {
                throw new NullPointerException("author is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        objectMapper.writeValue(resp.getWriter(), customerDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.addCustomer(customerDTO);
            System.out.println(customerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.updateCustomerName(customerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerDTO customerDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.deleteCustomer(customerDTO);
            System.out.println(customerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        System.out.println("customer deleted");
        objectMapper.writeValue(resp.getWriter(), customerDTO);
    }
}

