package co.com.pragma.r2dbc.loan.loanupdate.customrepository;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanUpdateCustomRepository {
    Mono<LoanEntity> updateLoanStatus(UUID publicId, RequestStatus status, String advisorId);
}
