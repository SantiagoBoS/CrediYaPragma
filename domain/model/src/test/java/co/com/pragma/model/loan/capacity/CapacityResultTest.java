package co.com.pragma.model.loan.capacity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacityResultTest {

    @Test
    void shouldCreateCapacityResultWithBuilder() {
        LoanInstallment installment = new LoanInstallment(
                1,
                BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(950.0)
        );

        CapacityResult result = CapacityResult.builder()
                .decision("APPROVED")
                .paymentPlan(List.of(installment))
                .build();

        assertNotNull(result);
        assertEquals("APPROVED", result.getDecision());
        assertNotNull(result.getPaymentPlan());
        assertEquals(1, result.getPaymentPlan().size());
        assertEquals(BigDecimal.valueOf(1000.0), result.getPaymentPlan().get(0).getCapitalPayment());
    }

    @Test
    void shouldSetAndGetDecisionAndPaymentPlan() {
        LoanInstallment installment = new LoanInstallment(
                2,
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(25.0),
                BigDecimal.valueOf(475.0)
        );

        CapacityResult result = CapacityResult.builder()
                .decision("REJECTED")
                .paymentPlan(List.of(installment))
                .build();

        assertEquals("REJECTED", result.getDecision());
        assertNotNull(result.getPaymentPlan());
        assertEquals(1, result.getPaymentPlan().size());
        assertEquals(BigDecimal.valueOf(500.0), result.getPaymentPlan().get(0).getCapitalPayment());
    }
}
