package co.com.pragma.sqsadapter;

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
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SqsNotificationServiceAdapter implements NotificationServiceGateway {
    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.queue-url}")
    private String sqsQueueUrl;

    private final ObjectMapper objectMapper;
    private final SqsProperties sqsProperties;

    @Override
    public Mono<Void> sendLoanStatusUpdateNotification(String loanRequestId, String newStatus, String userEmail) {

        if (!sqsProperties.isEnabled()) {
            log.info("🚧 SQS está deshabilitado. Simulando envío de notificación para solicitud: {}, estado: {}, email: {}", loanRequestId, newStatus, userEmail);
            return Mono.empty();
        }

        Map<String, String> messagePayload = new HashMap<>();
        messagePayload.put("loanRequestId", loanRequestId);
        messagePayload.put("newStatus", newStatus);
        messagePayload.put("userEmail", userEmail);
        messagePayload.put("type", "LOAN_STATUS_UPDATE");

        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(messagePayload);
        } catch (JsonProcessingException e) {
            log.error("Error serializando mensaje para SQS", e);
            return Mono.error(e);
        }

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(sqsQueueUrl)
                .messageBody(messageBody)
                .build();

        return Mono.fromFuture(sqsAsyncClient.sendMessage(sendMessageRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(response -> log.info("✅ Notificación enviada a SQS. MessageId: {}, para solicitud: {}", response.messageId(), loanRequestId))
                .doOnError(error -> log.error("❌ Error enviando notificación a SQS para la solicitud: {}", loanRequestId, error))
                .then();
    }
}
