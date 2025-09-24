package co.com.pragma.config.notification;

import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class LoanNotificationServiceConfigTest {

    @Test
    void testLoanNotificationServiceBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            LoanNotificationService bean = context.getBean(LoanNotificationService.class);
            assertNotNull(bean, "LoanNotificationService bean should exist in the context");
        }
    }

    @Configuration
    @Import(LoanNotificationServiceConfig.class)
    static class TestConfig {

        @Bean
        public NotificationServiceGateway notificationServiceGateway() {
            return mock(NotificationServiceGateway.class);
        }
    }
}
