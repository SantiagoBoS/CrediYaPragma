package co.com.pragma.api.exception;

import co.com.pragma.model.loan.exceptions.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(new ObjectMapper());
    }

    @Test
    void shouldHandleWebExchangeBindException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "loanRequest");
        bindingResult.addError(new FieldError("loanRequest", "amount", "must not be blank"));
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String json = exchange.getResponse().getBodyAsString().block();
        assertThat(json).contains("\"code\":\"400.01\"");
        assertThat(json).contains("\"message\"");
        assertThat(json).contains("\"errors\"");
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Usuario ya existe");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        String json = exchange.getResponse().getBodyAsString().block();
        assertThat(json).contains("\"code\":\"409.01\"");
        assertThat(json).contains("\"message\":\"Usuario ya existe\"");
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        StepVerifier.create(handler.handle(exchange, ex)).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        String json = exchange.getResponse().getBodyAsString().block();
        assertThat(json).contains("\"code\":\"500.01\"");
        assertThat(json).contains("\"message\"");
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
                    @Override public ElementKind getKind() { return null; }
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
        String json = exchange.getResponse().getBodyAsString().block();
        assertThat(json).contains("\"code\":\"400.02\"");
        assertThat(json).contains("\"message\"");
        assertThat(json).contains("\"errors\"");
    }
}
