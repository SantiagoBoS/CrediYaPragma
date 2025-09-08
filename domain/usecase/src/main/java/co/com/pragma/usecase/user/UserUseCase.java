package co.com.pragma.usecase.user;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    public Mono<User> registerUser(User user) {
        return userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())
            .flatMap(existing -> Mono.<User>error(new BusinessException(AppMessages.USER_ALREADY_EXISTS.getMessage())))
            .switchIfEmpty(Mono.defer(() -> userRepository.save(user)));
    }
}
