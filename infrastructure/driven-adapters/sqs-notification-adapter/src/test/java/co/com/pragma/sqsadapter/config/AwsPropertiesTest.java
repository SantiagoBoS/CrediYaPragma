package co.com.pragma.sqsadapter.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwsPropertiesTest {

    @Test
    void testSetAndGetRegion() {
        AwsProperties props = new AwsProperties();
        props.setRegion("us-east-1");

        assertEquals("us-east-1", props.getRegion());
    }

    @Test
    void testCredentialsAndSqsProperties() {
        AwsProperties props = new AwsProperties();

        AwsProperties.Credentials credentials = new AwsProperties.Credentials();
        credentials.setAccessKeyId("key123");
        credentials.setSecretAccessKey("secret123");
        props.setCredentials(credentials);

        AwsProperties.Sqs sqs = new AwsProperties.Sqs();
        sqs.setQueueUrl("https://sqs.test.com/queue");
        props.setSqs(sqs);

        assertEquals("key123", props.getCredentials().getAccessKeyId());
        assertEquals("secret123", props.getCredentials().getSecretAccessKey());
        assertEquals("https://sqs.test.com/queue", props.getSqs().getQueueUrl());
    }
}