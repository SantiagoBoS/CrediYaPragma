package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    @Test
    void shouldCreateCorsWebFilterBean() {
        String allowedOrigins = "http://localhost:3000,http://example.com";
        CorsConfig corsConfig = new CorsConfig();
        CorsWebFilter filter = corsConfig.corsWebFilter(allowedOrigins);
        assertThat(filter).isNotNull();
    }
}
