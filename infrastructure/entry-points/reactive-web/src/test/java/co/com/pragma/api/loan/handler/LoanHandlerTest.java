package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.loan.router.LoanRouter;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.usecase.loan.LoanUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanHandlerTest {

    private LoanUseCase loanUseCase;
    private LoanListHandler loanListHandler;
    private LoanUpdateHandler loanUpdateHandler;
    private WebTestClient webTestClient;

    private LoanDTO dto;
    private LoanRequest savedLoan;

    @BeforeEach
    void setUp() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        loanListHandler = Mockito.mock(LoanListHandler.class);
        loanUpdateHandler = Mockito.mock(LoanUpdateHandler.class);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        LoanHandler loanHandler = new LoanHandler(loanUseCase, validator, null);

        RouterFunction<ServerResponse> routerFunction = new LoanRouter().loanRoutes(loanHandler, loanListHandler, loanUpdateHandler);
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
    void testCreateLoanRejectsIfDifferentClientDocument() {
        dto.setClientDocument("99999");

        // Evitar que el mock devuelva null
        when(loanUseCase.register(any(), any())).thenReturn(Mono.just(savedLoan));

        ServerRequest request = MockServerRequest.builder()
                .header("Authorization", "Bearer token")
                .body(Mono.just(dto));

        LoanHandler handler = new LoanHandler(
                loanUseCase,
                Validation.buildDefaultValidatorFactory().getValidator(),
                null
        );

        Mono<ServerResponse> responseMono = handler.createLoan(request)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                        new UsernamePasswordAuthenticationToken("user", "12345", Collections.emptyList())
                ));

        StepVerifier.create(responseMono)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void testCreateLoanSuccess() {
        dto.setClientDocument("12345"); // coincide con el JWT
        dto.setLoanType("HOME");

        when(loanUseCase.register(any(), any())).thenReturn(Mono.just(savedLoan));

        ServerRequest request = MockServerRequest.builder()
                .header("Authorization", "Bearer token")
                .body(Mono.just(dto));

        LoanHandler handler = new LoanHandler(
                loanUseCase,
                Validation.buildDefaultValidatorFactory().getValidator(),
                null
        );

        Mono<ServerResponse> responseMono = handler.createLoan(request)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                        new UsernamePasswordAuthenticationToken("user", "12345", Collections.emptyList())
                ));

        StepVerifier.create(responseMono)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(loanUseCase, times(1)).register(any(), any());
    }
}
