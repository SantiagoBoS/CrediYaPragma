package co.com.pragma.model.loan;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LoanRequestTest {

    private LocalDateTime fixedDate;
    private LoanRequest baseLoan;

    @BeforeEach
    void setUp() {
        fixedDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        baseLoan = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(fixedDate)
                .build();
    }

    @Test
    void shouldCreateLoanWithBuilder() {
        assertEquals("12345", baseLoan.getClientDocument());
        assertEquals(1000.0, baseLoan.getAmount());
        assertEquals(12, baseLoan.getTermMonths());
        assertEquals(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage(), baseLoan.getLoanType());
        assertEquals(RequestStatus.PENDING_REVIEW, baseLoan.getStatus());
        assertEquals(fixedDate, baseLoan.getCreatedAt());
    }

    @Test
    void shouldModifyUsingSetters() {
        baseLoan.setLoanType(AppMessages.VALID_TYPE_LOAN_MORTGAGE.getMessage());
        baseLoan.setStatus(RequestStatus.APPROVED);
        baseLoan.setCreatedAt(fixedDate.plusDays(1));

        assertEquals("12345", baseLoan.getClientDocument());
        assertEquals(1000.0, baseLoan.getAmount());
        assertEquals(12, baseLoan.getTermMonths());
        assertEquals(AppMessages.VALID_TYPE_LOAN_MORTGAGE.getMessage(), baseLoan.getLoanType());
        assertEquals(RequestStatus.APPROVED, baseLoan.getStatus());
        assertEquals(fixedDate.plusDays(1), baseLoan.getCreatedAt());
    }

    @Test
    void shouldUseToBuilderToCreateModifiedCopy() {
        LoanRequest modified = baseLoan.toBuilder()
                .amount(3000.0)
                .status(RequestStatus.APPROVED)
                .build();

        assertEquals("12345", modified.getClientDocument());
        assertEquals(3000.0, modified.getAmount());
        assertEquals(12, modified.getTermMonths());
        assertEquals(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage(), modified.getLoanType());
        assertEquals(RequestStatus.APPROVED, modified.getStatus());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        LoanRequest copy = baseLoan.toBuilder().build();
        assertEquals(baseLoan, copy);
        assertEquals(baseLoan.hashCode(), copy.hashCode());
    }

    @Test
    void shouldGenerateNonNullToString() {
        String toString = baseLoan.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("12345"));
        assertTrue(toString.contains("1000.0"));
    }
}
