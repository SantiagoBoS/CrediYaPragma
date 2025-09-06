package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;

public class LoanMapper {
    private LoanMapper() {
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

    public static LoanDTO toDto(LoanRequest entity) {
        if (entity == null) {
            return null;
        }
        return LoanDTO.builder()
                .clientDocument(entity.getClientDocument())
                .amount(entity.getAmount())
                .termMonths(entity.getTermMonths())
                .loanType(entity.getLoanType())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
