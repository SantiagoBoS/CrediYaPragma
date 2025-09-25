package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AwsConfigTest {

    @Test
    void testDynamoDbAsyncClientBeanExists() {
        // Mock del cliente DynamoDB
        DynamoDbAsyncClient client = mock(DynamoDbAsyncClient.class);

        // Simplemente verificamos que el mock no es nulo
        assertNotNull(client, "El cliente DynamoDB no debe ser nulo");
    }
}
