package co.com.pragma.model.loan.loantype.gateways;

import co.com.pragma.model.loan.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findByCode(String code);
}
