package co.com.pragma.r2dbc.user;

import co.com.pragma.r2dbc.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserReactiveRepositoryTest {

    private UserReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(UserReactiveRepository.class);
    }

    @Test
    void shouldFindByEmailAndDocumentNumberSuccessfully() {
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setEmail("user@example.com");
        user.setDocumentNumber("1234567890");

        when(repository.findByEmailAndDocumentNumber("user@example.com", "1234567890"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(repository.findByEmailAndDocumentNumber("user@example.com", "1234567890"))
                .expectNextMatches(found ->
                        found.getId().equals("1") &&
                                found.getEmail().equals("user@example.com") &&
                                found.getDocumentNumber().equals("1234567890")
                )
                .verifyComplete();

        verify(repository, times(1))
                .findByEmailAndDocumentNumber("user@example.com", "1234567890");
    }

    @Test
    void shouldReturnEmptyWhenEmailAndDocumentNumberNotFound() {
        when(repository.findByEmailAndDocumentNumber("notfound@example.com", "000000"))
                .thenReturn(Mono.empty());

        StepVerifier.create(repository.findByEmailAndDocumentNumber("notfound@example.com", "000000"))
                .verifyComplete();

        verify(repository, times(1))
                .findByEmailAndDocumentNumber("notfound@example.com", "000000");
    }

    @Test
    void shouldFindByDocumentNumberSuccessfully() {
        UserEntity user = new UserEntity();
        user.setId("2");
        user.setDocumentNumber("9876543210");

        when(repository.findByDocumentNumber("9876543210")).thenReturn(Mono.just(user));

        StepVerifier.create(repository.findByDocumentNumber("9876543210"))
                .expectNextMatches(found ->
                        found.getId().equals("2") &&
                                found.getDocumentNumber().equals("9876543210")
                )
                .verifyComplete();

        verify(repository, times(1))
                .findByDocumentNumber("9876543210");
    }

    @Test
    void shouldReturnEmptyWhenDocumentNumberNotFound() {
        when(repository.findByDocumentNumber("000000")).thenReturn(Mono.empty());

        StepVerifier.create(repository.findByDocumentNumber("000000"))
                .verifyComplete();

        verify(repository, times(1)).findByDocumentNumber("000000");
    }
}
