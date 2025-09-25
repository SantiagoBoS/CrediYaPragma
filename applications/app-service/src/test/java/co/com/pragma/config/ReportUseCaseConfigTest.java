package co.com.pragma.config;

import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.usecase.reports.ReportUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ReportUseCaseConfigTest {

    @Test
    void testReportUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            ReportUseCase bean = context.getBean(ReportUseCase.class);
            assertNotNull(bean, "ReportUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(ReportUseCaseConfig.class)
    static class TestConfig {

        @Bean
        public ReportRepository reportRepository() {
            return mock(ReportRepository.class);
        }
    }
}
