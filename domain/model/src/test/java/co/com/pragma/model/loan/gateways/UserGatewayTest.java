package co.com.pragma.model.loan.gateways;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

class UserGatewayTest {

    private UserGateway userGateway;
    private Set<String> users;

    @BeforeEach
    void setUp() {
        users = new HashSet<>();
        users.add("12345");
        users.add("67890");
        userGateway = documentNumber -> Mono.just(users.contains(documentNumber));
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        StepVerifier.create(userGateway.existsByDocument("12345"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        StepVerifier.create(userGateway.existsByDocument("99999"))
                .expectNext(false)
                .verifyComplete();
    }
}
