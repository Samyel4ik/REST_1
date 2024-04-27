package service.impl;

import JDBC_Conector.DB_Connector;
import dao.CustomerDAO;
import dto.CustomerDTO;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Setter
class CustomerServiceImplTest {

    private CustomerDAO customerDAOMock;

    private DB_Connector connectorMock;
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        customerDAOMock = mock(CustomerDAO.class);
        connectorMock = mock(DB_Connector.class);
        when(connectorMock.getConnection()).thenReturn(mock(Connection.class));

        customerService = new CustomerServiceImpl();
        customerService.setCustomerDAO(customerDAOMock);
    }

    @Test
    void testGetCustomer() {

        int customerId = 123;
        CustomerDTO expectedCustomer = new CustomerDTO();
        when(customerDAOMock.getCustomer(customerId)).thenReturn(expectedCustomer);
        CustomerDTO actualCustomer = customerService.getCustomer(customerId);
        verify(customerDAOMock).getCustomer(customerId);
        Assertions.assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void testDeleteCustomer() {

        CustomerDTO customerToDelete = new CustomerDTO();
        customerService.deleteCustomer(customerToDelete);
        verify(customerDAOMock).deleteCustomer(customerToDelete);
    }

    @Test
    void testAddCustomer() {

        CustomerDTO customerToAdd = new CustomerDTO();
        customerService.addCustomer(customerToAdd);
        verify(customerDAOMock).addCustomer(customerToAdd);
    }

    @Test
    void testUpdateCustomerName() {

        CustomerDTO customerToUpdate = new CustomerDTO();
        customerService.updateCustomerName(customerToUpdate);
        verify(customerDAOMock).updateCustomerName(customerToUpdate);
    }

}