package co.com.pragma.api.loan.mapper;

import co.com.pragma.api.loan.dto.LoanListResponseDTO;
import co.com.pragma.model.loan.LoanList;

public class LoanListResponseMapper {
    public static LoanListResponseDTO toDto(LoanList domain) {
        return LoanListResponseDTO.builder()
                .amount(domain.getAmount())
                .term(domain.getTerm())
                .email(domain.getEmail())
                .fullName(domain.getFullName())
                .loanType(domain.getLoanType())
                .interestRate(domain.getInterestRate())
                .requestStatus(domain.getRequestStatus().name())
                .monthlyPayment(domain.getMonthlyPayment())
                .build();
    }
}
