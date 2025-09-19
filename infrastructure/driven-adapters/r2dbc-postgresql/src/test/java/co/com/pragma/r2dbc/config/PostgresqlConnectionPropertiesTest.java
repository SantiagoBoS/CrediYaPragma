package co.com.pragma.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresqlConnectionPropertiesTest {

    @Test
    void shouldCreatePropertiesCorrectly() {
        // Valida que el constructor y los getters funcionan correctamente
        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
                "localhost", 5432, "testdb", "public", "test-user", "secret"
        );

        assertThat(props.host()).isEqualTo("localhost");
        assertThat(props.port()).isEqualTo(5432);
        assertThat(props.database()).isEqualTo("testdb");
        assertThat(props.schema()).isEqualTo("public");
        assertThat(props.username()).isEqualTo("test-user");
        assertThat(props.password()).isEqualTo("secret");

        // Verificación de igualdad completa
        assertThat(props).isEqualTo(
                new PostgresqlConnectionProperties("localhost", 5432, "testdb", "public", "test-user", "secret")
        );
    }

    @Test
    void shouldAllowNullValues() {
        //Valida que se pueden crear propiedades con valores nulos
        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
                null, null, null, null, null, null
        );

        assertThat(props.host()).isNull();
        assertThat(props.port()).isNull();
        assertThat(props.database()).isNull();
        assertThat(props.schema()).isNull();
        assertThat(props.username()).isNull();
        assertThat(props.password()).isNull();
    }
}
