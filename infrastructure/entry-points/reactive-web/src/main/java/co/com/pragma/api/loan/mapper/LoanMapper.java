package co.com.pragma.api.loan.mapper;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;

import java.time.LocalDateTime;

public class LoanMapper {
    public static LoanRequest toMain(LoanDTO dto) {
        if (dto == null) {
            return null;
        }

        return LoanRequest.builder()
                .clientDocument(dto.getClientDocument())
                .amount(dto.getAmount())
                .termMonths(dto.getTermMonths())
                .loanType(dto.getLoanType())
                .status(dto.getStatus() != null ? dto.getStatus() : RequestStatus.PENDING_REVIEW)
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .build();
    }
}
