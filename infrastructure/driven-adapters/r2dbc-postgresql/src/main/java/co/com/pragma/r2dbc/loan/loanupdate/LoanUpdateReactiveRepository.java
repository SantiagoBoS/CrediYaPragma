package co.com.pragma.r2dbc.loan.loanupdate;

import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanUpdateReactiveRepository extends ReactiveCrudRepository<LoanEntity, Long>, ReactiveQueryByExampleExecutor<LoanEntity> {
    Mono<LoanEntity> findByPublicId(UUID publicId);
}
