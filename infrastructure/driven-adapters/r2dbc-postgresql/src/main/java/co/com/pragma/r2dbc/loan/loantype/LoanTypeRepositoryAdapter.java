package co.com.pragma.r2dbc.loan.loantype;

import co.com.pragma.model.loan.LoanType;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.loan.loantype.entity.LoanTypeEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<LoanType, LoanTypeEntity, Long, LoanTypeReactiveRepository> implements LoanTypeRepository {

    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, LoanType.LoanTypeBuilder.class)
                .id(d.getId())
                .code(d.getCode())
                .description(d.getDescription())
                .interestRate(Double.valueOf(d.getInterestRate()))
                .automaticValidation(d.getAutomaticValidation())
                .build());
        this.repository = repository;
    }

    @Override
    public Mono<LoanType> findByCode(String code) {
        return repository.findByCode(code).map(this::toEntity);
    }
}