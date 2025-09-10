package co.com.pragma.r2dbc.loan.typeLoan;

import co.com.pragma.model.loan.LoanType;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.r2dbc.loan.entity.LoanTypeEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<LoanType, LoanTypeEntity, Long, LoanTypeReactiveRepository> implements LoanTypeRepository {

    private final LoanTypeReactiveRepository repository;

    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper, TransactionalOperator tsOperator) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
        this.repository = repository;
    }

    @Override
    public Flux<LoanType> findAll() {
        return repository.findAll().map(this::toEntity);
    }

    @Override
    public Mono<LoanType> findByCode(String code) {
        return repository.findByCode(code).map(this::toEntity);
    }
}