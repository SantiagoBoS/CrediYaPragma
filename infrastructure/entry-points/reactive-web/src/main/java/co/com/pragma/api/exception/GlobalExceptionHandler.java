package co.com.pragma.api.exception;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.FieldErrorDTO;
import co.com.pragma.api.util.Utils;
import co.com.pragma.model.exceptions.BusinessException;
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

import java.util.List;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ApiResponse<Object> response;
        HttpStatus status;

        if (ex instanceof WebExchangeBindException webEx) {
            List<FieldErrorDTO> errors = webEx.getFieldErrors().stream()
                    .map(err -> new FieldErrorDTO(err.getField(), err.getDefaultMessage()))
                    .toList();

            response = ApiResponse.builder()
                    .code(Utils.VALIDATION_CODE)
                    .message(Utils.VALIDATION_MESSAGE)
                    .errors(errors)
                    .build();
            status = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof ConstraintViolationException cvEx) {
            List<FieldErrorDTO> errors = cvEx.getConstraintViolations().stream()
                    .map(v -> new FieldErrorDTO(v.getPropertyPath().toString(), v.getMessage()))
                    .toList();

            response = ApiResponse.builder()
                    .code(Utils.VALIDATION_CODE_GENERAL)
                    .message(Utils.VALIDATION_MESSAGE)
                    .errors(errors)
                    .build();
            status = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof BusinessException be) {
            String message = be.getMessage() != null ? be.getMessage() : determineModuleMessage(exchange);
            response = ApiResponse.builder()
                    .code(Utils.CONFLICT_CODE)
                    .message(message)
                    .build();
            status = HttpStatus.CONFLICT;

        } else {
            response = ApiResponse.builder()
                    .code(Utils.INTERNAL_ERROR_CODE)
                    .message(Utils.INTERNAL_ERROR_MESSAGE)
                    .build();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer dataBuffer;
        try {
            dataBuffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(response));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }


    private String determineModuleMessage(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith(Utils.LOAN_ROUTER_BASE_PATH)) {
            return Utils.LOAN_CONFLICT_MESSAGE;
        } else if (path.startsWith(Utils.USER_PATH_API_USERS)) {
            return Utils.USER_CONFLICT_MESSAGE;
        }

        return Utils.VALIDATION_ERROR;
    }
}
