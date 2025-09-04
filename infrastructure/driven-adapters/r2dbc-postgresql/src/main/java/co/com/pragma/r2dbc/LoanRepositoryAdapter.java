package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.r2dbc.entity.LoanEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanRepositoryAdapter extends ReactiveAdapterOperations<LoanRequest, LoanEntity, String, LoanReactiveRepository> implements LoanRepository {
    private final LoanReactiveRepository repository;
    private final ObjectMapper mapper;

    public LoanRepositoryAdapter(LoanReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanRequest.class));
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LoanRequest> findByClientDocumentAndStatus(String clientDocument, String status) {
        return repository.findByClientDocumentAndStatus(clientDocument, status).map(this::toEntity);
    }

    @Override
    public Mono<LoanRequest> save(LoanRequest loanRequest) {
        return super.save(loanRequest)
            .onErrorResume(throwable -> {
                if (throwable.getMessage() != null && throwable.getMessage().contains("duplicate")) {
                    return Mono.error(new BusinessException(AppMessages.DUPLICATE_APPLICATION));
                }
                return Mono.error(new BusinessException(AppMessages.INTERNAL_ERROR));
            });
    }

    @Override
    public Flux<LoanRequest> findAll() {
        return repository.findAll().map(this::toEntity);
    }
}
