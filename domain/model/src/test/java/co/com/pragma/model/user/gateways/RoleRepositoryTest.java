package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private Map<String, RoleType> roles;

    @BeforeEach
    void setUp() {
        roles = new HashMap<>();
        roles.put("ADMIN", new RoleType(1L, "ADMIN", "Administrador"));
        roles.put("CLIENT", new RoleType(2L, "CLIENT", "Cliente"));

        roleRepository = code -> Mono.justOrEmpty(roles.get(code));
    }

    @Test
    void shouldReturnRoleWhenCodeExists() {
        //Validar que el repositorio retorne el rol correcto cuando el código existe
        StepVerifier.create(roleRepository.findByCode("ADMIN"))
                .expectNextMatches(role -> role.getCode().equals("ADMIN") && role.getDescription().equals("Administrador"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenCodeDoesNotExist() {
        //Validar que el repositorio retorne vacío cuando el código no existe
        StepVerifier.create(roleRepository.findByCode("UNKNOWN"))
                .verifyComplete();
    }
}
