package co.com.pragma.model.loan.gateways;


import co.com.pragma.model.loan.loantype.LoanType;
import co.com.pragma.model.loan.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

public class LoanTypeRepositoryTest {

    private LoanTypeRepository repository;
    private final Map<String, LoanType> database = new HashMap<>();

    private LoanType personalType;

    @BeforeEach
    void setUp() {
        personalType = LoanType.builder()
                .id(1L)
                .code("PERSONAL")
                .description("Préstamo personal")
                .build();

        // implementación fake en memoria
        repository = new LoanTypeRepository() {
            @Override
            public Mono<LoanType> findByCode(String code) {
                return Mono.justOrEmpty(database.get(code));
            }
        };

        database.clear();
        database.put(personalType.getCode(), personalType);
    }

    @Test
    void shouldFindLoanTypeByCode() {
        StepVerifier.create(repository.findByCode("PERSONAL"))
                .expectNextMatches(type ->
                        type.getId().equals(1L) &&
                                type.getCode().equals("PERSONAL") &&
                                type.getDescription().equals("Préstamo personal"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenCodeNotFound() {
        StepVerifier.create(repository.findByCode("CAR"))
                .verifyComplete();
    }
}
