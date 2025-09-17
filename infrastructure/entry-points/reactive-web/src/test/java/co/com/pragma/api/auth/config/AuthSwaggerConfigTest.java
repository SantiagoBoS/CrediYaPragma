package co.com.pragma.api.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthSwaggerConfigTest {

    private final AuthSwaggerConfig authSwaggerConfig = new AuthSwaggerConfig();

    @Test
    void shouldCreateCustomOpenAPI() {
        OpenAPI openAPI = authSwaggerConfig.customOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isInstanceOf(Info.class);
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("API de Pragma");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("Documentación de la API de autenticación");
    }
}