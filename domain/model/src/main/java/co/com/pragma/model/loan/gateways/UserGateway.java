package co.com.pragma.model.loan.gateways;


import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<Boolean> existsByDocument(String documentNumber);
}
