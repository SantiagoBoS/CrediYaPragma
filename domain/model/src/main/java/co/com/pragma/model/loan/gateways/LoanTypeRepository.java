package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findByCode(String code);
}
