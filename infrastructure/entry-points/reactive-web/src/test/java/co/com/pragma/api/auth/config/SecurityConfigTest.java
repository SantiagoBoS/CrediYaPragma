package co.com.pragma.api.auth.config;

import co.com.pragma.api.auth.filter.JwtAuthenticationWebFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private JwtAuthenticationWebFilter jwtFilter;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        jwtFilter = mock(JwtAuthenticationWebFilter.class);
    }

    @Test
    void securityWebFilterChainMethodShouldReturnNonNull() {
        //Valida que el metodo securityWebFilterChain no retorne nulo
        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(new DummyServerHttpSecurity(), jwtFilter);
        assertNotNull(filterChain, "SecurityWebFilterChain should not be null");
    }

    //Se crea para que el dummy de ServerHttpSecurity funcione
    static class DummyServerHttpSecurity extends org.springframework.security.config.web.server.ServerHttpSecurity {
        // No hace nada, solo para que compile el test
        @Override
        public SecurityWebFilterChain build() {
            return mock(SecurityWebFilterChain.class);
        }
    }
}