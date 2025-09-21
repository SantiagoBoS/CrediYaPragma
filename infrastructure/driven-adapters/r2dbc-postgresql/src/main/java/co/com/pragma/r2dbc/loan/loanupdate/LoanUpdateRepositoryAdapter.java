package co.com.pragma.r2dbc.loan.loanupdate;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.model.loan.loanupdate.gateways.LoanUpdateRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import co.com.pragma.r2dbc.loan.loanupdate.customrepository.LoanUpdateCustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Repository
public class LoanUpdateRepositoryAdapter extends ReactiveAdapterOperations<LoanRequest, LoanEntity, Long, LoanUpdateReactiveRepository> implements LoanUpdateRepository {

    private final LoanUpdateReactiveRepository repository;
    private final ObjectMapper mapper;
    private final TransactionalOperator tsOperator;
    private final UserRepository userRepository;
    private final LoanUpdateCustomRepository customRepository;

    public LoanUpdateRepositoryAdapter(
            LoanUpdateReactiveRepository repository,
            ObjectMapper mapper,
            TransactionalOperator tsOperator,
            UserRepository userRepository,
            LoanUpdateCustomRepository customRepository
    ) {
        super(repository, mapper, d -> mapper.map(d, LoanRequest.class));
        this.repository = repository;
        this.mapper = mapper;
        this.tsOperator = tsOperator;
        this.userRepository = userRepository;
        this.customRepository = customRepository;
    }

    @Override
    public Mono<LoanRequest> updateStatus(String publicId, String newStatus, String advisorId) {
        UUID publicUUID = UUID.fromString(publicId);

        log.info("Iniciando actualización - publicId: {}, status: {}, advisor: {}", publicId, newStatus, advisorId);
        return repository.findByPublicId(publicUUID)
                .doOnNext(entity -> log.info("Préstamo encontrado: {}", entity.getId()))
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_NOT_FOUND)))
                .flatMap(this::validateLoanNotProcessed)
                .flatMap(this::validateUserExists)
                .flatMap(loanEntity -> customRepository.updateLoanStatus(
                        publicUUID,
                        RequestStatus.valueOf(newStatus.toUpperCase()),
                        advisorId
                ))
                .map(this::toEntity)
                .doOnSuccess(updated -> log.info("Préstamo actualizado exitosamente"))
                .as(tsOperator::transactional)
                .onErrorResume(throwable -> {
                    return Mono.error(new BusinessException(AppMessages.LOAN_UPDATE_ERROR));
                });

    }

    private Mono<LoanEntity> validateLoanNotProcessed(LoanEntity loanEntity) {
        if (loanEntity.getStatus() == RequestStatus.APPROVED || loanEntity.getStatus() == RequestStatus.REJECTED) {
            log.warn("Préstamo ya procesado - ID: {}, Estado actual: {}", loanEntity.getId(), loanEntity.getStatus());
            return Mono.error(new BusinessException(AppMessages.LOAN_ALREADY_PROCESSED));
        }
        return Mono.just(loanEntity);
    }

    private Mono<LoanEntity> validateUserExists(LoanEntity loanEntity) {
        return userRepository.findByDocumentNumber(loanEntity.getClientDocument())
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.USER_NOT_FOUND)))
                .thenReturn(loanEntity);
    }
}