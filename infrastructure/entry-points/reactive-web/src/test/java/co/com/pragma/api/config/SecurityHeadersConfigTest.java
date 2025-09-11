package co.com.pragma.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class SecurityHeadersConfigTest {
    private SecurityHeadersConfig securityHeadersConfig;
    private WebFilterChain filterChain;

    @BeforeEach
    void setUp() {
        securityHeadersConfig = new SecurityHeadersConfig();
        filterChain = mock(WebFilterChain.class);
        when(filterChain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void shouldAddSecurityHeaders() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        Mono<Void> result = securityHeadersConfig.filter(exchange, filterChain);
        StepVerifier.create(result).verifyComplete();
        MockServerHttpResponse response = exchange.getResponse();
        assert response.getHeaders().getFirst("Content-Security-Policy").equals("default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        assert response.getHeaders().getFirst("Strict-Transport-Security").equals("max-age=31536000;");
        assert response.getHeaders().getFirst("X-Content-Type-Options").equals("nosniff");
        assert response.getHeaders().getFirst("Server").equals("");
        assert response.getHeaders().getFirst("Cache-Control").equals("no-store");
        assert response.getHeaders().getFirst("Pragma").equals("no-cache");
        assert response.getHeaders().getFirst("Referrer-Policy").equals("strict-origin-when-cross-origin");
        verify(filterChain, times(1)).filter(exchange);
    }
}
