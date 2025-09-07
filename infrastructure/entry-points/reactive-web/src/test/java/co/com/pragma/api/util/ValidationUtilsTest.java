package co.com.pragma.api.util;

import co.com.pragma.api.exception.ValidationErrorHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class ValidationUtilsTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = mock(Validator.class);
    }

    static class DummyDto {
        String name;
    }

    @Test
    void shouldReturnEmptyMonoWhenNoViolations() {
        DummyDto dto = new DummyDto();
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        Mono<ServerResponse> result = ValidationUtils.validate(dto, validator);
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorResponseWhenViolationsExist() {
        DummyDto dto = new DummyDto();
        ConstraintViolation<DummyDto> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<DummyDto>> violations = new HashSet<>();
        violations.add(violation);
        when(validator.validate(dto)).thenReturn(violations);
        Mono<ServerResponse> fakeResponse = Mono.just(mock(ServerResponse.class));
        mockStatic(ValidationErrorHandler.class).when(() ->
                ValidationErrorHandler.buildValidationErrorResponse(violations)
        ).thenReturn(fakeResponse);

        Mono<ServerResponse> result = ValidationUtils.validate(dto, validator);
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        verify(validator).validate(dto);
        ValidationErrorHandler.buildValidationErrorResponse(violations);
    }
}
