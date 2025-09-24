package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class LoanCapacityCalculatorServiceTest {

    private LoanCapacityCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = mock(LoanCapacityCalculatorService.class);
    }

    @Test
    void shouldReturnCapacityResultSuccessfully() {
        LoanInstallment installment = new LoanInstallment(
                1,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(9000)
        );

        CapacityResult result = CapacityResult.builder()
                .decision("APPROVED")
                .paymentPlan(List.of(installment))
                .build();

        when(calculatorService.calculateCapacity(
                "user123",
                BigDecimal.valueOf(5000),
                10000.0,
                5.0,
                12
        )).thenReturn(Mono.just(result));

        Mono<CapacityResult> response = calculatorService.calculateCapacity(
                "user123",
                BigDecimal.valueOf(5000),
                10000.0,
                5.0,
                12
        );

        StepVerifier.create(response)
                .expectNextMatches(cap -> cap.getDecision().equals("APPROVED")
                        && cap.getPaymentPlan().size() == 1)
                .verifyComplete();

        verify(calculatorService, times(1)).calculateCapacity(
                "user123",
                BigDecimal.valueOf(5000),
                10000.0,
                5.0,
                12
        );
    }

    @Test
    void shouldReturnErrorWhenCalculationFails() {
        when(calculatorService.calculateCapacity(anyString(), any(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Calculation failed")));

        Mono<CapacityResult> response = calculatorService.calculateCapacity(
                "user123",
                BigDecimal.valueOf(5000),
                10000.0,
                5.0,
                12
        );

        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Calculation failed"))
                .verify();
    }
}
