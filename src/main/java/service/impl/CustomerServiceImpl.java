package service.impl;

import JDBC_Conector.DB_Connector;
import dao.CustomerDAO;
import dto.CustomerDTO;
import lombok.Getter;
import lombok.Setter;
import service.CustomerService;


@Setter
@Getter
public class CustomerServiceImpl implements CustomerService {
    DB_Connector connector = new DB_Connector();
    CustomerDAO customerDAO = new CustomerDAO(connector.getConnection());


    @Override
    public CustomerDTO getCustomer(int id) {
        return customerDAO.getCustomer(id);
    }

    @Override
    public void deleteCustomer(CustomerDTO customerDTO) {
        customerDAO.deleteCustomer(customerDTO);
    }

    @Override
    public void addCustomer(CustomerDTO customerDTO) {
        customerDAO.addCustomer(customerDTO);
    }

    @Override
    public void updateCustomerName(CustomerDTO customerDTO) {
        customerDAO.updateCustomerName(customerDTO);
    }
}
