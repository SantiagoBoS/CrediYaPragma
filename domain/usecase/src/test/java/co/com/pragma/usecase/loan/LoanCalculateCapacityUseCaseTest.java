package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.gateways.LoanCapacityCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class LoanCalculateCapacityUseCaseTest {

    private LoanCapacityCalculatorService loanCapacityCalculatorService;
    private LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;

    @BeforeEach
    void setUp() {
        loanCapacityCalculatorService = Mockito.mock(LoanCapacityCalculatorService.class);
        loanCalculateCapacityUseCase = new LoanCalculateCapacityUseCase(loanCapacityCalculatorService);
    }

    @Test
    void shouldReturnCapacityResult() {
        CapacityResult mockResult = mock(CapacityResult.class);
        when(loanCapacityCalculatorService.calculateCapacity(
                anyString(),
                any(BigDecimal.class),
                anyDouble(),
                anyDouble(),
                anyInt()
        )).thenReturn(Mono.just(mockResult));

        StepVerifier.create(
                        loanCalculateCapacityUseCase.execute(
                                "user123",
                                BigDecimal.valueOf(5000),
                                10000.0,
                                1.5,
                                12
                        )
                ).expectNext(mockResult)
                .verifyComplete();

        verify(loanCapacityCalculatorService, times(1))
                .calculateCapacity(
                        anyString(),
                        any(BigDecimal.class),
                        anyDouble(),
                        anyDouble(),
                        anyInt()
                );
    }


    @Test
    void shouldReturnEmptyWhenServiceReturnsEmpty() {
        when(loanCapacityCalculatorService.calculateCapacity(
                anyString(),
                any(BigDecimal.class),
                anyDouble(),
                anyDouble(),
                anyInt()
        )).thenReturn(Mono.empty());

        StepVerifier.create(
                loanCalculateCapacityUseCase.execute(
                        "user123",
                        BigDecimal.valueOf(5000),
                        10000.0,
                        1.5,
                        12
                )
        ).verifyComplete();

        verify(loanCapacityCalculatorService, times(1))
                .calculateCapacity(
                        anyString(),
                        any(BigDecimal.class),
                        anyDouble(),
                        anyDouble(),
                        anyInt()
                );
    }
}
