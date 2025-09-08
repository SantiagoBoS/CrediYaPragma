package co.com.pragma.api.util;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.exception.ValidationErrorHandler;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class ValidationUtilsTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldReturnEmptyWhenNoViolations() {
        LoanDTO dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        StepVerifier.create(ValidationUtils.validate(dto, validator)).verifyComplete();
    }

    @Test
    void shouldReturnErrorResponseWhenViolationsExist() {
        LoanDTO invalidDto = new LoanDTO();

        try (MockedStatic<ValidationErrorHandler> mocked = mockStatic(ValidationErrorHandler.class)) {
            mocked.when(() -> ValidationErrorHandler.buildValidationErrorResponse(any()))
                    .thenReturn(Mono.just(ServerResponse.badRequest().build().block()));

            StepVerifier.create(ValidationUtils.validate(invalidDto, validator))
                    .expectNextMatches(resp -> resp.statusCode().is4xxClientError())
                    .verifyComplete();
        }
    }
}