package co.com.pragma.api.config;

import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderServiceImpl implements PasswordEncoderService {

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }
}