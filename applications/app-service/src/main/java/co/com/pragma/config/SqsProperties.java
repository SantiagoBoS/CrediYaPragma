package co.com.pragma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.sqs")
public class SqsProperties {
    //Configuracion de propiedades
    private Boolean enabled;
}
