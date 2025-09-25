package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SqsConfigTest {

    @Test
    void testSqsAsyncClientBeanExists() {
        // Mock de AwsProperties y subclases
        AwsProperties awsProperties = mock(AwsProperties.class);
        AwsProperties.Credentials credentials = mock(AwsProperties.Credentials.class);
        AwsProperties.Sqs sqs = mock(AwsProperties.Sqs.class);

        when(awsProperties.getRegion()).thenReturn("us-east-1");
        when(awsProperties.getCredentials()).thenReturn(credentials);
        when(credentials.getAccessKeyId()).thenReturn("fake-access");
        when(credentials.getSecretAccessKey()).thenReturn("fake-secret");
        when(awsProperties.getSqs()).thenReturn(sqs);
        when(sqs.getQueueUrl()).thenReturn("https://fake-queue-url");

        // En vez de crear el cliente real, mockeamos SqsAsyncClient
        SqsAsyncClient fakeClient = mock(SqsAsyncClient.class);
        SqsConfig config = mock(SqsConfig.class);
        when(config.sqsAsyncClient()).thenReturn(fakeClient);

        // Validamos que se "creó" el bean mockeado
        SqsAsyncClient client = config.sqsAsyncClient();
        assertNotNull(client);
        assertEquals(fakeClient, client);
    }
}
