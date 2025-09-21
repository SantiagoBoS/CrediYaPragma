package co.com.pragma.r2dbc.loan.loanupdate;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.model.loan.loanupdate.gateways.LoanUpdateRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.loan.loan.LoanReactiveRepository;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanUpdateRepositoryAdapter extends ReactiveAdapterOperations<LoanRequest, LoanEntity, String, LoanReactiveRepository> implements LoanUpdateRepository {

    private final LoanReactiveRepository repository;
    private final ObjectMapper mapper;
    private final TransactionalOperator tsOperator;
    private final UserRepository userRepository;

    public LoanUpdateRepositoryAdapter(LoanReactiveRepository repository, ObjectMapper mapper, TransactionalOperator tsOperator, UserRepository userRepository) {
        super(repository, mapper, d -> mapper.map(d, LoanRequest.class));
        this.repository = repository;
        this.mapper = mapper;
        this.tsOperator = tsOperator;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<LoanRequest> updateStatus(String publicId, String newStatus, String advisorId) {
        System.out.println("ESTAMOS READY");
        return repository.findByPublicId(UUID.fromString(publicId))
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_NOT_FOUND)))
                .flatMap(loanEntity -> validateAndUpdateLoan(loanEntity, newStatus, advisorId))
                .flatMap(this::saveEntity)
                .map(this::toEntity)
                .as(tsOperator::transactional);
    }

    private Mono<LoanEntity> validateAndUpdateLoan(LoanEntity loanEntity, String newStatus, String advisorId) {
        // Validar que el préstamo no esté ya procesado
        if (loanEntity.getStatus() == RequestStatus.APPROVED || loanEntity.getStatus() == RequestStatus.REJECTED) {
            return Mono.error(new BusinessException(AppMessages.LOAN_ALREADY_PROCESSED));
        }

        // Validar que el asesor exista
        return userRepository.findByDocumentNumber(advisorId)
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.ADVISOR_NOT_FOUND)))
                .then(Mono.fromCallable(() -> {
                    // Actualizar entidad
                    loanEntity.setStatus(RequestStatus.valueOf(newStatus.toUpperCase()));
                    loanEntity.setAdvisorId(advisorId);
                    loanEntity.setUpdatedDate(java.time.LocalDateTime.now());
                    return loanEntity;
                }));
    }

    private Mono<LoanEntity> saveEntity(LoanEntity loanEntity) {
        return repository.save(loanEntity)
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(AppMessages.LOAN_UPDATE_ERROR))
                );
    }
}