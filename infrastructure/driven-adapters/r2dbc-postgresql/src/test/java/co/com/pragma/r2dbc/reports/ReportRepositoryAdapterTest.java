package co.com.pragma.r2dbc.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportRepositoryAdapterTest {

    private DynamoDbAsyncClient dynamoDbAsyncClient;
    private ReportRepositoryAdapter reportRepositoryAdapter;

    @BeforeEach
    void setUp() {
        dynamoDbAsyncClient = mock(DynamoDbAsyncClient.class);
        reportRepositoryAdapter = new ReportRepositoryAdapter(dynamoDbAsyncClient);
    }

    @Test
    void testGetTotalApprovedLoansReturnsValue() {
        // Mock DynamoDB response
        when(dynamoDbAsyncClient.getItem((GetItemRequest) any()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(
                        GetItemResponse.builder()
                                .item(Map.of("count", AttributeValue.builder().n("5").build()))
                                .build()
                ));

        Mono<Long> result = reportRepositoryAdapter.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    void testGetTotalApprovedLoansReturnsZeroIfNoItem() {
        when(dynamoDbAsyncClient.getItem((GetItemRequest) any()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(
                        GetItemResponse.builder().build()
                ));

        Mono<Long> result = reportRepositoryAdapter.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void testIncrementCounterCompletesSuccessfully() {
        when(dynamoDbAsyncClient.updateItem((UpdateItemRequest) any()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(UpdateItemResponse.builder().build()));

        Mono<Void> result = reportRepositoryAdapter.incrementCounter();

        StepVerifier.create(result)
                .verifyComplete();
    }
}
