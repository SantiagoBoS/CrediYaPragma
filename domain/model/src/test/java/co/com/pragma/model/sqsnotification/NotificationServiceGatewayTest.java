package co.com.pragma.model.sqsnotification;

import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceGatewayTest {
    private NotificationServiceGateway notificationServiceGateway;

    @BeforeEach
    void setUp() {
        notificationServiceGateway = mock(NotificationServiceGateway.class);
    }

    @Test
    void shouldSendNotificationSuccessfully() {
        when(notificationServiceGateway.sendLoanStatusUpdateNotification(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        Mono<Void> result = notificationServiceGateway.sendLoanStatusUpdateNotification(
                "123e4567-e89b-12d3-a456-426614174000",
                "APPROVED",
                "test@mail.com",
                "CAR"
        );

        StepVerifier.create(result)
                .verifyComplete();

        verify(notificationServiceGateway, times(1))
                .sendLoanStatusUpdateNotification("123e4567-e89b-12d3-a456-426614174000",
                        "APPROVED",
                        "test@mail.com",
                        "CAR");
    }

    @Test
    void shouldReturnErrorWhenNotificationFails() {
        when(notificationServiceGateway.sendLoanStatusUpdateNotification(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Error sending notification")));

        Mono<Void> result = notificationServiceGateway.sendLoanStatusUpdateNotification(
                "123e4567-e89b-12d3-a456-426614174000",
                "REJECTED",
                "user@mail.com",
                "MORTGAGE"
        );

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("Error sending notification"))
                .verify();

        verify(notificationServiceGateway, times(1))
                .sendLoanStatusUpdateNotification("123e4567-e89b-12d3-a456-426614174000",
                        "REJECTED",
                        "user@mail.com",
                        "MORTGAGE");
    }
}
