package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.loanlist.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.loanlist.gateways.LoanListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class LoanListRepositoryTest {

    private LoanListRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LoanListRepository.class);
    }

    @Test
    void shouldReturnLoanListFlux() {
        LoanList loan = LoanList.builder()
                .amount(BigDecimal.valueOf(1000))
                .term(12)
                .email("user@example.com")
                .fullName("Juan Perez")
                .loanType("HOME")
                .interestRate(BigDecimal.valueOf(1.5))
                .requestStatus(RequestStatus.APPROVED)
                .baseSalary(BigDecimal.valueOf(3000))
                .monthlyPayment(BigDecimal.valueOf(100))
                .build();

        when(repository.findByStatuses(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.just(loan));

        StepVerifier.create(repository.findByStatuses(List.of(RequestStatus.APPROVED), 0, 10))
                .expectNextMatches(l -> l.getFullName().equals("Juan Perez"))
                .verifyComplete();
    }

    @Test
    void shouldReturnCountMono() {
        when(repository.countByStatuses(anyList()))
                .thenReturn(Mono.just(5L));

        StepVerifier.create(repository.countByStatuses(List.of(RequestStatus.APPROVED)))
                .expectNext(5L)
                .verifyComplete();
    }
}
