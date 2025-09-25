package co.com.pragma.sqsadapter.reports;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.sqsadapter.config.LoanEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportSqsListenerTest {

    private SqsAsyncClient sqsAsyncClient;
    private ObjectMapper objectMapper;
    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        objectMapper = new ObjectMapper();
        reportRepository = mock(ReportRepository.class);
    }

    @Test
    void testStartListenerProcessesApprovedLoan() throws Exception {
        ReportSqsListener listener = new ReportSqsListener(sqsAsyncClient, objectMapper, reportRepository);

        LoanEvent event = new LoanEvent("1", RequestStatus.APPROVED.toString(), 100.0);
        Message msg = Message.builder()
                .body(objectMapper.writeValueAsString(event))
                .receiptHandle("handle-1")
                .build();

        when(sqsAsyncClient.receiveMessage(any(java.util.function.Consumer.class)))
                .thenAnswer(invocation -> {
                    ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                            .messages(msg)
                            .build();
                    return CompletableFuture.completedFuture(response);
                });

        when(sqsAsyncClient.deleteMessage(any(java.util.function.Consumer.class)))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(null));

        when(reportRepository.incrementCounter())
                .thenReturn(Mono.empty());

        listener.startListener();
    }

}
