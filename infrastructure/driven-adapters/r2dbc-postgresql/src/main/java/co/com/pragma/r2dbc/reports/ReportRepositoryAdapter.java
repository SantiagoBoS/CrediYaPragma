package co.com.pragma.r2dbc.reports;

import co.com.pragma.model.reports.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    private final String tableName = "reports_table";
    private final String counterKey = "approvedLoans";

    @Override
    public Mono<Long> getTotalApprovedLoans() {
        //Obtener las solicitudes aprovadas
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(counterKey).build()))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.getItem(request))
                .map(response -> {
                    if (response.item() == null || !response.item().containsKey("count")) {
                        return 0L;
                    }
                    String value = response.item().get("count").n();
                    return Long.parseLong(value);
                })
                .onErrorReturn(0L);
    }

    @Override
    public Mono<Void> incrementCounter() {
        //Incremetar su valor
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(counterKey).build()))
                .updateExpression("SET #count = if_not_exists(#count, :zero) + :inc")
                .expressionAttributeNames(Map.of("#count", "count"))
                .expressionAttributeValues(Map.of(
                        ":inc", AttributeValue.builder().n("1").build(),
                        ":zero", AttributeValue.builder().n("0").build()
                ))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.updateItem(request))
                .doOnSuccess(v -> log.info("Contador incrementado en DynamoDB"))
                .doOnError(e -> log.error("Error incrementando contador en DynamoDB", e))
                .then();
    }
}