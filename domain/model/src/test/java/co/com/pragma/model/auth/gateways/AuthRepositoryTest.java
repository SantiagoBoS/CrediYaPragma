package co.com.pragma.model.auth.gateways;

import co.com.pragma.model.auth.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

class AuthRepositoryTest {

    private AuthRepository authRepository;
    private Map<String, Auth> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();

        // Implementación fake de AuthRepository
        authRepository = new AuthRepository() {
            @Override
            public Mono<Auth> findByEmail(String email) {
                return Mono.justOrEmpty(storage.get(email));
            }

            @Override
            public Mono<Auth> save(Auth auth) {
                storage.put(auth.getEmail(), auth);
                return Mono.just(auth);
            }
        };
    }

    @Test
    void shouldSaveAndFindAuthSuccessfully() {
        // Crear y guardar un Auth
        Auth auth = new Auth();
        auth.setEmail("test@test.com");
        auth.setPassword("encodedPassword");
        auth.setRole("CLIENT");

        StepVerifier.create(authRepository.save(auth))
                .expectNext(auth)
                .verifyComplete();

        StepVerifier.create(authRepository.findByEmail("test@test.com"))
                .expectNextMatches(found ->
                        found.getEmail().equals("test@test.com") &&
                                found.getPassword().equals("encodedPassword") &&
                                found.getRole().equals("CLIENT"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenAuthNotFound() {
        // Intentar encontrar un Auth que no existe
        StepVerifier.create(authRepository.findByEmail("notfound@test.com"))
                .verifyComplete();
    }
}
