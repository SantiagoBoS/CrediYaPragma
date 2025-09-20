package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.loan.router.LoanRouter;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.usecase.loan.LoanUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanHandlerTest {

    private LoanUseCase loanUseCase;
    private LoanListHandler loanListHandler;
    private WebTestClient webTestClient;

    private LoanDTO dto;
    private LoanRequest savedLoan;

    @BeforeEach
    void setUp() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        loanListHandler = Mockito.mock(LoanListHandler.class);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        LoanHandler loanHandler = new LoanHandler(loanUseCase, validator, null);

        RouterFunction<ServerResponse> routerFunction =
                new LoanRouter().loanRoutes(loanHandler, loanListHandler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        dto = LoanDTO.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType("HOME")
                .build();

        savedLoan = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .build();
    }

    @Test
    void testCreateLoanReturnsSuccess() {
        when(loanUseCase.register(any(), any())).thenReturn(Mono.just(savedLoan));

        webTestClient.post()
                .uri(ApiPaths.LOAN_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
