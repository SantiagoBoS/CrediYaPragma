package co.com.pragma.model.auth.gateways;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderServiceTest {

    private PasswordEncoderService passwordEncoderService;

    @BeforeEach
    void setUp() {
        // Fake implementation para probar el contrato
        passwordEncoderService = new PasswordEncoderService() {
            @Override
            public boolean matches(String rawPassword, String encodedPassword) {
                return encodedPassword.equals(encode(rawPassword));
            }

            @Override
            public String encode(String rawPassword) {
                return "ENCODED_" + rawPassword;
            }
        };
    }

    @Test
    void shouldEncodePassword() {
        //Validar que el password se encode correctamente
        String rawPassword = "12345";
        String encoded = passwordEncoderService.encode(rawPassword);
        assertNotNull(encoded);
        assertTrue(encoded.startsWith("ENCODED_"));
        assertEquals("ENCODED_12345", encoded);
    }

    @Test
    void shouldMatchEncodedPasswordCorrectly() {
        // Validar que el password coincida correctamente
        String rawPassword = "secret";
        String encodedPassword = passwordEncoderService.encode(rawPassword);
        assertTrue(passwordEncoderService.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldFailWhenPasswordsDoNotMatch() {
        // Validar que falle cuando los passwords no coinciden
        String rawPassword = "secret";
        String encodedPassword = passwordEncoderService.encode("other");
        assertFalse(passwordEncoderService.matches(rawPassword, encodedPassword));
    }
}
