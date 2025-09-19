package co.com.pragma.jwtprovider;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigTest {

    @Test
    void shouldSetAndGetPropertiesCorrectly() {
        // Validar que los getters y setters funcionan correctamente
        JwtConfig jwtConfig = new JwtConfig();

        jwtConfig.setSecret("mySecretKey");
        jwtConfig.setExpirationMs(3600000L);

        assertEquals("mySecretKey", jwtConfig.getSecret());
        assertEquals(3600000L, jwtConfig.getExpirationMs());
    }

    @Test
    void shouldSupportLombokBuilderAndToString() {
        //Validar que los metodos de lombok funcionan correctamente
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("secret123");
        jwtConfig.setExpirationMs(60000L);

        // Verifica getters
        assertEquals("secret123", jwtConfig.getSecret());
        assertEquals(60000L, jwtConfig.getExpirationMs());

        // Verifica toString no nulo
        assertNotNull(jwtConfig.toString());
    }
}