package co.com.pragma.model.user.gateways;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

class UserDocumentRepositoryTest {

    private UserDocumentRepository userDocumentRepository;
    private Set<String> users;
    private static final String VALID_TOKEN = "valid-token";

    @BeforeEach
    void setUp() {
        users = new HashSet<>();
        users.add("12345");
        users.add("67890");

        // Implementación fake del gateway para pruebas
        userDocumentRepository = new UserDocumentRepository() {
            @Override
            public Mono<Boolean> existsByDocument(String documentNumber) {
                return Mono.just(users.contains(documentNumber));
            }

            @Override
            public Mono<Boolean> existsByDocumentToken(String documentNumber, String token) {
                boolean exists = users.contains(documentNumber) && VALID_TOKEN.equals(token);
                return Mono.just(exists);
            }
        };
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        StepVerifier.create(userDocumentRepository.existsByDocument("12345"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        StepVerifier.create(userDocumentRepository.existsByDocument("99999"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenUserExistsAndTokenIsValid() {
        StepVerifier.create(userDocumentRepository.existsByDocumentToken("12345", VALID_TOKEN))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserExistsButTokenIsInvalid() {
        StepVerifier.create(userDocumentRepository.existsByDocumentToken("12345", "invalid-token"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistEvenWithValidToken() {
        StepVerifier.create(userDocumentRepository.existsByDocumentToken("99999", VALID_TOKEN))
                .expectNext(false)
                .verifyComplete();
    }
}
