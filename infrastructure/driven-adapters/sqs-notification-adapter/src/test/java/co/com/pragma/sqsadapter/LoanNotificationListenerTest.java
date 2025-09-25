package co.com.pragma.sqsadapter;

import co.com.pragma.sqsadapter.notification.EmailService;
import co.com.pragma.sqsadapter.notification.LoanNotificationListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
        listener.queueUrl = "http://fake-queue-url";
    }

    @Test
    void testPollMessages_processesValidMessage() throws Exception {
        // Arrange
        String body = """
        {
            "userEmail":"test@example.com",
            "loanType":"CAR",
            "newStatus":"APPROVED",
            "loanRequestId":"REQ123",
            "amount":15000.0,
            "interestRate":7.5,
            "termMonths":24
        }
        """;

        Message message = Message.builder()
                .body(body)
                .receiptHandle("receipt-123")
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(message)
                .build();

        //Primera vez → devuelve el mensaje válido, segunda vez → vacío
        when(sqsAsyncClient.receiveMessage(any(Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture(response))
                .thenReturn(CompletableFuture.completedFuture(ReceiveMessageResponse.builder().build()));

        when(sqsAsyncClient.deleteMessage(any(Consumer.class))).thenReturn(CompletableFuture.completedFuture(DeleteMessageResponse.builder().build()));
        listener.startListener();

        verify(emailService, timeout(1000).times(1))
                .sendLoanStatusUpdate(eq("test@example.com"), eq("CAR"), eq("APPROVED"),
                        eq("REQ123"), eq(15000.0), eq(7.5), eq(24));
    }

    @Test
    void testPollMessages_handlesInvalidJsonGracefully() throws Exception {
        Message message = Message.builder()
                .body("invalid-json")
                .receiptHandle("receipt-456")
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(message)
                .build();

        // Primera vez devuelve un mensaje inválido, segunda vez vacío → rompe el loop
        when(sqsAsyncClient.receiveMessage(any(Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture(response))
                .thenReturn(CompletableFuture.completedFuture(ReceiveMessageResponse.builder().build()));
        listener.startListener();

        // Assert → EmailService nunca debe ser llamado
        verify(emailService, timeout(1000).times(0))
                .sendLoanStatusUpdate(any(), any(), any(), any(), anyDouble(), anyDouble(), anyInt());
    }

    @Test
    void testPollMessages_handlesEmptyResponse() {
        ReceiveMessageResponse response = ReceiveMessageResponse.builder().build();
        when(sqsAsyncClient.receiveMessage(any(Consumer.class))).thenReturn(CompletableFuture.completedFuture(response));
        listener.startListener();

        // Assert → No debe llamar a EmailService
        verify(emailService, timeout(500).times(0)).sendLoanStatusUpdate(any(), any(), any(), any(), anyDouble(), anyDouble(), anyInt());
    }
}