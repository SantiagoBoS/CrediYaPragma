package co.com.pragma.jwtprovider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    // Llave secreta para firmar los tokens JWT
    private String secret;
    private long expirationMs;
}