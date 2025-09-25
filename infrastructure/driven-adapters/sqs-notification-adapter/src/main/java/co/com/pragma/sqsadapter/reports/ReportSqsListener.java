package co.com.pragma.sqsadapter.reports;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.sqsadapter.config.LoanEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportSqsListener {
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;
    private final ReportRepository reportRepository; // Contador en DynamoDB

    @Value("${aws.sqs.reports-queue-url:}")
    String queueUrl;

    @PostConstruct
    public void startListener() {
        pollMessages();
    }

    private void pollMessages() {
        sqsAsyncClient.receiveMessage(b -> b.queueUrl(queueUrl)
                        .maxNumberOfMessages(5)
                        .waitTimeSeconds(10))
                .thenAccept(response -> {
                    for (Message msg : response.messages()) {
                        try {
                            LoanEvent event = objectMapper.readValue(msg.body(), LoanEvent.class);

                            if (RequestStatus.APPROVED.toString().equals(event.getStatus())) {
                                reportRepository.incrementCounter()
                                        .subscribe();
                            }

                            sqsAsyncClient.deleteMessage(b -> b.queueUrl(queueUrl)
                                    .receiptHandle(msg.receiptHandle()));
                        } catch (Exception e) {
                            log.error("Error procesando mensaje SQS", e);
                        }
                    }
                    pollMessages();
                })
                .exceptionally(e -> {
                    log.error("Error en polling de SQS", e);
                    pollMessages();
                    return null;
                });
    }
}