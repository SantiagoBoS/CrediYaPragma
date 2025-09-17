package co.com.pragma.jwtprovider;

import co.com.pragma.model.auth.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderAdapterTest {

    private JwtProviderAdapter jwtProviderAdapter;
    private Auth auth;

    @BeforeEach
    void setUp() {
        // Configuración con un secret válido (Base64 de 64 bytes)
        JwtConfig config = new JwtConfig();
        config.setSecret("ZmFrZV9zZWNyZXRfZmFrZV9zZWNyZXRfZmFrZV9zZWNyZXRfZmFrZV9zZWNyZXRf");
        config.setExpirationMs(3600000);

        jwtProviderAdapter = new JwtProviderAdapter(config);
        jwtProviderAdapter.init();

        auth = Auth.builder()
                .email("test@example.com")
                .role("ADMIN")
                .document("123456")
                .build();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        //Valida que el token se genera y es válido
        String token = jwtProviderAdapter.generateToken(auth);
        assertThat(jwtProviderAdapter.validateToken(token)).isTrue();
    }

    @Test
    void shouldExtractClaimsCorrectly() {
        // Valida que los claims se extraen correctamente
        String token = jwtProviderAdapter.generateToken(auth);
        assertThat(jwtProviderAdapter.getEmailFromToken(token)).isEqualTo("test@example.com");
        assertThat(jwtProviderAdapter.getRoleFromToken(token)).isEqualTo("ADMIN");
        assertThat(jwtProviderAdapter.getDocumentNumber(token)).isEqualTo("123456");
    }

    @Test
    void shouldInvalidateTamperedToken() {
        // Valida que un token alterado no es válido
        String token = jwtProviderAdapter.generateToken(auth);
        // Altera el token
        String tamperedToken = token.substring(0, token.length() - 2) + "aa";
        assertThat(jwtProviderAdapter.validateToken(tamperedToken)).isFalse();
    }
}