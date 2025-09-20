package co.com.pragma.model.loan;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.loanlist.LoanList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanListTest {

    private LoanList loan;

    @BeforeEach
    void setUp() {
        loan = new LoanList();
        loan.setAmount(BigDecimal.valueOf(1000.0));
        loan.setTerm(12);
        loan.setEmail("user@example.com");
        loan.setFullName("Juan Perez");
        loan.setLoanType("HOME");
        loan.setInterestRate(BigDecimal.valueOf(1.5));
        loan.setRequestStatus(RequestStatus.PENDING_REVIEW);
        loan.setBaseSalary(BigDecimal.valueOf(3000.0));
        loan.setMonthlyPayment(BigDecimal.valueOf(100.0));
    }

    @Test
    void shouldHaveCorrectValues() {
        assertEquals(BigDecimal.valueOf(1000.0), loan.getAmount());
        assertEquals(12, loan.getTerm());
        assertEquals("user@example.com", loan.getEmail());
        assertEquals("Juan Perez", loan.getFullName());
        assertEquals("HOME", loan.getLoanType());
        assertEquals(BigDecimal.valueOf(1.5), loan.getInterestRate());
        assertEquals(RequestStatus.PENDING_REVIEW, loan.getRequestStatus());
        assertEquals(BigDecimal.valueOf(3000.0), loan.getBaseSalary());
        assertEquals(BigDecimal.valueOf(100.0), loan.getMonthlyPayment());
    }

    @Test
    void shouldAllowUpdatingValues() {
        loan.setFullName("Maria Lopez");
        loan.setAmount(BigDecimal.valueOf(2000.0));
        loan.setRequestStatus(RequestStatus.APPROVED);

        assertEquals("Maria Lopez", loan.getFullName());
        assertEquals(BigDecimal.valueOf(2000.0), loan.getAmount());
        assertEquals(RequestStatus.APPROVED, loan.getRequestStatus());
    }
}