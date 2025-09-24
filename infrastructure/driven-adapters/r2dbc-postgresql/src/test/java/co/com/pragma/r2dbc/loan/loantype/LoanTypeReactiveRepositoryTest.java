package co.com.pragma.r2dbc.loan.loantype;

import co.com.pragma.r2dbc.loan.loantype.entity.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class LoanTypeReactiveRepositoryTest {

    private LoanTypeReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(LoanTypeReactiveRepository.class);
    }

    @Test
    void shouldFindByCodeSuccessfully() {
        LoanTypeEntity loanType = new LoanTypeEntity();
        loanType.setId(1L);
        loanType.setCode("HOME");
        loanType.setDescription("Home Loan");
        loanType.setInterestRate("12");
        loanType.setAutomaticValidation(true);

        // Mockeamos la llamada
        when(repository.findByCode("HOME")).thenReturn(Mono.just(loanType));

        StepVerifier.create(repository.findByCode("HOME"))
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getCode().equals("HOME") &&
                                found.getDescription().equals("Home Loan") &&
                                found.getInterestRate().equals("12") &&
                                found.getAutomaticValidation()
                )
                .verifyComplete();

        verify(repository, times(1)).findByCode("HOME");
    }

    @Test
    void shouldReturnEmptyWhenCodeNotFound() {
        // Mockeamos que no se encuentra
        when(repository.findByCode("NOT_FOUND")).thenReturn(Mono.empty());

        StepVerifier.create(repository.findByCode("NOT_FOUND"))
                .verifyComplete();

        verify(repository, times(1)).findByCode("NOT_FOUND");
    }
}
