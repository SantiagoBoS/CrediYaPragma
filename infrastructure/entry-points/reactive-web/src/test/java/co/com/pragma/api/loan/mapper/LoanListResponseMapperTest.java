package co.com.pragma.api.loan.mapper;

import co.com.pragma.api.loan.dto.LoanListResponseDTO;
import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanListResponseMapperTest {

    @Test
    void testToDtoMapsAllFields() {
        LoanList domain = LoanList.builder()
                .amount(BigDecimal.valueOf(10000.0))
                .term(24)
                .email("test@example.com")
                .fullName("John Doe")
                .loanType("HOME")
                .interestRate(BigDecimal.valueOf(5.5))
                .requestStatus(RequestStatus.APPROVED)
                .monthlyPayment(BigDecimal.valueOf(450.75))
                .build();

        LoanListResponseDTO dto = LoanListResponseMapper.toDto(domain);

        assertNotNull(dto);
        assertEquals(domain.getAmount(), dto.getAmount());
        assertEquals(domain.getTerm(), dto.getTerm());
        assertEquals(domain.getEmail(), dto.getEmail());
        assertEquals(domain.getFullName(), dto.getFullName());
        assertEquals(domain.getLoanType(), dto.getLoanType());
        assertEquals(domain.getInterestRate(), dto.getInterestRate());
        assertEquals(domain.getRequestStatus().name(), dto.getRequestStatus());
        assertEquals(domain.getMonthlyPayment(), dto.getMonthlyPayment());
    }

    @Test
    void testToDtoHandlesNullValues() {
        LoanList domain = LoanList.builder()
                .amount(null)
                .term(null)
                .email(null)
                .fullName(null)
                .loanType(null)
                .interestRate(null)
                .requestStatus(RequestStatus.REJECTED)
                .monthlyPayment(null)
                .build();

        LoanListResponseDTO dto = LoanListResponseMapper.toDto(domain);

        assertNotNull(dto);
        assertNull(dto.getAmount());
        assertNull(dto.getTerm());
        assertNull(dto.getEmail());
        assertNull(dto.getFullName());
        assertNull(dto.getLoanType());
        assertNull(dto.getInterestRate());
        assertEquals("REJECTED", dto.getRequestStatus());
        assertNull(dto.getMonthlyPayment());
    }
}
