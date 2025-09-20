package co.com.pragma.sqsadapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private String region;
    private Sqs sqs;
    private Credentials credentials;

    @Data
    public static class Sqs {
        private String queueUrl;
    }

    @Data
    public static class Credentials {
        private String accessKeyId;
        private String secretAccessKey;
    }
}
