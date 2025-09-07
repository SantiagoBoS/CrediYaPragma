package co.com.pragma.config;

import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository) {
        return new RegisterUserUseCase(userRepository);
    }
}
