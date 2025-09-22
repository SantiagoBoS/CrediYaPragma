package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.junit.jupiter.api.Assertions.*;

class SqsConfigTest {

    private AwsProperties createBaseProperties(String region, String accessKey, String secretKey, String queueUrl) {
        AwsProperties props = new AwsProperties();
        props.setRegion(region);

        AwsProperties.Credentials credentials = new AwsProperties.Credentials();
        credentials.setAccessKeyId(accessKey);
        credentials.setSecretAccessKey(secretKey);
        props.setCredentials(credentials);

        AwsProperties.Sqs sqs = new AwsProperties.Sqs();
        sqs.setQueueUrl(queueUrl);
        props.setSqs(sqs);

        return props;
    }

    @Test
    void testSqsAsyncClientWithNormalEndpoint() {
        AwsProperties props = createBaseProperties(
                "us-east-1",
                "test-access",
                "test-secret",
                "https://sqs.us-east-1.amazonaws.com/123456789012/test-queue"
        );

        SqsConfig config = new SqsConfig(props);
        SqsAsyncClient client = config.sqsAsyncClient();

        assertNotNull(client);
        assertTrue(client instanceof SqsAsyncClient);
    }

    @Test
    void testSqsAsyncClientWithLocalhostEndpoint() {
        AwsProperties props = createBaseProperties(
                "us-east-1",
                "local-access",
                "local-secret",
                "http://localhost:4566/000000000000/test-queue"
        );

        SqsConfig config = new SqsConfig(props);
        SqsAsyncClient client = config.sqsAsyncClient();

        assertNotNull(client);
        assertTrue(client instanceof SqsAsyncClient);
    }

    @Test
    void testSqsAsyncClientWithLocalstackKeyword() {
        AwsProperties props = createBaseProperties(
                "us-west-2",
                "key123",
                "secret123",
                "https://localstack.aws/queue/test"
        );

        SqsConfig config = new SqsConfig(props);
        SqsAsyncClient client = config.sqsAsyncClient();

        assertNotNull(client);
        assertTrue(client instanceof SqsAsyncClient);
    }
}
