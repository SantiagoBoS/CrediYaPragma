package co.com.pragma.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class GlobalExceptionHandler  implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para serializar JSON

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseBody;

        if (ex instanceof WebExchangeBindException bindEx) {
            // Errores de validación
            List<Map<String, String>> errors = bindEx.getFieldErrors().stream()
                    .map(error -> {
                        assert error.getDefaultMessage() != null;
                        return Map.of(
                                "field", error.getField(),
                                "description", error.getDefaultMessage()
                        );
                    })
                    .toList();

            responseBody = Map.of(
                    "errors", errors,
                    "message", ErrorCatalog.VALIDATION_MESSAGE,
                    "code", ErrorCatalog.VALIDATION_CODE
            );
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

        } else if (ex instanceof jakarta.validation.ConstraintViolationException validationEx) {
            List<Map<String, String>> errors = validationEx.getConstraintViolations().stream()
                    .map(error -> Map.of(
                            "field", error.getPropertyPath().toString(),
                            "description", error.getMessage()
                    ))
                    .toList();

            responseBody = Map.of(
                    "errors", errors,
                    "message", ErrorCatalog.VALIDATION_MESSAGE,
                    "code", ErrorCatalog.VALIDATION_CODE
            );
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else {
            //Errores no controlados
            responseBody = Map.of(
                    "message", ex.getMessage() != null ? ex.getMessage() : ErrorCatalog.INTERNAL_ERROR_MESSAGE,
                    "code", ErrorCatalog.INTERNAL_ERROR_CODE
            );
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(responseBody);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
