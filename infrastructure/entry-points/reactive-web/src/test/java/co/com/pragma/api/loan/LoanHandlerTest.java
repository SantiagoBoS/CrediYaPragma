package co.com.pragma.api.loan;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.api.loan.util.LoanUtils;
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
    private final String TEST_HANDLER_CODE = "$.code";
    private final String TEST_HANDLER_MESSAGE = "$.message";

    private LoanDTO dto;
    private LoanRequest loanTest;

    @BeforeEach
    void setUp() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        LoanHandler handler = new LoanHandler(loanUseCase, validator);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().POST(LoanUtils.ROUTER_BASE_PATH, handler::createLoan).build();
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .build();

        loanTest = LoanRequest.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        when(loanUseCase.register(any(LoanRequest.class))).thenReturn(Mono.just(loanTest));
        webTestClient.post()
                .uri(LoanUtils.ROUTER_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange().expectStatus().isCreated().expectBody()
                .jsonPath(TEST_HANDLER_CODE).isEqualTo(LoanUtils.CREATE_CODE)
                .jsonPath(TEST_HANDLER_MESSAGE).isEqualTo(LoanUtils.CREATE_MESSAGE)
                .jsonPath("$.data.clientDocument").isEqualTo("12345")
                .jsonPath("$.data.status").isEqualTo(RequestStatus.PENDING_REVIEW.name());
        verify(loanUseCase, times(1)).register(any(LoanRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        LoanDTO invalidDto = new LoanDTO();
        when(loanUseCase.register(any(LoanRequest.class))).thenReturn(Mono.empty());
        webTestClient.post()
                .uri(LoanUtils.ROUTER_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange().expectStatus().isBadRequest().expectBody()
                .jsonPath(TEST_HANDLER_CODE).isEqualTo(LoanUtils.VALIDATION_CODE)
                .jsonPath(TEST_HANDLER_MESSAGE).isEqualTo(LoanUtils.VALIDATION_MESSAGE);
    }

    @Test
    void shouldHandleBusinessException() {
        when(loanUseCase.register(any(LoanRequest.class))).thenReturn(Mono.error(new BusinessException(AppMessages.APPLICATION_IN_PROCESS.getMessage())));
        webTestClient.post()
                .uri(LoanUtils.ROUTER_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange().expectStatus().isBadRequest().expectBody()
                .jsonPath(TEST_HANDLER_CODE).isEqualTo(LoanUtils.VALIDATION_CODE_GENERAL)
                .jsonPath(TEST_HANDLER_MESSAGE).isEqualTo(AppMessages.APPLICATION_IN_PROCESS.getMessage());
    }
}