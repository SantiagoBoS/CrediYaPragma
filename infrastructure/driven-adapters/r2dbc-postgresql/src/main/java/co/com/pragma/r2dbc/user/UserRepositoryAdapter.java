package co.com.pragma.r2dbc.user;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.user.entity.UserEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, String, UserReactiveRepository> implements UserRepository {
    private final UserReactiveRepository repository;
    private final ObjectMapper mapper;
    private final TransactionalOperator tsOperator;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, TransactionalOperator tsOperator) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.repository = repository;
        this.mapper = mapper;
        this.tsOperator = tsOperator;
    }

    @Override
    public Mono<User> save(User User) {
        return super.save(User)
            .as(tsOperator::transactional)
            .onErrorResume(throwable -> {
                String mss = throwable.getMessage();
                if (mss != null && (mss.contains("email") || mss.contains("document_number"))) {
                    return Mono.error(new BusinessException(AppMessages.USER_ALREADY_EXISTS.getMessage()));
                }
                return Mono.error(new BusinessException(AppMessages.ERROR_SAVING_USER.getMessage()));
            });
    }

    @Override
    public Mono<User> findByEmailAndDocumentNumber(String email, String documentNumber) {
        return repository.findByEmailAndDocumentNumber(email, documentNumber).map(this::toEntity).as(tsOperator::transactional);
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .map(this::toEntity)
                .as(tsOperator::transactional)
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.USER_NOT_FOUND.getMessage())));
    }
}
