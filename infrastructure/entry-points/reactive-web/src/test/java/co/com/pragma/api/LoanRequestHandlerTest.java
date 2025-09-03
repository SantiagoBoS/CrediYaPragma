package co.com.pragma.api;

import co.com.pragma.api.dto.LoanRequestDTO;
import co.com.pragma.api.exception.LoanRequestUtils;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.usecase.registerloanrequest.RegisterLoanRequestUseCase;
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

class LoanRequestHandlerTest {

    private RegisterLoanRequestUseCase registerLoanRequestUseCase;
    private Validator validator;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        //Validación de datos de entrada y configuración del WebTestClient
        registerLoanRequestUseCase = Mockito.mock(RegisterLoanRequestUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        LoanRequestHandler handler = new LoanRequestHandler(registerLoanRequestUseCase, validator);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().POST("/api/v1/solicitud", handler::createLoanRequest).build();
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        //Verificar que guarda correctamente y retorna el estado PENDING_REVIEW
        LoanRequestDTO dto = LoanRequestDTO.builder()
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

        when(registerLoanRequestUseCase.registerLoanRequest(any(LoanRequest.class))).thenReturn(Mono.just(savedRequest));
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanRequestUtils.CREATE_CODE)
                .jsonPath("$.message").isEqualTo(LoanRequestUtils.CREATE_MESSAGE)
                .jsonPath("$.data.clientDocument").isEqualTo("12345")
                .jsonPath("$.data.status").isEqualTo("PENDING_REVIEW");

        verify(registerLoanRequestUseCase, times(1)).registerLoanRequest(any(LoanRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        //Verificar que retorna error 400 si la validación de datos falla
        LoanRequestDTO invalidDto = LoanRequestDTO.builder().build();
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanRequestUtils.VALIDATION_CODE)
                .jsonPath("$.message").isEqualTo(LoanRequestUtils.VALIDATION_MESSAGE)
                .jsonPath("$.errors").isArray();
    }

    @Test
    void shouldHandleBusinessException() {
        //Verificar que maneja correctamente la excepción de negocio lanzada por el caso de uso
        LoanRequestDTO dto = LoanRequestDTO.builder()
                .clientDocument("12345")
                .amount(5000.0)
                .termMonths(6)
                .loanType("PERSONAL")
                .build();

        when(registerLoanRequestUseCase.registerLoanRequest(any(LoanRequest.class))).thenReturn(Mono.error(new BusinessException("El cliente ya tiene una solicitud en proceso")));
        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(LoanRequestUtils.VALIDATION_CODE_GENERAL)
                .jsonPath("$.message").isEqualTo("El cliente ya tiene una solicitud en proceso");
    }
}