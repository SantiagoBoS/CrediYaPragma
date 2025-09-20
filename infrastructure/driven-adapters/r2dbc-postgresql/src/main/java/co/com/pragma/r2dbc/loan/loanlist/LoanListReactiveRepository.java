package co.com.pragma.r2dbc.loan.loanlist;

import co.com.pragma.r2dbc.loan.loanlist.entity.LoanListEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanListReactiveRepository extends ReactiveCrudRepository<LoanListEntity, String>, ReactiveQueryByExampleExecutor<LoanListEntity> {
    Flux<LoanListEntity> findByRequestStatusIn(List<String> statuses);
    Mono<Long> countByRequestStatusIn(List<String> statuses);
}