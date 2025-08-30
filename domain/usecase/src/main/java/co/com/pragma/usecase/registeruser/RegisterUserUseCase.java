package co.com.pragma.usecase.registeruser;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public Mono<User> registerUser(User user) {
        if (user.getEmail() == null || user.getDocumentNumber() == null) {
            return Mono.error(new BusinessException("Email y documento no pueden ser nulos"));
        }

        return userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())
            .flatMap(existing -> {
                    return Mono.<User>error(new BusinessException("Usuario ya registrado con este correo y documento"));
            })
            .switchIfEmpty(Mono.defer(() -> userRepository.save(user)));
    }
}
