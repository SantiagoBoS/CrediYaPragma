package co.com.pragma.r2dbc.auth;

import co.com.pragma.r2dbc.auth.entity.AuthEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AuthReactiveRepositoryTest {

    private AuthReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(AuthReactiveRepository.class);
    }

    @Test
    void shouldFindByEmailSuccessfully() {
        AuthEntity auth = new AuthEntity();
        auth.setId(1L);
        auth.setEmail("user@example.com");
        auth.setPassword("encryptedPassword");

        // Mockeamos la llamada
        when(repository.findByEmail("user@example.com")).thenReturn(Mono.just(auth));

        StepVerifier.create(repository.findByEmail("user@example.com"))
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getEmail().equals("user@example.com") &&
                                found.getPassword().equals("encryptedPassword")
                )
                .verifyComplete();

        verify(repository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        // Mockeamos que no se encuentra
        when(repository.findByEmail("notfound@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(repository.findByEmail("notfound@example.com"))
                .verifyComplete();

        verify(repository, times(1)).findByEmail("notfound@example.com");
    }
}
