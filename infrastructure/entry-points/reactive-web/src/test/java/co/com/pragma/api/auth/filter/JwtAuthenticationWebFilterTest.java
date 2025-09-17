package co.com.pragma.api.auth.filter;


import co.com.pragma.api.auth.config.JwtAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class JwtAuthenticationWebFilterTest {

    private JwtAuthenticationManager authenticationManager;
    private JwtAuthenticationWebFilter filter;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(JwtAuthenticationManager.class);
        filter = new JwtAuthenticationWebFilter(authenticationManager);
        chain = mock(WebFilterChain.class);
    }

    @Test
    void shouldPassThroughWhenNoAuthorizationHeader() {
        //Valida que pase sin token cuando no hay header
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        when(chain.filter(exchange)).thenReturn(Mono.empty());
        Mono<Void> result = filter.filter(exchange, chain);
        StepVerifier.create(result).verifyComplete();
        verify(chain, times(1)).filter(exchange);
        verifyNoInteractions(authenticationManager);
    }

    @Test
    void shouldAuthenticateWhenBearerTokenPresent() {
        //Verifica que autentique cuando hay token para el header
        String token = "fake-token";
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(auth));
        when(chain.filter(exchange)).thenReturn(Mono.empty());
        Mono<Void> result = filter.filter(exchange, chain);
        StepVerifier.create(result).verifyComplete();
        verify(authenticationManager, times(1)).authenticate(any());
        verify(chain, atLeastOnce()).filter(exchange);
    }
}