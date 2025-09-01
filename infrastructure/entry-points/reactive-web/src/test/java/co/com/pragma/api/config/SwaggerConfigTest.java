package co.com.pragma.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void shouldCreateCustomOpenAPI() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isInstanceOf(Info.class);
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("API de Pragma");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
    }
}