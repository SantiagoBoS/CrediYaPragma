package co.com.pragma.r2dbc.loan.loancapacity;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loanupdate.LoanUpdateReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class LoanCapacityCalculatorAdapterTest {

    private LoanUpdateReactiveRepository loanRepository;
    private LoanCapacityCalculatorAdapter adapter;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanUpdateReactiveRepository.class);
        adapter = new LoanCapacityCalculatorAdapter(loanRepository);
    }

    @Test
    void shouldReturnApprovedCapacityResultWhenUserHasNoActiveLoans() {
        String userId = "12345";
        BigDecimal income = BigDecimal.valueOf(10000);
        Double loanAmount = 2000.0;
        Double interestRate = 12.0;
        Integer termMonths = 12;

        // Simulamos que el usuario no tiene préstamos activos
        when(loanRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(adapter.calculateCapacity(userId, income, loanAmount, interestRate, termMonths))
                .expectNextMatches(result ->
                        result.getDecision().equals(RequestStatus.APPROVED.toString()) &&
                                result.getPaymentPlan() != null &&
                                !result.getPaymentPlan().isEmpty()
                )
                .verifyComplete();

        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnRejectedCapacityResultWhenNewLoanExceedsCapacity() {
        String userId = "12345";
        BigDecimal income = BigDecimal.valueOf(1000); // muy bajo
        Double loanAmount = 5000.0; // demasiado alto
        Double interestRate = 10.0;
        Integer termMonths = 12;

        // Usuario sin préstamos activos
        when(loanRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(adapter.calculateCapacity(userId, income, loanAmount, interestRate, termMonths))
                .expectNextMatches(result -> result.getDecision().equals(RequestStatus.REJECTED.toString()))
                .verifyComplete();
    }
}
