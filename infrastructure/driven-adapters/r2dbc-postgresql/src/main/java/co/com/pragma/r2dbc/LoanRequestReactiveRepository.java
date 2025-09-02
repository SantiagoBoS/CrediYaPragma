package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoanRequestEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanRequestReactiveRepository extends ReactiveCrudRepository<LoanRequestEntity, String>, ReactiveQueryByExampleExecutor<LoanRequestEntity> {

}