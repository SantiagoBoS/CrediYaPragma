package co.com.pragma.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresqlConnectionPropertiesTest {

    @Test
    void shouldCreatePropertiesCorrectly() {
        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
                "localhost", 5432, "crediya", "public", "postgres", "123456789"
        );

        assertThat(props.host()).isEqualTo("localhost");
        assertThat(props.port()).isEqualTo(5432);
        assertThat(props.database()).isEqualTo("crediya");
        assertThat(props.schema()).isEqualTo("public");
        assertThat(props.username()).isEqualTo("postgres");
        assertThat(props.password()).isEqualTo("123456789");
    }
}