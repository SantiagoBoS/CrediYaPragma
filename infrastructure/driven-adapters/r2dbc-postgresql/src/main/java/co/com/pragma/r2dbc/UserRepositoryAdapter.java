package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserReactiveRepository repository;
    private final ObjectMapper mapper;

    @Transactional
    @Override
    public Mono<User> save(User user) {
        return repository.save(mapper.map(user, UserEntity.class))
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByEmailAndDocumentNumber(String email, String documentNumber) {
        return repository.findByEmailAndDocumentNumber(email, documentNumber)
                .map(entity -> mapper.map(entity, User.class));
    }
}
