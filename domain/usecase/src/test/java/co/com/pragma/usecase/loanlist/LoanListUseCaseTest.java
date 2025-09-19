package co.com.pragma.usecase.loanlist;

import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanListRepository;
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

class LoanListUseCaseTest {

    private LoanListRepository repository;
    private LoanListUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LoanListRepository.class);
        useCase = new LoanListUseCase(repository);
    }

    @Test
    void shouldReturnLoanList() {
        LoanList loan = LoanList.builder()
                .amount(BigDecimal.valueOf(1000))
                .term(12)
                .email("test@mail.com")
                .fullName("Juan Perez")
                .loanType("HOME")
                .interestRate(BigDecimal.valueOf(1.5))
                .requestStatus(RequestStatus.PENDING_REVIEW)
                .baseSalary(BigDecimal.valueOf(3000))
                .monthlyPayment(BigDecimal.valueOf(100))
                .build();

        when(repository.findByStatuses(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.just(loan));

        StepVerifier.create(useCase.list(List.of(RequestStatus.PENDING_REVIEW), 0, 10))
                .expectNextMatches(l -> l.getFullName().equals("Juan Perez"))
                .verifyComplete();
    }

    @Test
    void shouldReturnLoanCount() {
        when(repository.countByStatuses(anyList()))
                .thenReturn(Mono.just(3L));

        StepVerifier.create(useCase.countByStatuses(List.of(RequestStatus.PENDING_REVIEW)))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void shouldUseDefaultStatusesWhenEmptyList() {
        LoanList loan = LoanList.builder()
                .fullName("Default Status Loan")
                .build();

        when(repository.findByStatuses(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.just(loan));

        StepVerifier.create(useCase.list(List.of(), 0, 5))
                .expectNextMatches(l -> l.getFullName().equals("Default Status Loan"))
                .verifyComplete();
    }

    @Test
    void shouldUseDefaultStatusesForCountWhenNull() {
        when(repository.countByStatuses(anyList()))
                .thenReturn(Mono.just(7L));

        StepVerifier.create(useCase.countByStatuses(null))
                .expectNext(7L)
                .verifyComplete();
    }
}
