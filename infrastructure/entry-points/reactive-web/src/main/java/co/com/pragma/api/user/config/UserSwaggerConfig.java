package co.com.pragma.api.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSwaggerConfig {
    @Bean(name = "swaggerConfigUser")
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pragma")
                        .version("1.0")
                        .description("Documentación de la API de usuarios"));
    }
}
