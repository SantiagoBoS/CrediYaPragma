package co.com.pragma.api.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneratorTest {
    @Test
    void generatePassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
    }
}
