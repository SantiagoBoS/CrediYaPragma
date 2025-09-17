package co.com.pragma.r2dbc.loanlist;

import co.com.pragma.r2dbc.loanlist.entity.LoanListEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface LoanListReactiveRepository extends ReactiveCrudRepository<LoanListEntity, String>, ReactiveQueryByExampleExecutor<LoanListEntity> {
    Flux<LoanListEntity> findByRequestStatusIn(List<String> statuses, Pageable pageable);

}