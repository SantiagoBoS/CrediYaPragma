package co.com.pragma.usecase.user;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final AuthRepository authRepository;

    public Mono<User> registerUser(User user) {
        return userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())
            .flatMap(existing -> Mono.<User>error(new BusinessException(AppMessages.USER_ALREADY_EXISTS.getMessage())))
            .switchIfEmpty(roleRepository.findByCode(user.getRole())
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.ROLE_NOT_FOUND.getMessage())))
                .then(Mono.defer(() -> {
                    user.setPassword(passwordEncoderService.encode(user.getPassword()));
                    return userRepository.save(user)
                        .flatMap(savedUser -> {
                            Auth auth = new Auth();
                            auth.setEmail(savedUser.getEmail());
                            auth.setPassword(savedUser.getPassword());
                            auth.setRole(savedUser.getRole());
                            return authRepository.save(auth).thenReturn(savedUser);
                        });
                }))
            );
    }
}