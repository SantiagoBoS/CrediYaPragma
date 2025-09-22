package co.com.pragma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "cloud.aws.enabled=false"
})
class MainApplicationTest {

    @Test
    void mainMethodRunsWithoutErrors() {
        // Configurar system properties para forzar el perfil test
        System.setProperty("spring.profiles.active", "test");
        MainApplication.main(new String[]{});
    }
}