package co.com.pragma.api.reports.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportsSwaggerConfigTest {

    private ReportsSwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new ReportsSwaggerConfig();
    }

    @Test
    void testCustomOpenAPINotNull() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        assertNotNull(openAPI, "El OpenAPI bean no debe ser null");

        Info info = openAPI.getInfo();
        assertNotNull(info, "El info del OpenAPI no debe ser null");
        assertEquals("API de Pragma", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertEquals("Documentación de la API de reportes de los préstamos", info.getDescription());
    }
}
