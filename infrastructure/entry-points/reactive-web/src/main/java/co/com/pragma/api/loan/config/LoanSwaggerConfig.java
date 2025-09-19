package co.com.pragma.api.loan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LoanSwaggerConfig {
    @Bean(name = "swaggerConfigLoan")
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pragma")
                        .version("1.0")
                        .description("Documentación de la API de solicitudes de préstamo"));
    }
}
