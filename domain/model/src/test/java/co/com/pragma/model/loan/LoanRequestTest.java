package co.com.pragma.model.loan;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LoanRequestTest {

    @Test
    void shouldCreateLoanRequestWithAllArgsConstructor() {
        //Valida la creación de un objeto LoanRequest usando el constructor con todos los argumentos
        LocalDateTime now = LocalDateTime.now();
        LoanRequest loanRequest = new LoanRequest("12345", 1000.0, 12, "PERSONAL", RequestStatus.PENDING_REVIEW, now);

        assertEquals("12345", loanRequest.getClientDocument());
        assertEquals(1000.0, loanRequest.getAmount());
        assertEquals(12, loanRequest.getTermMonths());
        assertEquals("PERSONAL", loanRequest.getLoanType());
        assertEquals(RequestStatus.PENDING_REVIEW, loanRequest.getStatus());
        assertEquals(now, loanRequest.getCreatedAt());
    }

    @Test
    void shouldCreateLoanRequestWithBuilder() {
        //Valida la creación de un objeto LoanRequest
        LocalDateTime now = LocalDateTime.now();
        LoanRequest loanRequest = LoanRequest.builder()
                .clientDocument("67890")
                .amount(2000.0)
                .termMonths(24)
                .loanType("MORTGAGE")
                .status(RequestStatus.APPROVED)
                .createdAt(now)
                .build();

        assertEquals("67890", loanRequest.getClientDocument());
        assertEquals(2000.0, loanRequest.getAmount());
        assertEquals(24, loanRequest.getTermMonths());
        assertEquals("MORTGAGE", loanRequest.getLoanType());
        assertEquals(RequestStatus.APPROVED, loanRequest.getStatus());
        assertEquals(now, loanRequest.getCreatedAt());
    }

    @Test
    void shouldModifyFieldsWithSetters() {
        //Valida la modificación de los campos
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setClientDocument("11111");
        loanRequest.setAmount(1500.0);
        loanRequest.setTermMonths(36);
        loanRequest.setLoanType("CAR");
        loanRequest.setStatus(RequestStatus.REJECTED);
        LocalDateTime now = LocalDateTime.now();
        loanRequest.setCreatedAt(now);

        assertEquals("11111", loanRequest.getClientDocument());
        assertEquals(1500.0, loanRequest.getAmount());
        assertEquals(36, loanRequest.getTermMonths());
        assertEquals("CAR", loanRequest.getLoanType());
        assertEquals(RequestStatus.REJECTED, loanRequest.getStatus());
        assertEquals(now, loanRequest.getCreatedAt());
    }

    @Test
    void shouldUseToBuilderToModifyLoanRequest() {
        //Valida la creación de un nuevo objeto LoanRequest a partir de otro existente
        LoanRequest original = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        LoanRequest modified = original.toBuilder()
                .amount(2000.0)
                .status(RequestStatus.APPROVED)
                .build();

        assertEquals("12345", modified.getClientDocument());
        assertEquals(2000.0, modified.getAmount());
        assertEquals(12, modified.getTermMonths());
        assertEquals("PERSONAL", modified.getLoanType());
        assertEquals(RequestStatus.APPROVED, modified.getStatus());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        //Valida que dos objetos LoanRequest con los mismos valores sean iguales y tengan el mismo hash code
        LocalDateTime now = LocalDateTime.now();
        LoanRequest loan1 = new LoanRequest("12345", 1000.0, 12, "PERSONAL", RequestStatus.PENDING_REVIEW, now);
        LoanRequest loan2 = new LoanRequest("12345", 1000.0, 12, "PERSONAL", RequestStatus.PENDING_REVIEW, now);
        assertEquals(loan1, loan2);
        assertEquals(loan1.hashCode(), loan2.hashCode());
    }

    @Test
    void shouldGenerateNonNullToString() {
        //Valida que el toString genera una representación no nula del objeto
        LoanRequest loan = LoanRequest.builder()
                .clientDocument("99999")
                .amount(5000.0)
                .termMonths(48)
                .loanType("CAR")
                .status(RequestStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        assertNotNull(loan.toString());
        assertTrue(loan.toString().contains("99999"));
        assertTrue(loan.toString().contains("5000.0"));
    }
}
