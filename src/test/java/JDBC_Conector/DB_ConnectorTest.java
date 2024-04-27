package JDBC_Conector;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.mockito.Mockito.when;


class DB_ConnectorTest {


    @Test
    void testGetConnection() {
        Connection mockConnection = Mockito.mock(Connection.class);
        DB_Connector dbConnection = Mockito.spy(new DB_Connector());

        when(dbConnection.getConnection()).thenReturn(mockConnection);

        Connection connection = dbConnection.getConnection();

        Assertions.assertNotNull(connection);
    }
}