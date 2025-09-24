package co.com.pragma.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PostgreSQLConnectionPool {
    /* Change these values for your project */
    public static final Integer INITIAL_SIZE = 12;
    public static final Integer MAX_SIZE = 15;
    public static final Integer MAX_IDLE_TIME = 30;

    @Value("${DB_HOST}")
    String host;

    @Value("${DB_PORT}")
    Integer port;

    @Value("${DB_NAME}")
    String database;

    @Value("${DB_SCHEMA}")
    String schema;

    @Value("${DB_USERNAME}")
    String username;

    @Value("${DB_PASSWORD}")
    String password;

	@Bean
	public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .schema(schema)
                .username(username)
                .password(password)
                .build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

		return new ConnectionPool(poolConfiguration);
	}
}