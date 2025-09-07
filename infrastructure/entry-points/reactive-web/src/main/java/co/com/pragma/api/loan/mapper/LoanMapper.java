package co.com.pragma.api.loan.mapper;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;

public class LoanMapper {
    LoanMapper() {
        throw new UnsupportedOperationException(String.valueOf(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage()));
    }

    public static LoanRequest toMain(LoanDTO dto) {
        if (dto == null) {
            return null;
        }

        return LoanRequest.builder()
                .clientDocument(dto.getClientDocument())
                .amount(dto.getAmount())
                .termMonths(dto.getTermMonths())
                .loanType(dto.getLoanType())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
