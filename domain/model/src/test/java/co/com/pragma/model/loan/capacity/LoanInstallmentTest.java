package co.com.pragma.model.loan.capacity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanInstallmentTest {

    @Test
    void shouldCreateLoanInstallmentWithAllArgsConstructor() {
        LoanInstallment installment = new LoanInstallment(
                1,
                BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(9000.0)
        );

        assertNotNull(installment);
        assertEquals(1, installment.getMonth());
        assertEquals(BigDecimal.valueOf(1000.0), installment.getCapitalPayment());
        assertEquals(BigDecimal.valueOf(50.0), installment.getInterestPayment());
        assertEquals(BigDecimal.valueOf(9000.0), installment.getRemainingBalance());
    }

    @Test
    void shouldModifyValuesUsingSetters() {
        LoanInstallment installment = new LoanInstallment(
                1,
                BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(9000.0)
        );

        installment.setMonth(2);
        installment.setCapitalPayment(BigDecimal.valueOf(1100.0));
        installment.setInterestPayment(BigDecimal.valueOf(45.0));
        installment.setRemainingBalance(BigDecimal.valueOf(7900.0));

        assertEquals(2, installment.getMonth());
        assertEquals(BigDecimal.valueOf(1100.0), installment.getCapitalPayment());
        assertEquals(BigDecimal.valueOf(45.0), installment.getInterestPayment());
        assertEquals(BigDecimal.valueOf(7900.0), installment.getRemainingBalance());
    }
}
