package co.com.pragma.sqsadapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sqs")
public class SqsProperties {
    // Deshabilitado por defecto
    private boolean enabled = false;
    private String capacityQueueUrl;
}