package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.r2dbc.entity.LoanRequestEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;

public class LoanRequestRepositoryAdapter extends ReactiveAdapterOperations<LoanRequest, LoanRequestEntity, String, LoanRequestReactiveRepository> implements LoanRequestRepository {
    private final LoanRequestReactiveRepository repository;
    private final ObjectMapper mapper;

    public LoanRequestRepositoryAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanRequest.class));
        this.repository = repository;
        this.mapper = mapper;
    }
}
