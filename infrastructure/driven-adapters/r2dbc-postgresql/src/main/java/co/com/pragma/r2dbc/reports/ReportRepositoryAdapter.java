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

    @Override
    public Mono<Void> addApprovedAmount(Double amount) {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(counterKey).build()))
                .updateExpression("SET #totalAmount = if_not_exists(#totalAmount, :zero) + :amount")
                .expressionAttributeNames(Map.of("#totalAmount", "totalAmount"))
                .expressionAttributeValues(Map.of(
                        ":amount", AttributeValue.builder().n(amount.toString()).build(),
                        ":zero", AttributeValue.builder().n("0").build()
                ))
                .build();
        log.info("Request addApprovedAmount {}", request);
        return Mono.fromFuture(() -> dynamoDbAsyncClient.updateItem(request))
                .doOnSuccess(v -> log.info("Monto total actualizado en DynamoDB"))
                .doOnError(e -> log.error("Error actualizando monto total en DynamoDB", e))
                .then();
    }

    @Override
    public Mono<Double> getTotalApprovedAmount() {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(counterKey).build()))
                .build();

        log.info("Request getTotalApprovedAmount {}", request);
        return Mono.fromFuture(() -> dynamoDbAsyncClient.getItem(request))
                .map(response -> {
                    if (response.item() == null || !response.item().containsKey("totalAmount")) {
                        return 0.0;
                    }
                    log.info("Response getTotalApprovedAmount {}", response);
                    return Double.parseDouble(response.item().get("totalAmount").n());
                })
                .onErrorReturn(0.0);
    }
}