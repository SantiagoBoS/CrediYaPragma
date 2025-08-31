package co.com.pragma.api.exception;

import co.com.pragma.api.dto.ErrorResponse;
import co.com.pragma.api.dto.FieldErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler  implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ErrorResponse response;

        if (ex instanceof WebExchangeBindException webEx) {
            var errors = webEx.getFieldErrors().stream()
                    .map(err -> new FieldErrorDTO(err.getField(), err.getDefaultMessage()))
                    .toList();

            response = ErrorResponse.builder()
                    .code("400.01")
                    .message("Error de validación en los datos de entrada.")
                    .errors(errors)
                    .build();

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }
        else if (ex instanceof ConstraintViolationException cvEx) {
            var errors = cvEx.getConstraintViolations().stream()
                    .map(v -> new FieldErrorDTO(
                            v.getPropertyPath().toString(),
                            v.getMessage()))
                    .toList();

            response = ErrorResponse.builder()
                    .code("400.01")
                    .message("Error de validación en los datos de entrada.")
                    .errors(errors)
                    .build();

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }
        else {
            response = ErrorResponse.builder()
                    .code("500.01")
                    .message("Error interno del servidor")
                    .build();

            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer = null;
        try {
            dataBuffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(new ObjectMapper().writeValueAsBytes(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
