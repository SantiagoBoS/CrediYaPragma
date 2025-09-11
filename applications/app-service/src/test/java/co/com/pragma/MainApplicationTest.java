package co.com.pragma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTest {
    @Test
    void contextLoads() {}

    @Test
    void mainMethodRunsWithoutErrors() {
        String[] args = {};
        MainApplication.main(args);
    }
}