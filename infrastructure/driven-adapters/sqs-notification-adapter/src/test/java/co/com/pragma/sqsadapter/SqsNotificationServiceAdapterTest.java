package co.com.pragma.sqsadapter;

import co.com.pragma.sqsadapter.config.SqsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SqsNotificationServiceAdapterTest {

    private SqsAsyncClient sqsAsyncClient;
    private ObjectMapper objectMapper;
    private SqsProperties sqsProperties;
    private SqsNotificationServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        objectMapper = new ObjectMapper();
        sqsProperties = mock(SqsProperties.class);
        when(sqsProperties.isEnabled()).thenReturn(true); // habilitamos SQS para el test

        adapter = new SqsNotificationServiceAdapter(sqsAsyncClient, objectMapper, sqsProperties);
        adapter.sqsQueueUrl = "http://localhost:4566/queue/test";
    }

    @Test
    void testSendLoanStatusUpdateNotification_sendsMessage() throws Exception {
        // Mockear respuesta de sendMessage
        SendMessageResponse response = SendMessageResponse.builder().messageId("123").build();
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        // Llamar al método
        Mono<Void> result = adapter.sendLoanStatusUpdateNotification(
                "REQ123",
                "APPROVED",
                "test@example.com",
                "CAR"
        );

        // Forzar la ejecución de Mono
        result.block();

        // Verificar que se llamó a sendMessage
        verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }
}