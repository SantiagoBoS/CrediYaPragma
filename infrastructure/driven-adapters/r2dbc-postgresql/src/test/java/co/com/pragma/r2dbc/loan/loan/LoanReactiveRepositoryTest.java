package co.com.pragma.r2dbc.loan.loan;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class LoanReactiveRepositoryTest {

    private LoanReactiveRepository loanReactiveRepository;

    @BeforeEach
    void setUp() {
        loanReactiveRepository = mock(LoanReactiveRepository.class);
    }

    @Test
    void shouldReturnEmptyWhenLoanNotFound() {
        when(loanReactiveRepository.findByClientDocumentAndStatus("99999", "PENDING"))
                .thenReturn(Mono.empty());

        Mono<LoanEntity> result = loanReactiveRepository.findByClientDocumentAndStatus("99999", "PENDING");

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(loanReactiveRepository, times(1))
                .findByClientDocumentAndStatus("99999", "PENDING");
    }
}
