package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.r2dbc.entity.LoanRequestEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanRequestRepositoryAdapter extends ReactiveAdapterOperations<LoanRequest, LoanRequestEntity, String, LoanRequestReactiveRepository> implements LoanRequestRepository {
    private final LoanRequestReactiveRepository repository;
    private final ObjectMapper mapper;

    public LoanRequestRepositoryAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper) {
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
                String msg = throwable.getMessage();
                if (msg != null && msg.contains("duplicate")) {
                    return Mono.error(new BusinessException(AppMessages.DUPLICATE_APPLICATION));
                }
                return Mono.error(new BusinessException(AppMessages.INTERNAL_ERROR));
            });
    }

    @Override
    public Flux<LoanRequest> findAll() {
        return repository.findAll()
            .map(entity -> LoanRequest.builder()
                .clientDocument(entity.getClientDocument())
                .amount(entity.getAmount())
                .termMonths(entity.getTermMonths())
                .loanType(entity.getLoanType())
                .status(RequestStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .build());
    }
}
