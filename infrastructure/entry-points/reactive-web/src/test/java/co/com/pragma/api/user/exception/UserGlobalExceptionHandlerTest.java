package co.com.pragma.api.user.exception;

import co.com.pragma.api.user.util.UserUtils;
import co.com.pragma.model.exceptions.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserGlobalExceptionHandlerTest {

    private UserGlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UserGlobalExceptionHandler(new ObjectMapper());
    }

    @Test
    void shouldHandleWebExchangeBindException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "user");
        bindingResult.addError(new FieldError("user", "email", "must not be blank"));
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        ServerHttpResponse response = exchange.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException(UserUtils.VALIDATION_ERROR_USER_EXISTS);
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolation<?> violation = new ConstraintViolation<>() {
            @Override public String getMessage() { return "Invalid value"; }
            @Override public String getMessageTemplate() { return null; }
            @Override public Object getRootBean() { return null; }
            @Override public Class<Object> getRootBeanClass() { return Object.class; }
            @Override public Object getLeafBean() { return null; }
            @Override public Object[] getExecutableParameters() { return new Object[0]; }
            @Override public Object getExecutableReturnValue() { return null; }
            @Override
            public Path getPropertyPath() {
                return () -> Set.<Path.Node>of(new Path.Node() {
                    @Override public String getName() { return "field"; }
                    @Override public boolean isInIterable() { return false; }
                    @Override public Integer getIndex() { return null; }
                    @Override public Object getKey() { return null; }

                    @Override
                    public ElementKind getKind() {
                        return null;
                    }

                    @Override public <T extends Path.Node> T as(Class<T> nodeType) { return null; }
                }).iterator();
            }
            @Override public Object getInvalidValue() { return null; }
            @Override public ConstraintDescriptor<?> getConstraintDescriptor() { return null; }
            @Override public <U> U unwrap(Class<U> type) { return null; }
        };

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}