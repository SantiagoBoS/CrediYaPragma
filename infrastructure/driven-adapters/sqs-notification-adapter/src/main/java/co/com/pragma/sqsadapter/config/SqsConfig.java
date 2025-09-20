package co.com.pragma.sqsadapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {
    // Primero crea esta clase AwsProperties (ver más abajo)
    private final AwsProperties awsProperties;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        SqsAsyncClientBuilder builder = SqsAsyncClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsProperties.getCredentials().getAccessKeyId(),
                                awsProperties.getCredentials().getSecretAccessKey()
                        )
                ));

        if (awsProperties.getSqs().getQueueUrl().contains("localhost") ||
                awsProperties.getSqs().getQueueUrl().contains("localstack")) {
            builder.endpointOverride(URI.create("http://localhost:4566"));
        }

        return builder.build();
    }
}
