package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneratorTest {
    @Test
    void generatePassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Password:" + encoder.encode("123456"));
        System.out.println("Password:" + encoder.encode("123456789"));
        System.out.println("Password:" + encoder.encode("1234"));
    }
}
