package co.com.pragma.api.loan.mapper;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {

    @Test
    void shouldMapDtoToLoanRequest() {
        LocalDateTime now = LocalDateTime.now();

        LoanDTO dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(5000.0)
                .termMonths(24)
                .loanType("PERSONAL")
                .status(RequestStatus.APPROVED)
                .createdAt(now)
                .build();

        LoanRequest result = LoanMapper.toMain(dto);

        assertNotNull(result);
        assertEquals("12345", result.getClientDocument());
        assertEquals(5000.0, result.getAmount());
        assertEquals(24, result.getTermMonths());
        assertEquals("PERSONAL", result.getLoanType());
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(LoanMapper.toMain(null));
    }

    @Test
    void shouldAssignDefaultStatusWhenDtoStatusIsNull() {
        LoanDTO dto = LoanDTO.builder()
                .clientDocument("99999")
                .amount(10000.0)
                .termMonths(12)
                .loanType("MORTGAGE")
                .status(null) // no se envía status
                .createdAt(LocalDateTime.now())
                .build();

        LoanRequest result = LoanMapper.toMain(dto);

        assertNotNull(result);
        assertEquals(RequestStatus.PENDING_REVIEW, result.getStatus());
    }

    @Test
    void shouldAssignCurrentDateWhenCreatedAtIsNull() {
        LoanDTO dto = LoanDTO.builder()
                .clientDocument("88888")
                .amount(7500.0)
                .termMonths(36)
                .loanType("CAR")
                .status(RequestStatus.REJECTED)
                .createdAt(null)
                .build();

        LoanRequest result = LoanMapper.toMain(dto);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertEquals("88888", result.getClientDocument());
        assertEquals(RequestStatus.REJECTED, result.getStatus());
    }
}
