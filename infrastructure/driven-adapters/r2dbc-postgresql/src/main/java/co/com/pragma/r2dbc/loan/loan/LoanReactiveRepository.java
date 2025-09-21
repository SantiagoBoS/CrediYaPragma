package co.com.pragma.r2dbc.loan.loan;

import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanReactiveRepository extends ReactiveCrudRepository<LoanEntity, String>, ReactiveQueryByExampleExecutor<LoanEntity> {
    Mono<LoanEntity> findByClientDocumentAndStatus(String clientDocument, String status);
}