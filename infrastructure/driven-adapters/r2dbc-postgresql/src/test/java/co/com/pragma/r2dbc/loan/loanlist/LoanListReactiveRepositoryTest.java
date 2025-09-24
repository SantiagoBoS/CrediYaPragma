package co.com.pragma.r2dbc.loan.loanlist;

import co.com.pragma.r2dbc.loan.loanlist.entity.LoanListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class LoanListReactiveRepositoryTest {

    private LoanListReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(LoanListReactiveRepository.class);
    }

    @Test
    void shouldReturnLoansByStatus() {
        LoanListEntity loan1 = new LoanListEntity();
        loan1.setId("1");
        loan1.setRequestStatus("APPROVED");

        LoanListEntity loan2 = new LoanListEntity();
        loan2.setId("2");
        loan2.setRequestStatus("REJECTED");

        when(repository.findByRequestStatusIn(List.of("APPROVED", "REJECTED")))
                .thenReturn(Flux.just(loan1, loan2));

        StepVerifier.create(repository.findByRequestStatusIn(List.of("APPROVED", "REJECTED")))
                .expectNext(loan1)
                .expectNext(loan2)
                .verifyComplete();

        verify(repository, times(1)).findByRequestStatusIn(List.of("APPROVED", "REJECTED"));
    }

    @Test
    void shouldReturnCountByStatus() {
        when(repository.countByRequestStatusIn(List.of("APPROVED", "REJECTED")))
                .thenReturn(Mono.just(2L));

        StepVerifier.create(repository.countByRequestStatusIn(List.of("APPROVED", "REJECTED")))
                .expectNext(2L)
                .verifyComplete();

        verify(repository, times(1)).countByRequestStatusIn(List.of("APPROVED", "REJECTED"));
    }
}
