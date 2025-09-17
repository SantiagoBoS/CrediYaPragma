package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.auth.gateways.TokenProvider;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    private AuthRepository authRepository;
    private TokenProvider tokenProvider;
    private PasswordEncoderService passwordEncoder;
    private AuthUseCase authUseCase;
    private Auth auth;

    @BeforeEach
    void setUp() {
        authRepository = mock(AuthRepository.class);
        tokenProvider = mock(TokenProvider.class);
        passwordEncoder = mock(PasswordEncoderService.class);
        authUseCase = new AuthUseCase(authRepository, tokenProvider, passwordEncoder);

        auth = Auth.builder()
                .email("test@mail.com")
                .password("encodedPass")
                .role("CLIENT")
                .build();
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() {
        //Valida que el login sea exitoso y retorne un token JWT
        when(authRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(auth));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt-token");

        StepVerifier.create(authUseCase.login("test@mail.com", "rawPass"))
                .expectNext("jwt-token")
                .verifyComplete();

        verify(authRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("rawPass", "encodedPass");
        verify(tokenProvider).generateToken(auth);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        //Valida que se lance una excepción cuando el usuario no es encontrado
        when(authRepository.findByEmail("notfound@mail.com")).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login("notfound@mail.com", "pass"))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    assert error.getMessage().equals(AppMessages.USER_NOT_FOUND.getMessage());
                })
                .verify();

        verify(authRepository).findByEmail("notfound@mail.com");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(tokenProvider, never()).generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        //Valida que se lance una excepción cuando la contraseña es inválida
        when(authRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(auth));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        StepVerifier.create(authUseCase.login("test@mail.com", "wrongPass"))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    assert error.getMessage().equals(AppMessages.INVALID_CREDENTIALS.getMessage());
                })
                .verify();

        verify(authRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("wrongPass", "encodedPass");
        verify(tokenProvider, never()).generateToken(any());
    }
}