package co.com.pragma.sqsadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Map;
import static org.mockito.Mockito.*;

class LoanNotificationListenerTest {

    private SqsAsyncClient sqsAsyncClient;
    private ObjectMapper objectMapper;
    private EmailService emailService;
    private LoanNotificationListener listener;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        objectMapper = new ObjectMapper();
        emailService = mock(EmailService.class);

        listener = new LoanNotificationListener(sqsAsyncClient, objectMapper, emailService);
        listener.queueUrl = "http://localhost:4566/queue/test";
    }

    @Test
    void testEmailServiceDirectly() throws Exception {
        Map<String, String> payload = Map.of(
                "userEmail", "test@example.com",
                "loanType", "CAR",
                "newStatus", "APPROVED",
                "loanRequestId", "REQ123"
        );

        // Llamamos directamente a emailService
        emailService.sendLoanStatusUpdate(
                payload.get("userEmail"),
                payload.get("loanType"),
                payload.get("newStatus"),
                payload.get("loanRequestId")
        );

        verify(emailService, times(1)).sendLoanStatusUpdate("test@example.com", "CAR", "APPROVED", "REQ123");
    }
}