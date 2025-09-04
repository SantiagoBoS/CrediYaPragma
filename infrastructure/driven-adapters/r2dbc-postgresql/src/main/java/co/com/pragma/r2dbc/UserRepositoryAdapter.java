package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.constants.AppMessages;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, String, UserReactiveRepository> implements UserRepository {
    private final UserReactiveRepository repository;
    private final ObjectMapper mapper;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User User) {
        return super.save(User)
            .onErrorResume(throwable -> {
                String mss = throwable.getMessage();
                if (mss != null &&
                    (mss.contains(String.valueOf(AppMessages.EMAIL_FIELD)) || mss.contains(String.valueOf(AppMessages.DOCUMENT_FIELD)))
                ) {
                    return Mono.error(new BusinessException(String.valueOf(AppMessages.USER_ALREADY_EXISTS)));
                }
                return Mono.error(new BusinessException(String.valueOf(AppMessages.ERROR_SAVING_USER)));
            });
    }

    @Override
    public Mono<User> findByEmailAndDocumentNumber(String email, String documentNumber) {
        return repository.findByEmailAndDocumentNumber(email, documentNumber).map(this::toEntity);
    }
}
