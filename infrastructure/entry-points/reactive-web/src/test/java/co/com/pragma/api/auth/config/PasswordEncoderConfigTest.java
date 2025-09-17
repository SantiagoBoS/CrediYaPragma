package co.com.pragma.api.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    private final PasswordEncoderConfig config = new PasswordEncoderConfig();

    @Test
    void passwordEncoderBeanShouldNotBeNull() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder, "PasswordEncoder bean should not be null");
    }

    @Test
    void passwordEncoderShouldEncodeAndMatchPassword() {
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "MySecret123!";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertNotEquals(rawPassword, encodedPassword, "Encoded password should be different from raw password");
        assertTrue(encoder.matches(rawPassword, encodedPassword), "PasswordEncoder should match raw and encoded password");
    }
}