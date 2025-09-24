package co.com.pragma.sqsadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LoanNotificationListener {
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @Value("${aws.sqs.queue-url}")
    String queueUrl;

    @PostConstruct
    public void startListener() {
        pollMessages();
    }

    private void pollMessages() {
        sqsAsyncClient.receiveMessage(b -> b.queueUrl(queueUrl).maxNumberOfMessages(5).waitTimeSeconds(10))
                .thenAccept(response -> {
                    response.messages().forEach(msg -> {
                        try {
                            Map payload = objectMapper.readValue(msg.body(), Map.class);
                            log.info("Mensaje recibido: {}", payload);

                            String email = (String) payload.get("userEmail");
                            String loanType = (String) payload.get("loanType");
                            String status = (String) payload.get("newStatus");
                            String loanRequestId = (String) payload.get("loanRequestId");
                            Double amount = (Double) payload.get("amount");
                            Double interestRate = (Double) payload.get("interestRate");
                            Integer termMonths = (Integer) payload.get("termMonths");

                            emailService.sendLoanStatusUpdate(
                                    email, loanType, status, loanRequestId, amount, interestRate, termMonths
                            );

                            // Borrar el mensaje de la cola después de procesar
                            sqsAsyncClient.deleteMessage(b -> b.queueUrl(queueUrl).receiptHandle(msg.receiptHandle()));
                        } catch (Exception e) {
                            log.error("Error procesando mensaje SQS", e);
                        }
                    });
                    pollMessages(); // vuelve a escuchar
                })
                .exceptionally(e -> {
                    log.error("Error en polling de SQS", e);
                    pollMessages();
                    return null;
                });
    }
}
