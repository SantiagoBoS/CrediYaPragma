package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRepository {
    Mono<LoanRequest> save(LoanRequest loanRequest);
    Flux<LoanRequest> findAll();
}
