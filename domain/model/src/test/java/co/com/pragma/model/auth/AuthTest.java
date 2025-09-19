package co.com.pragma.model.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    private Auth auth;
    private String user;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        auth = new Auth("12345", "user@test.com", "password123", "ADMIN");
        user = "12345";
        email = "user@test.com";
        password = "password123";
    }

    @Test
    void shouldCreateAuthWithAllArgsConstructor() {
        assertEquals(user, auth.getDocument());
        assertEquals(email, auth.getEmail());
        assertEquals(password, auth.getPassword());
        assertEquals("ADMIN", auth.getRole());
    }

    @Test
    void shouldSetAndGetValues() {
        auth.setRole("USER");

        assertEquals(user, auth.getDocument());
        assertEquals(email, auth.getEmail());
        assertEquals(password, auth.getPassword());
        assertEquals("USER", auth.getRole());
    }

    @Test
    void shouldBuildUsingBuilder() {
        auth.setRole("CLIENT");

        assertEquals(user, auth.getDocument());
        assertEquals(email, auth.getEmail());
        assertEquals(password, auth.getPassword());
        assertEquals("CLIENT", auth.getRole());
    }

    @Test
    void shouldModifyWithToBuilder() {
        auth.setRole("USER");

        Auth modified = auth.toBuilder()
                .email("modified@test.com")
                .build();

        assertEquals(user, auth.getDocument());
        assertEquals("modified@test.com", modified.getEmail());
        assertEquals(password, auth.getPassword());
        assertEquals("USER", modified.getRole());
    }
}
