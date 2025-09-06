package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
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
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(now)
                .build();

        LoanRequest result = LoanMapper.toMain(dto);

        assertNotNull(result);
        assertEquals("12345", result.getClientDocument());
        assertEquals(5000.0, result.getAmount());
        assertEquals(24, result.getTermMonths());
        assertEquals(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage(), result.getLoanType());
        assertEquals(RequestStatus.PENDING_REVIEW, result.getStatus());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(LoanMapper.toMain(null));
    }

    @Test
    void constructorShouldThrowException() {
        Exception exception = assertThrows(UnsupportedOperationException.class, LoanMapper::new);
        assertEquals(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage(), exception.getMessage());
    }
}
