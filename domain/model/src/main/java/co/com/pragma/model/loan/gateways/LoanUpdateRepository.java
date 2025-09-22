package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanUpdateRepository {
    Mono<LoanRequest> updateStatus(String publicId, String newStatus, String advisorId);
}
