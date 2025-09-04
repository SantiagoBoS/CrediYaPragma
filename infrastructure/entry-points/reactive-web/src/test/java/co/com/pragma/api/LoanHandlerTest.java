package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.api.exception.LoanUtils;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.usecase.loan.LoanUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanHandlerTest {

    private LoanUseCase loanUseCase;
    private Validator validator;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        LoanHandler handler = new LoanHandler(loanUseCase, validator);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().POST("/api/v1/solicitud", handler::createLoanRequest).build();
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        LoanDTO dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .build();

        LoanRequest savedRequest = LoanRequest.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        when(loanUseCase.register(any(LoanRequest.class))).thenReturn(Mono.just(savedRequest));
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanUtils.CREATE_CODE)
                .jsonPath("$.message").isEqualTo(LoanUtils.CREATE_MESSAGE)
                .jsonPath("$.data.clientDocument").isEqualTo("12345")
                .jsonPath("$.data.status").isEqualTo("PENDING_REVIEW");

        verify(loanUseCase, times(1)).register(any(LoanRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        LoanDTO invalidDto = LoanDTO.builder().build();
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanUtils.VALIDATION_CODE)
                .jsonPath("$.message").isEqualTo(LoanUtils.VALIDATION_MESSAGE)
                .jsonPath("$.errors").isArray();
    }

    @Test
    void shouldHandleBusinessException() {
        LoanDTO dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(5000.0)
                .termMonths(6)
                .loanType("PERSONAL")
                .build();

        when(loanUseCase.register(any(LoanRequest.class))).thenReturn(Mono.error(new BusinessException("El cliente ya tiene una solicitud en proceso")));
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanUtils.VALIDATION_CODE_GENERAL)
                .jsonPath("$.message").isEqualTo("El cliente ya tiene una solicitud en proceso");
    }
}