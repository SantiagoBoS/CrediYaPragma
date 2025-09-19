package co.com.pragma.model.auth.gateways;

import co.com.pragma.model.auth.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private TokenProvider tokenProvider;
    private Auth auth;

    @BeforeEach
    void setUp() {
        auth = new Auth("67890", "user@test.com", "67890", "ADMIN");

        // Implementación fake para probar contrato
        tokenProvider = new TokenProvider() {
            @Override
            public String generateToken(Auth auth) {
                return auth.getEmail() + "|" + auth.getRole() + "|" + auth.getDocument();
            }

            @Override
            public boolean validateToken(String token) {
                return token != null && token.split("\\|").length == 3;
            }

            @Override
            public String getEmailFromToken(String token) {
                return token.split("\\|")[0];
            }

            @Override
            public String getRoleFromToken(String token) {
                return token.split("\\|")[1];
            }

            @Override
            public String getDocumentNumber(String token) {
                return token.split("\\|")[2];
            }
        };
    }

    @Test
    void shouldGenerateAndValidateToken() {
        //Validar que el token se genere y valide correctamente
        String token = tokenProvider.generateToken(auth);
        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void shouldExtractEmailFromToken() {
        //Validar que se extraiga el email correctamente del token
        String token = tokenProvider.generateToken(auth);
        assertEquals("user@test.com", tokenProvider.getEmailFromToken(token));
    }

    @Test
    void shouldExtractRoleFromToken() {
        //Validar que se extraiga el rol correctamente del token
        String token = tokenProvider.generateToken(auth);
        assertEquals("ADMIN", tokenProvider.getRoleFromToken(token));
    }

    @Test
    void shouldExtractDocumentNumberFromToken() {
        //Validar que se extraiga el número de documento correctamente del token
        String token = tokenProvider.generateToken(auth);
        assertEquals("67890", tokenProvider.getDocumentNumber(token));
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        //Validar que un token inválido no pase la validación
        assertFalse(tokenProvider.validateToken("invalidTokenFormat"));
    }
}

