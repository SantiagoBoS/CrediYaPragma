package co.com.pragma.model.loan.loanupdate.gateways;

import co.com.pragma.model.loan.loanrequest.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanUpdateRepository {
    Mono<LoanRequest> updateStatus(String publicId, String newStatus, String advisorId);
}
