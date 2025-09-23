package co.com.pragma.api.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ResponseUtilsTest {

    @Test
    void testSuccessResponseBuilder() {
        Mono<ServerResponse> responseMono = ResponseUtils.success()
                .status(HttpStatus.CREATED)
                .code("200")
                .message("OK")
                .withData("Hello");

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    // Verifica que el status sea el correcto
                    assert response.statusCode().equals(HttpStatus.CREATED);
                })
                .verifyComplete();
    }
}