package co.com.pragma.model.user.gateways;

import reactor.core.publisher.Mono;

public interface UserDocumentRepository {
    Mono<Boolean> existsByDocument(String documentNumber);
    Mono<Boolean> existsByDocumentToken(String documentNumber, String token);
}