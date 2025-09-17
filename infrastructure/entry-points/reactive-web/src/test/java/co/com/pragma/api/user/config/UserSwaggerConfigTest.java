package co.com.pragma.api.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserSwaggerConfigTest {

    private final UserSwaggerConfig userSwaggerConfig = new UserSwaggerConfig();

    @Test
    void shouldCreateCustomOpenAPI() {
        OpenAPI openAPI = userSwaggerConfig.customOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isInstanceOf(Info.class);
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("API de Pragma");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("Documentación de la API para la autenticacion y gestión de usuarios");
    }
}