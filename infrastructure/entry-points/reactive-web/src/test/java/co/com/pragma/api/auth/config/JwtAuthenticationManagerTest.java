package co.com.pragma.api.auth.config;

import co.com.pragma.jwtprovider.JwtProviderAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationManagerTest {

    private JwtProviderAdapter jwtProvider;
    private JwtAuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProviderAdapter.class);
        authenticationManager = new JwtAuthenticationManager(jwtProvider);
    }

    @Test
    void shouldReturnAuthenticationWhenTokenIsValid() {
        //Verificamos que el token es válido y extraemos la información del usuario
        String fakeToken = "valid.token";

        when(jwtProvider.validateToken(fakeToken)).thenReturn(true);
        when(jwtProvider.getDocumentNumber(fakeToken)).thenReturn("hidden-doc");
        when(jwtProvider.getEmailFromToken(fakeToken)).thenReturn("hidden-email");
        when(jwtProvider.getRoleFromToken(fakeToken)).thenReturn("USER");
        Authentication inputAuth = new UsernamePasswordAuthenticationToken(null, fakeToken);
        Mono<Authentication> result = authenticationManager.authenticate(inputAuth);
        StepVerifier.create(result)
                .assertNext(auth -> {
                    assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
                    assertThat(auth.getAuthorities())
                            .extracting("authority")
                            .containsExactly("ROLE_USER");
                    // No validamos email ni documento por seguridad
                })
                .verifyComplete();

        verify(jwtProvider).validateToken(fakeToken);
        verify(jwtProvider).getDocumentNumber(fakeToken);
        verify(jwtProvider).getEmailFromToken(fakeToken);
        verify(jwtProvider).getRoleFromToken(fakeToken);
    }

    @Test
    void shouldReturnEmptyWhenTokenIsInvalid() {
        //Verificamos que el token es inválido y no se autentica
        String fakeToken = "invalid.token";
        when(jwtProvider.validateToken(fakeToken)).thenReturn(false);
        Authentication inputAuth = new UsernamePasswordAuthenticationToken(null, fakeToken);
        Mono<Authentication> result = authenticationManager.authenticate(inputAuth);
        StepVerifier.create(result).verifyComplete();

        verify(jwtProvider).validateToken(fakeToken);
        verify(jwtProvider, never()).getDocumentNumber(anyString());
        verify(jwtProvider, never()).getEmailFromToken(anyString());
        verify(jwtProvider, never()).getRoleFromToken(anyString());
    }
}

