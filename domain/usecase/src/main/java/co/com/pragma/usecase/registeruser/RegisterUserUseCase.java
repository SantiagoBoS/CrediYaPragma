package co.com.pragma.usecase.registeruser;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public Mono<User> registerUser(User user) {
        validateUser(user);
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> {
                    return Mono.<User>error(new BusinessException("El correo electrónico ya está registrado"));
                })
                .switchIfEmpty(userRepository.save(user));
    }

    private void validateUser(User user) {
        if (isNullOrEmpty(user.getName()) || isNullOrEmpty(user.getLastName())
                || isNullOrEmpty(user.getEmail()) || user.getBaseSalary() == null) {
            throw new BusinessException("Rellena todos los campos obligatorios");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException("Formato de correo electrónico inválido");
        }

        if (user.getBaseSalary().compareTo(BigDecimal.ZERO) < 0
                || user.getBaseSalary().compareTo(new BigDecimal("15000000")) > 0) {
            throw new BusinessException("El rango del salario es de (0 - 15000000)");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
