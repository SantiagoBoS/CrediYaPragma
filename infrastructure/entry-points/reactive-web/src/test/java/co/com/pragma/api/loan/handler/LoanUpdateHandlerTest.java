package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanStatusUpdateDTO;
import co.com.pragma.model.exceptions.BusinessException;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;

class LoanUpdateHandlerTest {

    private LoanUpdateUseCase loanUpdateUseCase;
    private LoanUpdateHandler loanUpdateHandler;
    private Validator validator;

    @BeforeEach
    void setUp() {
        loanUpdateUseCase = Mockito.mock(LoanUpdateUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        loanUpdateHandler = new LoanUpdateHandler(loanUpdateUseCase, validator);
    }

    @Test
    void testUpdateLoanStatusReturnsSuccess() {
        //Para poder actualizar el estado del prestamo
        UUID publicId = UUID.randomUUID();
        LoanRequest updatedLoan = LoanRequest.builder()
                .publicId(publicId)
                .amount(5000.0)
                .loanType("PENDING_REVIEW")
                .build();

        LoanStatusUpdateDTO dto = new LoanStatusUpdateDTO();
        dto.setStatus("APPROVED");

        ServerRequest request = Mockito.mock(ServerRequest.class);
        when(request.pathVariable("publicId")).thenReturn(publicId.toString());
        when(request.bodyToMono(LoanStatusUpdateDTO.class)).thenReturn(Mono.just(dto));

        when(loanUpdateUseCase.updateLoanStatus(any(), any(), any())).thenReturn(Mono.just(updatedLoan));

        Mono<ServerResponse> responseMono = loanUpdateHandler.updateLoanStatus(request)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                        new UsernamePasswordAuthenticationToken("advisor1", null, Collections.emptyList())
                ));

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r != null)
                .verifyComplete();
    }

    @Test
    void testUpdateLoanStatusWhenUseCaseFails() {
        LoanStatusUpdateDTO dto = new LoanStatusUpdateDTO();
        dto.setStatus("APPROVED");

        ServerRequest request = Mockito.mock(ServerRequest.class);
        when(request.pathVariable("publicId")).thenReturn(UUID.randomUUID().toString());
        when(request.bodyToMono(LoanStatusUpdateDTO.class)).thenReturn(Mono.just(dto));

        when(loanUpdateUseCase.updateLoanStatus(any(), any(), any()))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ServerResponse> responseMono = loanUpdateHandler.updateLoanStatus(request)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                        new UsernamePasswordAuthenticationToken("advisor1", null, Collections.emptyList())
                ));

        StepVerifier.create(responseMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testUpdateLoanStatusValidationFails() {
        LoanStatusUpdateDTO invalidDto = new LoanStatusUpdateDTO();

        ServerRequest request = Mockito.mock(ServerRequest.class);
        when(request.pathVariable("publicId")).thenReturn(UUID.randomUUID().toString());
        when(request.bodyToMono(LoanStatusUpdateDTO.class)).thenReturn(Mono.just(invalidDto));

        when(loanUpdateUseCase.updateLoanStatus(any(), any(), any()))
                .thenReturn(Mono.error(new BusinessException("Validación fallida")));

        Mono<ServerResponse> responseMono = loanUpdateHandler.updateLoanStatus(request)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                        new UsernamePasswordAuthenticationToken("advisor1", null, Collections.emptyList())
                ));

        StepVerifier.create(responseMono)
                .expectError(BusinessException.class)
                .verify();

        verify(loanUpdateUseCase).updateLoanStatus(any(), any(), any());
    }
}
