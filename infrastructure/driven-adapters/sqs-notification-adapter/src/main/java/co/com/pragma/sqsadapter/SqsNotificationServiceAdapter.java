package co.com.pragma.sqsadapter;

import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.sqsadapter.config.SqsProperties;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SqsNotificationServiceAdapter implements NotificationServiceGateway {

    private final SqsAsyncClient sqsAsyncClient;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final SqsProperties sqsProperties;

    // Cola de NOTIFICACIONES (estado del préstamo)
    @Value("${aws.sqs.notification-queue-url}")
    private String notificationQueueUrl;

    @Override
    public Mono<Void> sendLoanStatusUpdateNotification(
            String loanRequestId,
            String newStatus,
            String userEmail,
            String loanType,
            Double amount,
            Double interestRate,
            Integer termMonths,
            List<LoanInstallment> paymentPlan
    ) {

        if (!sqsProperties.isEnabled()) {
            log.info("SQS está deshabilitado. Simulando envío de notificación para solicitud: {}, estado: {}, email: {}", loanRequestId, newStatus, userEmail);
            return Mono.empty();
        }

        if (notificationQueueUrl == null || notificationQueueUrl.isEmpty()) {
            log.error("URL de notificaciones no configurada");
            return Mono.error(new RuntimeException("URL de cola de notificaciones no configurada"));
        }

        try {
            Map<String, Object> messagePayload = new HashMap<>();
            messagePayload.put("loanRequestId", loanRequestId);
            messagePayload.put("newStatus", newStatus);
            messagePayload.put("userEmail", userEmail);
            messagePayload.put("loanType", loanType);

            if (amount != null) messagePayload.put("amount", amount);
            if (interestRate != null) messagePayload.put("interestRate", interestRate);
            if (termMonths != null) messagePayload.put("termMonths", termMonths);

            messagePayload.put("paymentPlan", paymentPlan.stream()
                    .map(pi -> Map.of(
                            "month", pi.getMonth(),
                            "capitalPayment", pi.getCapitalPayment(),
                            "interestPayment", pi.getInterestPayment(),
                            "remainingBalance", pi.getRemainingBalance()
                    ))
                    .toList()
            );
            String messageBody = objectMapper.writeValueAsString(messagePayload);

            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(notificationQueueUrl)
                    .messageBody(messageBody)
                    .build();

            return Mono.fromFuture(sqsAsyncClient.sendMessage(sendMessageRequest))
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnSuccess(response -> log.info("Notificación enviada a SQS. MessageId: {}, Loan: {}",
                            response.messageId(), loanRequestId))
                    .doOnError(error -> log.error("Error enviando notificación. Loan: {}, URL: {}",
                            loanRequestId, notificationQueueUrl, error))
                    .then();

        } catch (JsonProcessingException e) {
            log.error("Error serializando mensaje para SQS", e);
            return Mono.error(e);
        }
    }
}
