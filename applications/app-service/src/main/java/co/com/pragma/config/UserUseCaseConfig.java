package co.com.pragma.config;

import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderService passwordEncoderService) {
        return new UserUseCase(userRepository, roleRepository, passwordEncoderService);
    }
}
