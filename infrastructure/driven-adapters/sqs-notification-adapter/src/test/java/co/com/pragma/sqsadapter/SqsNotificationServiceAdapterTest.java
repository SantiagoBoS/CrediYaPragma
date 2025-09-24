package co.com.pragma.sqsadapter;

import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.sqsadapter.config.SqsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SqsNotificationServiceAdapterTest {

    private SqsAsyncClient sqsAsyncClient;
    private ObjectMapper objectMapper;
    private SqsProperties sqsProperties;
    private UserRepository userRepository;
    private SqsNotificationServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        objectMapper = new ObjectMapper();
        sqsProperties = mock(SqsProperties.class);
        userRepository = mock(UserRepository.class);

        when(sqsProperties.isEnabled()).thenReturn(true);

        adapter = new SqsNotificationServiceAdapter(sqsAsyncClient, userRepository, objectMapper, sqsProperties);
        ReflectionTestUtils.setField(adapter, "notificationQueueUrl", "http://localhost:4566/queue/test");
    }

    @Test
    void testSendLoanStatusUpdateNotification_sendsMessage() {
        SendMessageResponse response = SendMessageResponse.builder().messageId("123").build();
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        Mono<Void> result = adapter.sendLoanStatusUpdateNotification(
                "REQ123",
                "APPROVED",
                "test@example.com",
                "CAR",
                10000.0,
                2.5,
                12,
                List.of(new LoanInstallment(
                        1,
                        BigDecimal.valueOf(500),
                        BigDecimal.valueOf(200),
                        BigDecimal.valueOf(9500)
                ))
        );

        StepVerifier.create(result).verifyComplete();
        verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void testSqsDisabledDoesNotSendMessage() {
        when(sqsProperties.isEnabled()).thenReturn(false);

        Mono<Void> result = adapter.sendLoanStatusUpdateNotification(
                "REQ123", "APPROVED", "test@example.com", "CAR",
                null, null, null, List.of()
        );

        StepVerifier.create(result)
                .verifyComplete();

        verify(sqsAsyncClient, never()).sendMessage((SendMessageRequest) any());
    }

    @Test
    void testSerializationError() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.writeValueAsString(any())).thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("error serializando") {});
        SqsNotificationServiceAdapter faultyAdapter = new SqsNotificationServiceAdapter(sqsAsyncClient, userRepository, mockMapper, sqsProperties);
        ReflectionTestUtils.setField(faultyAdapter, "notificationQueueUrl", "http://localhost:4566/queue/test");
        when(sqsProperties.isEnabled()).thenReturn(true);

        StepVerifier.create(faultyAdapter.sendLoanStatusUpdateNotification(
        "REQ1", "APPROVED", "x@y.com", "CAR", null, null, null, List.of()
                ))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof com.fasterxml.jackson.core.JsonProcessingException);
                    assertEquals("error serializando", error.getMessage());
                })
                .verify();
    }

    @Test
    void testSendMessageFailure() {
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("SQS down")));

        Mono<Void> result = adapter.sendLoanStatusUpdateNotification(
                "REQ_FAIL",
                "REJECTED",
                "fail@example.com",
                "CAR",
                null,
                null,
                null,
                List.of()
        );

        StepVerifier.create(result)
            .expectErrorSatisfies(error -> {
                assertTrue(error instanceof RuntimeException);
                assertEquals("SQS down", error.getMessage());
            })
            .verify();
    }
}