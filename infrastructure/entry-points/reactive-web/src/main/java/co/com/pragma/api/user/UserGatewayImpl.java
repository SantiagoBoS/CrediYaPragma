package co.com.pragma.api.user;

import co.com.pragma.api.user.config.UserServiceProperties;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.UserGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserGatewayImpl implements UserGateway {
    private final WebClient webClient;

    public UserGatewayImpl(WebClient.Builder builder, UserServiceProperties userServiceProperties) {
        this.webClient = builder.baseUrl(userServiceProperties.getBaseUrl() + ApiPaths.USER_BASE).build();
    }

    @Override
    public Mono<Boolean> existsByDocument(String documentNumber) {
        return webClient.get()
            .uri("/{documentNumber}", documentNumber)
            .retrieve()
            .toBodilessEntity()
            .map(r -> true)
            .onErrorResume(e -> Mono.error(new BusinessException(AppMessages.USER_NOT_EXIST.getMessage())));
    }
}
