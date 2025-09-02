package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.Loan;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.r2dbc.entity.LoanRequestEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;

public class LoanRequestReactiveRequestRepositoryAdapter extends ReactiveAdapterOperations<Loan, LoanRequestEntity, String, LoanRequestReactiveRepository> implements LoanRequestRepository {
    private final LoanRequestReactiveRepository repository;
    private final ObjectMapper mapper;

    public LoanRequestReactiveRequestRepositoryAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Loan.class));
        this.repository = repository;
        this.mapper = mapper;
    }
}
