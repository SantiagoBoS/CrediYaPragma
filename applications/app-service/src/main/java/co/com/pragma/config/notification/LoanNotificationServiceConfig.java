package co.com.pragma.config.notification;

import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanNotificationServiceConfig {
    @Bean
    public LoanNotificationService loanNotificationService(NotificationServiceGateway notificationServiceGateway) {
        return new LoanNotificationService(notificationServiceGateway);
    }
}
