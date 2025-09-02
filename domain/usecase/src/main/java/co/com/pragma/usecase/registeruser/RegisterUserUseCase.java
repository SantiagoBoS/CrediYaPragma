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
        return userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())
            .flatMap(existing -> Mono.<User>error(new BusinessException("El usuario ya está registrado con este correo y/o documento")))
            .switchIfEmpty(Mono.defer(() -> userRepository.save(user)
                .onErrorResume(throwable -> {
                    String mss = throwable.getMessage();
                    if (mss != null && (mss.contains("email") || mss.contains("document_number"))) {
                        return Mono.error(new BusinessException("El usuario ya está registrado con este correo y/o documento"));
                    }
                    return Mono.error(new BusinessException("Error interno al registrar usuario"));
                })
            ));
    }
}
