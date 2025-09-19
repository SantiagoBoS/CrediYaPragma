package co.com.pragma.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostgreSQLConnectionPoolTest {

    @InjectMocks
    private PostgreSQLConnectionPool connectionPool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setear directamente los valores de las @Value
        connectionPool.host = "localhost";
        connectionPool.port = 5432;
        connectionPool.database = "dbName";
        connectionPool.schema = "schema";
        connectionPool.username = "username";
        connectionPool.password = "password";
    }

    @Test
    void getConnectionConfigSuccess() {
        //Validar que la configuración de la conexión no sea nula
        ConnectionPool pool = connectionPool.getConnectionConfig(null);
        assertNotNull(pool);
    }
}