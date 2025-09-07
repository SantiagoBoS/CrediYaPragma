package co.com.pragma.api.exception;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.FieldErrorDTO;
import co.com.pragma.api.util.UserUtils;
import co.com.pragma.model.user.exceptions.BusinessException;
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

            response = ApiResponse.builder().code(UserUtils.VALIDATION_CODE).message(UserUtils.VALIDATION_MESSAGE).errors(errors).build();
            status = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof ConstraintViolationException cvEx) {
            List<FieldErrorDTO> errors = cvEx.getConstraintViolations().stream()
                    .map(v -> new FieldErrorDTO(v.getPropertyPath().toString(), v.getMessage()))
                    .toList();

            response = ApiResponse.builder().code(UserUtils.VALIDATION_CODE_GENERAL).message(UserUtils.VALIDATION_MESSAGE).errors(errors).build();
            status = HttpStatus.BAD_REQUEST;

        } else if (ex instanceof BusinessException be) {
            response = ApiResponse.builder().code(UserUtils.CONFLICT_CODE).message(be.getMessage() != null ? be.getMessage() : UserUtils.CONFLICT_MESSAGE).build();
            status = HttpStatus.CONFLICT;

        } else {
            response = ApiResponse.builder().code(UserUtils.INTERNAL_ERROR_CODE).message(UserUtils.INTERNAL_ERROR_MESSAGE).build();
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
}
