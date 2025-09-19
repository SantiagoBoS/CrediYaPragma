package co.com.pragma.api.auth.config;

import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderServiceImplTest {

    private PasswordEncoderService passwordEncoderService;

    @BeforeEach
    void setUp() {
        passwordEncoderService = new PasswordEncoderServiceImpl();
    }

    @Test
    void shouldEncodePassword() {
        String rawPassword = "MySecret123!";
        String encodedPassword = passwordEncoderService.encode(rawPassword);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertNotEquals(rawPassword, encodedPassword, "Encoded password should differ from raw password");
    }

    @Test
    void shouldMatchEncodedPassword() {
        String rawPassword = "MySecret123!";
        String encodedPassword = passwordEncoderService.encode(rawPassword);

        assertTrue(passwordEncoderService.matches(rawPassword, encodedPassword),
                "PasswordEncoderService should match raw and encoded password");

        assertFalse(passwordEncoderService.matches("WrongPassword", encodedPassword),
                "PasswordEncoderService should not match wrong password");
    }
}
