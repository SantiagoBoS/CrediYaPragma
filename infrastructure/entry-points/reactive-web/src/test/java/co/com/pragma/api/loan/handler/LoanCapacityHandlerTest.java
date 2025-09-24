package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanCapacityCalculationRequestDTO;
import co.com.pragma.api.loan.dto.LoanCapacityCalculationResponseDTO;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoanCapacityHandlerTest {

    private LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        loanCalculateCapacityUseCase = mock(LoanCalculateCapacityUseCase.class);
        LoanCapacityHandler handler = new LoanCapacityHandler(loanCalculateCapacityUseCase);

        webTestClient = WebTestClient.bindToRouterFunction(
                org.springframework.web.reactive.function.server.RouterFunctions.route()
                        .POST(ApiPaths.CAPACITY_BASE, handler::calculateCapacity)
                        .build()
        ).build();
    }

    @Test
    void testCalculateCapacityReturnsResponse() {
        LoanCapacityCalculationRequestDTO dto = new LoanCapacityCalculationRequestDTO();
        dto.setUserId("USR123");
        dto.setUserSalary(BigDecimal.valueOf(5000));
        dto.setLoanAmount(20000.0);
        dto.setAnnualInterestRate(10.0);
        dto.setTermInMonths(12);

        CapacityResult result = CapacityResult.builder()
                .decision("APPROVED")
                .paymentPlan(List.of(
                        new LoanInstallment(
                                1,
                                BigDecimal.valueOf(1000),
                                BigDecimal.valueOf(200),
                                BigDecimal.valueOf(19000)
                        )
                ))
                .build();

        when(loanCalculateCapacityUseCase.execute(
                anyString(), any(BigDecimal.class), any(Double.class), any(Double.class), anyInt()
        )).thenReturn(Mono.just(result));

        webTestClient.post().uri("/api/v1/calcular-capacidad")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanCapacityCalculationResponseDTO.class)
                .value(body -> {
                    assertNotNull(body);
                    assertEquals("USR123", body.getLoanRequestId());
                    assertEquals("APPROVED", body.getDecision());
                    assertEquals(1, body.getPaymentPlan().size());
                });
    }

    @Test
    void testCalculateCapacityWhenUseCaseReturnsEmpty() {
        LoanCapacityCalculationRequestDTO dto = new LoanCapacityCalculationRequestDTO();
        dto.setUserId("USR999");
        dto.setUserSalary(BigDecimal.valueOf(1000));
        dto.setLoanAmount(50000.0);
        dto.setAnnualInterestRate(20.0);
        dto.setTermInMonths(60);

        when(loanCalculateCapacityUseCase.execute(
                anyString(), any(BigDecimal.class), any(Double.class), any(Double.class), anyInt()
        )).thenReturn(Mono.empty());

        webTestClient.post().uri(ApiPaths.CAPACITY_BASE)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound();
    }
}
